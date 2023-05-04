package com.codunite.sonikapay;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivityZoomMeeting extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};
    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};

    private String str_meeting_link = "";
    private TextView tvMeetingTime, tvMeetingID, tvMeetingPass, tvMeetingLink;
    private LinearLayout layMeetingLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zoom_meeting);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    private void resumeApp() {
        Button copyandshare = (Button) findViewById(R.id.btn_copy_share);
        tvMeetingTime = ((TextView) findViewById(R.id.meeting_time));
        tvMeetingID = ((TextView) findViewById(R.id.meeting_id));
        tvMeetingPass = ((TextView) findViewById(R.id.meeting_password));
        tvMeetingLink = ((TextView) findViewById(R.id.meeting_link));
        layMeetingLink = findViewById(R.id.lay_meeting_link);

        tvMeetingID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", tvMeetingID.getText().toString());
                clipboard.setPrimaryClip(clip);
                customToast.showCustomToast(svContext, "Zoom Meeting Id Copied", customToast.ToastyNormal);
            }
        });
        tvMeetingPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", tvMeetingPass.getText().toString());
                clipboard.setPrimaryClip(clip);
                customToast.showCustomToast(svContext, "Zoom Meeting Password Copied", customToast.ToastyNormal);
            }
        });
        layMeetingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(str_meeting_link);
            }
        });
        copyandshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Meeting Start Time :- " + tvMeetingTime.getText().toString()
                        + "\nMeeting Link :- " + str_meeting_link
                        + "\nMeeting ID :- " + tvMeetingID.getText().toString()
                        + "\nMeeting Password :- " + tvMeetingPass.getText().toString();
                copyAndShare(shareBody);
            }
        });

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GETZOOMDETAILS, lstUploadData);

    }

    private void copyAndShare(String shareBody) {
        ClipboardManager clipboard = (ClipboardManager) svContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("EditText", str_meeting_link);
        clipboard.setPrimaryClip(clip);
        customToast.showCustomToast(svContext, "Zoom Meeting Link Copied", customToast.ToastyNormal);

        Intent intent = new Intent(Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Link");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share Via"));

        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shareBody)));
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityZoomMeeting.this);
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

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Zoom Meeting Details");
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
        if (url.contains(ApiInterface.GETZOOMDETAILS)) {
            try {
                JSONArray json = new JSONArray(result);
                JSONObject detail_obj = json.getJSONObject(0);

                str_meeting_link = detail_obj.getString("meeting_link");
                tvMeetingTime.setText(detail_obj.getString("meeting_time"));
                tvMeetingID.setText(detail_obj.getString("zoom_id"));
                tvMeetingPass.setText(detail_obj.getString("password"));
                tvMeetingLink.setText(str_meeting_link);
                tvMeetingLink.setSelected(true);

                PreferenceConnector.writeString(svContext, PreferenceConnector.ZOOM_MEETING_TIME, detail_obj.getString("meeting_time"));
            } catch (JSONException e) {
                e.printStackTrace();
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

    public void openWebPage(String urlString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

}