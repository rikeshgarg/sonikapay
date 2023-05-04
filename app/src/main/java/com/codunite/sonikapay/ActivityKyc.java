package com.codunite.sonikapay;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.retrofit.ApiInterface;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.CheckValidation;
import com.codunite.commonutility.GetFormattedDateTime;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ImageLoading;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class ActivityKyc extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Button removeAadharcardFront, btnRemoveAadharcardBack, btnPanCard;
    private Button btnChooseAadhrFront, btnChooseAadharBack, btnChoosePanCard;
    private View[] allViewWithClick = {btnChooseAadhrFront, btnChooseAadharBack,
            btnChoosePanCard, removeAadharcardFront, btnRemoveAadharcardBack, btnPanCard};
    private int[] allViewWithClickId = {R.id.choose_aadharfront,
            R.id.choose_aadharback, R.id.choose_pancard, R.id.remove_aadharfront,
            R.id.remove_aadharback, R.id.remove_pancard};

    private TextView edDob;
    private EditText edAccounteeName, edAccountNumbe, edMobileNo, edIfscCode, edBankName, edAadhar;
    private EditText[] edTexts = {edAccounteeName, edAccountNumbe, edMobileNo, edIfscCode, edBankName, edAadhar};
    private String[] edTextsError = {"Enter account holder name", "Enter account number", "Enter mobile number",
            "Enter ifsc code", "Enter bank name", "Enter aadhar"};
    private int[] editTextsClickId = {R.id.ed_accountholder_name, R.id.ed_account_number, R.id.ed_mob_number,
            R.id.ed_bank_ifsc, R.id.ed_bank_name, R.id.ed_adhar_no};

    private View parentView;
    private NestedScrollView nested_scroll_view;
    private Button bt_save_input;

    private ImageView imgFrontAadharcard, imgBackAadharCard, imgPanCard;

    private int selectedImageView = 0;
    private boolean LoadFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_kyc);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    private void resumeApp() {
        parentView = findViewById(android.R.id.content);

        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        imgFrontAadharcard = (ImageView) findViewById(R.id.img_frontaadharcard);
        imgBackAadharCard = (ImageView) findViewById(R.id.img_backaadharcard);
        imgPanCard = (ImageView) findViewById(R.id.img_pancard);

        edDob = findViewById(R.id.ed_dob);
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            int month = monthOfYear + 1;
            String selectedDate = year + "-" + (month >= 10 ? month : "0" + month)
                    + "-" + (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth);
            edDob.setText(selectedDate);
        };

        edDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtDate = edDob.getText().toString().trim();
                try {
                    String[] strDate = txtDate.split("-");
                    new DatePickerDialog(svContext, onDateSetListener,
                            Integer.parseInt(strDate[0]),
                            Integer.parseInt(strDate[1]) - 1,
                            Integer.parseInt(strDate[2])).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    txtDate = GetFormattedDateTime.getcurrentcalDate();
                    String[] strDate = txtDate.split("-");
                    new DatePickerDialog(svContext, onDateSetListener,
                            Integer.parseInt(strDate[0]),
                            Integer.parseInt(strDate[1]) - 1,
                            Integer.parseInt(strDate[2])).show();
                }
            }
        });

        bt_save_input = (Button) findViewById(R.id.bt_save_input);
        bt_save_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessingKyc();
            }
        });

        if (LoadFirstTime) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            callWebService(ApiInterface.USERKYCDETAIL, lstUploadData);
        }

        OpenDemoLink();
    }

    private void ProcessingKyc() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (response == 0 && (edMobileNo.getText().toString().trim()).length() < 10) {
            response++;
            edMobileNo.setError("Invalid mobile no");
        }

        if (response == 0 && TextUtils.isEmpty(edDob.getText())) {
            response++;
            edDob.setError("Select Dob");
        }

//        if (imgFrontAadharcard == null) {
//            response++;
//            customToast.showCustomToast(svContext, "Please select front aadhar card", customToast.ToastyWarning);
//        }
        if (response == 0 && imageFrontAadharCardUri == null) {
            response++;
            customToast.showCustomToast(svContext, "Please select front aadhar card", customToast.ToastyWarning);
        }

        if (response == 0 && imageBackAadharCardUri == null) {
            response++;
            customToast.showCustomToast(svContext, "Please select back aadhar card", customToast.ToastyWarning);
        }

