package com.codunite.sonikapay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.UpgradeAdapter;
import com.codunite.model.PackageModel;
import com.codunite.commonutility.CheckInternet;
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

public class ActivityUpgrade extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};

    private EditText[] edTexts = {};
    private String[] edTextsError = {"Enter phone number"};
    private int[] editTextsClickId = {};

    private RecyclerView rvUpgrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_upgrade);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    public void resumeApp() {
        rvUpgrade = (RecyclerView) findViewById(R.id.rv_upgrade);
        LoadData();
        OpenDemoLink();
    }

    private void LoadData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETPACKAGELIST, lstUploadData);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
//                        case R.id.btn_finish:
//                            CheckData();
//                            break;
//                        case R.id.btn_backform:
//                            ShowBackCardView();
//                            break;
                    }
                }
            });
        }

//        btnBack = (Button) allViewWithClick[0];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityUpgrade.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        loadToolBar();
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Upgrade");
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

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    List<PackageModel> lstItems;

    public static final String TAG_DATA = "data";
    public static final String TAG_PACKAGE_AMOUNT = "package_amount";
    public static final String TAG_ID = "id";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GETPACKAGELIST)) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data_obj = json.getJSONArray(TAG_DATA);
                    for (int j = 0; j < data_obj.length(); j++) {
                        JSONObject jsonObj = data_obj.getJSONObject(j);
                        String str_package_amount = jsonObj.getString("package_amount");
                        String str_id = jsonObj.getString("package_id");
                        String str_package_name = jsonObj.getString("package_name");

                        lstItems.add(new PackageModel(str_id, str_package_name, str_package_amount));
                    }
                    loadRecyClerView();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.UPGRADE)) {
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
        } else if (url.contains(ApiInterface.GETUSERNAME)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    txtUsername.setText(json.getString("member_name"));
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }else if (url.contains(ApiInterface.GETDEMOLINK)) {
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    strDemoServiceName = json.getString("service");
                    dtrDemoServiceUrl = json.getString("demo_link");
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private void loadRecyClerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUpgrade.setLayoutManager(layoutManager);

        rvUpgrade.setHasFixedSize(true);
        int animation_type = ItemAnimation.LEFT_RIGHT;
        UpgradeAdapter mAdapter = new UpgradeAdapter(this, lstItems, animation_type);
        rvUpgrade.setAdapter(mAdapter);
        mAdapter.setOnItemUpgradeClickListener(new UpgradeAdapter.OnItemUpgradeClickListener() {
            @Override
            public void onItemUpgradeClick(View view, int position) {
                showPinDialog(position, "1");
            }
        });

        mAdapter.setmOnItemWalletListener(new UpgradeAdapter.OnItemWalletClickListener() {
            @Override
            public void onItemWalletClick(View view, int position) {
                showPinDialog(position, "2");
            }
        });
    }

    TextView txtUsername;

    public void showPinDialog(int position, String upgradeBy) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Memberid and PIN Token");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_header_editext, null);
        EditText edtMemberId = customLayout.findViewById(R.id.editText_memberid);
        EditText edPin = customLayout.findViewById(R.id.editText_pin);
        if (upgradeBy.equalsIgnoreCase("2")) {
            edPin.setVisibility(View.GONE);
        }else {
            edPin.setVisibility(View.VISIBLE);
        }
        edtMemberId.setText(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINMEMBERID, ""));
        ImageView imgSearch = customLayout.findViewById(R.id.img_search);
        txtUsername = customLayout.findViewById(R.id.member_name);
        txtUsername.setText(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERNAME, ""));
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((edtMemberId.getText().toString().trim()).length() == 0) {
                    edtMemberId.setError("No member id enterred");
                } else {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(edtMemberId.getText().toString().trim());
                    callWebService(ApiInterface.GETUSERNAME, lstUploadData);
                }
            }
        });
        builder.setView(customLayout);
        builder.setPositiveButton("Upgrade",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ((edtMemberId.getText().toString().trim()).length() == 0) {
                            customToast.showCustomToast(svContext, "Please enter member id", customToast.ToastyError);
                        } else {
                            if (upgradeBy.equalsIgnoreCase("2")) {
                                UpgradeUser(lstItems.get(position).getId(), "",
                                        edtMemberId.getText().toString().trim(), upgradeBy);
                            }else {
                                if ((edPin.getText().toString().trim()).length() == 0) {
                                    customToast.showCustomToast(svContext, "Please enter PIN Token", customToast.ToastyError);
                                }else {
                                    UpgradeUser(lstItems.get(position).getId(), edPin.getText().toString().trim(),
                                            edtMemberId.getText().toString().trim(), upgradeBy);
                                }
                            }

                        }
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void UpgradeUser(String strPackageId, String pin, String memberid, String upgradeBy) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(strPackageId);
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
        super.onBackPressed();
    }

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("14");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}