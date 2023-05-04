package com.codunite.sonikapay;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.PinHistoryAdapter;
import com.codunite.model.PinHistoryModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityPinList extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    private ImageView imgToolBarBack;
    private RecyclerView wallethistoryrv;
    private TextView txtWalletbal;
    private Button btnAddWallet;
    private CardView cvAddWallet, cardShowBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pinhistory);
        StartApp();

        resumeApp();
    }

    public void resumeApp() {
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);
        txtWalletbal = (TextView) findViewById(R.id.walletbal);
        btnAddWallet = (Button) findViewById(R.id.btn_addwallet);
        txtWalletbal.setVisibility(View.INVISIBLE);
        cvAddWallet = (CardView) findViewById(R.id.card_addwallet);
        cardShowBalance = (CardView) findViewById(R.id.card_wallbal);
        cvAddWallet.setVisibility(View.GONE);
        cardShowBalance.setVisibility(View.GONE);

        btnAddWallet.setOnClickListener(this);
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.PINLIST, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityPinList.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
            // FontUtils.setThemeColor(root, svContext, true);
        }else {
            // FontUtils.setThemeColor(root, svContext, false);
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

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Pin List");
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

    LinkedList<String> lstUploadData = new LinkedList<>();
    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private List<PinHistoryModel> lstItems = new ArrayList<>();
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.PINLIST)) {
            try {
                lstItems = new ArrayList<>();

                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < ((JSONArray) data).length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String package_name = data_obj.getString("package_name");
                        String package_amount = data_obj.getString("package_amount");
                        String token = "Will approve soon", is_used = "NA", used_by = "NA", used_date = "NA";
                        if (data_obj.has("token")) {
                            token = data_obj.getString("token");
                            is_used = data_obj.getString("is_used");
                            used_by = data_obj.getString("used_by");
                            used_date = data_obj.getString("used_date");
                        }

                        lstItems.add(new PinHistoryModel(package_name, package_amount, token,
                                is_used, used_by, used_date));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

//            cvAddWallet.setVisibility(View.VISIBLE);
//            cardShowBalance.setVisibility(View.VISIBLE);

            txtWalletbal.setVisibility(View.VISIBLE);
            txtWalletbal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, "0"));

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            wallethistoryrv.setLayoutManager(layoutManager);
            wallethistoryrv.setHasFixedSize(true);
            int animation_type = ItemAnimation.LEFT_RIGHT;
            PinHistoryAdapter mAdapter = new PinHistoryAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new PinHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String obj, int position) {
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("Token Copied", lstItems.get(position).getToken());
//                    clipboard.setPrimaryClip(clip);
//                    customToast.showCustomToast(svContext, "Token Copied", customToast.ToastySuccess);

                    showPinDialog(position, "1");
                }
            });
        }else if (url.contains(ApiInterface.UPGRADE)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    public void showPinDialog(int position, String upgradeBy) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Memberid");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_header_editext, null);
        EditText edtMemberId = customLayout.findViewById(R.id.editText_memberid);
        EditText edPin = customLayout.findViewById(R.id.editText_pin);
        edPin.setVisibility(View.VISIBLE);

        edtMemberId.setText(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINMEMBERID, ""));
        edPin.setText(lstItems.get(position).getToken());
        ImageView imgSearch = customLayout.findViewById(R.id.img_search);

        TextView txtUsername = customLayout.findViewById(R.id.member_name);
        txtUsername.setText(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERNAME, ""));
        imgSearch.setOnClickListener(view -> {
            if ((edtMemberId.getText().toString().trim()).length() == 0) {
                edtMemberId.setError("No member id enterred");
            } else {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(edtMemberId.getText().toString().trim());
                callWebService(ApiInterface.GETUSERNAME, lstUploadData);
            }
        });
        builder.setView(customLayout);
        builder.setPositiveButton("Upgrade",
                (dialog1, which) -> {
                    if ((edtMemberId.getText().toString().trim()).length() == 0) {
                        customToast.showCustomToast(svContext, "Please enter member id", customToast.ToastyError);
                    }else {
                        if (upgradeBy.equalsIgnoreCase("2")) {
                            UpgradeUser("", "",
                                    edtMemberId.getText().toString().trim(), upgradeBy);
                        }else {
                            if ((edPin.getText().toString().trim()).length() == 0) {
                                customToast.showCustomToast(svContext, "Please enter PIN Token", customToast.ToastyError);
                            }else {
                                UpgradeUser("", edPin.getText().toString().trim(),
                                        edtMemberId.getText().toString().trim(), upgradeBy);
                            }
                        }
                    }
                });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void UpgradeUser(String strPackageId, String pin, String memberid, String upgradeBy) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add("2");
        lstUploadData.add(pin);
        lstUploadData.add(memberid);
        lstUploadData.add(upgradeBy);
        callWebService(ApiInterface.UPGRADE, lstUploadData);
    }


    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }
}