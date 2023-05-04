package com.codunite.sonikapay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.webviewsuite.WebViewSuite;
import com.retrofit.ApiInterface;
import com.codunite.model.WalletHistoryModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityFundRequest extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private EditText edAmount, edTransId;
    private Button btnProceedPay, btnViewPayQr, btnChosseScreen, btnRemoveScreen;
    private TextView txtCurrentBal, txtNofileScreenshot;

    private ImageView imgBack;
    private View[] allViewWithClick = {btnProceedPay, btnViewPayQr, btnChosseScreen, btnRemoveScreen, imgBack};
    private int[] allViewWithClickId = {R.id.btnproceedtopay, R.id.btn_view_qr, R.id.choose_scrrenshot, R.id.remove_scrrenshot, R.id.img_back};

    private EditText[] edTexts = {edAmount, edTransId};
    private String[] edTextsError = {"Enter amount", "Enter transcation id"};
    private int[] editTextsClickId = {R.id.edtenteramount, R.id.edttransid};

    private List<WalletHistoryModel> lstItems = new ArrayList<>();
    private RecyclerView wallethistoryrv;

    private Uri imagePayScreenShot = null;
    private int selectedImageView = 0;
    private ImageView imgPayScreenShot;
    private String strQrUrl = "";
    private WebViewSuite webViewSuite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fund_request);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        txtCurrentBal = (TextView) findViewById(R.id.current_bal);
        txtNofileScreenshot = (TextView) findViewById(R.id.nofile_scrrenshot);
        txtNofileScreenshot.setVisibility(View.VISIBLE);
        webViewSuite    = findViewById(R.id.webViewSuite);
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);
        imgPayScreenShot = (ImageView) findViewById(R.id.img_scrrenshot);
        imgPayScreenShot.setVisibility(View.GONE);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebServiceWithoutDialog(ApiInterface.GET_FUNDREQUEST_MANUUAL_QR, lstUploadData);

        txtCurrentBal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, ""));

        OpenDemoLink();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            if (selectedImageView == 0) {
                imagePayScreenShot = ActivityBrowseProfileImage.imageUri;
                imgPayScreenShot.setImageURI(null);
                imgPayScreenShot.setImageURI(imagePayScreenShot);
                imgPayScreenShot.setVisibility(View.VISIBLE);
                txtNofileScreenshot.setVisibility(View.GONE);
            }
            ActivityBrowseProfileImage.imageUri = null;
        }
    }

    private void Proceedtopay() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (response == 0) {
            if (Integer.parseInt(edAmount.getText().toString().trim()) < 1) {
                response++;
                customToast.showCustomToast(svContext, "Please enter at least 1 Rs", customToast.ToastyError);
            }

            if (edTransId.getText().toString().trim().length() < 12) {
                response++;
                customToast.showCustomToast(svContext, "Please enter valid transcation id", customToast.ToastyError);
            }

            if (imagePayScreenShot == null) {
                response++;
                customToast.showCustomToast(svContext, "Please attach payment screenshot", customToast.ToastyWarning);
            }

            if (response == 0) {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                lstUploadData.add(edAmount.getText().toString().trim());
                lstUploadData.add(edTransId.getText().toString().trim());
                lstUploadData.add(encodeImage(imagePayScreenShot));
                callWebService(ApiInterface.FUNDREQUESTAUTH, lstUploadData);
            }
        }
    }

    private String encodeImage(Uri imgUri) {
        File imagefile = new File(imgUri.getPath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btnproceedtopay:
                            Proceedtopay();
                            break;
                        case R.id.btn_view_qr:
                            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, "Payment QR");
                            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, strQrUrl);
                            Intent svIntent = new Intent(svContext, WebViewActivity.class);
                            svContext.startActivity(svIntent);
                            break;
                        case R.id.choose_scrrenshot:
                            selectedImageView = 0;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            svIntent.putExtra("isShowCrop", false);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.remove_scrrenshot:
                            imagePayScreenShot = null;
                            imgPayScreenShot.setImageURI(null);
                            imgPayScreenShot.setVisibility(View.GONE);
                            txtNofileScreenshot.setVisibility(View.VISIBLE);
                            break;
                        case R.id.img_back:
                            onBackPressed();
                            break;
                    }
                }
            });
        }

        btnProceedPay = (Button) allViewWithClick[0];
        btnViewPayQr = (Button) allViewWithClick[1];
        btnChosseScreen = (Button) allViewWithClick[2];
        btnRemoveScreen = (Button) allViewWithClick[3];
        imgBack = (ImageView) allViewWithClick[4];
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edAmount = (EditText) editTexts[0];
        edTransId = (EditText) editTexts[1];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.termscondition:
//                break;
            default:
                break;
        }
    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActivityFundRequest.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
//        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
//        } else {
//            FontUtils.setThemeColor(root, svContext, false);
//        }
        hideKeyboard();
        GlobalData.SetLanguage(svContext);
        if (checkNetwork.isConnectingToInternet()) {
            errrorScreen.hideError();
        } else {
            errrorScreen.showInternetError();
        }
        loadToolBar();
    }

    private void loadToolBar() {
        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Fund Request");

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));

        LinearLayout imgToolBarWallet = (LinearLayout) findViewById(R.id.img_wallet);
        imgToolBarWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpenWalletActivity(svContext);
            }
        });

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgToolBareWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpeneWalletActivity(svContext);
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    LinkedList<String> lstUploadData = new LinkedList<>();

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private void callWebServiceWithoutDialog(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_TYPE = "type";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GET_FUNDREQUEST_MANUUAL_QR)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    strQrUrl = json.getString("qr_code");
                    webViewSuite.startLoading(strQrUrl);
                    webViewSuite.setCustomProgressBar(new ProgressBar(this));
                    webViewSuite.setOpenPDFCallback(new WebViewSuite.WebViewOpenPDFCallback() {
                        @Override
                        public void onOpenPDF() {
                            finish();
                        }
                    });
                    webViewSuite.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {}
                        @Override
                        public void onPageFinished(WebView view, String url) {}
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return false;
                        }
                    });
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.FUNDREQUESTAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);

                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }else if (url.contains(ApiInterface.GETDEMOLINK)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    strDemoServiceName = json.getString("service");
                    dtrDemoServiceUrl = json.getString("demo_link");
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private EditText getEditext(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return edTexts[i];
            }
        }
        return new EditText(svContext);
    }

    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("11");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}