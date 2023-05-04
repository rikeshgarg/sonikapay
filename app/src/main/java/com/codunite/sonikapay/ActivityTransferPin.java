package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.retrofit.ApiInterface;
import com.codunite.model.PackageModel;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityTransferPin extends AppCompatActivity implements View.OnClickListener, WebServiceListener {

    private Button btnproceedtopay;
    private ImageView imgBack;
    private View[] allViewWithClick = {btnproceedtopay, imgBack};
    private int[] allViewWithClickId = {R.id.btnproceedtopay, R.id.img_back};


    private EditText edPinAvailable, edNoOfPinTrasnfer, edTransferToMember;
    private EditText[] edTexts = {edPinAvailable, edNoOfPinTrasnfer, edTransferToMember};
    private String[] edTextsError = {"Available Pin", "Number Of Pin Transfer", "Transfer to MemberID/Mobile"};
    private int[] editTextsClickId = {R.id.edt_availablepin, R.id.edtnumberpin, R.id.edmember_mobile};

    private List<PackageModel> lstItems = new ArrayList<>();
    private String strPackageId = "0";
    private ProgressBar pbLoadOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_transferpin);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        pbLoadOperator = (ProgressBar) findViewById(R.id.progressbar_load_two);
        pbLoadOperator.setVisibility(View.GONE);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETPACKAGELIST, lstUploadData);
    }

    private void Proceedtopay() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (strPackageId.equalsIgnoreCase("0")) {
            response++;
            customToast.showCustomToast(svContext, "Select Package", customToast.ToastyError);
        }

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(strPackageId);
            lstUploadData.add(edNoOfPinTrasnfer.getText().toString().trim());
            lstUploadData.add(edTransferToMember.getText().toString());
            callWebService(ApiInterface.TRANSFER_PIN_AUTH, lstUploadData);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.img_back:
                            onBackPressed();
                            break;
                        case R.id.btnproceedtopay:
                            Proceedtopay();
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
        edPinAvailable = (EditText) editTexts[0];
        edNoOfPinTrasnfer = (EditText) editTexts[1];
        edTransferToMember = (EditText) editTexts[2];

        edTransferToMember.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edTransferToMember.getText().toString().trim()).length() == 10) {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edTransferToMember.getText().toString().trim());
                    callWebServiceWithoutDialog(ApiInterface.GETUSERNAME, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityTransferPin.this);
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

    private void loadToolBar() {
        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Transfer Pin");
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

    private void callWebServiceWithoutDialog(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    public static final String TAG_PACKAGE_AMOUNT = "package_amount";
    public static final String TAG_ID = "id";

    private Spinner spinnerOperatorList;
    private List<String> listSpinnerOperatorList = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETPACKAGELIST)) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data_obj = json.getJSONArray(TAG_DATA);
                    for (int j = 0; j < data_obj.length(); j++) {
                        JSONObject jsonObj = data_obj.getJSONObject(j);
                        String str_package_amount = jsonObj.getString("package_amount");
                        String str_id = jsonObj.getString("package_id");
                        String str_package_name = jsonObj.getString("package_name");

                        lstItems.add(new PackageModel(str_id, str_package_name, str_package_amount));
                    }
                    PopulateOperatorList();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
            pbLoadOperator.setVisibility(View.GONE);

        } else if (url.contains(ApiInterface.GET_AVAILABLE_PIN)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    String strPin = json.getString("pin");
                    edPinAvailable.setText(strPin);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.TRANSFER_PIN_AUTH)) {
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
        } else if (url.contains(ApiInterface.GETUSERNAME)) {
            try {
                JSONObject json = new JSONObject(result);
                TextView txtUsername = findViewById(R.id.txt_username);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                    txtUsername.setVisibility(View.GONE);
                } else {
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
        }
    }

    private void PopulateOperatorList() {
        listSpinnerOperatorList = new ArrayList<>();
        listSpinnerOperatorList.add("0#:#Select Package");
        for (int i = 0; i < lstItems.size(); i++) {
            String strId = lstItems.get(i).getId();
            String strName = lstItems.get(i).getName() + " (INR " + lstItems.get(i).getPrice() + ")";
            listSpinnerOperatorList.add(strId + "#:#" + strName);
        }

        spinnerOperatorList = (Spinner) findViewById(R.id.spinner_operatorlist);
        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(svContext, listSpinnerOperatorList, true);
        spinnerOperatorList.setAdapter(spindapter);
        spinnerOperatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                strPackageId = listSpinnerOperatorList.get(position).split("#:#")[0];
                if (!listSpinnerOperatorList.get(position).equalsIgnoreCase("0#:#Select Package")) {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                    lstUploadData.add(strPackageId);
                    callWebService(ApiInterface.GET_AVAILABLE_PIN, lstUploadData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
        hideKeyboard();
        super.onBackPressed();
    }
}