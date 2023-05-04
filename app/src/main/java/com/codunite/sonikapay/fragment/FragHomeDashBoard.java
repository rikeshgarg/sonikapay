package com.codunite.sonikapay.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codunite.adapter.DashboardAdapter;
import com.codunite.adapter.DashboardAdapterWallet;
import com.codunite.sonikapay.ActivityFundRequest;
import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.ActivityComisionWalletHistory;
import com.codunite.sonikapay.ActivityDirectIncome;
import com.codunite.sonikapay.ActivityDownLine;
import com.codunite.sonikapay.ActivityLevelincome;
import com.codunite.sonikapay.ActivityMainWalletTransfer;
import com.codunite.sonikapay.ActivityPointWalletHistory;
import com.codunite.sonikapay.ActivityUpgrade;
import com.codunite.sonikapay.ActivityWalletHistory;
import com.codunite.sonikapay.ActivityWithdrawalRequest;
import com.codunite.sonikapay.bbps.ActivityBbpsAllServices;
import com.codunite.sonikapay.bbps.ActivityBbpsElectricity;
import com.codunite.sonikapay.bbps.ActivityBbpsTollTax;
import com.codunite.sonikapay.ewalletmanagment.ActivityEwalletPayout;
import com.codunite.sonikapay.ActivityRecharge;
import com.codunite.sonikapay.R;
import com.codunite.sonikapay.WebViewActivity;
import com.codunite.model.DashboardModel;
import com.codunite.model.SliderModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.FragmentTAG;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.customfont.FontUtils;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class FragHomeDashBoard extends Fragment implements OnClickListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private List<DashboardModel> lstDashBoardMain = new ArrayList<>();
    private List<DashboardModel> lstDashBoardAeps = new ArrayList<>();
    private List<DashboardModel> lstDashBoardBbps = new ArrayList<>();
    private List<DashboardModel> lstDashBoardwallet = new ArrayList<>();

    public static String[] walletTransferItemList = {"Add Money", "Withdraw", "Wallet Transfer", "Report"};
    private int[] allDrawablewallet = {R.drawable.ic_addfund, R.drawable.ic_withdraw, R.drawable.ic_wallettransfer, R.drawable.ic_report};

    public static String[] selectedItemItemList = {"Prepaid", "Postpaid", "DTH", "Data Card",
            "Broadband", "Landline", "Water", "Fasttag"};
    private int[] allDrawable = {R.drawable.prepaid, R.drawable.postpaid, R.drawable.dth, R.drawable.datacard,
            R.drawable.broadband, R.drawable.landline, R.drawable.water, R.drawable.fastag};

    public String[] BBPSItemItemList = {"Mobile Postpaid", "LOAN", "Gas", "ELECTRICITY", "Broadband", "Fastag", "Insurance", "More"};
    private int[] allDrawableBBPS = {R.drawable.bbps_mobile_recharge, R.drawable.bbps_loan,
            R.drawable.bbps_lpg_gas, R.drawable.bbps_electricity, R.drawable.bbps_boardband,
            R.drawable.bbps_fastag, R.drawable.bbps_insurance, R.drawable.ic_round_arrow_forward_ios_24};

    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};

    private int BBSPPOSITION = 2;
    private Button btnBankTransfer, btnPayToMerchant, copyandshare;
    private LinearLayout layPayMoney, layPassbook, layRequestMoney;
    private View[] allViewWithClick = {layPayMoney, layPassbook, layRequestMoney};
    private int[] allViewWithClickId = {R.id.pay_money, R.id.view_passbook, R.id.request_money};
    private FrameLayout cardRecharge;
    private CardView cardMarquee;
    private TextView referral;
    private SwipeRefreshLayout layrefrsh;
    private View layIsFundRequest;
    public FragHomeDashBoard() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_home, container, false);
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
        }

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        resumeApp();
    }

    private int animation_type = ItemAnimation.NONE;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                break;
            default:
                break;
        }
    }

    public void switchContent(Fragment fragment) {
        hideFragmentkeyboard(svContext, aiView);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void switchContent(Fragment fragment, String tag) {
        hideFragmentkeyboard(svContext, aiView);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void resumeApp() {
        Button btnupgrade = (Button) aiView.findViewById(R.id.btn_upgrade);
        btnupgrade.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityUpgrade.class);
            startActivity(svIntent);
        });
        copyandshare = (Button) aiView.findViewById(R.id.btn_copy_share);
        layIsFundRequest = (View) aiView.findViewById(R.id.lay_isfundrequest);

        cardRecharge = (FrameLayout) aiView.findViewById(R.id.card_recharge);
        cardMarquee = (CardView) aiView.findViewById(R.id.card_marque);
        layrefrsh = (SwipeRefreshLayout) aiView.findViewById(R.id.layrefrsh);
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISRECHARGEACTIVE, false)) {
            cardRecharge.setVisibility(View.VISIBLE);
        } else {
            cardRecharge.setVisibility(View.GONE);
        }

        (aiView.findViewById(R.id.btn_fundrequest)).setOnClickListener(view -> {
            Intent svIntent = new Intent(svContext, ActivityFundRequest.class);
            startActivity(svIntent);
        });

        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.IS_FUNDREQUEST_ACTIVE, false)) {
            layIsFundRequest.setVisibility(View.VISIBLE);
        }else {
            layIsFundRequest.setVisibility(View.GONE);
        }

        ((TextView) aiView.findViewById(R.id.txt_main_wallet)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, "0"));
        ((TextView) aiView.findViewById(R.id.txt_aeps)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, "0"));
        ((TextView) aiView.findViewById(R.id.txt_commissions)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.COMMISIONBALANCE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_points)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.COINBALANCE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_direct_downline)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALDIRECTDOWNLINE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_direct_active)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALDIRECTACTIVE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_direct_deactive)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALDIRECTDEACTIVE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_total_downline)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALDOWNLINE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_active_downline)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALACTIVEDOWNLINE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_deactive_downline)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.TOTALDEACTIVEDOWNLINE, "0"));
        ((TextView) aiView.findViewById(R.id.txt_direct_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.DIRECTINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_level_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.LEVELINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_recharge_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.RECHARGEINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_bbps_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.BBPSINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_cashback_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.CASHBACKINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_total_income)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.TOTALINCOME, "0"));
        ((TextView) aiView.findViewById(R.id.txt_membership)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.MEMBERSHIP, "0"));
        ((TextView) aiView.findViewById(R.id.txt_rank)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.RANK, "0"));
        ((TextView) aiView.findViewById(R.id.referallink)).setText(PreferenceConnector.readString(svContext, PreferenceConnector.REFFERALLINK, "0"));
        ((TextView) aiView.findViewById(R.id.total_success)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.TOTALSUCCESS, "")+"/-");
        ((TextView) aiView.findViewById(R.id.total_pending)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.TOTALPENDING, "")+"/-");
        ((TextView) aiView.findViewById(R.id.total_failed)).setText(GlobalVariables.CURRENCYSYMBOL + PreferenceConnector.readString(svContext, PreferenceConnector.TOTALFAILED, "")+"/-");


        (aiView.findViewById(R.id.lay_zoom)).setOnClickListener(v -> ActivityMain.onDrawerItemClick("Zoom Meeting", svContext));
        (aiView.findViewById(R.id.lay_upgrade)).setOnClickListener(v -> ActivityMain.onDrawerItemClick("Upgrade", svContext));

        TextView txtMarquee = (TextView) aiView.findViewById(R.id.text_marquee);
        String dashNEws = PreferenceConnector.readString(svContext, PreferenceConnector.DASHNEWS, "");
        if (dashNEws.equals("")) {
            cardMarquee.setVisibility(View.GONE);
        } else {
            cardMarquee.setVisibility(View.VISIBLE);
        }
        txtMarquee.setText(dashNEws);
        txtMarquee.setSelected(true);

        layrefrsh.setRefreshing(false);
        layrefrsh.setOnRefreshListener(() -> {
            ((ActivityMain) requireActivity()).loadUserData();
            layrefrsh.setRefreshing(false);
            resumeApp();
        });

        layrefrsh.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        root.setVisibility(View.VISIBLE);

        LinearLayout mainwallet = aiView.findViewById(R.id.lay_main_wallet);
        mainwallet.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityWalletHistory.class);
            startActivity(svIntent);
        });
        LinearLayout commissionwallet = aiView.findViewById(R.id.lay_commission_wallet);
        commissionwallet.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityComisionWalletHistory.class);
            startActivity(svIntent);
        });
        LinearLayout pointwallet = aiView.findViewById(R.id.lay_points);
        pointwallet.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityPointWalletHistory.class);
            startActivity(svIntent);
        });
        LinearLayout directInc = aiView.findViewById(R.id.lay_direct_income);
        directInc.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDirectIncome.class);
            startActivity(svIntent);
        });
        LinearLayout levelInc = aiView.findViewById(R.id.lay_level_income);
        levelInc.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityLevelincome.class);
            startActivity(svIntent);
        });
        LinearLayout directdown = aiView.findViewById(R.id.lay_direct_downline);
        directdown.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "direct");
            startActivity(svIntent);
        });
        LinearLayout directactive = aiView.findViewById(R.id.lay_direct_active);
        directactive.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "Active");
            startActivity(svIntent);
        });
        LinearLayout directdeactive = aiView.findViewById(R.id.lay_direct_deactive);
        directdeactive.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "DeActive");
            startActivity(svIntent);
        });
        LinearLayout totaldown = aiView.findViewById(R.id.lay_total_downline);
        totaldown.setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "total");
            startActivity(svIntent);
        });


        (aiView.findViewById(R.id.lay_active_downline)).setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "Total Active");
            startActivity(svIntent);
        });


        (aiView.findViewById(R.id.lay_deactive_downline)).setOnClickListener(v -> {
            Intent svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "Total DeActive");
            startActivity(svIntent);
        });

        referral = (TextView) aiView.findViewById(R.id.referallink);
        referral.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("EditText", referral.getText().toString());
            clipboard.setPrimaryClip(clip);
            customToast.showCustomToast(svContext, "Link Copied", customToast.ToastyNormal);
        });

        copyandshare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", referral.getText().toString());
                clipboard.setPrimaryClip(clip);
                customToast.showCustomToast(svContext, "Link Copied", customToast.ToastyNormal);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "VSI Digital";
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Refer Code");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Via"));

            }
        });

        LoadSlider();
        LoadRechargeManu();
        LoadBBPS();
        LoadWALLETManu();
    }

    public void LoadBBPS() {
        RecyclerView recyclerView = (RecyclerView) aiView.findViewById(R.id.rv_dashboard_bbps);

        lstDashBoardBbps = new ArrayList<>();
        for (int j = 0; j < BBPSItemItemList.length; j++) {
            lstDashBoardBbps.add(new DashboardModel(BBPSItemItemList[j], allDrawableBBPS[j], false));
        }

        DashboardAdapter mAdapter = new DashboardAdapter(svContext, lstDashBoardBbps, animation_type);
        recyclerView.setAdapter(mAdapter);

        String BBPSServices = "{\"status\":1,\"message\":\"Success\",\"data\":" +
                "[{\"service_id\":\"14\",\"title\":\"BROADBAND POSTPAID\"},{\"service_id\":\"12\",\"title\":\"CABLE\"}," +
                "{\"service_id\":\"24\",\"title\":\"CLUBS AND ASSOCIATIONS\"},{\"service_id\":\"22\",\"title\":\"CREDIT CARD\"}," +
                "{\"service_id\":\"13\",\"title\":\"DTH\"},{\"service_id\":\"7\",\"title\":\"EDUCATION\"}," +
                "{\"service_id\":\"4\",\"title\":\"ELECTRICITY\"},{\"service_id\":\"9\",\"title\":\"ENTERTAINMENT\"}," +
                "{\"service_id\":\"6\",\"title\":\"FASTAG\"},{\"service_id\":\"3\",\"title\":\"GAS\"}," +
                "{\"service_id\":\"19\",\"title\":\"HOSPITAL\"},{\"service_id\":\"17\",\"title\":\"HOUSING SOCIETY\"}" + "," +
                "{\"service_id\":\"2\",\"title\":\"INSURANCE\"},{\"service_id\":\"10\",\"title\":\"LANDLINE POSTPAID\"}" + "," +
                "{\"service_id\":\"1\",\"title\":\"LOAN\"},{\"service_id\":\"15\",\"title\":\"LPG GAS\"}," +
                "{\"service_id\":\"11\",\"title\":\"MOBILE POSTPAID\"},{\"service_id\":\"5\",\"title\":\"MOBILE PREPAID\"}," +
                "{\"service_id\":\"18\",\"title\":\"MUNICIPAL SERVICES\"},{\"service_id\":\"16\",\"title\":\"MUNICIPAL TAXES\"}," +
                "{\"service_id\":\"20\",\"title\":\"SUBSCRIPTION\"},{\"service_id\":\"23\",\"title\":\"TRANSIT CARD\"}," +
                "{\"service_id\":\"21\",\"title\":\"TRAVEL-SUB\"},{\"service_id\":\"8\",\"title\":\"WATER\"}]}";

        mAdapter.setOnItemClickListener((view, obj, position) -> {
            String strAction = lstDashBoardBbps.get(position).getName();
            if (strAction.equals(BBPSItemItemList[0])) {
                startNewActivity(ActivityBbpsAllServices.class, "MOBILE POSTPAID", "11");
            } else if (strAction.equals(BBPSItemItemList[1])) {
                startNewActivity(ActivityBbpsAllServices.class, "LOAN", "1");
            } else if (strAction.equals(BBPSItemItemList[2])) {
                startNewActivity(ActivityBbpsAllServices.class, "GAS", "3");
            } else if (strAction.equals(BBPSItemItemList[3])) {
                startNewActivity(ActivityBbpsElectricity.class);
            } else if (strAction.equals(BBPSItemItemList[4])) {
                startNewActivity(ActivityBbpsAllServices.class, "BROADBAND POSTPAID", "14");
            } else if (strAction.equals(BBPSItemItemList[5])) {
                startNewActivity(ActivityBbpsTollTax.class);
            } else if (strAction.equals(BBPSItemItemList[6])) {
                startNewActivity(ActivityBbpsAllServices.class, "INSURANCE", "2");
            } else if (strAction.equals(BBPSItemItemList[7])) {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISBBPSACTIVE, false)) {
                    switchContent(new FragBBPSDashBoard(), FragmentTAG.FragBBPSLiveDashBoard);
                } else {
                    customToast.showCustomToast("BBPS is not active for you", customToast.ToastyError);
                }
            }
        });


    }

    private void startNewActivity(Class<?> cls, String serviceType, String strServiceId) {
        Intent intent = new Intent(svContext, cls);
        intent.putExtra("servicetype", serviceType);
        FragBBPSDashBoard.strServiceId = strServiceId;
        intent.putExtra("serviceid", strServiceId);
        startActivity(intent);
    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(svContext, cls);
        svContext.startActivity(intent);
    }

    public void LoadWALLETManu() {
        RecyclerView recyclerView = aiView.findViewById(R.id.rv_dashboard_wallet);
        lstDashBoardwallet = new ArrayList<>();
        for (int j = 0; j < walletTransferItemList.length; j++) {
            lstDashBoardwallet.add(new DashboardModel(walletTransferItemList[j], allDrawablewallet[j], false));
        }

        GridLayoutManager layoutManager = new GridLayoutManager(svContext, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DashboardAdapterWallet mAdapter = new DashboardAdapterWallet(svContext, lstDashBoardwallet, animation_type);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, itemName, position) -> {
            if ((lstDashBoardwallet.get(position).getName()).equalsIgnoreCase("Add Money")) {
                Intent svIntent = new Intent(svContext, ActivityFundRequest.class);
                svContext.startActivity(svIntent);
            } else if ((lstDashBoardwallet.get(position).getName()).equalsIgnoreCase("Withdraw")) {
                Intent svIntent = new Intent(svContext, ActivityWithdrawalRequest.class);
                startActivity(svIntent);
            } else if ((lstDashBoardwallet.get(position).getName()).equalsIgnoreCase("Report")) {
                Intent svIntent = new Intent(svContext, ActivityWalletHistory.class);
                startActivity(svIntent);
            } else if ((lstDashBoardwallet.get(position).getName()).equalsIgnoreCase("Wallet Transfer")) {
                Intent svIntent = new Intent(svContext, ActivityMainWalletTransfer.class);
                svContext.startActivity(svIntent);
            }
        });
    }

    public void LoadRechargeManu() {
        RecyclerView recyclerView = aiView.findViewById(R.id.rv_dashboard);

        lstDashBoardMain = new ArrayList<>();
        for (int j = 0; j < selectedItemItemList.length; j++) {
            if ((selectedItemItemList[j]).equals("M Transfer")) {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.IS_MONEY_TRANSFER_ACTIVE, false)) {
                    lstDashBoardMain.add(new DashboardModel(selectedItemItemList[j], allDrawable[j], false));
                }
            } else {
                lstDashBoardMain.add(new DashboardModel(selectedItemItemList[j], allDrawable[j], false));
            }
        }

        GridLayoutManager layoutManager = new GridLayoutManager(svContext, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DashboardAdapter mAdapter = new DashboardAdapter(svContext, lstDashBoardMain, animation_type);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, obj, position) -> {
            if (selectedItemItemList[0].equals(obj)) {
                Intent svIntent = new Intent(svContext, ActivityRecharge.class);
                svIntent.putExtra("selecteditem", 0);
                svContext.startActivity(svIntent);
            } else if (selectedItemItemList[1].equals(obj)) {
                Intent svIntent = new Intent(svContext, ActivityRecharge.class);
                svIntent.putExtra("selecteditem", 0);
                svContext.startActivity(svIntent);
            } else if (selectedItemItemList[2].equals(obj)) {
                Intent svIntent = new Intent(svContext, ActivityRecharge.class);
                svIntent.putExtra("selecteditem", 1);
                svContext.startActivity(svIntent);
            } else if (selectedItemItemList[3].equals(obj)) {
                Intent svIntent = new Intent(svContext, ActivityRecharge.class);
                svIntent.putExtra("selecteditem", 2);
                svContext.startActivity(svIntent);
            } else if (selectedItemItemList[4].equals(obj)) {
                startNewActivity(ActivityBbpsAllServices.class, "BROADBAND POSTPAID", "14");
            } else if (selectedItemItemList[5].equals(obj)) {
                Intent svIntent = new Intent(svContext, ActivityRecharge.class);
                svIntent.putExtra("selecteditem", 2);
                svContext.startActivity(svIntent);
            } else if (selectedItemItemList[6].equals(obj)) {
                startNewActivity(ActivityBbpsAllServices.class, "WATER", "8");
            } else if (selectedItemItemList[7].equals(obj)) {
                startNewActivity(ActivityBbpsTollTax.class);
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
                    Intent svIntent;
                    switch (v.getId()) {
                        case R.id.pay_money:
                            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.IS_MONEY_TRANSFER_ACTIVE, false)) {
                                svIntent = new Intent(svContext, ActivityEwalletPayout.class);
                                svContext.startActivity(svIntent);
                            } else {
                                Toast.makeText(svContext, "This is not active for you", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
        }

    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private ViewGroup root;
    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }

        root.setVisibility(View.INVISIBLE);

        GlobalData.SetLanguage(svContext);
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    public static List<SliderModel> lstSlider = new ArrayList<>();
    public void LoadSlider() {
        ImageSlider sliderView = aiView.findViewById(R.id.image_slider);
        List<SlideModel> imageList = new ArrayList<>();

        for (int j = 0; j < FragHomeDashBoard.lstSlider.size(); j++) {
            imageList.add(new SlideModel((FragHomeDashBoard.lstSlider.get(j).getBanner_img()).replaceAll("\\/", "/"), ScaleTypes.FIT));
        }

        sliderView.setImageList(imageList, ScaleTypes.FIT);
        sliderView.setItemClickListener(i -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, FragHomeDashBoard.lstSlider.get(i).getBanner_name());
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, FragHomeDashBoard.lstSlider.get(i).getBanner_url());

            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}