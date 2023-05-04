package com.codunite.sonikapay;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.ContactAdapter;
import com.codunite.model.ContactModel;
import com.codunite.commonutility.AppExecutors;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.MessageListener;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.SpinnerPopulateAdapter;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityMainWalletTransfer extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener, MessageListener {
    private EditText edbal, edmemid, edAmount, edtxd;
    private Button btnSubmitMem, btnCancel;
    private ImageView imgDrop, imgDropOne, imgDropTwo, imgContact;
    private TextView txtUsername;

    private View[] allViewWithClick = {btnSubmitMem, btnCancel, imgContact};
    private int[] allViewWithClickId = {R.id.btn_SubmitMem, R.id.btnCancel, R.id.img_contacts};

    private EditText[] edTexts = {edbal, edmemid, edAmount, edtxd};
    private String[] edTextsError = {"Enter Current Bal", "Enter Member Id", "Enter amount", "Enter Transaction Password"};
    private int[] editTextsClickId = {R.id.edtcurrentwalletbal, R.id.edtMobile, R.id.edt_amount_transfer, R.id.edt_transaction_pass};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mainwallettransfer);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        bottomSheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottomSheet);

        ((EditText) findViewById(R.id.edtcurrentwalletbal)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, ""));

        resumeApp();
    }

    private RadioGroup rgTxtType;
    private Spinner spinnerBankList;
    private ProgressBar pbLoadOperator;
    private EditText edPhoneNumber;

    public void resumeApp() {
        txtUsername = (TextView) findViewById(R.id.member_username);
        rgTxtType = (RadioGroup) findViewById(R.id.rg_txttype);
        spinnerBankList = (Spinner) findViewById(R.id.spinner_bank_name);
        pbLoadOperator = (ProgressBar) findViewById(R.id.progressbar_load_two);
        pbLoadOperator.setVisibility(View.GONE);

        edPhoneNumber = (EditText) findViewById(R.id.edtMobile);
        edPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edPhoneNumber.getText().toString().trim()).length() == 10) {
                    txtUsername.setVisibility(View.INVISIBLE);
                    pbLoadOperator.setVisibility(View.VISIBLE);

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edPhoneNumber.getText().toString().trim());
                    callWebServiceWithoutLoader(ApiInterface.GETUSERNAME, lstUploadData);
                }
            }
        });

        OpenDemoLink();
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
//        if ((edAmount.getText().toString().trim()).length() < 3) {
//            response++;
//            customToast.showCustomToast(svContext, "Please enter at least 100 Rs", customToast.ToastyError);
//        }

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(edmemid.getText().toString().trim());
            lstUploadData.add(edAmount.getText().toString().trim());
            lstUploadData.add(edtxd.getText().toString().trim());
            callWebService(ApiInterface.GETMAINWALLETTRANSFER, lstUploadData);
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
                        case R.id.img_contacts:
                            showContactBottomSheet();
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
        edbal = (EditText) editTexts[0];
        edmemid = (EditText) editTexts[1];
        edAmount = (EditText) editTexts[2];
        edtxd = (EditText) editTexts[3];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityMainWalletTransfer.this);
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
        } else if (url.contains(ApiInterface.GETMAINWALLETTRANSFER)) {
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

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Main Wallet Transfer");

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


    @Override
    protected void onPause() {
        super.onPause();
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
            mBottomSheetDialog = null;
        }
    }

    private View bottomSheet;
    private BottomSheetBehavior mBehavior;
    private List<ContactModel> lstContacts;
    private BottomSheetDialog mBottomSheetDialog;
    public final static int REQUEST_READ_CONTACTS_PERMISSION = 24563;

    public void showContactBottomSheet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                ActivityCompat.checkSelfPermission(svContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_CONTACTS, getString(R.string.permission_read_contacts_rationale), REQUEST_READ_CONTACTS_PERMISSION);
            return;
        }

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        View view = getLayoutInflater().inflate(R.layout.bs_select_contact, null);
        //view.setBackgroundColor(ContextCompat.getColor(svContext, android.R.color.transparent));

        mBottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) mBottomSheetDialog.findViewById(R.id.searchview);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHintTextColor(getResources().getColor(R.color.c_black));
        txtSearch.setTextColor(getResources().getColor(R.color.c_black));

        RecyclerView rvContactsList = mBottomSheetDialog.findViewById(R.id.rv_contacts);
        rvContactsList.setLayoutManager(new LinearLayoutManager(svContext, LinearLayoutManager.VERTICAL, false));
        rvContactsList.setHasFixedSize(true);
        lstContacts = new ArrayList<>();
        ContactAdapter mAdapter = new ContactAdapter(svContext, lstContacts, ItemAnimation.NONE);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                lstContacts = ActivityRecharge.getContactsList(svContext, "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateData(lstContacts);
                    }
                });
            }
        });

        rvContactsList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ContactModel contactModel, int position) {
                String number = contactModel.getContactNumber();
                number = number.replaceAll("\\D", "");
                number = number.replaceAll("\\s", "");
                String getnumber = "";
                if (number.length() > 10) {
                    //if (number.startsWith("+91") || number.startsWith("0")) {
                    //    number = number.split("+91")[1];
                    //}
                    int startidx = number.length()-10;
                    getnumber = number.substring(startidx,number.length());
                } else {
                    getnumber = number;
                }

                edPhoneNumber.setText(number);
                edPhoneNumber.setError(null);
                mBottomSheetDialog.dismiss();
            }
        });

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                //mAdapter.updateData(getContactsList(svContext, query));
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showContactBottomSheet();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityMainWalletTransfer.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }


    private void showAlertDialog(String title, String message,
                                 DialogInterface.OnClickListener onPositiveButtonClickListener, String positiveText,
                                 DialogInterface.OnClickListener onNegativeButtonClickListener, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(svContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }


    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("7");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }


}