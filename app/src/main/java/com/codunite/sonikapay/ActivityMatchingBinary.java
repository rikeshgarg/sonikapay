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
import com.codunite.adapter.MatchingBinaryAdapter;
import com.codunite.model.MatchingBinaryModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityMatchingBinary extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private ImageView imgToolBarBack;
    private RecyclerView rvDownline;

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_matchingbinary);
        StartApp();

        resumeApp();
    }

    public void resumeApp() {
        rvDownline = (RecyclerView) findViewById(R.id.rv_downline);

        LoadData();
    }

    private void LoadData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETMATCHINGBINARY, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityMatchingBinary.this);
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
        txtHeading.setText("Binary Income");

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

    private List<MatchingBinaryModel> lstItems = new ArrayList<>();

    public static final String TAG_TDS_AMOUNT = "tds_amount";
    public static final String TAG_DATE = "date";
    public static final String TAG_BINARY_AMOUNT = "binary_amount";
    public static final String TAG_WALLET_SETTLE_AMOUNT = "wallet_settle_amount";
    public static final String TAG_IS_PAID = "is_paid";
    public static final String TAG_INCOME_AMOUNT = "income_amount";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETMATCHINGBINARY)) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_tds_amount = data_obj.getString(TAG_TDS_AMOUNT);
                        String str_date = data_obj.getString(TAG_DATE);
                        String str_binary_amount = data_obj.getString(TAG_BINARY_AMOUNT);
                        String str_wallet_settle_amount = data_obj.getString(TAG_WALLET_SETTLE_AMOUNT);
                        String str_is_paid = data_obj.getString(TAG_IS_PAID);
                        String str_income_amount = data_obj.getString(TAG_INCOME_AMOUNT);

                        lstItems.add(new MatchingBinaryModel(str_is_paid, str_date, str_tds_amount, str_binary_amount,
                                str_wallet_settle_amount, str_income_amount));
                    }

                    loadRecyClerView();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private void loadRecyClerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDownline.setLayoutManager(layoutManager);
        rvDownline.setHasFixedSize(true);
        int animation_type = ItemAnimation.LEFT_RIGHT;
        MatchingBinaryAdapter mAdapter = new MatchingBinaryAdapter(this, lstItems, animation_type);
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