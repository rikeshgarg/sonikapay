package com.codunite.sonikapay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
import com.codunite.adapter.PinHisAdapter;
import com.codunite.model.WalletHistoryModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityRequestPin extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private static final String TAG = "RupayMine";
    private EditText edAmount;
    private Button btnproceedtopay, chooseScreenshot, removeScreenshot;
    private TextView txtCurrentBal, txtTotal, txtNofileScreenshot;

    private ImageView imgBack;
    private View[] allViewWithClick = {btnproceedtopay, imgBack, chooseScreenshot, removeScreenshot};
    private int[] allViewWithClickId = {R.id.btnproceedtopay, R.id.img_back, R.id.choose_scrrenshot, R.id.remove_scrrenshot};

    private EditText[] edTexts = {edAmount};
    private String[] edTextsError = {"Enter amount"};
    private int[] editTextsClickId = {R.id.edtenteramount};

    private List<WalletHistoryModel> lstItems = new ArrayList<>();
    private RecyclerView wallethistoryrv;
    private String strPackageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_requestpin);
        StartApp();

        //Start Coding from here
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        txtCurrentBal = (TextView) findViewById(R.id.current_bal);
        txtTotal = (TextView) findViewById(R.id.txt_amount);

        txtNofileScreenshot = (TextView) findViewById(R.id.nofile_scrrenshot);
        txtNofileScreenshot.setVisibility(View.VISIBLE);
        imgPayScreenShot = (ImageView) findViewById(R.id.img_scrrenshot);
        imgPayScreenShot.setVisibility(View.GONE);

        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    txtTotal.setText("Total Amount = " + (Double.parseDouble(str_package_amount) * Integer.parseInt(edAmount.getText().toString())));
                }
            }
        });

        LoadData();
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebServiceWithoutDialog(ApiInterface.PINREQUESTHISTORY, lstUploadData);
    }

    private Uri imagePayScreenShot = null;
    private int selectedImageView = 0;
    private ImageView imgPayScreenShot;
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            //Toast.makeText(svContext, ActivityBrowseProfileImage.imageUri.toString() + "\n"+ selectedImageView, Toast.LENGTH_SHORT).show();
            if (selectedImageView == 0) {
                imagePayScreenShot = ActivityBrowseProfileImage.imageUri;
                imgPayScreenShot.setImageURI(null);
                imgPayScreenShot.setImageURI(imagePayScreenShot);
                imgPayScreenShot.setVisibility(View.VISIBLE);
                txtNofileScreenshot.setVisibility(View.GONE);
            }
            ActivityBrowseProfileImage.imageUri = null;
        }
    }

    private void LoadData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETPACKAGELIST, lstUploadData);
    }

    private void Proceedtopay() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (imagePayScreenShot == null) {
            response++;
            customToast.showCustomToast(svContext, "Please attach payment screenshot", customToast.ToastyWarning);
        }

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(strPackageId);
            lstUploadData.add(edAmount.getText().toString().trim());
            lstUploadData.add(encodeImage(imagePayScreenShot));
            callWebService(ApiInterface.REQUESTTOKENAUTH, lstUploadData);
        }
    }

    private String encodeImage(Uri imgUri) {
        File imagefile = new File(imgUri.getPath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.img_back:
                        onBackPressed();
                        break;
                    case R.id.btnproceedtopay:
                        Proceedtopay();
                        break;
                    case R.id.choose_scrrenshot:
                        selectedImageView = 0;
                        Intent svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                        svIntent.putExtra("isShowCrop", false);
                        startActivity(svIntent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case R.id.remove_scrrenshot:
                        imagePayScreenShot = null;
                        imgPayScreenShot.setImageURI(null);
                        imgPayScreenShot.setVisibility(View.GONE);
                        txtNofileScreenshot.setVisibility(View.VISIBLE);
                        break;
                }
            });
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edAmount = (EditText) editTexts[0];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//
            default:
                break;
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityRequestPin.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        loadToolBar();
    }

    private void loadToolBar() {
        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Request Pin");
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

    private void callWebServiceWithoutDialog(String postUrl, LinkedList<String> lstUploadData) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, this, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static final String TAG_DATA = "data";
    public static final String TAG_AFTER_BALANCE = "after_balance";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_BEFORE_BALANCE = "before_balance";
    public static final String TAG_TYPE = "type";
    public static final String TAG_WALLET_BALANCE = "wallet_balance";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    public static final String TAG_PACKAGE_AMOUNT = "package_amount";
    public static final String TAG_ID = "id";
    String str_package_amount;
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.REQUESTTOKENAUTH)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_status = json.getString("status");
                String str_msg = json.getString("message");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);

                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.PINREQUESTHISTORY)) {
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
                        String str_amount = data_obj.getString("package_amount");
                        String str_datetime = data_obj.getString("date");
                        String str_description = data_obj.getString("package_name") +"\nTotal Pin " +
                                data_obj.getString("total_pin");
                        String str_type = data_obj.getString("status");

                        lstItems.add(new WalletHistoryModel(str_amount, str_datetime, str_description, str_type));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

//            PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBAL, strWalletBalance);
//            txtCurrentBal.setVisibility(View.VISIBLE);
            txtCurrentBal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBAL, "0"));

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            wallethistoryrv.setLayoutManager(layoutManager);
            wallethistoryrv.setHasFixedSize(true);
            int animation_type = ItemAnimation.LEFT_RIGHT;
            PinHisAdapter mAdapter = new PinHisAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new PinHisAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String obj, int position) {

                }
            });
        } else if (url.contains(ApiInterface.GETPACKAGELIST)) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < (data).length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        str_package_amount = data_obj.getString("package_amount");
                        strPackageId = data_obj.getString("package_id");
                        String str_package_name = data_obj.getString("package_name");

                        ((TextView) findViewById(R.id.name)).setText(str_package_name);
                        ((TextView) findViewById(R.id.price)).setText(str_package_amount+"/-");
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private EditText getEditext(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return edTexts[i];
            }
        }
        return new EditText(svContext);
    }

    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
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