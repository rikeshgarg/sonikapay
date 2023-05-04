package com.codunite.commonutility;

import android.os.Environment;

import java.io.File;

public class GlobalVariables {
    public static final String defaultAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "Recharge/";

    public static final String CUSTOMFONTNAME       = "font/font_medium.ttf";
    public static final String CUSTOMFONTNAMEBOLD   = "font/font_bold.ttf";
    public static final String UPIPAYMENTGATEWAYKEY = "a69f485a-8d0c-41b2-bd71-f79b46c09cbd";
    public static String ORDERID = "";

    public static String PRE_URL_MAIN = "https://vsidigital.in/";
    public static String PRE_URL = PRE_URL_MAIN + "appservice/auth/";

    public static final String CURRENCYSYMBOL = "₹ ";
    public static final boolean ISTESTING = true;
    public static final String TAGPOSTTEXT = ".............tagprint..............";

    public static String TOKEN = "";
    public static String DEVICEID = "";
}