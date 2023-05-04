package com.codunite.sonikapay.bbps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.ActivitySplash;
import com.codunite.sonikapay.R;
import com.codunite.model.ParamDataModel;
import com.codunite.sonikapay.WebViewActivity;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
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

public class ActivityBbpsTollTax extends AppCompatActivity implements OnClickListener, WebServiceListener {
    private List<String> listSpinnerOperatorList = new ArrayList<>();
    private List<String> listSpinnerShowData = new ArrayList<>();

    private Button btnElectricRecharge, btnFetch, btnOtpVerify, btnCancelOtp;
    private ImageView imgDropOperator;
    private TextView txtUserName;

    private EditText edRechargeAmount;
    private EditText[] edRecharge = {edRechargeAmount};
    private String[] edTextsError = {"Enter recharge amount"};
    private int[] editTextDthClickId = {R.id.electricity_amount};

    private View[] allViewWithClick = {btnElectricRecharge, btnFetch, imgDropOperator, btnOtpVerify, btnCancelOtp};
    private int[] allViewWithClickId = {R.id.btn_electricrecharge, R.id.btn_fetch, R.id.img_drop_1, R.id.btn_otpcancel, R.id.btn_otpauth};

    private String str_minLength;
    private LinearLayout layBiller;
    private LinearLayout item;

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    private EditText edtOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bbpselectricitytoll);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edRecharge);

        resumeApp();
    }

    private int animation_type = ItemAnimation.RIGHT_LEFT;

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

    public void resumeApp() {
        edtOtp = findViewById(R.id.edt_otp);
        layBiller = (LinearLayout) findViewById(R.id.lay_biller);
        layBiller.setVisibility(View.INVISIBLE);
        item = (LinearLayout) findViewById(R.id.lay_dynamic_lay);
        txtUserName = (TextView) findViewById(R.id.txt_username);
        spinnerOperatorList = (Spinner) findViewById(R.id.spinner_electricoperatorlist);
        LoadOperatorList();

        hideOtpLayout();
        OpenDemoLink();
    }

    @Override
    public void onResume() {
        super.onResume();
//      edKnumber.setText("");
//        layBiller.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private void LoadOperatorList() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETBBSPFASTTAGOPERATOR, lstUploadData);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextDthClickId[j]);
        }
        edRechargeAmount = (EditText) editTexts[0];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_electricrecharge:
                            EletricRecharge();
                            break;
                        case R.id.btn_fetch:
                            if (lstEditext != null && lstEditext.size() > 0) {
                                FetchBill(lstEditext.get(0));
                            }
                            break;
                        case R.id.btn_otpcancel:
                            hideOtpLayout();
                            break;
                        case R.id.btn_otpauth:
                            if (actReferTo.equalsIgnoreCase("electric")) {
                                RechargeProcess(svContext);
                            } else if (actReferTo.equalsIgnoreCase("Toll")) {
                                RechargeProcess(svContext);
                            } else if (actReferTo.equalsIgnoreCase("Service")) {
                                RechargeProcess(svContext);
                            }
                            break;
                        case R.id.img_drop_1:
                            spinnerOperatorList.performClick();
                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    private void FetchBill(View v) {
        int edtTextxId = v.getId();
        if (spinnerOperatorList.getSelectedItemPosition() != 0 &&
                ((EditText) v).getText().toString().trim().length() != 0) {

            String paramOne = "", paramTwo = "", paramThree = "";

            paramOne = lstEditext.get(0).getText().toString().trim();

            if (lstEditext.size() > 1) {
                paramTwo = lstEditext.get(1).getText().toString().trim();
            }

            if (lstEditext.size() > 2) {
                paramThree = lstEditext.get(2).getText().toString().trim();
            }


            if (edtTextxId == 0) {
                strRechargeNumber = ((EditText) v).getText().toString().trim();
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                lstUploadData.add(strOperatorCode);
                lstUploadData.add(paramOne);
                lstUploadData.add(paramTwo);
                lstUploadData.add(paramThree);
                callWebService(ApiInterface.GETBBSPFASTTAGBILLFETCH, lstUploadData);
            }
        }
    }


    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
        }

        GlobalData.SetLanguage(svContext);
        if (checkNetwork.isConnectingToInternet()) {
//            NoInternetScreen.hideError();
        } else {
//            NoInternetScreen.showInternetError();
        }
        loadToolBar();
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("BBPS Toll Tax");

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));

        LinearLayout imgToolBarWallet = (LinearLayout) findViewById(R.id.img_wallet);
        imgToolBarWallet.setOnClickListener(new OnClickListener() {
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


    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    LinkedList<String> lstUploadData = new LinkedList<>();

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_FIELDNAME = "fieldName";
    public static final String TAG_FIELDOTHER = "fieldOther";
    public static final String TAG_MINLENGTH = "minLength";
    public static final String TAG_MAXLENGTH = "maxLength";
    public static final String TAG_AMOUNT = "amount";
    private String str_amount = "", str_customername = "";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETBBSPFASTTAGOPERATOR)) {
            try {
                listSpinnerOperatorList = new ArrayList<>();
                listSpinnerShowData = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                listSpinnerOperatorList.add("-1" + "#:#" + "Select Biller");

                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("biller_id");
                        String str_name = data_obj.getString("billerName");
                        String str_billerAliasName = data_obj.getString("billerAliasName");
                        String is_fetch = data_obj.getString("is_fetch");

                        listSpinnerOperatorList.add(str_code + "#:#" + str_name);
                        listSpinnerShowData.add(str_code);
                    }
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }

                PopulateEltricOperatorList();
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETBBSPFASTTAGBILLPAY)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {

                    txtUserName.setText("");
                    edRechargeAmount.setText("");
//                    edKnumber.setText("");
//                    edSecondValue.setText("");
                    spinnerOperatorList.setSelection(0);
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);
                    Intent svIntent = new Intent(svContext, ActivityBbpsHistory.class);
                    startActivity(svIntent);
                    ((Activity) svContext).finish();

                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETBBSPFASTAGFORM)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_json_status = json.getString(TAG_STATUS);

                if (str_json_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, json.getString("msg"), customToast.ToastyError);
                } else {
                    txtUserName.setText("");
                    edRechargeAmount.setText("");
                    String str_status = json.getString(TAG_STATUS);
                    if (str_status.equalsIgnoreCase("1")) {
                        JSONArray data_obj = json.getJSONArray(TAG_DATA);
                        if (data_obj.length() != 0) {
                            lstParam = new ArrayList<>();
                            for (int i = 0; i < data_obj.length(); i++) {
//                              {"fieldKey":"para1","paramName":"K Number","datatype":"NUMERIC","minlength":12,"maxlength":12,"optional":0}]}
                                JSONObject jsonObj = data_obj.getJSONObject(i);
                                lstParam.add(new ParamDataModel(jsonObj.getString("fieldKey"),
                                        jsonObj.getString("paramName"), jsonObj.getString("datatype"),
                                        jsonObj.getString("minlength"), jsonObj.getString("maxlength"),
                                        jsonObj.getString("optional")));

                                layBiller.setVisibility(View.VISIBLE);
                            }

                            AttachDynamicLay();
                        } else {
                            customToast.showCustomToast(svContext, "", customToast.ToastyError);
                        }
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        } else if (url.contains(ApiInterface.GETBBSPFASTTAGBILLFETCH)) {

            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_json_status = json.getString(TAG_STATUS);

                if (str_json_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {

                    txtUserName.setText("");
                    edRechargeAmount.setText("");
                    if (json.has("amount")) {
                        str_amount = json.getString("amount");
                    } else {
                        str_amount = "";
                    }
                    if (json.has("accountHolderName")) {
                        str_customername = json.getString("accountHolderName");
                    } else {
                        str_customername = "";
                    }

                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            txtUserName.setText(str_customername);
            edRechargeAmount.setText(str_amount);
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

    List<ParamDataModel> lstParam;
    List<EditText> lstEditext;
    String strRechargeNumber = "";

    private void AttachDynamicLay() {
        lstEditext = new ArrayList<>();
        for (int i = 0; i < lstParam.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.item_dynamiclay, null);
            EditText edItem = (child).findViewById(R.id.param_name);
            edItem.setHint(lstParam.get(i).getParamName());
//            edItem.setFilters(new InputFilter[]{new InputFilterMinMax(lstParam.get(i).getMinlength(),
//                    lstParam.get(i).getMaxlength())});

            edItem.setId(i);
            edItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int edtTextxId = v.getId();
                        if (spinnerOperatorList.getSelectedItemPosition() != 0 &&
                                ((EditText) v).getText().toString().trim().length() != 0) {

                            String paramOne = "", paramTwo = "", paramThree = "";

                            paramOne = lstEditext.get(0).getText().toString().trim();

                            if (lstEditext.size() > 1) {
                                paramTwo = lstEditext.get(1).getText().toString().trim();
                            }

                            if (lstEditext.size() > 2) {
                                paramThree = lstEditext.get(2).getText().toString().trim();
                            }


                            if (edtTextxId == 0) {
                                strRechargeNumber = ((EditText) v).getText().toString().trim();
                                lstUploadData = new LinkedList<>();
                                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                                lstUploadData.add(strOperatorCode);
                                lstUploadData.add(paramOne);
                                lstUploadData.add(paramTwo);
                                lstUploadData.add(paramThree);
                                callWebService(ApiInterface.GETBBSPFASTTAGBILLFETCH, lstUploadData);
                            }
                        }
                    }
                }
            });
            lstEditext.add(edItem);
            item.addView(child);
        }


    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    private void ShowOtpLayout() {
        ((LinearLayout) findViewById(R.id.card_electrical)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.card_otp)).setVisibility(View.VISIBLE);
    }

    private void hideOtpLayout() {
        ((LinearLayout) findViewById(R.id.card_electrical)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.card_otp)).setVisibility(View.GONE);
    }

    Spinner spinnerOperatorList;

    private void PopulateEltricOperatorList() {
        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerOperatorList, true);
        spinnerOperatorList.setAdapter(spindapter);

        spinnerOperatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strOperatorCode = (listSpinnerOperatorList.get(i)).split("#:#")[0];
                item.removeAllViews();
                if (!strOperatorCode.equalsIgnoreCase("-1")) {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(strOperatorCode);
                    callWebService(ApiInterface.GETBBSPFASTAGFORM, lstUploadData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String strOperatorCode;

    private void EletricRecharge() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edRecharge, edTextsError);

        if (spinnerOperatorList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select operator", customToast.ToastyError);
        }

        if ((edRechargeAmount.getText().toString().trim()).length() == 1) {
            response++;
            customToast.showCustomToast(svContext, "Please enter at least 10 Rs", customToast.ToastyError);
        }

        if (response == 0) {
            if (strRechargeNumber.length() == 0) {
                response++;
                customToast.showCustomToast(svContext, "Please enter correct value", customToast.ToastyError);
            }
        }


        if (response == 0) {
            ShowOtpLayout();
        }
    }

    String actReferTo = "";
    Context con;

    public boolean checkEWalletAndAddWallet(Float rechargeAmount, String actRefer, Context svContext) {
        actReferTo = actRefer;
        con = svContext;
        boolean isWalletToLoad = false;
        float requiredAmountToAdd = 0;
        float walletAmount = Float.parseFloat(PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, ""));
        if (rechargeAmount > walletAmount) {
            requiredAmountToAdd = rechargeAmount - walletAmount;
            isWalletToLoad = true;
        } else {
            requiredAmountToAdd = 0;
            isWalletToLoad = false;
        }

        ProcessRecharge(svContext);
        return isWalletToLoad;
    }

    private void ProcessRecharge(Context svContext) {
       ShowOtpLayout();
    }
    private String addAmountToWallet;
    private static final String TAG = "Add To Wallet";

    public void RechargeProcess(Context svContext) {
        String paramOne = "", paramTwo = "", paramThree = "";

        paramOne = lstEditext.get(0).getText().toString().trim();
        if (lstEditext.size() > 1) {
            paramTwo = lstEditext.get(1).getText().toString().trim();
        }
        if (lstEditext.size() > 2) {
            paramThree = lstEditext.get(2).getText().toString().trim();
        }

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(strOperatorCode);
        lstUploadData.add(paramOne);
        lstUploadData.add(paramTwo);
        lstUploadData.add(paramThree);
        lstUploadData.add(edRechargeAmount.getText().toString().trim());
        lstUploadData.add(edtOtp.getText().toString().trim());
        callWebService(ApiInterface.GETBBSPFASTTAGBILLPAY, lstUploadData);
    }

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("4");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}