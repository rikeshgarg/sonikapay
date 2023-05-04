package com.codunite.sonikapay;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.retrofit.ApiInterface;
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

public class ActivityCommissionWalletPayoutTransfer extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener, MessageListener {
    private EditText edPhone, edName, edAccountNo, edConfirmAccountno, edIfsc, edtxd, edAmount;
    private Button btnSubmitMem, btnCancel;
    private ImageView imgDrop, imgDropOne, imgDropTwo;

    private View[] allViewWithClick = {btnSubmitMem, btnCancel};
    private int[] allViewWithClickId = {R.id.btn_SubmitMem, R.id.btnCancel};

    private EditText[] edTexts = {edPhone, edName, edAccountNo, edConfirmAccountno, edIfsc, edtxd, edAmount};
    private String[] edTextsError = {"Enter mobile", "Enter name", "Enter account no", "Enter confirm account no",
            "Enter ifsc code", "Enter Transaction Password", "Enter amount"};
    private int[] editTextsClickId = {R.id.edtMobile, R.id.edt__acc_name, R.id.edt_acc_no, R.id.edt_cnfrm_acc_no,
            R.id.edt_ifsc, R.id.edt_transaction_pass, R.id.edt_amount_transfer};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_commissionwalletpayouttransfer);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    private RadioGroup rgTxtType;
    private Spinner spinnerBankList;

    public void resumeApp() {
        rgTxtType = (RadioGroup) findViewById(R.id.rg_txttype);
        spinnerBankList = (Spinner) findViewById(R.id.spinner_bank_name);
        UpdateData();
        //     spinnercountrylist = (Spinner) findViewById(R.id.spinner_countrylist);
        //    radioDistributor = (RadioButton) findViewById(R.id.radio_distributor);

        //    spinnerMemberPosition.setAdapter(LegAdapter);

        //    spinnerMemberPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //        @Override
        //        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                strMemberPosition = listSpinner.get(position).split("#:#")[0];
        //         }
//
        //        @Override
        //        public void onNothingSelected(AdapterView<?> parent) {

        //        }
        //     });

//        lstUploadData = new LinkedList<>();
//        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
//        callWebService(ApiInterface.AEPSBANKLIST, lstUploadData);

        ShowRegisterScreen();

        OpenDemoLink();
    }


    private CardView layoutOtp;
    private LinearLayout layoutMain;

    private void ShowRegisterScreen() {
        layoutOtp = findViewById(R.id.layout_transactionpassword);
        layoutMain = findViewById(R.id.linearLayout);
        layoutMain.setVisibility(View.VISIBLE);
        layoutOtp.setVisibility(View.GONE);
    }

    private void ShowOTPScreen() {
        layoutOtp = findViewById(R.id.layout_transactionpassword);
        layoutMain = findViewById(R.id.linearLayout);
        layoutMain.setVisibility(View.GONE);
        layoutOtp.setVisibility(View.VISIBLE);

        TextView tvBackBtn = findViewById(R.id.goback);
        Button verifyBtn = findViewById(R.id.btn_transaction_password);

        tvBackBtn.setOnClickListener(v -> {
            hideKeyboard();
            ShowRegisterScreen();
        });

        verifyBtn.setOnClickListener(v -> {
            hideKeyboard();
            OtpVerifyStart();
        });
    }

    private void OtpVerifyStart() {
        EditText edVerifyOtp = findViewById(R.id.edt_transactionpassword);
        int response = 0;
        response = CheckValidation.emptyEditTextError(new EditText[]{edVerifyOtp}, new String[]{"Enter otp"});

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(edVerifyOtp.getText().toString().trim());
            callWebService(ApiInterface.COMMISSION_WALLET_PAYOUT_OTP_AUTH, lstUploadData);
        }
    }

    private void UpdateData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.USERKYCDETAIL, lstUploadData);
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
            if (!CheckValidation.isPhoneNumberValid(edPhone.getText().toString().trim())) {
                response++;
                edPhone.setError("Invalid Phone no. Id");
            }

            if (!((edAccountNo.getText().toString().trim()).equalsIgnoreCase(edConfirmAccountno.getText().toString().trim()))) {
                response++;
                customToast.showCustomToast(svContext, "Account no. not matching", customToast.ToastyError);

            }
        }

        // if (spinnercountrylist.getSelectedItemPosition() == 0) {
        //    response++;
        //   customToast.showCustomToast(svContext, "Select Country", customToast.ToastyError);
        //  }

        // if (spinnerstatelist.getSelectedItemPosition() == 0) {
        //    response++;
        //      customToast.showCustomToast(svContext, "Select State", customToast.ToastyError);
        //  }


        if (response == 0) {
            String txtType = "RGS";
//            int radioButtonID = rgTxtType.getCheckedRadioButtonId();
//            View radioButton = rgTxtType.findViewById(radioButtonID);
//            if (rgTxtType.indexOfChild(radioButton) == 0) {
//                txtType = "RGS";
//            }else if (rgTxtType.indexOfChild(radioButton) == 1) {
//                txtType = "IFS";
//            } else {
//                txtType = "RTG";
//            }

            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(getEditextValue(edPhone));
            lstUploadData.add(getEditextValue(edName));
            lstUploadData.add(getEditextValue(edAccountNo));
            lstUploadData.add(getEditextValue(edConfirmAccountno));
            lstUploadData.add(getEditextValue(edIfsc));
            lstUploadData.add(getEditextValue(edtxd));
            lstUploadData.add(getEditextValue(edAmount));
            callWebService(ApiInterface.COMMISSION_WALLET_PAYOUT_AUTH, lstUploadData);
        }
    }

    private void PopulateBanbkList() {
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listBankList, true);
        spinnerBankList.setAdapter(LegAdapter);
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
                        //    case R.id.img_drop:
                        //        spinnerMemberPosition.performClick();
                        //        break;
                        //    case R.id.img_drop_1:
                        //      spinnercountrylist.performClick();
                        //       break;
                        //    case R.id.img_drop_2:
                        //      spinnerstatelist.performClick();
                        //        break;
                    }
                }
            });
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edPhone = (EditText) editTexts[0];
        edName = (EditText) editTexts[1];
        edAccountNo = (EditText) editTexts[2];
        edConfirmAccountno = (EditText) editTexts[3];
        edIfsc = (EditText) editTexts[4];
        edtxd = (EditText) editTexts[5];
        edAmount = (EditText) editTexts[6];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityCommissionWalletPayoutTransfer.this);
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
        } else if (url.contains(ApiInterface.COMMISSION_WALLET_PAYOUT_AUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
                    ShowOTPScreen();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.COMMISSION_WALLET_PAYOUT_OTP_AUTH)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
                    Intent svIntent = new Intent(svContext, ActivityCompletion.class);
                    svIntent.putExtra("from_act", "payout");
                    startActivity(svIntent);
                    finish();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.USERKYCDETAIL)) {
            String strStatus = "Not Applied";
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_status = json.getString("status");

                if (str_status.equalsIgnoreCase("1")) {
                    if (json.has("data")) {
                        JSONObject data = json.getJSONObject("data");
                        edName.setText(data.getString("ac_holder_name"));
                        edAccountNo.setText(data.getString("ac_no"));
                        edIfsc.setText(data.getString("ifsc"));
                        edConfirmAccountno.setText(data.getString("ac_no"));

                        strStatus = data.getString("status");
                    }
                } else {
//                 "aadhar_front":"https:\/\/www.rupaymine.com\/media\/kyc_document\/16184787669646.jpg","aadhar_back":"https:\/\/www.rupaymine.com\/media\/kyc_document\/16184787663256.jpg","pan_card":"https:\/\/www.rupaymine.com\/media\/kyc_document\/16184787664853.jpg","status":"Pending"}}
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);


//                    if (strStatus.equalsIgnoreCase("Pending")) {
//
//                    } else if (strStatus.equalsIgnoreCase("Approved")) {
//                    } else {
//                    }

//                    TextView txStatus = ((TextView) findViewById(R.id.txt_kyc_status));
//                    if (strStatus.equalsIgnoreCase("Pending")) {
//                        txStatus.setText("Status of your KYC is: " + strStatus);
//                        tx///Status.setTextColor(getResources().getColor(R.color.orange_900));
//                    } else if (strStatus.equalsIgnoreCase("Approved")) {
//                        txStatus.setText("Status of your KYC is: " + strStatus);
//                        txStatus.setTextColor(getResources().getColor(R.color.green_900));
//                    } else {
//                        txStatus.setText("Status of your KYC is: " + strStatus);
//                        txStatus.setTextColor(getResources().getColor(R.color.red_900));
//                    }
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

        if (url.contains(ApiInterface.AEPSBANKLIST)) {
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

                    PopulateBanbkList();
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
        txtHeading.setText("Commission Payout Transfer");

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

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("15");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}