package com.codunite.sonikapay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codunite.sonikapay.activity.ActivityLogin;
import com.retrofit.ApiInterface;
import com.codunite.sonikapay.fragment.FragHomeDashBoard;
import com.codunite.model.SliderModel;
import com.codunite.sonikapay.spinner.SpinnerModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class ActivitySplash extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public static final String TAG_USER_DETAIL = "user_detail";
    public static final String TAG_USER_CODE = "user_code";
    public static final String TAG_PROFILE_PHOTO = "profile_photo";
    public static final String TAG_IS_RECHARGE_ACTIVE = "is_recharge_active";
    public static final String TAG_NAME = "name";
    public static final String TAG_MOBILE = "mobile";
    public static final String TAG_USERID = "userID";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    public static final String TAG_IS_MONEY_TRANSFER_ACTIVE = "is_money_transfer_active";
    public static final String TAG_IS_MAIN_TRANSFER_ACTIVE = "is_main_wallet_transfer_active";
    public static final String TAG_IS_AEPS_TRANSFER_ACTIVE = "is_aeps_wallet_transfer_active";
    public static final String TAG_IS_COMMISSION_TRANSFER_ACTIVE = "is_commission_wallet_transfer_active";

    private ProgressBar progreesBar;

    public static void OpenWalletActivity(Context svContext) {
        Intent svIntent = new Intent(svContext, ActivityWalletHistory.class);
        svContext.startActivity(svIntent);
    }

    public static void OpeneWalletActivity(Context svContext) {
        Intent svIntent = new Intent(svContext, ActivityComisionWalletHistory.class);
        svContext.startActivity(svIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalData.Fullscreen(ActivitySplash.this);
        setContentView(R.layout.act_splash);

        progreesBar = (ProgressBar) findViewById(R.id.progressbar);
        progreesBar.setVisibility(View.VISIBLE);

        StartApp();
        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISDARKTHEME, false);
        resumeApp();
    }

    public void resumeApp() {
        showDynamicLinksOffer(getIntent());
    }

    private void LoadSplash() {
        if (checkNetwork.isConnectingToInternet()) {
            errrorScreen.hideError();
            Intent svIntent;
            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.AUTOLOGIN, false)) {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""));
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.DEVICE_ID, ""));
                callWebServiceWithoutLoader(ApiInterface.UPDATEFCM, lstUploadData);
            } else {
//                svIntent = new Intent(svContext, ActivityLogin.class);
//                startActivity(svIntent);
//                progreesBar.setVisibility(View.INVISIBLE);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                finish();
                svIntent = new Intent(svContext, ActivityIntro.class);
                startActivity(svIntent);
                progreesBar.setVisibility(View.INVISIBLE);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        } else {
            errrorScreen.showInternetError();
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivitySplash.this);
        if (!(GlobalVariables.CUSTOMFONTNAME).equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }

        //change app heme from
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
        }

        hideKeyboard();
        GlobalData.SetLanguage(svContext);
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

    private void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.UPDATEFCM)) {
            LoadUserData(result, svContext, true);
            progreesBar.setVisibility(View.INVISIBLE);
        }
    }

    public static void LoadUserData(String result, Context svContext) {
        LoadUserData(result, svContext, false);
    }

    public static void LoadUserData(String result, Context svContext, boolean isLogin) {
        try {
            JSONObject json = new JSONObject(result);
            String str_status = json.getString(TAG_STATUS);
            String str_message = json.getString(TAG_MESSAGE);

            if (GlobalVariables.ISTESTING) {
                Log.e("result>>>>", result);
            }
            if (str_status.equalsIgnoreCase("1")) {
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

                JSONObject user_detail_obj = json.getJSONObject(TAG_USER_DETAIL);
                String str_is_recharge_active = user_detail_obj.getString(TAG_IS_RECHARGE_ACTIVE);
                String str_profile_photo = user_detail_obj.getString(TAG_PROFILE_PHOTO);
                String str_wallet_balance = user_detail_obj.getString("wallet_balance");
                String str_ewallet_balance = user_detail_obj.getString("aeps_wallet_balance");
                String str_commision_balance = user_detail_obj.getString("commision_wallet_balance");
                String str_coin_balance = user_detail_obj.getString("point");

                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALDIRECTDOWNLINE, user_detail_obj.getString("total_direct_downline"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALDIRECTACTIVE, user_detail_obj.getString("total_direct_active"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALACTIVEDOWNLINE, user_detail_obj.getString("active_downline_list"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALDEACTIVEDOWNLINE, user_detail_obj.getString("deactive_downline_list"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALDIRECTDEACTIVE, user_detail_obj.getString("total_direct_deactive"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALDOWNLINE, user_detail_obj.getString("total_downline"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.LEVELINCOME, user_detail_obj.getString("level_income"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.DIRECTINCOME, user_detail_obj.getString("direct_income"));

                PreferenceConnector.writeString(svContext, PreferenceConnector.RECHARGEINCOME, user_detail_obj.getString("total_recharge_income"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.BBPSINCOME, user_detail_obj.getString("total_bbps_income"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.CASHBACKINCOME, user_detail_obj.getString("total_cashback_income"));

                PreferenceConnector.writeString(svContext, PreferenceConnector.RANK, user_detail_obj.getString("rank"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.MEMBERSHIP, user_detail_obj.getString("membership"));

                if (user_detail_obj.has("token")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.H1, user_detail_obj.getString("token"));
                }

                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINMEMBERID, user_detail_obj.getString(TAG_USER_CODE));
                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERNAME, user_detail_obj.getString(TAG_NAME));
                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINEDUSERID, user_detail_obj.getString(TAG_USERID));

                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERPHONE, user_detail_obj.getString(TAG_MOBILE));
                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSEREMAIL, user_detail_obj.getString(TAG_EMAIL));
                PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBAL, str_wallet_balance);
                PreferenceConnector.writeString(svContext, PreferenceConnector.EWALLETBAL, str_ewallet_balance);
                PreferenceConnector.writeString(svContext, PreferenceConnector.COMMISIONBALANCE, str_commision_balance);
                PreferenceConnector.writeString(svContext, PreferenceConnector.COINBALANCE, str_coin_balance);
                PreferenceConnector.writeString(svContext, PreferenceConnector.REFFERALLINK, user_detail_obj.getString("refferal_link"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.FINOKYC_CHARGE, user_detail_obj.getString("fino_kyc_charge"));

                if (user_detail_obj.has("news")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DASHNEWS, user_detail_obj.getString("news"));
                }

                if (user_detail_obj.has("is_fund_request")) {
                    String is_fundrequest_active = user_detail_obj.getString("is_fund_request");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_FUNDREQUEST_ACTIVE, is_fundrequest_active.equalsIgnoreCase("1"));
                }


                if (user_detail_obj.has("today_total_income")) {
                    String str_today_income = user_detail_obj.getString("today_total_income");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.TODAYINCOME, str_today_income);
                }

                if (user_detail_obj.has("total_income")) {
                    String str_total_income = user_detail_obj.getString("total_income");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALINCOME, str_total_income);
                }

                if (user_detail_obj.has(TAG_IS_MONEY_TRANSFER_ACTIVE)) {
                    String is_transfer_active = user_detail_obj.getString(TAG_IS_MONEY_TRANSFER_ACTIVE);
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_MONEY_TRANSFER_ACTIVE, is_transfer_active.equalsIgnoreCase("1"));
                }
                if (user_detail_obj.has(TAG_IS_MAIN_TRANSFER_ACTIVE)) {
                    String is_transfer_active = user_detail_obj.getString(TAG_IS_MAIN_TRANSFER_ACTIVE);
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_MAIN_TRANSFER_ACTIVE, is_transfer_active.equalsIgnoreCase("1"));
                }
                if (user_detail_obj.has(TAG_IS_AEPS_TRANSFER_ACTIVE)) {
                    String is_transfer_active = user_detail_obj.getString(TAG_IS_AEPS_TRANSFER_ACTIVE);
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_AEPS_TRANSFER_ACTIVE, is_transfer_active.equalsIgnoreCase("1"));
                }
                if (user_detail_obj.has(TAG_IS_COMMISSION_TRANSFER_ACTIVE)) {
                    String is_transfer_active = user_detail_obj.getString(TAG_IS_COMMISSION_TRANSFER_ACTIVE);
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_COMMISSION_TRANSFER_ACTIVE, is_transfer_active.equalsIgnoreCase("1"));
                }

                if (str_is_recharge_active.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISRECHARGEACTIVE, true);
                } else {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISRECHARGEACTIVE, false);
                }

                String str_is_razorpay_active = "";
                if (user_detail_obj.has("is_razorypay_active")) {
                    str_is_razorpay_active = user_detail_obj.getString("is_razorypay_active");
                }

                String str_is_bbps_live_active = "";
                if (user_detail_obj.has("is_bbps_active")) {
                    str_is_bbps_live_active = user_detail_obj.getString("is_bbps_active");
                }
                if (str_is_bbps_live_active.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISBBPSACTIVE, true);
                } else {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISBBPSACTIVE, false);
                }

                String str_is_apes_active = user_detail_obj.getString("is_apes_active");
                String str_newis_apes_active = user_detail_obj.getString("is_new_apes_active");
                String str_user_aeps_status = user_detail_obj.getString("user_aeps_status");
                String str_newuser_aeps_status = user_detail_obj.getString("user_new_aeps_status");

                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISAEPSACTIVE, str_is_apes_active.equalsIgnoreCase("1"));
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISNEWAEPSACTIVE, str_newis_apes_active.equalsIgnoreCase("1"));
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISAEPSKYCDONE, str_user_aeps_status.equalsIgnoreCase("1"));
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISNEWAEPSKYCDONE, str_newuser_aeps_status.equalsIgnoreCase("1"));

                String str_is_account_upgrade = user_detail_obj.getString("account_upgrade_status");
                if (str_is_account_upgrade.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISACCOUNTUPGRADE, true);
                } else {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISACCOUNTUPGRADE, false);
                }

                if (user_detail_obj.has("user_icici_aeps_status")) {
                    String icici_user_aeps_status = user_detail_obj.getString("user_icici_aeps_status");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISICICIAEPSKYCDONE, icici_user_aeps_status.equalsIgnoreCase("1"));
                }

                if (user_detail_obj.has("is_profile_updated")) {
                    String is_profile_updated = user_detail_obj.getString("is_profile_updated");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISPROFILEUPDATED, is_profile_updated.equalsIgnoreCase("1"));
                }

                if (user_detail_obj.has("is_cyrus_qr_active")) {
                    String is_profile_updated = user_detail_obj.getString("is_cyrus_qr_active");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.IS_CYRUS_QRACTIVE, is_profile_updated.equalsIgnoreCase("1"));
                }

                if (user_detail_obj.has("zoom_meeting_time")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.ZOOM_MEETING_TIME, user_detail_obj.getString("zoom_meeting_time"));
                }

                if (user_detail_obj.has("vendor_status")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.VENDOR_STATUS, user_detail_obj.getString("vendor_status"));
                }
                if (user_detail_obj.has("is_vendor_package_purchased")) {
                    String isVendorPackagePurchased = user_detail_obj.getString("is_vendor_package_purchased");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.VENDOR_PACKAGE_PURCHASED, isVendorPackagePurchased.equalsIgnoreCase("1"));
                }

                if (user_detail_obj.has("total_notification")) {
                    String strTotalNotification = user_detail_obj.getString("total_notification");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.NOTIFICATION_COUNT, strTotalNotification);
                }

                if (user_detail_obj.has("paysprint_partner_id")) {
                    String str_partner_id = user_detail_obj.getString("paysprint_partner_id");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.PARTNER_ID, str_partner_id);
                }
                if (user_detail_obj.has("paysprint_secret_key")) {
                    String str_partner_key = user_detail_obj.getString("paysprint_secret_key");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.PARTNER_KEY, str_partner_key);
                }

                FragHomeDashBoard.lstSlider = new ArrayList<>();
                JSONArray slider_data = user_detail_obj.getJSONArray("sliderData");
                for (int i = 0; i < slider_data.length(); i++) {
                    JSONObject slider_img = slider_data.getJSONObject(i);
                    FragHomeDashBoard.lstSlider.add(new SliderModel(i + "",
                            slider_img.getString("link"),
                            GlobalVariables.PRE_URL_MAIN + slider_img.getString("imageUrl")));
                }

                ActivityMain.lstCategoryData = new ArrayList<>();
                JSONArray docCategoryList = user_detail_obj.getJSONArray("documentData");
                for (int i = 0; i < docCategoryList.length(); i++) {
                    JSONObject categoryObj = docCategoryList.getJSONObject(i);
                    ActivityMain.lstCategoryData.add(new SpinnerModel(categoryObj.getString("id"), categoryObj.getString("title"), ""));
                }

                if (str_is_razorpay_active.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISRAZORPAYACTIVE, true);
                    PreferenceConnector.writeString(svContext, PreferenceConnector.RAZORPAYID,
                            user_detail_obj.getString("razor_pay_key"));
                }else {
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISRAZORPAYACTIVE, false);
                    PreferenceConnector.writeString(svContext, PreferenceConnector.RAZORPAYID, "");
                }

                String str_success = "", str_failed = "", str_pending = "", str_certificate_url = "", str_idcard_url = "";
                if (user_detail_obj.has("successAmount")) {
                    str_success = user_detail_obj.getString("successAmount");
                }
                if (user_detail_obj.has("failedAmount")) {
                    str_failed = user_detail_obj.getString("failedAmount");
                }
                if (user_detail_obj.has("pendingAmount")) {
                    str_pending = user_detail_obj.getString("pendingAmount");
                }
                if (user_detail_obj.has("certificate_url")) {
                    str_pending = user_detail_obj.getString("certificate_url");
                }
                if (user_detail_obj.has("idcard_url")) {
                    str_pending = user_detail_obj.getString("idcard_url");
                }
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALSUCCESS, str_success);
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALFAILED, str_failed);
                PreferenceConnector.writeString(svContext, PreferenceConnector.TOTALPENDING, str_pending);

                PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERPROFILEPIC, str_profile_photo);
                PreferenceConnector.writeString(svContext, PreferenceConnector.CERTIFICATE_URL, str_certificate_url);
                PreferenceConnector.writeString(svContext, PreferenceConnector.IDCARD_URL, str_idcard_url);


                if (user_detail_obj.has("address")) {
                    String strAddress = user_detail_obj.getString("address");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINADDRESS, strAddress);
                }
                if (user_detail_obj.has("pincode")) {
                    String strPincode = user_detail_obj.getString("pincode");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPINCODE, strPincode);
                }
                if (user_detail_obj.has("city_id")) {
                    String strCityId = user_detail_obj.getString("city_id");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINCITYID, strCityId);
                }
                if (user_detail_obj.has("state_id")) {
                    String strStateId = user_detail_obj.getString("state_id");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINSTATEID, strStateId);
                }

                if (user_detail_obj.has("block_id")) {
                    String strBlockId = user_detail_obj.getString("block_id");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINBLOCKID, strBlockId);
                }

                if (user_detail_obj.has("is_login")) {
                    String strIsDeviceLogin = user_detail_obj.getString("is_login");
                    if (strIsDeviceLogin.equalsIgnoreCase("0")) {
                        PreferenceConnector.cleanPrefrences(svContext);
                        Intent svIntent = new Intent(svContext, ActivityLogin.class);
                        svContext.startActivity(svIntent);
                        ShowCustomToast customToast = new ShowCustomToast(svContext);
                        customToast.showCustomToast(svContext, "Someone other login this account", customToast.ToastyError);
                        ((Activity) svContext).finishAffinity();
                    }
                }

                if (isLogin) {
                    if (user_detail_obj.has("is_address_updated")) {
                        String strIsAddressUpdated = user_detail_obj.getString("is_address_updated");
                        if (strIsAddressUpdated.equalsIgnoreCase("0")) {
                            Intent svIntent = new Intent(svContext, ActivityProfile.class);
                            svContext.startActivity(svIntent);
//                            ((Activity) svContext).finishAffinity();
                            return;
                        }
                    }

                    Intent svIntent = new Intent(svContext, ActivityMain.class);
                    svContext.startActivity(svIntent);
                    ((Activity) svContext).finishAffinity();
                }

            } else {
                PreferenceConnector.cleanPrefrences(svContext);
                Intent svIntent = new Intent(svContext, ActivityLogin.class);
                svContext.startActivity(svIntent);
                ((Activity) svContext).finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    private void showDynamicLinksOffer(Intent intent) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        String promotionCode = deepLink.getQueryParameter("referral_id");
                        if (TextUtils.isEmpty(promotionCode)) {
                            Toast.makeText(svContext, "Referral Code not valid.", Toast.LENGTH_SHORT).show();
                        } else {
                            strReferCode = (promotionCode);

                            Intent svIntent = new Intent(svContext, ActivitySignUp.class);
                            svIntent.putExtra("refercode", strReferCode);
                            startActivity(svIntent);
                            finish();
                        }
                    } else {
                        LoadSplash();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                        LoadSplash();
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showDynamicLinksOffer(intent);
        setIntent(intent);
    }

    private String strReferCode;
}