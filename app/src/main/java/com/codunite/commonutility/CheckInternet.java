package com.codunite.commonutility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.codunite.sonikapay.R;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

public class CheckInternet {
    private Context context;
    public CheckInternet(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
                            Log.i("CheckInternet_status", "Network is available"+GlobalVariables.TAGPOSTTEXT);
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
}
