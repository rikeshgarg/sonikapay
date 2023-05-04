package com.codunite.sonikapay;

import static com.codunite.commonutility.GlobalVariables.PRE_URL_MAIN;
import static com.codunite.sonikapay.BaseApp.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codunite.sonikapay.activity.ActivityLogin;
import com.retrofit.ApiInterface;
import com.codunite.adapter.ExpandableListAdapter;
import com.codunite.sonikapay.bbps.ActivityBBPSLiveCommision;
import com.codunite.sonikapay.bbps.ActivityBbpsHistory;
import com.codunite.sonikapay.ewalletmanagment.ActivityEWalletSettlement;
import com.codunite.sonikapay.firebase.FirebaseMessageReceiver;
import com.codunite.sonikapay.fragment.FragHomeDashBoard;
import com.codunite.sonikapay.incomes.ActivityTotalIncome;
import com.codunite.model.MenuModel;
import com.codunite.sonikapay.spinner.SpinnerModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ImageLoading;
import com.codunite.commonutility.LocaleHelper;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener, WebServiceListener,
        NavigationView.OnNavigationItemSelectedListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};
    public NavigationView navigationView;

    public static String ShowBalance(Context svContext) {
        return GlobalVariables.CURRENCYSYMBOL +
                PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, "0");
    }

    public static String ShoweBalance(Context svContext) {
        return GlobalVariables.CURRENCYSYMBOL +
                PreferenceConnector.readString(svContext, PreferenceConnector.EWALLETBAL, "0");
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        StartApp();
        OnClickCombineDeclare(allViewWithClick);
        resumeApp();
        initFirebaseNotifyToken();
        checkInAppUpdate();
    }

    private DrawerLayout drawer;

    public void resumeApp() {
        headerList = new ArrayList<>();
        childList = new HashMap<>();
        initToolbar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableListView = findViewById(R.id.expandableListView);

        initMenuData();
        prepareMenuData();
        populateExpandableList();

        LinearLayout main_lay = (LinearLayout)findViewById(R.id.main_lay);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationMenu(
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERNAME, ""),
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERTYPE, ""),
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERPROFILEPIC, ""),
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINMEMBERID, "memberid"),
                navigationView);
        switchContent(new FragHomeDashBoard());

