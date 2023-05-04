package com.codunite.sonikapay.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codunite.sonikapay.ActivitySignUp;
import com.codunite.sonikapay.ActivitySplash;
import com.codunite.sonikapay.R;
import com.otpless.views.OtplessManager;
import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.MessageListener;
import com.codunite.commonutility.ShowCustomToast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, WebServiceListener, MessageListener {

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;
    private ImageView img_back;
    private EditText edUsername, edPassword;

    private Button btnLogin, btnSuccessfullLogin;
    private TextView btnRegister;
    private TextView txtForgotPassword, goback;
    private EditText[] edTexts = {edUsername, edPassword};
    private int[] editTextsClickId = {R.id.edt_loginID, R.id.edt_password};
    private View[] allViewWithClick = {btnLogin, btnRegister, txtForgotPassword, btnSuccessfullLogin, goback, img_back};
    private int[] allViewWithClickId = {R.id.btnSignIn, R.id.btnSignUp, R.id.btn_forgetpassword, R.id.btn_transaction_password, R.id.goback, R.id.img_back};
    private CardView layoutLogin, layoutTransactionpassword;

    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        requestPermissionForApp();
        StartApp();
        OtplessManager.getInstance().init(this);
    }
    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent svIntent = null;
                    switch (v.getId()) {
                        case R.id.btnSignIn:
                            LoginStart();
                            break;
                        case R.id.btnSignUp:
                            svIntent = new Intent(svContext, ActivitySignUp.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.btn_forgetpassword:
                            svIntent = new Intent(svContext, ActivityForgotPassword.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.btn_transaction_password:
                            FinalStart();
                            break;
                        case R.id.goback:
                            ShowLoginScreen();
                            break;
                        case R.id.img_back:
                            finish();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + v.getId());
                    }
                }
            });
        }
        btnRegister = (TextView) allViewWithClick[1];
    }
    private void LoginStart() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTexts,
                new String[]{"enter username", "enter password"});

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(getEditextValue(edUsername));
            lstUploadData.add(getEditextValue(edPassword));
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
            callWebService(ApiInterface.LOGIN, lstUploadData);
        }
    }

    private EditText edTranPwd;
    private void FinalStart() {
        edTranPwd = (EditText) findViewById(R.id.edt_transactionpassword);
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                new EditText[]{edTranPwd},
                new String[]{"Enter OTP"});

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(edTranPwd.getText().toString().trim());
            lstUploadData.add(strEncodeLoginCode);
            callWebService(ApiInterface.LOGNUSER, lstUploadData);
        }
    }
    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
    }
    private void requestPermissionForApp() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edUsername = (EditText) editTexts[0];
        edPassword = (EditText) editTexts[1];

//        if (GlobalVariables.ISTESTING) {
//            editTexts[0].setText("ssridharatiwari@gmail.com");
//            editTexts[1].setText("123456");
//            ((EditText)findViewById(R.id.edt_transactionpassword)).setText("1234");
//        }
    }

//    private void LoginStart() {
//        int response = 0;
//        response = CheckValidation.emptyEditTextError(
//                edTexts,
//                new String[]{"enter username", "enter password"});
//
//        if (response == 0) {
//            lstUploadData = new LinkedList<>();
//            lstUploadData.add(edTexts[0].getText().toString().trim());
//            lstUploadData.add(edTexts[1].getText().toString().trim());
//            callWebService(ApiInterface.LOGIN, lstUploadData);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActivityLogin.this);
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
        resumeApp();
    }

    public void resumeApp() {
        layoutLogin = (CardView) findViewById(R.id.layoutLogin);
        layoutTransactionpassword = (CardView) findViewById(R.id.layout_transactionpassword);
        PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_ID, GlobalData.getDeviceId(this));
        ShowLoginScreen();

    }

    private void ShowTranscationScreen() {
        layoutLogin.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        layoutTransactionpassword.setVisibility(View.VISIBLE);
    }
    private void ShowLoginScreen() {
        layoutLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        layoutTransactionpassword.setVisibility(View.GONE);
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

    String strEncodeLoginCode = "";
    public static final String TAG_USER_DATA = "user_data";

    public static final String TAG_USER_ID = "user_id";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.LOGIN)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
                    String str_isOtp = "";
                    if (json.has("is_otp")) {
                        str_isOtp = json.getString("is_otp");
                    }

                    if (str_isOtp.equalsIgnoreCase("1")) {
                        strEncodeLoginCode = json.getString("encode_login_id");
                        ShowTranscationScreen();
                    } else {
                        JSONObject data = json.getJSONObject("user_data");
                        String str_name = data.getString("name");
                        String str_user_code = data.getString("user_code");
                        String str_userID = data.getString("user_id");

                        if (data.has("token")) {
                            PreferenceConnector.writeString(svContext, PreferenceConnector.H1, data.getString("token"));
                        }
                        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERNAME, str_name);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINMEMBERID, str_user_code);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINEDUSERID, str_userID);

                        lstUploadData = new LinkedList<>();
                        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
                        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
                        callWebService(ApiInterface.UPDATEFCM, lstUploadData);
                    }

                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.LOGNUSER)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    JSONObject user_data_obj = json.getJSONObject(TAG_USER_DATA);
                    String str_user_id = user_data_obj.getString(TAG_USER_ID);

                    if (user_data_obj.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.H1, user_data_obj.getString("token"));
                    }

                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINEDUSERID, str_user_id);

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
                    callWebService(ApiInterface.UPDATEFCM, lstUploadData);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.UPDATEFCM)) {
//            try {
            ActivitySplash.LoadUserData(result, svContext, true);
//                if (ActivitySplash.LoadUserData(result, svContext)) {

//                } else {
//                JSONObject json = new JSONObject(result);
//                String str_message = json.getString(TAG_MESSAGE);
//                customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
//                PreferenceConnector.cleanPrefrences(svContext);
//                }
//            } catch (JSONException e) {
//                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
//                e.printStackTrace();
//            }
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