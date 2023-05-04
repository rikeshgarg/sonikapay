package com.codunite.commonutility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.codunite.sonikapay.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ShowCustomToast {
    private Context con;

    public final int ToastyError = 0;
    public final int ToastySuccess = 1;
    public final int ToastyInfo = 2;
    public final int ToastyWarning = 3;
    public final int ToastyNormal = 4;
    public final int ToastyNormalWithIcon = 5;
    public final int SweetAlertSuccess = 6;
    public final int SweetAlertFailed = 7;

    public ShowCustomToast(Context context) {
        this.con = context;
    }

    public void showCustomToast(final Context con, final String toastString, final int ToastType) {
        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (ToastType) {
                    case ToastyError:
                        Toasty.error(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastySuccess:
                        Toasty.success(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyInfo:
                        Toasty.info(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyWarning:
                        Toasty.warning(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyNormal:
                        Toasty.normal(con, toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyNormalWithIcon:
                        Drawable icon = con.getResources().getDrawable(R.drawable.loader);
                        Toasty.normal(con, toastString, icon).show();
                        break;
                    case SweetAlertSuccess:
                        new SweetAlertDialog(con, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText(toastString)
                                .setConfirmText("Close")
                                .setConfirmClickListener(sDialog -> sDialog.cancel())
                                .show();
                        break;
                    case SweetAlertFailed:
                        new SweetAlertDialog(con, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Failed")
                                .setContentText(toastString)
                                .setConfirmText("Close")
                                .setConfirmClickListener(sDialog -> sDialog.cancel())
                                .show();
                        break;
                    default:
                        break;
                }
                if (GlobalVariables.ISTESTING) {
                    System.out.println(toastString + "..........toastString__print......");
                }
            }
        });
    }

    public void showCustomToast(final String toastString, final int ToastType) {
        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                .setToastTypeface(Typeface.createFromAsset(con.getAssets(), GlobalVariables.CUSTOMFONTNAME)) // optional
                .setTextSize(18) // optional
                .apply(); // required

        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (ToastType) {
                    case ToastyError:
                        Toasty.error(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastySuccess:
                        Toasty.success(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyInfo:
                        Toasty.info(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyWarning:
                        Toasty.warning(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyNormal:
                        Toasty.normal(con, toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyNormalWithIcon:
                        Drawable icon = con.getResources().getDrawable(R.drawable.loader);
                        Toasty.normal(con, toastString, icon).show();
                        break;
//                  case ToastySuccess:
//                        Toasty.Config.getInstance()
//                                .setTextColor(Color.GREEN)
//                                .setToastTypeface(Typeface.createFromAsset(getAssets(), "PCap Terminal.otf"))
//                                .apply();
//                        Toasty.custom(_context, "sudo kill -9 everyone", getResources().getDrawable(R.drawable.laptop512),
//                                Color.BLACK, Toast.LENGTH_SHORT, true, true).show();
//                        Toasty.Config.reset(); // Use this if you want to use the configuration above only once
//                        break;
                    default:
                        break;
                }

                if (GlobalVariables.ISTESTING) {
                    System.out.println(toastString + "..........toastString__print......");
                }
            }
        });
    }

    public void showToast(final String string, final Context con) {
        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCustomToast(string, ToastyNormal);

                if (GlobalVariables.ISTESTING) {
                    System.out.println(string + "..........toast print......");
                }
            }
        });
    }
}
