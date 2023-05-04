package com.codunite.sonikapay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.adapter.CompanyDocAdapter;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.FileUtil;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;
import com.retrofit.ApiInterface;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;
import com.codunite.model.CompanyDocModel;
import com.codunite.sonikapay.spinner.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityCompanyDoc extends AppCompatActivity implements View.OnClickListener,
        WebServiceListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};

    private EditText[] edTexts = {};
    private String[] edTextsError = {"Enter phone number"};
    private int[] editTextsClickId = {};

    private RecyclerView rvUpgrade;
    private SpinnerModel spinnerModel;

    private Request request;
    private Fetch fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gst_document);
        if (getIntent().hasExtra("spinnerModel")) {
            spinnerModel = (SpinnerModel) getIntent().getSerializableExtra("spinnerModel");
        }
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    public void resumeApp() {
        rvUpgrade = (RecyclerView) findViewById(R.id.rv_upgrade);
        fetch = Fetch.Impl.getInstance(GlobalData.getFetchConfig(svContext));

        if (spinnerModel != null) {
            LoadGetDocument(spinnerModel.getId());
        }
    }

    private void LoadGetDocument(String docCategoryId) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(docCategoryId);
        callWebService(ApiInterface.GET_DOCUMENT, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityCompanyDoc.this);
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
        if (spinnerModel != null) {
            txtHeading.setText(spinnerModel.getTitle());
        } else {
            txtHeading.setText("Document Details");
        }
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

    List<CompanyDocModel> lstItems = new ArrayList<>();

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GET_DOCUMENT)) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_id = data_obj.getString("id");
                        String str_image = data_obj.getString("image");
                        String str_title = "";
                        if (data_obj.has("title")) {
                            str_title = data_obj.getString("title");
                        }
                        lstItems.add(new CompanyDocModel(str_id, str_title, str_image));
                    }
                    loadRecyClerView();
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
        CompanyDocAdapter mAdapter = new CompanyDocAdapter(this, lstItems, animation_type);
        rvUpgrade.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CompanyDocAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CompanyDocModel obj, int position) {
            }

            @Override
            public void onViewDocumentClick(View view, CompanyDocModel obj, int position) {
                String extension = FileUtil.getFileExtension(obj.getStrUrl());
                String fileName = "CompanyDocument_" + position + "." + extension;
                GlobalData.downloadFileSystem(svContext, obj.getStrUrl(), fileName, true,
                        (path, uri) -> {
                            try {
                                Intent intent = FileUtil.getIntentForFile(svContext, new File(path));
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        });

        if (lstItems.isEmpty()) {
            customToast.showCustomToast(svContext, "No data found!", customToast.ToastyError);
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

//    FetchListener fetchListener = new FetchListener() {
//        @Override
//        public void onAdded(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
//            //if (request.getId() == download.getId()) {
//            //    showDownloadInList(download);
//            //}
//        }
//
//        @Override
//        public void onWaitingNetwork(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onCompleted(@NotNull Download download) {
//            //fileUri = FileProvider.getUriForFile(context, pkgName + ".provider", file);
//            if (download.getFileUri() != null) {
//                final Uri fileUri = download.getFileUri();
//                String mimeType = getContentResolver().getType(fileUri);
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(fileUri, mimeType);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
//            }
//            fetch.removeListener(fetchListener);
//        }
//
//        @Override
//        public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
//            fetch.removeListener(fetchListener);
//        }
//
//        @Override
//        public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {
//
//        }
//
//        @Override
//        public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
//
//        }
//
//        @Override
//        public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
//            //if (request.getId() == download.getId()) {
//            //    updateDownload(download, etaInMilliSeconds);
//            //}
//            //int progress = download.getProgress();
//        }
//
//        @Override
//        public void onPaused(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onResumed(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onCancelled(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onRemoved(@NotNull Download download) {
//
//        }
//
//        @Override
//        public void onDeleted(@NotNull Download download) {
//            fetch.removeListener(fetchListener);
//        }
//    };

}