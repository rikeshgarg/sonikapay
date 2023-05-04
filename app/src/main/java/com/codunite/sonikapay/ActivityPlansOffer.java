package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.AdapterHeadTab;
import com.codunite.adapter.AdapterListSectioned;
import com.codunite.adapter.ViewOffersAdapter;
import com.codunite.sonikapay.fragment.FragDth;
import com.codunite.sonikapay.fragment.FragPrePostRecharge;
import com.codunite.model.HeadTabsModel;
import com.codunite.model.OffersModel;
import com.codunite.model.ViewAllPlans;
import com.codunite.commonutility.CheckInternet;
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

import static com.codunite.sonikapay.fragment.FragPrePostRecharge.strSeletedCircleId;
import static com.codunite.sonikapay.fragment.FragPrePostRecharge.strSeletedOperatorId;

public class ActivityPlansOffer extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};
    private EditText[] edTexts = {};
    private String[] edTextsError = {"Enter phone number"};
    private int[] editTextsClickId = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_viewplansoffer);
        StartApp();
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        resumeApp();
    }

    public void resumeApp() {
        String sessionId = getIntent().getStringExtra("filename");
        if (sessionId.equalsIgnoreCase("offer_recharge")) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(FragPrePostRecharge.strMobile);
            lstUploadData.add(FragPrePostRecharge.strOperatorCode);
            callWebService(ApiInterface.ROFFERS, lstUploadData);
        } else {
            ShowViewAllPlans();
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
//                        case R.id.btn_finish:
//                            CheckData();
//                            break;
//                        case R.id.btn_backform:
//                            ShowBackCardView();
//                            break;
                    }
                }
            });
        }
