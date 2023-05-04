package com.codunite.sonikapay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codunite.sonikapay.activity.ActivityLogin;
import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.MessageListener;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener, WebServiceListener, MessageListener {
    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;
    private EditText edName, edEmail, edPhone, edReferCode, edtPanCard, edPassword, edRePassword, edTranscationPassword, edOtp;
    private Button btnSignIn, btnSignUp;
    private View viewOtp, layoutLogin;
    private int[] allViewWithClickId = {R.id.btn_loginIn, R.id.btnSignUp, R.id.goback, R.id.btn_transaction_password};
    private EditText[] edTexts = {edName, edEmail, edPhone, edPassword, edRePassword, edTranscationPassword, edReferCode, edtPanCard};
    private String[] edTextsError = {"Enter name", "Enter email", "Enter mobile", "Enter password", "Enter password again", "Enter transcation password", "Enter refer code",
            "Enter Pan Card"};
    private int[] editTextsClickId = {R.id.edt_name, R.id.edtEmail, R.id.edtMobile, R.id.edt_password, R.id.edt_repassword, R.id.edt_transaction_password,
            R.id.edt_refercode, R.id.edt_pancard};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        StartApp();
        //Start Coding from here
        OnClickCombineDeclare(allViewWithClickId);
        EditTextDeclare(edTexts);
        resumeApp();
    }
    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActivitySignUp.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
    }
    public void resumeApp() {
        viewOtp = findViewById(R.id.layout_transactionpassword);
        layoutLogin = findViewById(R.id.layoutLogin);
        setFocus(getEditext(edName));
        layoutLogin.setVisibility(View.VISIBLE);
        viewOtp.setVisibility(View.GONE);
        txtUsername = (TextView) findViewById(R.id.member_username);
        pbLoadOperator = (ProgressBar) findViewById(R.id.progressbar_load_two);
        pbLoadOperator.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            edReferCode.setText(extras.getString("refercode"));
        }
    }

    private void setFocus(EditText edTxt) {
        edTxt.setFocusable(true);
        edTxt.requestFocus();
    }

    private void SubmitSignUpForm() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if ((edPhone.getText().toString().trim()).length() != 10) {
            response++;
            edPhone.setError("Invalid mobile number");
        }

        if (!CheckValidation.isEmailValid(edEmail.getText().toString().trim())) {
            response++;
            edEmail.setError("Invalid email id");
        }

        if ((edReferCode.getText().toString().trim()).length() == 0) {
            response++;
//            ShowConfirmWithoutReferCode("", "You don't entered refer code ",
//                    "Are you sure you want to register account without refer code? Cancel if you want " +
//                            "to enter refer code or register if you don't have refer code");
            edReferCode.setError("Please enter refer code");
        }

        if (!(edPassword.getText().toString().trim()).equals(edRePassword.getText().toString().trim())) {
            response++;
            edRePassword.setError("Password not matching");
        }
        if (response == 0) {
            RegisterUser();
        }
    }

    private void RegisterUser() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(getEditextValue(edName));
        lstUploadData.add(getEditextValue(edPhone));
        lstUploadData.add(getEditextValue(edEmail));
        lstUploadData.add(edReferCode.getText().toString().trim());
        lstUploadData.add(getEditextValue(edPassword));
        lstUploadData.add(getEditextValue(edTranscationPassword));
        lstUploadData.add(getEditextValue(edtPanCard));
        callWebService(ApiInterface.SIGNUP, lstUploadData);
    }

    private void ShowConfirmWithoutReferCode(String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        TextView textHead = dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setText("Enter Refer code");
        declineDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setText("Register");
        confirmDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
        dialog.show();
    }


    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (findViewById(allViewWithClickId[j])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_loginIn:
                            Intent svIntent = new Intent(svContext, ActivityLogin.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                            break;
                        case R.id.btnSignUp:
                            SubmitSignUpForm();
                            break;
                        case R.id.goback:
                            layoutLogin.setVisibility(View.VISIBLE);
                            viewOtp.setVisibility(View.GONE);
                            break;
                        case R.id.btn_transaction_password:
                            if ((edPhone.getText().toString().trim()).length() == 0) {
                                edPhone.setError("Please enter otp");
                            } else {
                                lstUploadData = new LinkedList<>();
                                lstUploadData.add(edOtp.getText().toString().trim());
                                lstUploadData.add(encodedTPin);
                                callWebService(ApiInterface.VERIFYSIGNUPOTP, lstUploadData);
                            }
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
        edName = (EditText) editTexts[0];
        edEmail = (EditText) editTexts[1];
        edPhone = (EditText) editTexts[2];
        edPassword = (EditText) editTexts[3];
        edRePassword = (EditText) editTexts[4];
        edTranscationPassword = (EditText) editTexts[5];
        edOtp = findViewById(R.id.edt_transactionpassword);
        edReferCode = (EditText) editTexts[6];
        edtPanCard = (EditText) editTexts[7];

        edReferCode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edReferCode.getText().toString().trim()).length() == 10) {
                    txtUsername.setVisibility(View.INVISIBLE);
                    pbLoadOperator.setVisibility(View.VISIBLE);

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edReferCode.getText().toString().trim());
                    callWebServiceWithoutLoader(ApiInterface.GETUSERNAME, lstUploadData);
                }
            }
        });
    }

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private TextView txtUsername;
    private ProgressBar pbLoadOperator;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    public void messageReceived(String message) {
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
    }

    LinkedList<String> lstUploadData = new LinkedList<>();

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private String encodedTPin = "";

    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.SIGNUP)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
//                    encodedTPin = json.getString("encrypt_otp_code");
//                    layoutLogin.setVisibility(View.GONE);
//                    viewOtp.setVisibility(View.VISIBLE);

                    TextView txtMemberId = (TextView) findViewById(R.id.txt_loginerror);
                    txtMemberId.setText(str_msg);
                    EmptyData(edTexts);

                    if (json.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.H1, json.getString("token"));
                    }
                    JSONObject data = json.getJSONObject("user_data");
                    String str_name = data.getString("name");
                    String str_user_code = data.getString("user_code");
                    String str_userID = data.getString("user_id");

                    if (data.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.H1, data.getString("token"));
                    }

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(str_userID);
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
                    callWebService(ApiInterface.UPDATEFCM, lstUploadData);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETUSERNAME)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    pbLoadOperator.setVisibility(View.GONE);
                    txtUsername.setVisibility(View.VISIBLE);
                    if (json.has("member_name")) {
                        txtUsername.setText(json.getString("member_name"));
                        txtUsername.setTextColor(getResources().getColor(R.color.c_black));
                    } else {
                        txtUsername.setText(str_message);
                        txtUsername.setTextColor(getResources().getColor(R.color.red_400));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.VERIFYSIGNUPOTP)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
                    TextView txtMemberId = (TextView) findViewById(R.id.txt_loginerror);
                    txtMemberId.setText(str_msg);
                    EmptyData(edTexts);

                    JSONObject data = json.getJSONObject("user_data");
                    String str_name = data.getString("name");
                    String str_user_code = data.getString("user_code");
                    String str_userID = data.getString("user_id");

                    if (data.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.H1, data.getString("token"));
                    }

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(str_userID);
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
                    callWebService(ApiInterface.UPDATEFCM, lstUploadData);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.UPDATEFCM)) {
            ActivitySplash.LoadUserData(result, svContext, true);
            onBackPressed();
        }
    }

    private void EmptyData(EditText[] edTexts) {
        for (int i = 0; i < edTexts.length; i++) {
            edTexts[i].setText("");
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
        super.onBackPressed();
    }
}