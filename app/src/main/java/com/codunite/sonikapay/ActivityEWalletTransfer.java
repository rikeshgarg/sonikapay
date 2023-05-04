package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.retrofit.ApiInterface;
import com.chaos.view.PinView;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.MessageListener;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityEWalletTransfer extends AppCompatActivity implements View.OnClickListener, WebServiceListener, MessageListener {
    private EditText edCurrentBal, edAmount, edDesc, edPhoneNumber;
    private Button btnProceed, btnOtpAuth;
    private ImageView imgDrop, imgDropOne;

    private ImageView imgBack;
    private View[] allViewWithClick = {btnProceed, imgBack, btnOtpAuth, imgDrop, imgDropOne};
    private int[] allViewWithClickId = {R.id.btn_proceed, R.id.img_back, R.id.btn_otpauth,
            R.id.img_drop_1, R.id.img_drop_2};

    private EditText[] edTexts = {edCurrentBal, edAmount, edDesc};
    private String[] edTextsError = {"Enter current wallet Balance", "Enter Amount", "Enter Description"};
    private int[] editTextsClickId = {R.id.edtcurrentwalletbal, R.id.edt_enteramount, R.id.edt_desc};
    private Spinner spinnerCrDr, spinnerMemberList;
    private List<String> listSpinnerCrDr, listSpinnerMemberListy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wallettransfer);
        StartApp();
        loadToolBar();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        spinnerCrDr = (Spinner) findViewById(R.id.spinner_crdr);
        spinnerMemberList = (Spinner) findViewById(R.id.spinner_memberlist);

        LoadAllData();

        listSpinnerCrDr = new ArrayList<>();
        listSpinnerCrDr.add("-1#:#Add Credit");
        listSpinnerCrDr.add("1#:#CR");
        listSpinnerCrDr.add("2#:#DR");
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinnerCrDr, true);
        spinnerCrDr.setAdapter(LegAdapter);

        ((EditText) findViewById(R.id.edtcurrentwalletbal)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, ""));
    }

    private void LoadAllData(){
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETMEMBERLIST, lstUploadData);
    }

    private void ProceedWalletTransfer() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

//        if (spinnerCrDr.getSelectedItemPosition() == 0) {
//            response++;
//            customToast.showCustomToast(svContext, "Select credit", customToast.ToastyError);
//        }

        if (spinnerMemberList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Select member", customToast.ToastyError);
        }

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add((listSpinnerMemberListy.get(spinnerMemberList.getSelectedItemPosition()).split("#:#")[0]));
//            lstUploadData.add((listSpinnerCrDr.get(spinnerCrDr.getSelectedItemPosition()).split("#:#")[0]));
            lstUploadData.add("2");
            lstUploadData.add(edAmount.getText().toString().trim());
            lstUploadData.add(edDesc.getText().toString().trim());
            callWebService(ApiInterface.WALLETTRANSFER, lstUploadData);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_proceed:
                            ProceedWalletTransfer();
                            break;
                        case R.id.img_back:
                            onBackPressed();
                            break;
                        case R.id.btn_otpauth:
                            OtpVerify();
                            break;
                        case R.id.img_drop_1:
                            spinnerCrDr.performClick();
                            break;
                        case R.id.img_drop_2:
                            spinnerMemberList.performClick();
                            break;
                    }
                }

            });
        }
    }

    private void loadToolBar() {
        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Credit E-Wallet");

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

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edCurrentBal = (EditText) editTexts[0];
        edAmount = (EditText) editTexts[1];
        edDesc = (EditText) editTexts[2];
        edPhoneNumber = (EditText) findViewById(R.id.edt_entermobile);
        edPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edPhoneNumber.getText().toString().trim()).length() == 10) {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edPhoneNumber.getText().toString().trim());
                    callWebService(ApiInterface.GETMEMBERBYMOBILE, lstUploadData);
                }else if((edPhoneNumber.getText().toString().trim()).length() == 0){
                    LoadAllData();
                }
            }
        });
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityEWalletTransfer.this);
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
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_NAME = "name";
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.WALLETTRANSFER)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBAL, json.getString("balance"));
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.WALLETOTPAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_message = json.getString("message");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    hideOtpLayout();
                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }else if (url.contains(ApiInterface.GETMEMBERBYMOBILE)) {
            listSpinnerMemberListy = new ArrayList<>();
            listSpinnerMemberListy.add("-1#:#Select member");
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_user_id = data_obj.getString("member_id");
                        String str_name = data_obj.getString("member_name")+" ("+data_obj.getString("member_code")+") ";

                        listSpinnerMemberListy.add(str_user_id+"#:#"+str_name);
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
            SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinnerMemberListy, true);
            spinnerMemberList.setAdapter(LegAdapter);
        }else if (url.contains(ApiInterface.GETMEMBERLIST)) {
            listSpinnerMemberListy = new ArrayList<>();
            listSpinnerMemberListy.add("-1#:#Select member");
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_user_id = data_obj.getString("user_id");
                        String str_name = data_obj.getString("name")+" ("+data_obj.getString("user_code")+") ";

                        listSpinnerMemberListy.add(str_user_id+"#:#"+str_name);
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
            SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinnerMemberListy, true);
            spinnerMemberList.setAdapter(LegAdapter);
        }
    }

    private void ShowOtpLayout() {
        ((CardView) findViewById(R.id.layoutLogin)).setVisibility(View.GONE);
        ((CardView) findViewById(R.id.card_otp)).setVisibility(View.VISIBLE);
    }

    private void hideOtpLayout() {
        ((CardView) findViewById(R.id.layoutLogin)).setVisibility(View.VISIBLE);
        ((CardView) findViewById(R.id.card_otp)).setVisibility(View.GONE);
    }

    private void OtpVerify() {
        PinView edtOtp = (PinView) findViewById(R.id.edt_otp);
        if (edtOtp.length() == 0) {
            customToast.showCustomToast(svContext, "Please enter otp", customToast.ToastyError);
        } else {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(edtOtp.getText().toString().trim());
            callWebService(ApiInterface.WALLETOTPAUTH, lstUploadData);
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

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }
}