//      btnBack = (Button) allViewWithClick[0];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityPlansOffer.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
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

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText(getString(R.string.app_name));

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

    public ArrayList<OffersModel> lstModel;

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.OPERATORLIST)) {
            try {
                listSpinnerOperatorList = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                listSpinnerOperatorList.add("-1" + "#:#" + "Select Operator");
                listSpinnerOperatorListId.add("-1");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(context, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("code");
                        String str_name = data_obj.getString("name");

                        listSpinnerOperatorList.add(str_code + "#:#" + str_name);
                        listSpinnerOperatorListId.add(str_code);
                    }

                    PopulateOperatorList();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(context, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.CIRCLELIST)) {
            try {
                listSpinnerCircleList = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                listSpinnerCircleList.add("-1" + "#:#" + "Select Circle");
                listSpinnerCircleListID.add("-1");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(context, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_code = data_obj.getString("code");
                        String str_name = data_obj.getString("name");

                        listSpinnerCircleList.add(str_code + "#:#" + str_name);
                        listSpinnerCircleListID.add(str_code);
                    }

                    PopulateCircleList();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(context, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.VIEWALLPLANS) || url.contains(ApiInterface.VIEWALLDTHPLANS)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(context, str_message, customToast.ToastyError);
                } else {
                    ArrayList<ViewAllPlans> lstAllPlans = new ArrayList<>();
                    ArrayList<HeadTabsModel> lstAllTabs = new ArrayList<>();
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_type = data_obj.getString(TAG_TYPE);
                        lstAllPlans.add(new ViewAllPlans(str_type, true));
                        lstAllTabs.add(new HeadTabsModel(str_type, lstAllPlans.size() + 2));

                        JSONArray data_obj_data = data_obj.getJSONArray(TAG_DATA);
                        for (int i = 0; i < data_obj_data.length(); i++) {
                            JSONObject data_obj_data_obj = data_obj_data.getJSONObject(i);
                            String str_amount = data_obj_data_obj.getString(TAG_AMOUNT);
                            String str_validity = data_obj_data_obj.getString(TAG_VALIDITY);
                            String str_desc = data_obj_data_obj.getString(TAG_DESC);

                            lstAllPlans.add(new ViewAllPlans(str_amount, str_desc, str_validity, false));
                        }
                    }

                    AdapterHeadTab mAdapterTab = new AdapterHeadTab(context, lstAllTabs, ItemAnimation.RIGHT_LEFT);
                    lstViewAllTabs.setAdapter(mAdapterTab);
                    lstViewAllTabs.setNestedScrollingEnabled(false);
                    mAdapterTab.setOnItemClickListener(new AdapterHeadTab.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int goToPos, int position) {
                            listViewPlans.smoothScrollToPosition(goToPos);
                            listViewPlans.scrollToPosition(goToPos);

                        }
                    });

                    AdapterListSectioned mAdapter = new AdapterListSectioned(context, lstAllPlans);
                    listViewPlans.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, ViewAllPlans obj, int position) {
                            editext.setText(lstAllPlans.get(position).getPlansPrice());
//                            FragPrePostRecharge frag = new FragPrePostRecharge();
//                            frag.setCircleOperator(strSelectedOperator, strSelectedCirecle);
                            rLayROffers.setVisibility(View.GONE);
                            onBackPressed();
                        }
                    });
                }
            } catch (JSONException e) {
                customToast.showCustomToast(context, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.ROFFERS) || url.contains(ApiInterface.RDTHOFFERS)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    final String TAG_AMOUNT = "amount";
                    final String TAG_DESC = "desc";
                    lstModel = new ArrayList<>();
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_amount = data_obj.getString(TAG_AMOUNT);
                        String str_desc = data_obj.getString(TAG_DESC);

                        lstModel.add(new OffersModel(str_amount, str_desc));
                    }
                    ShowOffersBottomList();

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
        super.onBackPressed();
    }

    private RecyclerView listView;

    public void ShowOffersBottomList() {
        RelativeLayout rLayROffers = (RelativeLayout) findViewById(R.id.headlayout_offers);
        rLayROffers.setVisibility(View.VISIBLE);

        listView = (RecyclerView) findViewById(R.id.listViewBtmSheet_offers);

        ((ImageView) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rLayROffers.setVisibility(View.GONE);
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        ViewOffersAdapter mAdapter = new ViewOffersAdapter(svContext, lstModel, ItemAnimation.NONE);
        listView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ViewOffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                FragPrePostRecharge.edRechargeAmount.setText(lstModel.get(position).getOfferPrice());
                rLayROffers.setVisibility(View.GONE);
                onBackPressed();
            }
        });


    }

    private RecyclerView listViewPlans, lstViewAllTabs;
    private Spinner spinnerCircleList, spinnerOperatorList;
    private List<String> listSpinnerCircleList = new ArrayList<>();
    private List<String> listSpinnerOperatorList = new ArrayList<>();
    private List<String> listSpinnerCircleListID = new ArrayList<>();
    private List<String> listSpinnerOperatorListId = new ArrayList<>();
    private Context context;
    private EditText editext;

    RelativeLayout rLayROffers;

    public void ShowViewAllDthPlans() {
        context = svContext;
        rLayROffers = (RelativeLayout) findViewById(R.id.headlayout_plans);
        rLayROffers.setVisibility(View.VISIBLE);
        editext = FragDth.edDthRechargeAmount;

        listViewPlans = (RecyclerView) findViewById(R.id.listViewBtmSheet_plans);
        lstViewAllTabs = (RecyclerView) findViewById(R.id.lst_tabs);
        spinnerOperatorList = (Spinner) findViewById(R.id.spinner_operatorlist);

        listViewPlans.setLayoutManager(new LinearLayoutManager(context));
        listViewPlans.setHasFixedSize(true);
        lstViewAllTabs.setHasFixedSize(true);

        listSpinnerOperatorList.add("-1" + "#:#" + "Select Operator");
        listSpinnerOperatorListId.add("-1");
        PopulateOperatorList();

        ((ImageView) findViewById(R.id.bt_close_plans)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rLayROffers.setVisibility(View.GONE);
                onBackPressed();
            }
        });
    }


    public void ShowViewAllPlans() {
        context = svContext;
        rLayROffers = (RelativeLayout) findViewById(R.id.headlayout_plans);
        rLayROffers.setVisibility(View.VISIBLE);
        editext = FragPrePostRecharge.edRechargeAmount;

        listViewPlans = (RecyclerView) findViewById(R.id.listViewBtmSheet_plans);
        lstViewAllTabs = (RecyclerView) findViewById(R.id.lst_tabs);
        spinnerCircleList = (Spinner) findViewById(R.id.spinner_circlelist);
        spinnerOperatorList = (Spinner) findViewById(R.id.spinner_operatorlist);

        listViewPlans.setLayoutManager(new LinearLayoutManager(context));
        listViewPlans.setHasFixedSize(true);
        lstViewAllTabs.setHasFixedSize(true);

        listSpinnerOperatorList.add("-1" + "#:#" + "Select Operator");
        listSpinnerCircleList.add("-1" + "#:#" + "Select Circle");
        listSpinnerOperatorListId.add("-1");
        listSpinnerCircleListID.add("-1");
        PopulateOperatorList();
        PopulateCircleList();

        ((ImageView) findViewById(R.id.bt_close_plans)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rLayROffers.setVisibility(View.GONE);
                onBackPressed();
            }
        });

        LoadCircleList();
    }

    int mLastOperatorPos = 0;
    public static String strSelectedOperator, strSelectedCirecle;

    private void PopulateOperatorList() {
        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(context, listSpinnerOperatorList, true);
        spinnerOperatorList.setAdapter(spindapter);

        if (strSeletedOperatorId != null) {
            spinnerOperatorList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerOperatorList, strSeletedOperatorId));
        }

        spinnerOperatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0 && pos != mLastOperatorPos) {
                    mLastOperatorPos = pos;
                    strSelectedOperator = (listSpinnerOperatorList.get(spinnerOperatorList.getSelectedItemPosition()).split("#:#")[0]);
                    strSelectedCirecle = (listSpinnerCircleList.get(spinnerCircleList.getSelectedItemPosition()).split("#:#")[0]);
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(strSelectedCirecle);
                    lstUploadData.add(strSelectedOperator);
                    callWebService(ApiInterface.VIEWALLPLANS, lstUploadData);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }


    int mLastCirclePos = 0;

    private void PopulateCircleList() {
        SpinnerPopulateAdapter spindapter = new SpinnerPopulateAdapter(context, listSpinnerCircleList, true);
        spinnerCircleList.setAdapter(spindapter);

        if (strSeletedCircleId != null) {
            spinnerCircleList.setSelection(GlobalData.getSpinnerPosByValue(listSpinnerCircleList, strSeletedCircleId));
        }
        spinnerCircleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0 && pos != mLastCirclePos) {
                    mLastCirclePos = pos;
                    LoadOperatorList("Prepaid");
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void LoadOperatorList(String rechargeType) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(rechargeType);
        callWebService(ApiInterface.OPERATORLIST, lstUploadData);
    }

    private void LoadCircleList() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("");
        callWebService(ApiInterface.CIRCLELIST, lstUploadData);
    }

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_VALIDITY = "validity";
    public static final String TAG_DESC = "desc";
    public static final String TAG_TYPE = "type";


}