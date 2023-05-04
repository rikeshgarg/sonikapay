package com.codunite.sonikapay.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.ActivitySplash;
import com.codunite.sonikapay.R;
import com.retrofit.ApiInterface;
import com.chaos.view.PinView;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.customfont.FontUtils;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener, WebServiceListener {

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;
    private Button btnFogetPasswordClick, btnSubmitTranscationPwd, btnOtpSubmit, bntSubmitResetPassword;
    private View[] allViewWithClick = {btnSubmitTranscationPwd, btnOtpSubmit,
            bntSubmitResetPassword, btnFogetPasswordClick};
    private int[] allViewWithClickId = {R.id.btn_transaction_password, R.id.btn_forgotOTPAuth_process,
            R.id.btn_resetPassword, R.id.btn_forgetpassword_process};

    private EditText edMobileNumber, edNewPawwword, edtConfirmPwd, edtTrancationPwd;
    private EditText[] edTexts = {edMobileNumber, edNewPawwword, edtConfirmPwd, edtTrancationPwd};
    private String[] edTextsError = {"Enter member id", "Enter password", "Re-enter password",
            "Enter transcation password"};
    private int[] editTextsClickId = {R.id.edt_forgetpass, R.id.edt_newpassword, R.id.edt_newRepassword,
            R.id.edt_transactionpassword};

    private CardView layoutForgotOTPAuth, layoutTransactionpassword, layoutResetpassword, layoutForgetpassword;
    private PinView edtOtp;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_forgotpassword);
        StartApp();
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        resumeApp();
    }

    public void resumeApp() {
        layoutForgotOTPAuth = (CardView) findViewById(R.id.layout_forgotOTPAuth);
        layoutTransactionpassword = (CardView) findViewById(R.id.layout_transactionpassword);
        layoutResetpassword = (CardView) findViewById(R.id.layout_resetpassword);
        layoutForgetpassword = (CardView) findViewById(R.id.layout_forgetpassword);
        edtOtp = (PinView) findViewById(R.id.edt_forgotOTPAuth);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }

        edMobileNumber = editTexts[0];
        edNewPawwword = editTexts[1];
        edtConfirmPwd = editTexts[2];
        edtTrancationPwd = editTexts[3];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_forgetpassword_process:
                            SendOtpToMobile();
                            break;
                        case R.id.btn_resetPassword:
                            UpdatePassword();
                            break;
                        case R.id.btn_forgotOTPAuth_process:
                            OtpVerify();
                            break;
                        case R.id.btn_transaction_password:
                            LoginStart();
                            break;
                    }
                }
            });
        }
    }

    private void ShowOtpScreen() {
        layoutForgotOTPAuth.setVisibility(View.VISIBLE);
        layoutForgetpassword.setVisibility(View.INVISIBLE);
    }

    private void ShowResetPwdScreen() {
        layoutResetpassword.setVisibility(View.VISIBLE);
        layoutForgotOTPAuth.setVisibility(View.INVISIBLE);
    }

    private void ShowTranscationScreen() {
//        layoutTransactionpassword.setVisibility(View.VISIBLE);
//        layoutResetpassword.setVisibility(View.INVISIBLE);
        UpdatePassword();
    }

    private void SendOtpToMobile() {
        if ((getEditextValue(edMobileNumber)).length() == 0) {
            customToast.showCustomToast(svContext, "Please enter member id", customToast.ToastyError);
        } else {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(getEditextValue(edMobileNumber));
            callWebService(ApiInterface.FORGOTPWDAUTH, lstUploadData);
        }
    }

    private void OtpVerify() {
        if (edtOtp.length() == 0) {
            customToast.showCustomToast(svContext, "Please enter otp", customToast.ToastyError);
        } else {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(edtOtp.getText().toString().trim());
//        lstUploadData.add(strEncodeLoginCode);
            callWebService(ApiInterface.FORGOTPWDOTPVERIFY, lstUploadData);
        }
    }

    private void UpdatePassword() {
        if ((getEditextValue(edNewPawwword)).length() == 0 || (getEditextValue(edtConfirmPwd)).length() == 0) {
            customToast.showCustomToast(svContext, "Please enter password", customToast.ToastyError);
        } else if ((getEditextValue(edNewPawwword)).equals(getEditextValue(edtConfirmPwd))) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(userId);
            lstUploadData.add(getEditextValue(edNewPawwword));
            lstUploadData.add(getEditextValue(edtConfirmPwd));
            callWebService(ApiInterface.FORGOTUPDATEPWD, lstUploadData);
        } else {
            customToast.showCustomToast(svContext, "password not matching", customToast.ToastyError);
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

    private void LoginStart() {

    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActivityForgotPassword.this);
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
        txtHeading.setText(getString(R.string.toolbar_forgotpwd));

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));

        LinearLayout imgToolBarWallet = (LinearLayout) findViewById(R.id.img_wallet);
        imgToolBarWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.codunite.sonikapay.ActivitySplash.OpenWalletActivity(svContext);
            }
        });
        imgToolBarWallet.setVisibility(View.GONE);

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgToolBareWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpeneWalletActivity(svContext);
            }
        });
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
        Log.e("errror>>>", result);
        if (url.contains(ApiInterface.FORGOTPWDAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    ShowOtpScreen();
                }

            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.FORGOTPWDOTPVERIFY)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    userId = json.getString("userID");
                    ShowResetPwdScreen();
                }

            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.FORGOTUPDATEPWD)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);

                    Intent svIntent = new Intent(svContext, ActivityLogin.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }

            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
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