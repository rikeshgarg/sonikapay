package com.codunite.sonikapay;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.PaginationAdapter;
import com.codunite.adapter.WalletHistoryAdapter;
import com.codunite.model.WalletHistoryModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GetFormattedDateTime;
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
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ActivityPointWalletHistory extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_TYPE = "type";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    private ImageView imgToolBarBack;
    private TextView txtWalletbal;
    private Button btnAddWallet;
    private CardView cvAddWallet, cardShowBalance;

    private RecyclerView wallethistoryrv, rvPagination;
    private boolean isFirstLoad = true;
    private NestedScrollView layNestedScroll;
    private int pageNumber = 1;
    private String strFromDate = "", strToDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pointwallethistory);
        StartApp();

        resumeApp();
    }

    boolean isDateFrom = true;
    Calendar myCalendar;
    TextView txtFrom, txtTo;
    LinearLayout layFilter;
    public void resumeApp() {
        txtWalletbal = (TextView) findViewById(R.id.walletbal);
        btnAddWallet = (Button) findViewById(R.id.btn_addwallet);
        txtWalletbal.setVisibility(View.INVISIBLE);
        cvAddWallet = (CardView) findViewById(R.id.card_addwallet);
        cardShowBalance = (CardView) findViewById(R.id.card_wallbal);
        cvAddWallet.setVisibility(View.GONE);
        cardShowBalance.setVisibility(View.INVISIBLE);
        layFilter = (LinearLayout) findViewById(R.id.lay_filter);
        layFilter.setVisibility(View.INVISIBLE);
        btnAddWallet.setOnClickListener(this);

        layNestedScroll = (NestedScrollView) findViewById(R.id.lay_nestedscroll);
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);
        rvPagination = (RecyclerView) findViewById(R.id.rv_pagination);


        TextView txteWalletbal = (TextView) findViewById(R.id.ewalletbal);
        txtWalletbal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.COINBALANCE, "0"));
        txteWalletbal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, "0"));

        myCalendar = Calendar.getInstance();
        txtFrom = (TextView) findViewById(R.id.datePicker_from);
        txtTo = (TextView) findViewById(R.id.datePicker_to);

//        txtFrom.setText(GetFormattedDateTime.getcurrentcalDate());
//        txtTo.setText(GetFormattedDateTime.getcurrentcalDate());


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String selectedDate = year + "-" + (month >= 10 ? month : "0" + month)
                        + "-" + (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth);

                if(isDateFrom){
                    txtFrom.setText(selectedDate);
                }else {
                    txtTo.setText(selectedDate);
                }
            }
        };


        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtDate = txtTo.getText().toString().trim();
                if (txtDate.equalsIgnoreCase("Select to date")) {
                    txtDate = GetFormattedDateTime.getcurrentcalDate();
                }
                isDateFrom = false;
                String[] strDate = txtDate.split("-");
                new DatePickerDialog(svContext, date,
                        Integer.parseInt(strDate[0]),
                        Integer.parseInt(strDate[1]) - 1,
                        Integer.parseInt(strDate[2])).show();
            }
        });

        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtDate = txtFrom.getText().toString().trim();
                if (txtDate.equalsIgnoreCase("Select from date")) {
                    txtDate = GetFormattedDateTime.getcurrentcalDate();
                }
                isDateFrom = true;
                String[] strDate = txtDate.split("-");
                new DatePickerDialog(svContext, date,
                        Integer.parseInt(strDate[0]),
                        Integer.parseInt(strDate[1]) - 1,
                        Integer.parseInt(strDate[2])).show();
            }
        });


        ImageView imgSearch = (ImageView) findViewById(R.id.filter_search);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((txtFrom.getText().toString().trim()).equalsIgnoreCase("Select from date")){
                    customToast.showCustomToast(svContext, "Please select from date first", customToast.ToastyInfo);
                }else if((txtTo.getText().toString().trim()).equalsIgnoreCase("Select to date")){
                    customToast.showCustomToast(svContext, "Please select to date first", customToast.ToastyInfo);
                }else {
                    isFirstLoad = true;
                    pageNumber = 1;
                    strFromDate = txtFrom.getText().toString().trim();
                    strToDate = txtTo.getText().toString().trim();
                    LoadHistory(strFromDate, strToDate);
                }
            }
        });

        isFirstLoad = true;
        pageNumber = 1;
        LoadHistory(strFromDate, strToDate);

    }

    private void LoadHistory(String fromDate, String toDate){
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(fromDate);
        lstUploadData.add(toDate);
        lstUploadData.add("" + pageNumber);
        callWebService(ApiInterface.GETPOINTWALLETHISTORY, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityPointWalletHistory.this);
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
        txtHeading.setText("Point Wallet History");

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_addwallet:
                Intent svIntent = new Intent(svContext, ActivityRequestWallet.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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

    private List<WalletHistoryModel> lstItems = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETPOINTWALLETHISTORY)) {
            int strPageCount = 0;
            try {
                lstItems = new ArrayList<>();

                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                try {
                    strPageCount = Integer.parseInt(json.getString("pages"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < ((JSONArray) data).length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_amount = data_obj.getString(TAG_AMOUNT);
                        String str_datetime = data_obj.getString(TAG_DATETIME);
                        String str_description = data_obj.getString(TAG_DESCRIPTION);
                        String str_type = data_obj.getString(TAG_TYPE);
                        String str_openbal = data_obj.getString("before_balance");
                        String str_closebal = data_obj.getString("after_balance");


                        lstItems.add(new WalletHistoryModel(str_amount, str_datetime, str_description, str_type,str_openbal,str_closebal));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            cvAddWallet.setVisibility(View.GONE);
            cardShowBalance.setVisibility(View.VISIBLE);
            layFilter.setVisibility(View.VISIBLE);

            txtWalletbal.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            wallethistoryrv.setLayoutManager(layoutManager);
            wallethistoryrv.setHasFixedSize(true);
            int animation_type = ItemAnimation.LEFT_RIGHT;
            WalletHistoryAdapter mAdapter = new WalletHistoryAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new WalletHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String obj, int position) {
                }
            });
            layNestedScroll.scrollTo(0, 0);
            if (isFirstLoad) {
                isFirstLoad = false;
                LoadPaginationView(strPageCount);
            }
        }
    }

    void LoadPaginationView(int paginationSize) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, LinearLayoutManager.HORIZONTAL, false);
        rvPagination.setLayoutManager(layoutManager);
        rvPagination.setHasFixedSize(true);
        int animation_type = ItemAnimation.LEFT_RIGHT;
        PaginationAdapter mAdapter = new PaginationAdapter(svContext, paginationSize, animation_type);
        rvPagination.setNestedScrollingEnabled(false);
        rvPagination.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PaginationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                pageNumber = Integer.parseInt(obj);
                LoadHistory(strFromDate, strToDate);
            }
        });
        if (paginationSize <= 1) {
            rvPagination.setVisibility(View.GONE);
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