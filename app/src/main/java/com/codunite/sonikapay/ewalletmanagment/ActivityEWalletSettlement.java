package com.codunite.sonikapay.ewalletmanagment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.retrofit.ApiInterface;
import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.ActivitySplash;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.MessageListener;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityEWalletSettlement extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener, MessageListener {
    private EditText edAmount, edDesc, edtMobile;
    private Button btnSubmitMem, btnCancel;
    private ImageView imgDrop, imgDropOne, imgDropTwo;

    private View[] allViewWithClick = {btnSubmitMem, btnCancel};
    private int[] allViewWithClickId = {R.id.btn_SubmitMem, R.id.btnCancel};

    private EditText[] edTexts = {edAmount, edDesc};
    private String[] edTextsError = {"Enter amount", "Enter description"};
    private int[] editTextsClickId = {R.id.edt_amount_transfer, R.id.edt_desc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ewalletsettlement);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    public void resumeApp() {

    }

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private void removeFocus(EditText edTxt) {
        edTxt.clearFocus();
    }

    private void setFocus(EditText edTxt) {
        edTxt.setFocusable(true);
        edTxt.requestFocus();
    }

    private void SubmitSignUpForm() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(getEditextValue(edAmount));
            lstUploadData.add(getEditextValue(edDesc));
            callWebService(ApiInterface.EWALLETSETTLEMENT, lstUploadData);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_SubmitMem:
                            SubmitSignUpForm();
                            break;
                        case R.id.btnCancel:
                            onBackPressed();
                            break;
                    }
                }
            });
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edAmount = (EditText) editTexts[0];
        edDesc = (EditText) editTexts[1];
        edtMobile  = (EditText) findViewById(R.id.edtMobile);
        edtMobile.setText(PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, "0"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityEWalletSettlement.this);
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

    @Override
    public void messageReceived(String message) {
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
    }

    LinkedList<String> lstUploadData = new LinkedList<>();

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_DATA = "data";
    public static final String TAG_COUNTRY_CODE = "country_code";
    public static final String TAG_COUNTRY_NAME = "country_name";
    public static final String TAG_COUNTRY_ID = "country_id";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    List<String> listcountry = new ArrayList<>();
    List<String> liststate = new ArrayList<>();
    List<String> listBankList = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETCOUNTRYLIST)) {
            try {
                JSONObject json = new JSONObject(result);
                listcountry = new ArrayList<>();
                listcountry.add("-1" + "#:#" + "Select Country");

                String str_status = json.getString(TAG_STATUS);
                String str_msg = json.getString(TAG_MESSAGE);
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_country_code = data_obj.getString(TAG_COUNTRY_CODE);
                        String str_country_name = data_obj.getString(TAG_COUNTRY_NAME);

                        listcountry.add(str_country_code + "#:#" + str_country_name);
                    }

                    //PopulateCountry(listcountry);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.EWALLETSETTLEMENT)) {
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
        }if (url.contains(ApiInterface.AEPSBANKLIST)) {
            try {
                JSONObject json = new JSONObject(result);
                listBankList = new ArrayList<>();
                listBankList.add("-1" + "#:#" + "Select Bank Name");

                String str_status = json.getString("status");
                String str_msg = json.getString(TAG_MESSAGE);
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_country_code = data_obj.getString("id");
                        String str_country_name = data_obj.getString("bank_name");

                        listBankList.add(str_country_code + "#:#" + str_country_name);
                    }

                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("E-Wallet Settlement");

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

        LinearLayout imgeToolBarWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgeToolBarWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpeneWalletActivity(svContext);
            }
        });
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
        super.onBackPressed();
    }
}