package com.codunite.sonikapay.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.retrofit.ApiInterface;
import com.codunite.sonikapay.ActivityCompletion;
import com.codunite.sonikapay.ActivityPlansOffer;
import com.codunite.sonikapay.ActivityRecharge;
import com.codunite.sonikapay.R;
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

public class FragPrePostRecharge extends Fragment implements OnClickListener, WebServiceListener {
    public static EditText edRechargeAmount;
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private RelativeLayout layOperator, layCircle;
    private TextView txtViewAllPlans, txtROffers;
    private ImageView imgLoadContacts, imgDropSpinner, imgSelectContact;
    private int strRechargeType;
    private RadioGroup rgMobileGroupRechargeType;
    private Button btnPrepaidRecharge, btnOtpVerify, btnCancelOtp;
    private View[] allViewWithClick = {btnPrepaidRecharge, btnOtpVerify, layOperator, layCircle, txtViewAllPlans, imgLoadContacts,
            imgDropSpinner, txtROffers, imgSelectContact, btnCancelOtp};
    private int[] allViewWithClickId = {R.id.btn_prepaidrecharge, R.id.btn_otpauth, R.id.lay_operator, R.id.lay_circle, R.id.txt_viewallplans,
            R.id.loadcontacts, R.id.img_drop_2, R.id.txt_roffers, R.id.img_contacts, R.id.btn_otpcancel};
    private EditText edPhoneNumber;
    private EditText[] edMobileRecharge = {edPhoneNumber, edRechargeAmount};
    private String[] edRechargeTextsError = {"Enter phone number", "Enter amount"};

    private int[] editTextMobileClickId = {R.id.edt_number, R.id.txtSelectedRechargePlane};
    private RadioButton radioPrepaid, radioPostPaid;

    private EditText[] edTexts = {edPhoneNumber};
    private String[] edTextsError = {"Enter mobile number"};
    private int[] editTextsClickId = {R.id.edt_number};
    private ProgressBar pbLoadOperator, pbLoadCircle;
    EditText edtOtp;
    public FragPrePostRecharge() {}

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    public void setCircleOperator(String strSelectedOperator, String strSelectedCirecle) {
        spinnerCircleList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerCircleList, strSelectedCirecle));
        spinnerOperatorList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerOperatorList, strSelectedOperator));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_prepostrecharge, container, false);
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
            EditTextDeclare(edTexts);
            resumeApp();
        }
    }

    private int animation_type = ItemAnimation.RIGHT_LEFT;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    RadioButton rbPrepaid, rbPostPaid;
    public void resumeApp() {
        strSeletedCircleId = null;
        strSeletedOperatorId = null;
        isOperatorLoad = false;
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        EditMobileRechargeTextDeclare(edMobileRecharge);
        edtOtp = aiView.findViewById(R.id.edt_otp);

        rgMobileGroupRechargeType = (RadioGroup) aiView.findViewById(R.id.rg_mobilerechargetype);
        radioPrepaid = (RadioButton) aiView.findViewById(R.id.r_prepaid);
        radioPostPaid = (RadioButton) aiView.findViewById(R.id.r_postpaid);
        pbLoadOperator = (ProgressBar) aiView.findViewById(R.id.progressbar_load_two);
        pbLoadCircle = (ProgressBar) aiView.findViewById(R.id.progressbar_load_one);
        pbLoadOperator.setVisibility(View.GONE);
        pbLoadCircle.setVisibility(View.GONE);
        strRechargeType = 1;

        listSpinnerOperatorList.add("-1" + "#:#" + "Select Operator");
        listSpinnerCircleList.add("-1" + "#:#" + "Select Circle");
        PopulateOperatorList();
        PopulateCircleList();

        LoadCircleList();

        rbPrepaid = (RadioButton) aiView.findViewById(R.id.r_prepaid);
        rbPostPaid = (RadioButton) aiView.findViewById(R.id.r_postpaid);

        if (ActivityRecharge.prePostpaid.equals("Prepaid")) {
            rbPrepaid.setChecked(true);
            rbPostPaid.setChecked(false);
        }else{
            rbPrepaid.setChecked(false);
            rbPostPaid.setChecked(true);
        }

        rgMobileGroupRechargeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r_prepaid:
                        LoadOperatorList("Prepaid");
                        break;
                    case R.id.r_postpaid:
                        LoadOperatorList("Postpaid");
                        break;
                }
            }
        });

        hideOtpLayout();
    }

    private boolean isOperatorLoad = false;

    private void LoadOperator() {
        if (!isOperatorLoad) {
            if (strRechargeType == 1) {
                LoadOperatorList("Prepaid");
                radioPrepaid.setSelected(true);
            } else {
                radioPostPaid.setSelected(true);
                LoadOperatorList("Postpaid");
            }
            isOperatorLoad = true;
        }
    }

    private void LoadOperatorList(String rechargeType) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(rechargeType);
        callWebServiceWithoutLoader(ApiInterface.OPERATORLIST, lstUploadData);
    }

    private void LoadCircleList() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("");
        callWebServiceWithoutLoader(ApiInterface.CIRCLELIST, lstUploadData);
    }

    private void EditMobileRechargeTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = aiView.findViewById(editTextMobileClickId[j]);
        }
        edPhoneNumber = (EditText) editTexts[0];
        edRechargeAmount = (EditText) editTexts[1];
        edPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edPhoneNumber.getText().toString().trim()).length() == 10) {
                    pbLoadOperator.setVisibility(View.VISIBLE);
                    pbLoadCircle.setVisibility(View.VISIBLE);
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edPhoneNumber.getText().toString().trim());
                    callWebServiceWithoutLoader(ApiInterface.GETOPERATORID, lstUploadData);
                }
            }
        });
    }


    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = aiView.findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_prepaidrecharge:
                            PrepaidRecharge();
                            break;
                        case R.id.txt_viewallplans:
                            Intent svIntent = new Intent(svContext, ActivityPlansOffer.class);
                            svIntent.putExtra("filename", "plans");
                            svContext.startActivity(svIntent);
                            break;
                        case R.id.txt_roffers:
                            ViewROffers();
                            break;
                        case R.id.btn_otpauth:
                            RechargeProcess();
                            break;
                        case R.id.btn_otpcancel:
                            hideOtpLayout();
                            break;
                        case R.id.lay_operator:
                            spinnerOperatorList.performClick();
                            break;
                        case R.id.lay_circle:
                            spinnerCircleList.performClick();
                            break;
                        case R.id.img_drop_2:
                            spinnerOperatorList.performClick();
                            break;
                        case R.id.loadcontacts:
