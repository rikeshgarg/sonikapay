package com.codunite.sonikapay;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.SpinnerPopulateAdapter;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityHelpFeedback extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private EditText edCompalintTitle, edCompalintDesc;
    private Button btnsignUp, btnRemovePic, btnSelectPic;
    private ImageView imgProfilePic, imgDrop;
    private TextView txtNoFile;
    public Uri imageUri = null;
    private Spinner ticketType;

    private View[] allViewWithClick = {imgDrop};
    private int[] allViewWithClickId = {R.id.img_drop_1};

    private EditText[] edTexts = {};
    private String[] edTextsError = {"Enter phone number"};
    private int[] editTextsClickId = {};
    private List<String> listSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_help);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    public void resumeApp() {
        txtNoFile = (TextView) findViewById(R.id.nofile_aadharfront);
        edCompalintTitle = (EditText) findViewById(R.id.et_title);
        edCompalintDesc = (EditText) findViewById(R.id.et_desc);
        btnsignUp = (Button) findViewById(R.id.submit_ticket);
        imgProfilePic = (ImageView) findViewById(R.id.imgae_dp);
        btnRemovePic = findViewById(R.id.btn_removepic);
        btnSelectPic = findViewById(R.id.choose_aadharfront);
        btnSelectPic.setOnClickListener(this);

        btnsignUp.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        btnRemovePic.setOnClickListener(this);

        ticketType = (Spinner) findViewById(R.id.spinner_countrylist);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETTICKETTYPELIST, lstUploadData);

        OpenDemoLink();
    }

    private void PopulateSpinner(){
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinner, true);
        ticketType.setAdapter(LegAdapter);
        ticketType.setOnItemSelectedListener(onItemSelectedListener);
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
                        case R.id.img_drop_1:
                            ticketType.performClick();
                            break;
                    }
                }
            });
        }

//        btnBack = (Button) allViewWithClick[0];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityHelpFeedback.this);
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
        }else {
            errrorScreen.showInternetError();
        }

        loadToolBar();
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Raise Ticket");

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));

        LinearLayout imgToolBarWallet = (LinearLayout) findViewById(R.id.img_wallet);
        imgToolBarWallet.setOnClickListener(view -> ActivitySplash.OpenWalletActivity(svContext));

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgToolBareWallet.setOnClickListener(view -> ActivitySplash.OpeneWalletActivity(svContext));
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


    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.SUBMITCOMPALINT)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);
                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }else if (url.contains(ApiInterface.GETTICKETTYPELIST)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    listSpinner = new ArrayList<>();
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("id");
                        String str_name = data_obj.getString("title");

                        listSpinner.add(str_code + "#:#" + str_name);
                    }
                    PopulateSpinner();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_ticket:
                SubmitForm();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.imgae_dp:
            case R.id.choose_aadharfront:
                Intent svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                startActivity(svIntent);
                ((Activity) svContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.btn_removepic:
                if (imageUri == null) {
                    customToast.showCustomToast(svContext, "No image to remove", customToast.ToastyError);
                } else {
                    imageUri = null;
                    imgProfilePic.setImageDrawable(null);
                    imgProfilePic.setBackgroundResource(R.drawable.users);
                    imgProfilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            imageUri = ActivityBrowseProfileImage.imageUri;
            imgProfilePic.setImageURI(null);
            imgProfilePic.setImageURI(imageUri);
            ActivityBrowseProfileImage.imageUri = null;
            txtNoFile.setVisibility(View.INVISIBLE);
        } else {
            imgProfilePic.setBackgroundResource(R.drawable.users);
            txtNoFile.setVisibility(View.VISIBLE);
        }
        imgProfilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void SubmitForm() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                new EditText[]{edCompalintTitle, edCompalintDesc},
                new String[]{"enter ticket title", "enter ticket description"});

        if (ticketType.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select Ticket type", customToast.ToastyWarning);
        }

        if (response == 0) {
            if (imageUri == null) {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                lstUploadData.add(edCompalintTitle.getText().toString().trim());
                lstUploadData.add(listSpinner.get(ticketType.getSelectedItemPosition()).split("#:#")[0]);
                lstUploadData.add(edCompalintDesc.getText().toString().trim());
                lstUploadData.add("photo");
                callWebService(ApiInterface.SUBMITCOMPALINT, lstUploadData);
            } else {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                lstUploadData.add(edCompalintTitle.getText().toString().trim());
                lstUploadData.add(listSpinner.get(ticketType.getSelectedItemPosition()).split("#:#")[0]);
                lstUploadData.add(edCompalintDesc.getText().toString().trim());
                lstUploadData.add(encodeImage(imageUri));
                callWebService(ApiInterface.SUBMITCOMPALINT, lstUploadData);
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
        //Base64.de
        return encImage;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = view.findViewById(R.id.txtitem);
            textView.setTextColor(getResources().getColor(R.color.fontcoloreditext));
        }
        @Override public void onNothingSelected(AdapterView<?> parent) { }
    };

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("13");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}