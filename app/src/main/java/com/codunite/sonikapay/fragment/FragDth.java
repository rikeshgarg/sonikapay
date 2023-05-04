package com.codunite.sonikapay.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.retrofit.ApiInterface;
import com.codunite.sonikapay.ActivityCompletion;
import com.codunite.sonikapay.ActivityPlansOfferDth;
import com.codunite.sonikapay.ActivityRecharge;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
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

public class FragDth extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private TextView txtViewAllPlans, txtROffers;
    private RelativeLayout layOperator, layCircle;
    private Button btnDthrecharge, btnCancelOtp, btnOtpVerify;
    private EditText edCardNumber;
    public static EditText edDthRechargeAmount;
    private EditText[] edDthRecharge = {edCardNumber, edDthRechargeAmount};
    private String[] edDthTextsError = {"Enter card number", "Enter amount"};
    private int[] editTextDthClickId = {R.id.dth_cardnumber, R.id.dth_amount};

    private View[] allViewWithClick = {btnDthrecharge, layOperator, layCircle, txtViewAllPlans, txtROffers, btnCancelOtp, btnOtpVerify};
    private int[] allViewWithClickId = {R.id.btn_dthrecharge, R.id.lay_operator,
            R.id.lay_circle, R.id.txt_viewallplans, R.id.txt_roffers, R.id.btn_otpcancel, R.id.btn_otpauth};

    public FragDth() {}

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_dth, container, false);
        }
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            aiView = getView();

            OnClickCombineDeclare(allViewWithClick);
            EditTextDeclare(edDthRecharge);
            resumeApp();
        }

    }

    private int animation_type = ItemAnimation.RIGHT_LEFT;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                break;
            default:
                break;
        }
    }

    public void resumeApp() {
        edDthRechargeAmount = (EditText) aiView.findViewById(R.id.dth_amount);
        spinnerDthOperatorList = (Spinner) aiView.findViewById(R.id.spinner_dthoperatorlist);

        edtOtp = aiView.findViewById(R.id.edt_otp);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebServiceWithoutLoader(ApiInterface.CIRCLELIST, lstUploadData);

        hideOtpLayout();
    }

    private void LoadOperatorList(String rechargeType) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(rechargeType);
        callWebServiceWithoutLoader(ApiInterface.OPERATORLIST, lstUploadData);
    }

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }


    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = aiView.findViewById(editTextDthClickId[j]);
        }

        edCardNumber = (EditText) editTexts[0];
        edDthRechargeAmount = (EditText) editTexts[1];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_otpauth:
                            RechargeProcess();
                            break;
                        case R.id.btn_dthrecharge:
                            if (listSpinnerOperatorList.size() != 0) {
                                DthRecharge();
                            }
                            break;
                        case R.id.txt_viewallplans:
                            if (listSpinnerOperatorList.size() != 0) {
                                strDthMobile = edCardNumber.getText().toString().trim();
                                strDthOperatorCode = (listSpinnerOperatorList.get(spinnerDthOperatorList.getSelectedItemPosition()).split("#:#")[0]);

                                Intent svIntent = new Intent(svContext, ActivityPlansOfferDth.class);
                                svIntent.putExtra("filename", "plans_d2h");
                                svContext.startActivity(svIntent);
                            }
                            break;
                        case R.id.txt_roffers:
                            if (listSpinnerOperatorList.size() != 0) {
                                ViewROffers();
                            }
                            break;
                        case R.id.btn_otpcancel:
                            hideOtpLayout();
                            break;
                        case R.id.lay_operator:
                            spinnerDthOperatorList.performClick();
                            break;
                        case R.id.lay_circle:
                            spinnerDthCircleList.performClick();
                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    private void ViewROffers() {
        int response = 0;
//        response = CheckValidation.emptyEditTextError(edDthRecharge, edDthTextsError);

        if (spinnerDthOperatorList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select operator", customToast.ToastyError);
        }

        if ((edCardNumber.getText().toString().trim()).length() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please enter correct card number", customToast.ToastyError);
        }

//        if ((edDthRechargeAmount.getText().toString().trim()).length() == 1) {
//            response++;
//            customToast.showCustomToast(svContext, "Please enter at least 10 Rs", customToast.ToastyError);
//        }

        if (response == 0) {
            strDthMobile = edCardNumber.getText().toString().trim();
            strDthOperatorCode = (listSpinnerOperatorList.get(spinnerDthOperatorList.getSelectedItemPosition()).split("#:#")[0]);

            lstUploadData = new LinkedList<>();
            lstUploadData.add(strDthMobile);
            lstUploadData.add(strDthOperatorCode);
            callWebService(ApiInterface.VIEWALLDTHPLANS, lstUploadData);

//            Intent svIntent = new Intent(svContext, ActivityPlansOfferDth.class);
//            svIntent.putExtra("filename", "offer_d2h");
//            svContext.startActivity(svIntent);
        }
    }

    public static String strDthMobile, strDthOperatorCode;

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
        }

        GlobalData.SetLanguage(svContext);
        if (checkNetwork.isConnectingToInternet()) {
            NoInternetScreen.hideError();
        } else {
            NoInternetScreen.showInternetError();
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

    private List<String> listSpinnerCircleList = new ArrayList<>();
    private List<String> listSpinnerOperatorList = new ArrayList<>();
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.OPERATORLIST)) {
            try {
                listSpinnerOperatorList = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                listSpinnerOperatorList.add("-1" + "#:#" + "Select Operator");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("code");
                        String str_name = data_obj.getString("name");

                        listSpinnerOperatorList.add(str_code + "#:#" + str_name);
                    }

                    PopulateDthOperatorList();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.VIEWALLDTHPLANS)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