//        if (imgBackAadharCard == null) {
//            response++;
//            customToast.showCustomToast(svContext, "Please select back aadhar card", customToast.ToastyWarning);
//        }

        if (response == 0 && imagePanCardUri == null) {
            response++;
            customToast.showCustomToast(svContext, "Please select pan card", customToast.ToastyWarning);
        }

        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
            lstUploadData.add(edAccounteeName.getText().toString().trim());
            lstUploadData.add(edAccountNumbe.getText().toString().trim());
            lstUploadData.add(edMobileNo.getText().toString().trim());
            lstUploadData.add(edIfscCode.getText().toString().trim());
            lstUploadData.add(edBankName.getText().toString().trim());
            lstUploadData.add(edAadhar.getText().toString().trim());
            lstUploadData.add(edDob.getText().toString().trim());
            lstUploadData.add(encodeImage(imageFrontAadharCardUri));
            lstUploadData.add(encodeImage(imageBackAadharCardUri));
            lstUploadData.add(encodeImage(imagePanCardUri));
            callWebService(ApiInterface.USERKYC, lstUploadData);
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

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edAccounteeName = (EditText) editTexts[0];
        edAccountNumbe = (EditText) editTexts[1];
        edMobileNo = (EditText) editTexts[2];
        edIfscCode = (EditText) editTexts[3];
        edBankName = (EditText) editTexts[4];
        edAadhar = (EditText) editTexts[5];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent svIntent;
                    switch (v.getId()) {
                        case R.id.choose_aadharfront:
                            selectedImageView = 0;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.choose_aadharback:
                            selectedImageView = 1;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.choose_pancard:
                            selectedImageView = 2;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case R.id.remove_aadharfront:
                            imgFrontAadharcard.setImageURI(null);
                            imgFrontAadharcard.setVisibility(View.GONE);
                            break;
                        case R.id.remove_aadharback:
                            imgBackAadharCard.setImageURI(null);
                            imgBackAadharCard.setVisibility(View.GONE);
                            break;
                        case R.id.remove_pancard:
                            imgPanCard.setImageURI(null);
                            imgPanCard.setVisibility(View.GONE);
                            //decodeImage(encodeImage(imageFrontAadharCardUri));
                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    private void decodeImage(String encImage) {
        byte[] b = Base64.decode(encImage, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        Bitmap bm = BitmapFactory.decodeStream(bais);
        imgPanCard.setImageBitmap(bm);
    }

    private Uri imageFrontAadharCardUri = null, imageBackAadharCardUri = null, imagePanCardUri = null;

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            //Toast.makeText(svContext, ActivityBrowseProfileImage.imageUri.toString() + "\n"+ selectedImageView, Toast.LENGTH_SHORT).show();
            if (selectedImageView == 0) {
                imageFrontAadharCardUri = ActivityBrowseProfileImage.imageUri;
                imgFrontAadharcard.setImageURI(null);
                imgFrontAadharcard.setImageURI(imageFrontAadharCardUri);
                imgFrontAadharcard.setVisibility(View.VISIBLE);
            } else if (selectedImageView == 1) {
                imageBackAadharCardUri = ActivityBrowseProfileImage.imageUri;
                imgBackAadharCard.setImageURI(null);
                imgBackAadharCard.setImageURI(imageBackAadharCardUri);
                imgBackAadharCard.setVisibility(View.VISIBLE);
            } else if (selectedImageView == 2) {
                imagePanCardUri = ActivityBrowseProfileImage.imageUri;
                imgPanCard.setImageURI(null);
                imgPanCard.setImageURI(imagePanCardUri);
                imgPanCard.setVisibility(View.VISIBLE);
            }
            ActivityBrowseProfileImage.imageUri = null;
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityKyc.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
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

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("KYC");
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

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.USERKYC)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_status = json.getString("status");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);

                    onBackPressed();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.USERKYCDETAIL)) {
            LoadFirstTime = false;
            String strStatus = "Not Applied";
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_status = json.getString("status");

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);

                } else {
                    if (json.has("data") && !json.get("data").toString().equals("[]")) {
                        JSONObject data = json.getJSONObject("data");

                        String strAcoountName = "";
                        String strAccNo = "";
                        String strIFSC = "";
                        String strBankNAme = "";
                        if (data.has("ac_holder_name")) {
                            strAcoountName = data.getString("ac_holder_name");
                            ((TextView) findViewById(R.id.ed_accountholder_name)).setText(strAcoountName);

                        }
                        if (data.has("ac_no")) {
                            strAccNo = data.getString("ac_no");
                            ((TextView) findViewById(R.id.ed_account_number)).setText(strAccNo);
                        }

                        if (data.has("mobile_no")) {
                            String mobile = data.getString("mobile_no");
                            ((TextView) findViewById(R.id.ed_mob_number)).setText(mobile);
                        }

                        if (data.has("ifsc")) {
                            strIFSC = data.getString("ifsc");
                            ((TextView) findViewById(R.id.ed_bank_ifsc)).setText(strIFSC);

                        }
                        if (data.has("bank_name")) {
                            strBankNAme = data.getString("bank_name");
                            ((TextView) findViewById(R.id.ed_bank_name)).setText(strBankNAme);
                        }
                        if (data.has("aadhar_no")) {
                            strBankNAme = data.getString("aadhar_no");
                            ((TextView) findViewById(R.id.ed_adhar_no)).setText(strBankNAme);
                        }
                        if (data.has("dob")) {
                            strBankNAme = data.getString("dob");
                            ((TextView) findViewById(R.id.ed_dob)).setText(strBankNAme);
                        }

                        if (data.has("aadhar_front")) {
                            String strAadharFront = data.getString("aadhar_front");
                            ImageLoading.loadImages(strAadharFront,
                                    imgFrontAadharcard, 0);
                        }

                        if (data.has("aadhar_back")) {
                            String strAadharFront = data.getString("aadhar_back");
                            ImageLoading.loadImages(strAadharFront, imgBackAadharCard, R.drawable.logo);
                        }

                        if (data.has("pan_card")) {
                            String strAadharFront = data.getString("pan_card");
                            ImageLoading.loadImages(strAadharFront,
                                    imgPanCard, R.drawable.logo);
                        }

                        imgFrontAadharcard.setVisibility(View.VISIBLE);
                        imgBackAadharCard.setVisibility(View.VISIBLE);
                        imgPanCard.setVisibility(View.VISIBLE);

                        strStatus = data.getString("status");
                    }

                    TextView txStatus = ((TextView) findViewById(R.id.txt_kyc_status));
                    if (strStatus.equalsIgnoreCase("Pending")) {
                        txStatus.setText("Status of your KYC is: " + strStatus);
                        txStatus.setTextColor(getResources().getColor(R.color.orange_900));
                    } else if (strStatus.equalsIgnoreCase("Approved")) {
                        txStatus.setText("Status of your KYC is: " + strStatus);
                        txStatus.setTextColor(getResources().getColor(R.color.green_900));
                    } else {
                        txStatus.setText("Status of your KYC is: " + strStatus);
                        txStatus.setTextColor(getResources().getColor(R.color.red_900));
                    }

                    DiableAllViews(nested_scroll_view);
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

    private void DiableAllViews(ViewGroup parentView){
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View child = parentView.getChildAt(i);
            if (child instanceof EditText) {
                child.setEnabled(false);
                child.setFocusable(false);
                child.setFocusableInTouchMode(false);
            }else if (child instanceof ImageView) {
                child.setClickable(false);
            }else if (child instanceof TextView) {
                child.setClickable(false);
            }else if (child instanceof Button) {
                child.setEnabled(false);
            }else if (child instanceof RadioButton) {
                child.setEnabled(false);
            }else if (child instanceof ViewGroup) {
                DiableAllViews((ViewGroup) child);
            }
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

    private String strDemoServiceName = "", dtrDemoServiceUrl = "";
    private void OpenDemoLink() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add("18");
        callWebService(ApiInterface.GETDEMOLINK, lstUploadData);

        ((View) findViewById(R.id.lay_demo_url)).setOnClickListener(v -> {
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, strDemoServiceName);
            PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, dtrDemoServiceUrl);
            Intent svIntent = new Intent(svContext, WebViewActivity.class);
            svContext.startActivity(svIntent);
        });
    }
}