//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {
                                         main_lay.setX(navigationView.getWidth() * slideOffset);
                                         RelativeLayout.LayoutParams lp =
                                                 (RelativeLayout.LayoutParams) main_lay.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         main_lay.setLayoutParams(lp);

                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );

    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);

        FirebaseMessageReceiver.CreateNotificationChannel(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        GlobalData.SetLanguage(svContext);
        SetLanguage("en");
    }

    Intent svIntent;

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
//                        case R.id.img_notification:
//                            break;
                    }
                }
            });
        }
    }

    private void SetLanguage(String languageCode) {
        LocaleHelper.setLocale(svContext, languageCode);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

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

    boolean loadApiInBackground = false;
    LinkedList<String> lstUploadData = new LinkedList<>();

    public void loadUserData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
        callWebService(ApiInterface.UPDATEFCM, lstUploadData);
    }

    private void loadUserDataBackground() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
        callWebServiceWithoutLoader(ApiInterface.UPDATEFCM, lstUploadData);
    }

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        loadApiInBackground = true;
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        loadApiInBackground = false;
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        //if (url.contains(ApiInterface.UPDATEFCM)) {
            //ActivitySplash.LoadUserData(result, svContext);
            initMenuData();
            prepareMenuData();
            populateExpandableList();

            initNavigationMenu(
                    PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERNAME, ""),
                    PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSEREMAIL, ""),
                    PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERPROFILEPIC, ""),
                    PreferenceConnector.readString(svContext, PreferenceConnector.LOGINMEMBERID, "memberid"),
                    navigationView);
            initToolbar();
        //}
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDataBackground();
        hideKeyboard();
    }

    public static String getcurrentDate() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return (date >= 10 ? date : "0" + date) + "" + (month >= 10 ? month : "0" + month) + "" + year;
    }

    public static String getFormatedcurrentTime() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        if (hour == 0) {
            hour = 12;
        }

        return (hour >= 10 ? hour : "0" + hour) + "" + (minute >= 10 ? minute : "0" + minute) + "" + second;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initToolbar() {
        ImageView imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_scancode);
        imgToolBareWallet.setVisibility(View.VISIBLE);
        imgToolBareWallet.setOnClickListener(view -> {
            svIntent = new Intent(svContext, ActivitySupport.class);
            svContext.startActivity(svIntent);
        });
    }

    public void LoadScanner() {
        Collection<String> permissionNeeded = new ArrayList<>();
        permissionNeeded.add(Manifest.permission.CAMERA);
        PermissionPermit(permissionNeeded, svContext);
    }

    private void PermissionPermit(Collection<String> permissionNeeded, Context con) {
        Dexter.withContext(con).withPermissions(permissionNeeded).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Intent svIntent = new Intent(svContext, ActivityScanner.class);
                startActivity(svIntent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    public void initNavigationMenu(String strName, String usertype, String imageUrl, String memberId, NavigationView navigationView) {
        View navigation_header = navigationView.getHeaderView(0);
        TextView tvName = navigation_header.findViewById(R.id.menuheader_name);
        TextView tvMemberId = navigation_header.findViewById(R.id.menuheader_memberid);
        CircularImageView avatar = navigation_header.findViewById(R.id.menuheader_dp);

        tvName.setText(strName);
        tvMemberId.setText(memberId);

        Typeface fontFamily = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAMEBOLD);
        tvName.setTypeface(fontFamily);
        tvMemberId.setTypeface(fontFamily);
        try {
            ImageLoading.loadImages(imageUrl, avatar, R.drawable.users);
        } catch (Exception e) {
            e.printStackTrace();
        }

        avatar.setOnClickListener(view -> {
            svIntent = new Intent(svContext, ActivityProfile.class);
            startActivity(svIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });
    }

    public static boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }

    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            view.animate().setDuration(delay ? 200 : 0).rotation(180);
            return true;
        } else {
            view.animate().setDuration(delay ? 200 : 0).rotation(0);
            return false;
        }
    }

    public void switchContent(Fragment fragment, String tag) {
        hideKeyboard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void switchContent(Fragment fragment) {
        hideKeyboard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void switchBack() {
        hideKeyboard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }


    private static void LogOut(Context svContext) {
        PreferenceConnector.cleanPrefrences(svContext);
        Intent svIntent = new Intent(svContext, ActivityLogin.class);
        svContext.startActivity(svIntent);
        ((Activity) svContext).finish();
    }

    private String getStringResponse(int colorDraw) {
        return getResources().getString(colorDraw);
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    public static List<SpinnerModel> lstCategoryData = new ArrayList<>();

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private String[] strMenuItemMaster;
    private String[][] strMenuChildItemMaster;

    private String[] strMenuItem;
    private String[][] strMenuChildItem;

    private void initMenuData() {
        String[] documentsMenuItems = new String[lstCategoryData.size()];
        for (int i = 0; i < lstCategoryData.size(); i++) {
            documentsMenuItems[i] = lstCategoryData.get(i).getTitle();
        }

//        strMenuItemMaster = new String[]{
//                "Profile",
//                "Recharge",
//                "Main Wallet",
//                "Commisssion Wallet",
//                "Pin",
//                "Upgrade",
//                "Genealogy",
//                "Reports",
//                "Income",
//                "Referral",
//                "KYC",
//                "Company Document",
//                "Raise Ticket",
//                "Policies",
//                "Logout"};

//        strMenuChildItemMaster = new String[][]{
//                {"Personal Profile", "Change Password", "Change T-Pin", "Add Nominee"},
//                {"Prepaid", "DTH", "Recharge History"},
//                {"Add Money UPI", "Fund Request", "Wallet Transfer", "Fund Request History", "Wallet History"},
//                {"Commission Wallet Transfer", "Commission Withdrawal", "Commission Wallet History"},
//                {"Request Pin", "Transfer Pin", "Pin List"},
//                new String[0],
//                {"Direct Downline", "Direct Active", "Direct Deactive", "Total Downline", "Total Active", "Total Deactive", "Upline Details", "Rank Wise Team", "Level Wise Team"},
//                {"Recharge History", "BBPS History", "Recharge Commission", "BBPS Commission", "Recharge Income", "BBPS Income", "Direct Income", "Level Income", "Cashback Income"},
//                new String[0],
//                new String[0],
//                new String[0],
//                documentsMenuItems,
//                {"Create Ticket", "View Ticket"},
//                {"Cancellation & Refund policy", "Website Disclaimer", "Privacy Policy", "Terms and conditions", "Contact Us", "Support"},
//                new String[0], new String[0], new String[0], new String[0],
//                new String[0], new String[0], new String[0], new String[0],
//                new String[0], new String[0], new String[0]};


        strMenuItemMaster = new String[]{
                "Profile",
                "Recharge",
                "AEPS",
                "NSDL Pan",
                "Fino AEPS",
                "Fino Aeps Settlement",
                "Money Transfer",
                "Order History",
                "Main Wallet",
                "AEPS Wallet",
                "Commisssion Wallet",
                "Point Wallet",
                "Pin",
                "Upgrade",
                "UTI Pancard",
                "Register as vendor",
                "Vendor",
                "Vendor Banner List",
                "Sonika Store",
                "Genealogy",
                "Reports",
                "Income",
                "Referral",
                "KYC",
                "Company Document",
                "Change Account",
                "Raise Ticket",
                "Zoom Meeting",
                "Cancellation & refund policy",
                "Website Disclaimer",
                "Privacy Policy",
                "Terms and conditions",
                "Contact Us",
                "Support",
                "Logout"};

        strMenuChildItemMaster = new String[][]{
                {"My QR Code", "Personal Profile", "Change Password", "Change T-Pin", "Add Nominee"},
                {"Prepaid", "Postpaid", "DTH", "Recharge History"},
                {"Balance Enquiry", "Mini Statement", "Withdrawal", "Aadhar Pay", "AEPS History"},
                new String[0],
                {"Fino Balance Enquiry", "Fino Mini Statement", "Fino Withdrawal", "Fino Aadhar Pay", "Fino AEPS History"},
                {"Fino AEPS Settlement", "Fino AEPS Settlement Report"},
                {"Money Transfer", "Money Transfer Report"},
                new String[0],
                {"Payout", "Payout Report", "Add Money", "Add Fund", "Wallet Transfer", "Fund Request", "Fund Request History", "Wallet History"},
                {"AEPS Payout", "AEPS Payout Report", "AEPS Wallet Transfer", "AEPS Wallet History"},
                {"Commission Payout", "Commission Payout Report", "Commission Wallet Transfer", "Commission Wallet History"},
                {"Point Wallet History"},
                {"Request Pin", "Transfer Pin", "Pin List"},
                new String[0],
                {"UTI", "Coupon List"},
                new String[0],
                {"Vendor Package", "Vendor Bill"},
                new String[0],
                new String[0],
                {"Search Downline", "Direct Downline", "Direct Active", "Direct Deactive", "Total Downline", "Total Active", "Total Deactive", "Upline Details", "Rank Wise Team", "Level Wise Team"},
                {"Recharge History", "BBPS History", "AEPS History", "BBPS Live History", "Money Transfer History", "Recharge Commission", "BBPS Commission", "Recharge Income", "BBPS Income", "AEPS Income", "Aeps Commission", "Direct Income", "Level Income"},
                new String[0],
                new String[0],
                new String[0],
                documentsMenuItems,
                new String[0],
                {"Create Ticket", "View Ticket"},
                new String[0],
                new String[0], new String[0], new String[0], new String[0],
                new String[0], new String[0], new String[0], new String[0],
                new String[0], new String[0], new String[0]};
    }

    private void prepareMenuData() {
        String strVendorStatus = PreferenceConnector.readString(svContext, PreferenceConnector.VENDOR_STATUS, "0");

        LinkedHashMap<String, String[]> HashMap = new LinkedHashMap<String, String[]>();
        strMenuItem = new String[strMenuItemMaster.length];
        strMenuChildItem = new String[strMenuChildItemMaster.length][];
        for (int j = 0; j < strMenuItemMaster.length; j++) {
            if (strMenuItemMaster[j].equals("Recharge")) {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISRECHARGEACTIVE, false)) {
                    HashMap.put(strMenuItemMaster[j], strMenuChildItemMaster[j]);
                    strMenuItem[j] = strMenuItemMaster[j];
                    strMenuChildItem[j] = strMenuChildItemMaster[j];
                }
            } else if (strMenuItemMaster[j].equalsIgnoreCase("Register as vendor")) {
                if (!strVendorStatus.equals("2")) {
                    HashMap.put(strMenuItemMaster[j], strMenuChildItemMaster[j]);
                    strMenuItem[j] = strMenuItemMaster[j];
                    strMenuChildItem[j] = strMenuChildItemMaster[j];
                }
            } else if (strMenuItemMaster[j].equals("Vendor")) {
                if (strVendorStatus.equals("2")) {
                    HashMap.put(strMenuItemMaster[j], strMenuChildItemMaster[j]);
                    strMenuItem[j] = strMenuItemMaster[j];
                    strMenuChildItem[j] = strMenuChildItemMaster[j];
                }
            } else if (strMenuItemMaster[j].equals("Vendor Banner List")) {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.VENDOR_PACKAGE_PURCHASED, false)) {
                    HashMap.put(strMenuItemMaster[j], strMenuChildItemMaster[j]);
                    strMenuItem[j] = strMenuItemMaster[j];
                    strMenuChildItem[j] = strMenuChildItemMaster[j];
                }
            } else {
                HashMap.put(strMenuItemMaster[j], strMenuChildItemMaster[j]);
                strMenuItem[j] = strMenuItemMaster[j];
                strMenuChildItem[j] = strMenuChildItemMaster[j];
            }
        }

        headerList = new ArrayList<>();
        childList = new HashMap<>();
        for (Map.Entry map : HashMap.entrySet()) {
            System.out.println(map.getKey() + " " + map.getValue());
            String[] strChild = (String[]) map.getValue();
            MenuModel menuModel = new MenuModel(map.getKey().toString(), true, true, strChild);
            headerList.add(menuModel);

            List<MenuModel> childModelsList = new ArrayList<>();
            for (int j = 0; j < strChild.length; j++) {
                MenuModel childModel = new MenuModel(strChild[j], false, false, strMenuItem);
                childModelsList.add(childModel);
                childList.put(menuModel, childModelsList);
            }
        }
    }


    private void populateExpandableList() {
        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if ((headerList.get(groupPosition).strMenuChildItem).length < 1) {
                    onDrawerItemClick(headerList.get(groupPosition).menuName, svContext);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            boolean isExpanded = false;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && isExpanded) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
                isExpanded = true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                onDrawerItemClick(model.menuName, svContext);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private boolean CheckValidation(String[] strArrayData, String title) {
        for (int j = 0; j < strArrayData.length; j++) {
            if (title.equals(strArrayData[j])) {
                return true;
            }
        }
        return false;
    }

    public Fragment getFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }


    public static void onDrawerItemClick(String title, Context svContext) {
        ShowCustomToast customToast = new ShowCustomToast(svContext);
        Intent svIntent;

        for (int i = 0; i < lstCategoryData.size(); i++) {
            SpinnerModel data = lstCategoryData.get(i);
            if (title.equals(data.getTitle())) {
                svIntent = new Intent(svContext, ActivityCompanyDoc.class);
                svIntent.putExtra("spinnerModel", data);
                svContext.startActivity(svIntent);
                return;
            }
        }

        if (("Prepaid").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRecharge.class);
            svIntent.putExtra("selecteditem", 0);
            svContext.startActivity(svIntent);
        } else if (("Postpaid").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRecharge.class);
            svIntent.putExtra("selecteditem", 1);
            svContext.startActivity(svIntent);
        } else if (("Order History").equalsIgnoreCase(title)) {
            customToast.showCustomToast(svContext, "Coming Soon", customToast.ToastyInfo);
        } else if (title.equalsIgnoreCase("Generate Pin")) {
            svIntent = new Intent(svContext, ActivityGeneratePin.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Transfer Pin")) {
            svIntent = new Intent(svContext, ActivityTransferPin.class);
            svContext.startActivity(svIntent);
        } else if (("Upgrade").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityUpgrade.class);
            svContext.startActivity(svIntent);
        } else if (("Direct Downline").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "direct");
            svContext.startActivity(svIntent);
        } else if (("Search Downline").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivitySearchDownline.class);
            svContext.startActivity(svIntent);
        } else if (("Total Downline").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "total");
            svContext.startActivity(svIntent);
        } else if (("Direct Active").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "Active");
            svContext.startActivity(svIntent);
        } else if (("Direct Deactive").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "DeActive");
            svContext.startActivity(svIntent);
        } else if (("Total Active").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "TotActive");
            svContext.startActivity(svIntent);
        } else if (("Total Deactive").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDownLine.class);
            svIntent.putExtra("selecteditem", "TotDeActive");
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Request Pin")) {
            svIntent = new Intent(svContext, ActivityRequestPin.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Pin List")) {
            svIntent = new Intent(svContext, ActivityPinList.class);
            svContext.startActivity(svIntent);
        } else if (("DTH").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRecharge.class);
            svIntent.putExtra("selecteditem", 2);
            svContext.startActivity(svIntent);
        } else if (("Electricity").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRecharge.class);
            svIntent.putExtra("selecteditem", 2);
            svContext.startActivity(svIntent);
        } else if (("AEPS Settlement").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityEWalletSettlement.class);
            svContext.startActivity(svIntent);
            ((Activity) svContext).finish();
        } else if (("Recharge History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRechargeHistory.class);
            svIntent.putExtra("selecteditem", 2);
            svContext.startActivity(svIntent);
        } else if (("Bill Pay History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityBillPayHistory.class);
            svContext.startActivity(svIntent);
        } else if (("BBPS History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityBbpsHistory.class);
            svContext.startActivity(svIntent);
        } else if (("BBPS Live History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityBillPayHistory.class);
            svContext.startActivity(svIntent);
        } else if (("Privacy Policy").equalsIgnoreCase(title)) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, title);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, PRE_URL_MAIN + "Privacy");
            svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        } else if (("Website Disclaimer").equalsIgnoreCase(title)) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, title);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, PRE_URL_MAIN + "Privacy/websiteDisclaimer");
            svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        } else if (("Cancellation & refund policy").equalsIgnoreCase(title)) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, title);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, PRE_URL_MAIN + "Privacy/ProductDesign");
            svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        } else if (("Terms and conditions").equalsIgnoreCase(title)) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, title);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, PRE_URL_MAIN + "Privacy/terms");
            svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        } else if (("Add Member").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityAddMember.class);
            svContext.startActivity(svIntent);
        } else if (("View All Member").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityMemberList.class);
            svContext.startActivity(svIntent);
        } else if (("Personal Profile").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityProfile.class);
            svContext.startActivity(svIntent);
        } else if (("Change Password").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityChangePassword.class);
            svContext.startActivity(svIntent);
        } else if (("Change T-Pin").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityChangeTransactionPassword.class);
            svContext.startActivity(svIntent);
        } else if (("Add Nominee").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityAddNominee.class);
            svContext.startActivity(svIntent);
        } else if (("Kyc").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityKyc.class);
            svContext.startActivity(svIntent);
        } else if (("Change Account").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityChangeAccount.class);
            svContext.startActivity(svIntent);
        } else if (("Income").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityTotalIncome.class);
            svContext.startActivity(svIntent);
        } else if (("Referral").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityReferral.class);
            svContext.startActivity(svIntent);
        } else if (("Support").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivitySupport.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Add Fund")) {
            svIntent = new Intent(svContext, ActivityRequestWallet.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Fund Request")) {
            svIntent = new Intent(svContext, ActivityFundRequest.class);
            svContext.startActivity(svIntent);
        } else if(title.equalsIgnoreCase("Add Money UPI")){
            svIntent = new Intent(svContext, ActivityTopupFund.class);
            svContext.startActivity(svIntent);
        }else if (title.equalsIgnoreCase("Fund Request History")) {
            svIntent = new Intent(svContext, ActivityFundRequestHistory.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Wallet Transfer")) {
            svIntent = new Intent(svContext, ActivityMainWalletTransfer.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Commission Wallet Transfer")) {
            svIntent = new Intent(svContext, ActivityCommisionWalletTransfer.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Commission Withdrawal")) {
            svIntent = new Intent(svContext, ActivityWithdrawalRequest.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Commission Payout")) {
            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.IS_COMMISSION_TRANSFER_ACTIVE, false)) {
                svIntent = new Intent(svContext, ActivityCommissionWalletPayoutTransfer.class);
                svContext.startActivity(svIntent);
            } else {
                customToast.showCustomToast(svContext, "This is not active for you", customToast.ToastyInfo);
            }
        } else if (("Zoom Meeting").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityZoomMeeting.class);
            svContext.startActivity(svIntent);
        } else if (("Upgrade").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityUpgrade.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Wallet History")) {
            svIntent = new Intent(svContext, ActivityWalletHistory.class);
            svContext.startActivity(svIntent);
        } else if (("Commission Wallet History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityComisionWalletHistory.class);
            svContext.startActivity(svIntent);
        } else if (("Point Wallet History").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityPointWalletHistory.class);
            svContext.startActivity(svIntent);
        } else if (("BBPS Commission").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityBBPSLiveCommision.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Recharge Income")) {
            svIntent = new Intent(svContext, ActivityRechargeIncome.class);
            svContext.startActivity(svIntent);
        } else if (("Recharge Commission").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityRechargeCommision.class);
            svContext.startActivity(svIntent);
        } else if (("DMR Commission").equalsIgnoreCase(title)) {
            svIntent = new Intent(svContext, ActivityDMRCommision.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Notification")) {
            svIntent = new Intent(svContext, ActivityNotification.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Refer and Earn")) {
            svIntent = new Intent(svContext, ActivityShare.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Contact Us")) {
            svIntent = new Intent(svContext, ActivityContact.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Create Ticket")) {
            svIntent = new Intent(svContext, ActivityHelpFeedback.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("View Ticket")) {
            svIntent = new Intent(svContext, ActivityTicketList.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("View Complaint")) {
            svIntent = new Intent(svContext, ActivityComplaintList.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Logout")) {
            ShowConfirmLogout(svContext, "Logout", "Are you confirm?",
                    "Are you are ready to end your current session. You have to enter login detail again");
        } else if (title.equalsIgnoreCase("My Fund Request")) {
            svIntent = new Intent(svContext, ActivityRequestHistory.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Recharge Commission")) {
            svIntent = new Intent(svContext, ActivityRechargeCommision.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("BBPS Income")) {
            svIntent = new Intent(svContext, ActivityBbpsincome.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Level Income")) {
            svIntent = new Intent(svContext, ActivityLevelincome.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Cashback Income")) {
            svIntent = new Intent(svContext, ActivityCashbackIncome.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Direct Income")) {
            svIntent = new Intent(svContext, ActivityDirectIncome.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Upline Details")) {
            svIntent = new Intent(svContext, ActivityUpline.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Rank Wise Team")) {
            svIntent = new Intent(svContext, ActivityRankList.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Level Wise Team")) {
            svIntent = new Intent(svContext, ActivityLevelList.class);
            svContext.startActivity(svIntent);
        }
    }

    private static void ShowConfirmLogout(Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> LogOut(svContext));

        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Request For Permissions
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (appUpdateManager == null) {
                    appUpdateManager = AppUpdateManagerFactory.create(svContext);
                }
                appUpdateManager.completeUpdate();
            } else {
                // If the update is cancelled or fails, restart the update again.
                checkInAppUpdate();
            }
        }
    }

    /**
     * App Update Flow Process
     */
    private View viewUpdate;
    private AppUpdateManager appUpdateManager;
    private int UPDATE_TYPE = AppUpdateType.IMMEDIATE;
    private final int APP_UPDATE_REQUEST_CODE = 1222;

    private void checkInAppUpdate() {
        viewUpdate = (View) findViewById(R.id.lay_update);
        viewUpdate.setVisibility(View.GONE);

        appUpdateManager = AppUpdateManagerFactory.create(svContext);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            PreferenceConnector.writeBoolean(svContext, PreferenceConnector.PACKAGE_DOWNLOADED, false);
            switch (appUpdateInfo.updateAvailability()) {
                case UpdateAvailability.UPDATE_AVAILABLE:
                    if (appUpdateInfo.isUpdateTypeAllowed(UPDATE_TYPE)) {
                        Log.d(TAG, "Update available");
                        startAppUpdateFlow(appUpdateInfo);
                    } else {
                        Log.d(TAG, "Update available but update type not allowed.");
                    }
                    break;
                case UpdateAvailability.UPDATE_NOT_AVAILABLE:
                    Log.d(TAG, "Update Not Available");
                    break;
                case UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS:
                    Log.d(TAG, "Update In Progress.");
                    break;
                case UpdateAvailability.UNKNOWN:
                    Log.d(TAG, "Error In Update.");
                    break;
            }
        });
    }

    private void startAppUpdateFlow(AppUpdateInfo appUpdateInfo) {
        if (UPDATE_TYPE == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(updatedListener);
        }

        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    ActivityMain.this,
                    AppUpdateOptions.newBuilder(UPDATE_TYPE)
                            .setAllowAssetPackDeletion(true)
                            .build(),
                    APP_UPDATE_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        } finally {
            if (UPDATE_TYPE == AppUpdateType.FLEXIBLE) {
                appUpdateManager.unregisterListener(updatedListener);
            }
        }
    }

    private InstallStateUpdatedListener updatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState installState) {
            switch (installState.installStatus()) {
                case InstallStatus.DOWNLOADED:
                    // After the update is downloaded, show a notification and request user confirmation to restart the app.
                    Log.d(TAG, "An update has been downloaded");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.PACKAGE_DOWNLOADED, true);
                    viewUpdate.setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_update).setOnClickListener(view -> appUpdateManager.completeUpdate());
                    appUpdateManager.completeUpdate();
                    break;
                case InstallStatus.DOWNLOADING:
                    Log.d(TAG, "An update downloading");
                    long bytesDownloaded = installState.bytesDownloaded();
                    long totalBytesToDownload = installState.totalBytesToDownload();
                    // Implement progress bar.
                    break;
                case InstallStatus.CANCELED:
                case InstallStatus.FAILED:
                case InstallStatus.UNKNOWN:
                    Log.d(TAG, "An update failed");
                    break;
                case InstallStatus.INSTALLING:
                case InstallStatus.INSTALLED:
                case InstallStatus.REQUIRES_UI_INTENT:
                case InstallStatus.PENDING:
                    Log.d(TAG, "An update pending");
                    break;
            }
        }
    };

    /**
     * Firebase Notification Token
     */
    private void initFirebaseNotifyToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, token);
                        if (GlobalVariables.ISTESTING)
                            Log.d(TAG, "Success FCM Token.\n" + token);
                    } else {
                        if (GlobalVariables.ISTESTING)
                            Log.d(TAG, "Fetching FCM registration token failed!!\n" + task.getException());
                    }
                });
    }

}