//                            Load();
                            break;
                        case R.id.img_contacts:
                            ((ActivityRecharge) svContext).showContactBottomSheet("mobile");
                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    public void SetSelectedContact(String number) {
        edPhoneNumber.setText(number);
        edPhoneNumber.setError(null);
    }

    private void ViewROffers() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edRechargeTextsError);

        if (spinnerOperatorList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select operator", customToast.ToastyError);
        }

        if ((edPhoneNumber.getText().toString().trim()).length() != 10) {
            response++;
            customToast.showCustomToast(svContext, "Please enter correct mobile number", customToast.ToastyError);
        }

        if ((edRechargeAmount.getText().toString().trim()).length() == 1) {
            response++;
            customToast.showCustomToast(svContext, "Please enter at least 10 Rs", customToast.ToastyError);
        }

        if (response == 0) {
            strMobile = edPhoneNumber.getText().toString().trim();
            strOperatorCode = (listSpinnerOperatorList.get(spinnerOperatorList.getSelectedItemPosition()).split("#:#")[0]);
            Intent svIntent = new Intent(svContext, ActivityPlansOffer.class);
            svIntent.putExtra("filename", "offer_recharge");
            svContext.startActivity(svIntent);
        }
    }
    public static String strMobile, strOperatorCode;

    private void PrepaidRecharge() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edMobileRecharge, edRechargeTextsError);

        if (spinnerOperatorList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select operator", customToast.ToastyError);
        }

        if (spinnerCircleList.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please select circle", customToast.ToastyError);
        }

        if ((edPhoneNumber.getText().toString().trim()).length() != 10) {
            response++;
            customToast.showCustomToast(svContext, "Please enter correct mobile number", customToast.ToastyError);
        }

        if ((edRechargeAmount.getText().toString().trim()).length() == 1) {
            response++;
            customToast.showCustomToast(svContext, "Please enter at least 10 Rs", customToast.ToastyError);
        }

        int genid = rgMobileGroupRechargeType.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) aiView.findViewById(genid);
        String rgSelection = radioButton.getText().toString();

        type = "1";
        strRechargePostType = "1";
        if (rgSelection.equalsIgnoreCase("Prepaid")) {
            type = "1";
            strRechargePostType = "1";
        } else if (rgSelection.equalsIgnoreCase("Postpaid")) {
            type = "1";
            strRechargePostType = "2";
        }

        String strMessage = "Prepaid Recharge\nMobile Number: " + edPhoneNumber.getText().toString().trim() + "\nOperator: " +
                (listSpinnerOperatorList.get(spinnerOperatorList.getSelectedItemPosition()).split("#:#")[1]) +
                "\nAmount: " + edRechargeAmount.getText().toString().trim();
        if (response == 0) {
            ((ActivityRecharge)getActivity()).ShowConfirmDialog(getActivity(), strMessage, "mobile");
        }
    }

    public void ConfirmRecharge(){
        int amount = Integer.parseInt(edRechargeAmount.getText().toString().trim());
        boolean isWalletLoading = ((ActivityRecharge)getActivity()).checkRWalletAndAddWallet(amount, "prepost");
        if (isWalletLoading) {
            customToast.showCustomToast(svContext, "Insufficient fund", customToast.SweetAlertFailed);
        }else{
            ShowOtpLayout();
        }
    }

    String type = "1", strRechargePostType = "1";
    public void RechargeProcess() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add((edPhoneNumber.getText().toString().trim()));
        lstUploadData.add((listSpinnerOperatorList.get(spinnerOperatorList.getSelectedItemPosition()).split("#:#")[0]));
        lstUploadData.add((listSpinnerCircleList.get(spinnerCircleList.getSelectedItemPosition()).split("#:#")[0]));
        lstUploadData.add(edRechargeAmount.getText().toString().trim());
        lstUploadData.add("1");
        lstUploadData.add(strRechargePostType);
        lstUploadData.add(edtOtp.getText().toString().trim());
        callWebService(ApiInterface.RECHARGEAUTH, lstUploadData);
    }


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
        GlobalData.SetLanguage(svContext);
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

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

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
                    isOperatorLoad = false;
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    isOperatorLoad = true;
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("code");
                        String str_name = data_obj.getString("name");

                        listSpinnerOperatorList.add(str_code + "#:#" + str_name);
                    }

                    PopulateOperatorList();
                }
            } catch (JSONException e) {
                isOperatorLoad = false;
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.CIRCLELIST)) {
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
                LoadOperatorList(ActivityRecharge.prePostpaid);
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            PopulateCircleList();
        }
        else if (url.contains(ApiInterface.RECHARGEAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    edPhoneNumber.setText("");
                    edRechargeAmount.setText("");
                    spinnerOperatorList.setSelection(0);
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
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETOPERATORID)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
//                 customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }else {
                    strSeletedOperatorId = json.getString("operator_id");
                    strSeletedCircleId = json.getString("circle_id");
                    spinnerOperatorList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerOperatorList, json.getString("operator_id")));
                    spinnerCircleList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerCircleList, json.getString("circle_id")));