//                {"status":1,"message":"Success","rechargeAmount":0,"balance":0,"customerName":""}
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    TextView txtCustomerName, txtAvailBal;
                    txtCustomerName = (TextView)aiView.findViewById(R.id.txt_name);
                    txtAvailBal = (TextView)aiView.findViewById(R.id.txt_availbalance);

                    edDthRechargeAmount.setText(json.getString("rechargeAmount"));
                    txtCustomerName.setText(json.getString("customerName"));
                    txtAvailBal.setText("Available Bal - " +GlobalVariables.CURRENCYSYMBOL+json.getString("balance"));
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }else if (url.contains(ApiInterface.CIRCLELIST)) {
            try {
                listSpinnerCircleList = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                listSpinnerCircleList.add("-1" + "#:#" + "Select Circle");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("code");
                        String str_name = data_obj.getString("name");

                        listSpinnerCircleList.add(str_code + "#:#" + str_name);
                    }
                }
                LoadOperatorList("dth");
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            PopulateDthCircleList();
        } else if (url.contains(ApiInterface.RECHARGEAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    edCardNumber.setText("");
                    edDthRechargeAmount.setText("");
                    spinnerDthOperatorList.setSelection(0);

                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);

                    Intent svIntent = new Intent(svContext, ActivityCompletion.class);
                    svIntent.putExtra("from_act", "recharge");
                    startActivity(svIntent);
                    ((Activity)svContext).finish();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.RECHARGEOTPAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);

//                    ShowPreviousLayout(layoutMain);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    public void ShowOtpLayout() {
        ((LinearLayout) aiView.findViewById(R.id.card_dth)).setVisibility(View.GONE);
        ((LinearLayout) aiView.findViewById(R.id.card_otp)).setVisibility(View.VISIBLE);

    }

    private void hideOtpLayout() {
        ((LinearLayout) aiView.findViewById(R.id.card_dth)).setVisibility(View.VISIBLE);
        ((LinearLayout) aiView.findViewById(R.id.card_otp)).setVisibility(View.GONE);
    }

    private Spinner spinnerDthCircleList, spinnerDthOperatorList;

    private void PopulateDthCircleList() {
        spinnerDthCircleList = (Spinner) aiView.findViewById(R.id.spinner_dthcirclelist);

        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerCircleList, true);
        spinnerDthCircleList.setAdapter(spindapter);

        spinnerDthCircleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        PopulateDthOperatorList();
    }

    private void PopulateDthOperatorList() {
        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerOperatorList, true);
        spinnerDthOperatorList.setAdapter(spindapter);

        spinnerDthOperatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DthRecharge() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edDthRecharge, edDthTextsError);

        if (spinnerDthOperatorList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select operator", customToast.ToastyError);
        }

//        if (spinnerDthCircleList.getSelectedItemPosition() == 0) {
//            response++;
//            customToast.showCustomToast(svContext, "Please select circle", customToast.ToastyError);
//        }

        if ((edDthRechargeAmount.getText().toString().trim()).length() == 1) {
            response++;
            customToast.showCustomToast(svContext, "Please enter at least 10 Rs", customToast.ToastyError);
        }

        String type = "2", strRechargePostType = "1";

        String strMessage = "DTH Recharge\nDTH Id: " + edCardNumber.getText().toString().trim() + "\nOperator: " +
                (listSpinnerOperatorList.get(spinnerDthOperatorList.getSelectedItemPosition()).split("#:#")[1]) +
                "\nAmount: " + edDthRechargeAmount.getText().toString().trim();
        if (response == 0) {
            ((ActivityRecharge)getActivity()).ShowConfirmDialog(svContext, strMessage, "dth");
        }
    }

    public void ConfirmRecharge(){
        int amount = Integer.parseInt(edDthRechargeAmount.getText().toString().trim());
        boolean isWalletLoading = ((ActivityRecharge)getActivity()).checkRWalletAndAddWallet(amount, "dth");
        if (isWalletLoading) {
            customToast.showCustomToast(svContext, "Insufficient fund", customToast.SweetAlertFailed);
        }else{
            ShowOtpLayout();
        }
    }

    EditText edtOtp;
    public void RechargeProcess() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add((edCardNumber.getText().toString().trim()));
        lstUploadData.add((listSpinnerOperatorList.get(spinnerDthOperatorList.getSelectedItemPosition()).split("#:#")[0]));
        lstUploadData.add((listSpinnerCircleList.get(spinnerDthCircleList.getSelectedItemPosition()).split("#:#")[0]));
        lstUploadData.add(edDthRechargeAmount.getText().toString().trim());
        lstUploadData.add("2");
        lstUploadData.add("");
        lstUploadData.add(edtOtp.getText().toString().trim());
        callWebService(ApiInterface.RECHARGEAUTH, lstUploadData);
    }

//    private void OtpVerify() {
//        PinView edtOtp = (PinView) aiView.findViewById(R.id.edt_otp);
//        if (edtOtp.length() == 0) {
//            customToast.showCustomToast(svContext, "Please enter otp", customToast.ToastyError);
//        } else {
//            lstUploadData = new LinkedList<>();
//            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
//            lstUploadData.add(edtOtp.getText().toString().trim());
//            callWebService(ApiInterface.RECHARGEOTPAUTH, lstUploadData);
//        }
//    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }
}