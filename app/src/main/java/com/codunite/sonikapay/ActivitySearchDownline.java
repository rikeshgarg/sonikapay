package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivitySearchDownline extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private ImageView btnSearch;
    private EditText edPincode;
    private CardView layCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_serachdownline);
        StartApp();
        resumeApp();
    }

    public void resumeApp() {
        Button btnGenrateVendorBill = (Button) findViewById(R.id.btn_add_beneficiary);
        btnGenrateVendorBill.setOnClickListener(this);

        LinearLayout layoutSearchStore = findViewById(R.id.linearLayout);
        layoutSearchStore.setVisibility(View.VISIBLE);
        layCardview = findViewById(R.id.cardview);
        layCardview.setVisibility(View.GONE);
        edPincode = findViewById(R.id.ed_pincode);
        btnSearch = findViewById(R.id.img_search1);
        btnSearch.setOnClickListener(v -> {
            SearchStore();
        });
    }

    private void SearchStore() {
        String strPincode = "";

        if (!TextUtils.isEmpty(edPincode.getText())) {
            strPincode = edPincode.getText().toString().trim();
        }


        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(strPincode);
        callWebService(ApiInterface.SEARCH_DOWNLINE, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivitySearchDownline.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }

        hideKeyboard();
        loadToolBar();
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtToolbarHeading = (TextView) findViewById(R.id.heading);
        txtToolbarHeading.setText("Search Downline");

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
                hideKeyboard();
                finish();
                break;
            case R.id.btn_add_beneficiary:
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

    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_STATUS = "status";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_JSON_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.SEARCH_DOWNLINE)){
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_json_status = json.getString(TAG_STATUS);
                if (str_json_status.equalsIgnoreCase("0")) {
                    layCardview.setVisibility(View.GONE);
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    String user_code = json.getString("user_code");
                    String name = json.getString("name");
                    String mobile = json.getString("mobile");
                    String package_name = json.getString("package_name");
                    String sponser_code = json.getString("sponser_code");
                    String sponser_name = json.getString("sponser_name");
                    String total_downline = json.getString("total_downline");

                    ((TextView)findViewById(R.id.tv_name)).setText(user_code);
                    ((TextView)findViewById(R.id.name)).setText(name);
                    ((TextView)findViewById(R.id.mobile)).setText(mobile);
                    ((TextView)findViewById(R.id.package_name)).setText(package_name);
                    ((TextView)findViewById(R.id.sponser_code)).setText(sponser_code);
                    ((TextView)findViewById(R.id.sponser_name)).setText(sponser_name);
                    ((TextView)findViewById(R.id.total_downline)).setText(total_downline);

                    layCardview.setVisibility(View.VISIBLE);
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
        hideKeyboard();
        super.onBackPressed();
    }
}