//                    {"status":1,"message":"Success","operator_id":"AT","circle_id":"19"}

                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
            pbLoadOperator.setVisibility(View.GONE);
            pbLoadCircle.setVisibility(View.GONE);
        }
    }

    public static String strSeletedCircleId, strSeletedOperatorId;
    public void ShowOtpLayout() {
        ((LinearLayout) aiView.findViewById(R.id.card_prepaidpostpaid)).setVisibility(View.GONE);
        ((LinearLayout) aiView.findViewById(R.id.card_otp)).setVisibility(View.VISIBLE);
    }

    private void hideOtpLayout() {
        ((LinearLayout) aiView.findViewById(R.id.card_prepaidpostpaid)).setVisibility(View.VISIBLE);
        ((LinearLayout) aiView.findViewById(R.id.card_otp)).setVisibility(View.GONE);
    }

    private void PopulateOperatorList() {
        spinnerOperatorList = (Spinner) aiView.findViewById(R.id.spinner_operatorlist);

        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerOperatorList, true);
        spinnerOperatorList.setAdapter(spindapter);
    }

    private Spinner spinnerCircleList, spinnerOperatorList;
    private List<String> listSpinnerCircleList = new ArrayList<>();
    private List<String> listSpinnerOperatorList = new ArrayList<>();


    private void PopulateCircleList() {
        spinnerCircleList = (Spinner) aiView.findViewById(R.id.spinner_circlelist);

        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerCircleList, true);
        spinnerCircleList.setAdapter(spindapter);

        spinnerCircleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoadOperator();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
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

//    ArrayList<ContactModel> lstContact = new ArrayList<>();
//    private void getContactList() {
//        ContentResolver cr = getActivity().getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//
//        if ((cur != null ? cur.getCount() : 0) > 0) {
//            while (cur != null && cur.moveToNext()) {
//                String id = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//
//                if (cur.getInt(cur.getColumnIndex(
//                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                    Cursor pCur = cr.query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i("Contact Name", "Name: " + name);
//                        Log.i("Contact phone number", "Phone Number: " + phoneNo);
//
//                        lstContact.add(new ContactModel(name, phoneNo));
//                    }
//                    pCur.close();
//                }
//            }
//        }
//        if (cur != null) {
//            cur.close();
//        }
//
//        ShowContactBottomList(lstContact);
//    }
}