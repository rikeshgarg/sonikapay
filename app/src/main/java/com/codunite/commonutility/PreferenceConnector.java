package com.codunite.commonutility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector {
    public static final String PREF_NAME = "app_prefrences";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String AUTOLOGIN = "autologin";

    public static final String WEBHEADING = "webhead";
    public static final String WEBURL = "weburl";
    public static final String REFFERALLINK = "refferal_link";

    public static final String LANGUAGE = "language";
    public static final String DEVICE_ID = "device_id";
    public static final String FCMID = "fcmid";
    public static final String NOTIFICATION_COUNT = "total_notification";

    public static final String LOGINEDUSERID = "logineduserid";
    public static final String DASHNEWS = "dashnews";
    public static final String LOGINUSERNAME = "loginusername";
    public static final String WALLETBAL = "walletbal";
    public static final String ZOOM_MEETING_TIME = "zoom_meeting_time";
    public static final String TOTALDIRECTDOWNLINE = "total_direct_downline";
    public static final String TOTALDIRECTACTIVE = "total_direct_active";

    public static final String TOTALACTIVEDOWNLINE = "total_downline_active";
    public static final String TOTALDEACTIVEDOWNLINE = "total_downline_deactive";

    public static final String TOTALDIRECTDEACTIVE = "total_direct_deactive";
    public static final String TOTALDOWNLINE = "total_downline";
    public static final String EWALLETBAL = "ewalletbal";
    public static final String COMMISIONBALANCE = "commisionbalance";
    public static final String COINBALANCE = "coinbalance";
    public static final String DIRECTINCOME = "direct_income";
    public static final String LEVELINCOME = "level_income";
    public static final String FINOKYC_CHARGE = "fino_kyc_charge";
    public static final String TODAYINCOME = "today_income";
    public static final String TOTALINCOME = "total_income";

    public static final String RECHARGEINCOME = "recharge_income";
    public static final String BBPSINCOME = "bbps_income";
    public static final String CASHBACKINCOME = "cashback_income";

    public static final String MEMBERSHIP = "membership";
    public static final String RANK = "rank";

    public static final String LOGINUSERPHONE = "loginuserphone";
    public static final String LOGINUSEREMAIL = "loginuseremail";
    public static final String LOGINUSERPROFILEPIC = "loginuserprofilepic";
    public static final String LOGINMEMBERID = "loginmemberid";
    public static final String LOGINADDRESS = "loginaddress";
    public static final String LOGINPINCODE = "loginpincode";
    public static final String LOGINCITYID = "logincityid";
    public static final String LOGINSTATEID = "loginstateid";

    public static final String AUTOLOGINID = "autologinid";
    public static final String AUTOLOGINPWD = "autologinpwd";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LOCATION = "location";
    public static final String ISMANTRA = "ismantra";

    public static final String MARCHENT_ID = "marchent_id";
    public static final String PARTNER_ID = "partner_id";
    public static final String PARTNER_KEY = "partner_key";
    public static final String ISACCOUNTUPGRADE = "account_upgrade_status";

    public static final String ISAEPSACTIVE = "isaepsactive";
    public static final String ISAEPSKYCDONE = "isaepskycactive";
    public static final String ISNEWAEPSACTIVE = "is_newaepsactive";    // for new icici apes
    public static final String ISNEWAEPSKYCDONE = "isnewaepskycactive"; // for new icici apes
    public static final String ISICICIAEPSKYCDONE = "isiciciaepskycactive";    // for new icici apes

    public static final String VENDOR_STATUS = "vendor_status";
    public static final String VENDOR_PACKAGE_PURCHASED = "is_vendor_package_purchased";
    public static final String IS_CYRUS_QRACTIVE = "is_cyrus_qr_active";
    public static final String ISDARKTHEME = "isdarktheme";
    public static final String LOGINUSERTYPE = "retailor";
    public static final String ISRECHARGEACTIVE = "isrechargeactive";
    public static final String TOTALSUCCESS = "totalsuccess";
    public static final String TOTALFAILED = "totalfailed";
    public static final String TOTALPENDING = "totalpending";
    public static final String ISRAZORPAYACTIVE = "israzorpayactive";
    public static final String ISBBPSACTIVE = "isbbpsactive";
    public static final String IS_MONEY_TRANSFER_ACTIVE = "is_money_transfer_active";
    public static final String IS_MAIN_TRANSFER_ACTIVE = "is_main_transfer_active";
    public static final String IS_AEPS_TRANSFER_ACTIVE = "is_aeps_transfer_active";
    public static final String IS_COMMISSION_TRANSFER_ACTIVE = "is_commission_transfer_active";
    public static final String IS_FUNDREQUEST_ACTIVE = "is_fund_request";

    public static final String RAZORPAYID = "razorpayId";
    public static final String CERTIFICATE_URL = "certificate_url";
    public static final String IDCARD_URL = "idcard_url";
    public static final String PACKAGE_DOWNLOADED = "packagedownloaded";
    public static final String LOGINBLOCKID = "block_id";
    public static final String H1 = "SDAS";
    public static final String ISPROFILEUPDATED = "isprofileupdated";
    public static final String MINDEPOSIT = "mindeposit";
    public static final String MAXDEPOSIT = "maxdeposit";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void cleanPrefrences(Context context) {
        getPreferences(context).edit().clear().commit();
    }
}
