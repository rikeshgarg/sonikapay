package com.codunite.commonutility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codunite.sonikapay.activity.ActivityLogin;
import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.ActivityNotification;
import com.codunite.sonikapay.ActivitySplash;
import com.codunite.sonikapay.R;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

public class NoInternetScreen {
    private Context context;
    private static RelativeLayout layConnection;
    private RelativeLayout layProgressbarRefresh;
    private TextView textErrorTitle, textErrorDesc;
    private ImageView imgActivateWifi, imgActivateRefresh, imgActivateMobiledata;
    private ProgressBar progressBarRefresh;
    private Activity act;
    public NoInternetScreen(Context context, View view, Activity act) {
        this.context = context;

        layConnection = (RelativeLayout) view.findViewById(R.id.lay_connection);
        layProgressbarRefresh = (RelativeLayout) view.findViewById(R.id.lay_activate_refresh);
        progressBarRefresh = (ProgressBar) view.findViewById(R.id.progressbar_refresh);
        textErrorTitle = (TextView) view.findViewById(R.id.nointernet_title);
        textErrorDesc = (TextView) view.findViewById(R.id.nointernet_desc);
        imgActivateWifi = (ImageView) view.findViewById(R.id.img_activate_wifi);
        imgActivateRefresh = (ImageView) view.findViewById(R.id.img_activate_refresh);
        imgActivateMobiledata = (ImageView) view.findViewById(R.id.img_activate_mobiledata);

        progressBarRefresh.setVisibility(View.INVISIBLE);

        imgActivateWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        layProgressbarRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isConnectingToInternet()){
                    hideError();
                }else {
                    showError();
                    onResumeLoadData(act);
                }
            }
        });

        imgActivateMobiledata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        });
    }

    private void onResumeLoadData(Activity act) {
        if (act instanceof  ActivitySplash) {
            ((ActivitySplash)act).resumeApp();
        }else if (act instanceof ActivityMain) {
            ((ActivityMain)act).resumeApp();
        }else if (act instanceof ActivityNotification) {
            ((ActivityNotification)act).resumeApp();
        }else if (act instanceof ActivityLogin) {
            ((ActivityLogin)act).resumeApp();
        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        if (GlobalVariables.ISTESTING) {
//                            Log.i("CheckInternet_status", "Network is available"+GlobalVariables.TAGPOSTTEXT);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    if (GlobalVariables.ISTESTING) {
                        Log.i("CheckInternet_error", "" + e.getMessage()+GlobalVariables.TAGPOSTTEXT);
                    }
                }
            }
        }
        if (GlobalVariables.ISTESTING) {
            Log.i("CheckInternet_status", "Network is not available"+GlobalVariables.TAGPOSTTEXT);
        }
        return false;
    }

    public void showInternetAlert() {
        getStringRes(R.string.internet_title);
        // Create Alert using Builder
        new AndExAlertDialog.Builder(context)
                .setTitle(getStringRes(R.string.internet_title))
                .setMessage(getStringRes(R.string.internet_msg))
                .setPositiveBtnText(getStringRes(R.string.internet_postivetext))
                .setNegativeBtnText(getStringRes(R.string.internet_negativetext))
                .setCancelableOnTouchOutside(false)
                .OnPositiveClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {
                        isConnectingToInternet();
                    }
                })
                .OnNegativeClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {

                    }
                })
                .build();
    }

    public String getStringRes(int strId) {
        String str = context.getResources().getString(strId);
        return str;
    }

    public static void showInternetError() {
        layConnection.setVisibility(View.VISIBLE);
    }

    public void showError() {
        layConnection.setVisibility(View.VISIBLE);
    }

    public static void hideError() {
        layConnection.setVisibility(View.GONE);
    }
}
