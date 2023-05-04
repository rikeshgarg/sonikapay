package com.codunite.sonikapay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codunite.adapter.AdapterAmount;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.codunite.commonutility.dialogandpicker.CustomeProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivityTopupFund extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private int[] allViewWithClickId = {R.id.btn_addmoney};

    private RecyclerView rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_topupwallet);
        StartApp();
        OnClickCombineDeclare(allViewWithClickId);
        resumeApp();
    }

    private TextView txtWinningAMount;
    private EditText txtWithdrawAmount;

    public void resumeApp() {
        rvAdapter = (RecyclerView) findViewById(R.id.recyclerView);
        txtWinningAMount = (TextView) findViewById(R.id.txt_total_bal);
        txtWithdrawAmount = (EditText) findViewById(R.id.txt_add_bal);

        txtWinningAMount.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, "0"));
        setAdapter();
    }

    String[] strId = {"100", "200", "300", "500", "800", "1000", "1200", "1500", "1800", "2000"};
    List<String> lstOpenHistory = new ArrayList<>();

    private void setAdapter() {
        for (int i = 0; i < strId.length; i++) {
            lstOpenHistory.add(strId[i]);
        }

        AdapterAmount mAdapter = new AdapterAmount(this, lstOpenHistory, R.layout.item_amount);
        rvAdapter.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, amount) -> txtWithdrawAmount.setText(amount));
    }

    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (findViewById(allViewWithClickId[j])).setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.btn_addmoney:
                        TopupWallet();
                        break;
                }
            });
        }
    }

    private void TopupWallet() {
        int response = 0;
        if (txtWithdrawAmount.getText().toString().trim().length() == 0) {
            response++;
            customToast.showCustomToast(svContext, "Please enter amount", customToast.ToastyError);
        }

        if (response == 0) {
            if (Integer.parseInt(txtWithdrawAmount.getText().toString().trim()) <
                    Integer.parseInt(PreferenceConnector.readString(svContext, PreferenceConnector.MINDEPOSIT, "1"))) {
                response++;
                customToast.showCustomToast(svContext, "Amount can't be less than " +
                                PreferenceConnector.readString(svContext, PreferenceConnector.MINDEPOSIT, "100"),
                        customToast.ToastyError);
            }

            if (Integer.parseInt(txtWithdrawAmount.getText().toString().trim()) >
                    Integer.parseInt(PreferenceConnector.readString(svContext, PreferenceConnector.MAXDEPOSIT, "2000"))) {
                response++;
                customToast.showCustomToast(svContext, "Amount can't be greater than " +
                                PreferenceConnector.readString(svContext, PreferenceConnector.MAXDEPOSIT, "2000"),
                        customToast.ToastyError);
            }
        }

        if (response == 0) {
            StartUpiPayment(txtWithdrawAmount.getText().toString());
        }


    }

    private CustomeProgressDialog customeProgressDialog;
    public static boolean isPaymentTried = false;
    public static boolean isTranscationComplete = false;
    private void StartUpiPayment(String amountAdd) {
        customeProgressDialog = new CustomeProgressDialog(svContext, R.layout.lay_customprogessdialog);
        TextView textView = customeProgressDialog.findViewById(R.id.loader_showtext);
        textView.setVisibility(View.GONE);

        customeProgressDialog.setCancelable(false);
        customeProgressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(svContext);
        StringRequest request = new StringRequest(Request.Method.POST, "https://merchant.upigateway.com/api/create_order",
                response -> {
                    Log.d("res=LoginUser==>", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String str_result = json.getString("status");
                        if (str_result.equalsIgnoreCase("true")) {
                            JSONObject logindetail_obj = json.getJSONObject("data");

                            GlobalVariables.ORDERID = logindetail_obj.getString("order_id");
                            String str_payment_url = logindetail_obj.getString("payment_url");

                            isPaymentTried = true;

                            Intent browserIntent = new Intent(svContext, WebViewActivity.class);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, "");
                            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, str_payment_url);
                            startActivity(browserIntent);
                        } else {
                            customToast.showCustomToast(svContext, json.getString("msg"), customToast.ToastyError);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                        customeProgressDialog.dismiss();
                    }
                }, error -> Log.d("error", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", GlobalVariables.UPIPAYMENTGATEWAYKEY);
                params.put("client_txn_id", getcurrentDate() + getFormatedcurrentTime());
                params.put("amount", amountAdd);
                params.put("p_info", "Add money to wallet");
                params.put("udf1", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
                params.put("customer_name", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERNAME, ""));
                params.put("customer_email", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSEREMAIL, ""));
                params.put("customer_mobile", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERPHONE, ""));
                params.put("redirect_url", "https://www.actioncomplete.com");

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Log.e(key, value);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
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
    public void onResume() {
        if (isPaymentTried && !GlobalVariables.ORDERID.equals("") && !isTranscationComplete) {
            isPaymentTried = false;
            isTranscationComplete = false;
        }else if(isPaymentTried && !GlobalVariables.ORDERID.equals("") && isTranscationComplete){
            isPaymentTried = false;
            isTranscationComplete = false;
            onBackPressed();
        }
        super.onResume();
    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;
    private ViewGroup root;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActivityTopupFund.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        loadToolBar();
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(view -> {
            onBackPressed();
        });

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("ADD MONEY");
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
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
//        if (url.contains(ApiInterface.ADDPOINT)) {
//            customToast.showCustomToast(svContext, result, customToast.ToastyInfo);
//        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}