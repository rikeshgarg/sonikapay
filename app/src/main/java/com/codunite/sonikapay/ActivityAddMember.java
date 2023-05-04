package com.codunite.sonikapay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ActivityAddMember extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener, MessageListener {
    private EditText edName, edEmail, edPhone, edPassword, edTranscationPassword, edCity;
    private Button btnSubmitMem, btnCancel;
    private ImageView imgDrop, imgDropOne, imgDropTwo;

    private View[] allViewWithClick = {btnSubmitMem, btnCancel, imgDrop, imgDropOne, imgDropTwo};
    private int[] allViewWithClickId = {R.id.btn_SubmitMem, R.id.btnCancel, R.id.img_drop, R.id.img_drop_1,
            R.id.img_drop_2};

    private EditText[] edTexts = {edName, edEmail, edPhone, edPassword, edTranscationPassword, edCity};
    private String[] edTextsError = {"Enter name", "Enter email", "Enter mobile", "Enter password",
            "Enter transcation password", "Enter city"};
    private int[] editTextsClickId = {R.id.edt_name, R.id.edtEmail, R.id.edtMobile, R.id.edt_password,
            R.id.edt_transaction_password, R.id.edt_city};
    private Spinner spinnerMemberPosition, spinnercountrylist, spinnerstatelist;
    private RadioGroup rgRoleId;
    private RadioButton radioDistributor, radioRetailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_addmember);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    public void resumeApp() {
        setFocus(getEditext(edName));
        spinnercountrylist = (Spinner) findViewById(R.id.spinner_countrylist);
        radioDistributor = (RadioButton) findViewById(R.id.radio_distributor);
        radioRetailer = (RadioButton) findViewById(R.id.radio_retailer);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebServiceWithoutLoader(ApiInterface.GETCOUNTRYLIST, lstUploadData);

        rgRoleId = (RadioGroup) findViewById(R.id.rg_roleid);

        imgDrop = (ImageView) findViewById(R.id.img_drop);
        imgDropOne = (ImageView) findViewById(R.id.img_drop_1);
        imgDropTwo = (ImageView) findViewById(R.id.img_drop_2);

        radioDistributor.setVisibility(View.VISIBLE);
        radioDistributor.setChecked(true);

        spinnerstatelist = (Spinner) findViewById(R.id.spinner_statelist);
        spinnerMemberPosition = (Spinner) findViewById(R.id.spinner_member_position);
        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("A#:#Active");
        listSpinner.add("D#:#Deactive");
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinner, true);
        spinnerMemberPosition.setAdapter(LegAdapter);

        spinnerMemberPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                strMemberPosition = listSpinner.get(position).split("#:#")[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        if (spinnercountrylist.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Select Country", customToast.ToastyError);
        }

        if (spinnerstatelist.getSelectedItemPosition() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Select State", customToast.ToastyError);
        }


        if (response == 0) {
            String roleId = "4";
            int radioButtonID = rgRoleId.getCheckedRadioButtonId();
            View radioButton = rgRoleId.findViewById(radioButtonID);
            if (rgRoleId.indexOfChild(radioButton) == 0) {
                roleId = "4";
            } else {
                roleId = "5";
            }

            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(roleId);
            lstUploadData.add(getEditextValue(edName));
            lstUploadData.add(getEditextValue(edEmail));
            lstUploadData.add(getEditextValue(edPhone));
            lstUploadData.add(getEditextValue(edPassword));
            lstUploadData.add(getEditextValue(edTranscationPassword));
            lstUploadData.add(listcountry.get(spinnercountrylist.getSelectedItemPosition()).split("#:#")[0]);
            lstUploadData.add((liststate.get(spinnerstatelist.getSelectedItemPosition()).split("#:#")[0]));
            lstUploadData.add(getEditextValue(edCity));
            callWebService(ApiInterface.ADDMEMBERAUTH, lstUploadData);
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
                        case R.id.img_drop:
                            spinnerMemberPosition.performClick();
                            break;
                        case R.id.img_drop_1:
                            spinnercountrylist.performClick();
                            break;
                        case R.id.img_drop_2:
                            spinnerstatelist.performClick();
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
        edTranscationPassword = (EditText) editTexts[4];
        edCity = (EditText) editTexts[5];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityAddMember.this);
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

                    PopulateCountry(listcountry);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.ADDMEMBERAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);

                    Intent svIntent = new Intent(svContext, ActivityMemberList.class);
                    svContext.startActivity(svIntent);
                    ((Activity) svContext).finish();
                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GetSTATELIST)) {
            try {
                JSONObject json = new JSONObject(result);
                liststate = new ArrayList<>();
                liststate.add("-1" + "#:#" + "Select State");

                String str_status = json.getString(TAG_STATUS);
                String str_msg = json.getString(TAG_MESSAGE);
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_country_name = data_obj.getString("state_name");
                        String str_country_id = data_obj.getString("state_id");

                        liststate.add(str_country_id + "#:#" + str_country_name);
                    }

                    PopulateCity(liststate);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Add Member");

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


    private void PopulateCity(List<String> listSpinner) {
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinner, true);
        spinnerstatelist.setAdapter(LegAdapter);

        spinnerstatelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void PopulateCountry(List<String> listSpinner) {
        SpinnerPopulateAdapter LegAdapter = new SpinnerPopulateAdapter(svContext, listSpinner, true);
        spinnercountrylist.setAdapter(LegAdapter);

        spinnercountrylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(listSpinner.get(position).split("#:#")[0]);
                    callWebService(ApiInterface.GetSTATELIST, lstUploadData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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