package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.DownLineAdapter;
import com.codunite.model.DownLineModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityDownLine extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private ImageView imgToolBarBack;
    private RecyclerView rvDownline;
    private TextView txtcount, txtgen;

    public static final String TAG_DATA = "data";
    public static final String TAG_USER_CODE = "user_code";
    public static final String TAG_LEVEL = "level";
    public static final String TAG_NAME = "name";
    public static final String TAG_MOBILE = "mobile";
    public static final String TAG_MEMBERSHIP = "membership";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_MEMBERID = "memberID";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_downline);
        StartApp();

        resumeApp();
    }

    boolean isMobilenoShow = false;

    public void resumeApp() {
        ChipGroup chipGroup = findViewById(R.id.chiptype);
        txtcount = (TextView) findViewById(R.id.count);
        txtgen = (TextView) findViewById(R.id.count_genealogy);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                switch (i) {
                    case R.id.chip_a:
                        LoadData(ApiInterface.GETDIRECTDOWNLINE);
                        break;
                    case R.id.chip_b:
                        LoadData(ApiInterface.GETDIRECTACTIVEDOWNLINE);
                        break;
                    case R.id.chip_c:
                        LoadData(ApiInterface.GETDIRECTDEACTIVEDOWNLINE);
                        break;
                    case R.id.chip_d:
                        LoadData(ApiInterface.GETTOTALDOWNLINE);
                        break;
                    case R.id.chip_e:
                        LoadData(ApiInterface.GETTOTALACTIVEDOWNLINE);
                        break;
                    case R.id.chip_f:
                        LoadData(ApiInterface.GETTOTALDEACTIVEDOWNLINE);
                        break;
                }
            }
        });

        rvDownline = (RecyclerView) findViewById(R.id.rv_downline);

        String pageData = getIntent().getStringExtra("selecteditem");
        if (pageData.equalsIgnoreCase("direct")) {
            LoadData(ApiInterface.GETDIRECTDOWNLINE);
            txtgen.setText("Direct Downline");
            isMobilenoShow = true;
        } else if (pageData.equalsIgnoreCase("total")) {
            LoadData(ApiInterface.GETTOTALDOWNLINE);
            txtgen.setText("Total Downline");
            isMobilenoShow = false;
        } else if (pageData.equalsIgnoreCase("Active")) {
            LoadData(ApiInterface.GETDIRECTACTIVEDOWNLINE);
            txtgen.setText("Direct Active");
            isMobilenoShow = true;
        } else if (pageData.equalsIgnoreCase("DeActive")) {
            LoadData(ApiInterface.GETDIRECTDEACTIVEDOWNLINE);
            txtgen.setText("Direct Deactive");
            isMobilenoShow = true;
        } else if (pageData.equalsIgnoreCase("TotActive")) {
            LoadData(ApiInterface.GETTOTALACTIVEDOWNLINE);
            txtgen.setText("Total Active");
            isMobilenoShow = false;
        } else {
            LoadData(ApiInterface.GETTOTALDEACTIVEDOWNLINE);
            txtgen.setText("Total Deactive");
            isMobilenoShow = false;
        }

    }

    private void LoadData(String url) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(url, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityDownLine.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
// FontUtils.setThemeColor(root, svContext, true);
        } else {
// FontUtils.setThemeColor(root, svContext, false);
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

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Downline");

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
                finish();
                break;
            default:
                break;
        }
    }

    private RelativeLayout layConnection, progressbarInternet;
    private TextView textError;
    private ProgressBar progressBarLayconnection;

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

    private List<DownLineModel> lstItems = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        try {
            lstItems = new ArrayList<>();
            JSONObject json = new JSONObject(result);

            String str_message = json.getString(TAG_MESSAGE);
            String str_status = json.getString(TAG_STATUS);
            if (str_status.equalsIgnoreCase("0")) {
                customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
            } else {
                JSONArray data = json.getJSONArray(TAG_DATA);
                txtcount.setText(json.getString("total"));

                for (int data_i = 0; data_i < data.length(); data_i++) {
                    JSONObject data_obj = data.getJSONObject(data_i);

                    String str_user_code = data_obj.getString(TAG_USER_CODE);
                    String str_level = data_obj.getString(TAG_LEVEL);
                    String str_name = data_obj.getString(TAG_NAME);
                    String str_mobile = data_obj.getString(TAG_MOBILE);
                    String str_membership = data_obj.getString(TAG_MEMBERSHIP);
                    String str_email = data_obj.getString(TAG_EMAIL);
                    String str_memberID = data_obj.getString(TAG_MEMBERID);

                    lstItems.add(new DownLineModel(str_memberID, str_name + "(" + str_user_code + ")", str_mobile, str_membership, str_email,
                            str_user_code, str_level));

                    loadRecyClerView();
                }
            }
        } catch (JSONException e) {
            customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
            e.printStackTrace();
        }
    }

    private void loadRecyClerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDownline.setLayoutManager(layoutManager);
        rvDownline.setHasFixedSize(true);
        int animation_type = ItemAnimation.LEFT_RIGHT;
        DownLineAdapter mAdapter = new DownLineAdapter(this, lstItems, animation_type, isMobilenoShow);
        rvDownline.setAdapter(mAdapter);

//        mAdapter.setOnItemClickListener(new DownLineAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, String obj, int position) {
//
//            }
//        });
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