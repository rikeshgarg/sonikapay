package com.codunite.sonikapay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import java.util.LinkedList;

public class ActivityReferral extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};
    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};
    private WebView webviewQrCode;
    private EditText edRefId;
    private Button copyandshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_referral);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    private void resumeApp() {
        copyandshare = (Button) findViewById(R.id.btn_copy_share);
        TextView referral = (TextView) findViewById(R.id.referallink);
        referral.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("EditText", referral.getText().toString());
            clipboard.setPrimaryClip(clip);
            customToast.showCustomToast(svContext, "Link Copied", customToast.ToastyNormal);
        });

        String deepLink = "";
        deepLink = PreferenceConnector.readString(svContext, PreferenceConnector.REFFERALLINK, "0");
        referral.setText(deepLink);

        String firebaseLink = "https://vsidigital.page.link/?";
        String webLink = "link=https://www.vsidigital.in/register/?referral_id%3D" +
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINMEMBERID, "0");
        String endLink = "&apn=" + getPackageName() + "&efr=1";
        String dynamicLink = firebaseLink + webLink + endLink;

        referral.setText(dynamicLink);

        loadUserData();
        copyandshare.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("EditText", referral.getText().toString());
            clipboard.setPrimaryClip(clip);
            customToast.showCustomToast(svContext, "Link Copied", customToast.ToastyNormal);
            Intent intent = new Intent(Intent.ACTION_SEND);
            /*This will be the actual content you wish you share.*/

            String shareBody = "Hi, I just invited you to use the VSI Digital app!\n" +
                    "\n" +
                    "Step1: Use my link to download the app\n" +
                    "Step2: Add money\n" +
                    "Step3: Start making 24x7 instant  payments for Recharges, bill payments & more services.\n" +
                    "\n" +
                    "\n" +
                    "Most Indians use with VSI Digital It's 100% safe & secure.\n" +
                    "Download the app now. "
                    + referral.getText().toString();
            /*The type of the content is text, obviously.*/
            intent.setType("text/plain");
            /*Applying information Subject and Body.*/
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share Refer Link");
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            /*Fire!*/
            startActivity(Intent.createChooser(intent, "Share Via"));
        });

    }

    private void loadUserData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
        callWebService(ApiInterface.UPDATEFCM, lstUploadData);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityReferral.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
        }

        hideKeyboard();
        GlobalData.SetLanguage(svContext);
        if (checkNetwork.isConnectingToInternet()) {
            errrorScreen.hideError();
        } else {
            errrorScreen.showInternetError();
        }

        loadToolBar();
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Referral Link");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
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

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.UPDATEFCM)) {
            ActivitySplash.LoadUserData(result, svContext);

//            ActivityMain.toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));
//            ActivityMain.toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));


        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}