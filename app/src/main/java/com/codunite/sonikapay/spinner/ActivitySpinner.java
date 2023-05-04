package com.codunite.sonikapay.spinner;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.MyDividerItemDecoration;
import com.codunite.commonutility.NoInternetScreen;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.customfont.FontUtils;

import java.util.List;

import me.grantland.widget.AutofitTextView;

public class ActivitySpinner extends AppCompatActivity implements View.OnClickListener, PopulateSpinnerAdapter.OnItemClickListener {
    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};

    private EditText[] edTexts = {};
    private String[] edTextsError = {""};
    private int[] editTextsClickId = {};

    private ProgressBar progressbarLoad;
    private RecyclerView recyclerView;
    private PopulateSpinnerAdapter mAdapter;
    private SearchView searchView;

    boolean isImageShow = true;
    private String title = "";
    private static List<SpinnerModel> itemList;

    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_populatespinner);
        if (getIntent() != null && getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        }

        StartApp();
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);

        resumeApp();
    }

    private String firstLetterInUpperCase(String str) {
        try {
            String firstLetStr = str.substring(0, 1);
            String remLetStr = str.substring(1).toLowerCase();
            firstLetStr = firstLetStr.toUpperCase();
            String firstLetterCapitalizedName = firstLetStr + remLetStr;
            return firstLetterCapitalizedName;
        } catch (Exception ignored) {
            return str;
        }
    }

    public void resumeApp() {
        AutofitTextView txtHeading = (AutofitTextView) findViewById(R.id.heading);
        txtHeading.setText(title);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        mAdapter = new PopulateSpinnerAdapter(svContext, itemList, this, isImageShow, false);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 30));
        recyclerView.setAdapter(mAdapter);
        progressbarLoad.setVisibility(View.GONE);

//        if (itemList == null || itemList.size() == 0) {
//            customToast.showCustomToast(svContext, "No item to show", customToast.ToastyInfo);
//            finish();
//        }
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivitySpinner.this);
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

        progressbarLoad = (ProgressBar) findViewById(R.id.progressbar_load);
        progressbarLoad.setVisibility(View.VISIBLE);
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText(getString(R.string.app_name));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(View view, SpinnerModel selectedSpinnerModel, int position) {
        itemList = null;
        setResultIntent(selectedSpinnerModel, position);
    }

    public static String defaultSpinnerTitle = "Select Item";
    public static String EXTRA_SPINNER_DATA = "spinner_data";
    public static String EXTRA_SPINNER_POSITION = "spinner_position";

    public void setResultIntent(SpinnerModel spinnerData, int position) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SPINNER_DATA, spinnerData);
        intent.putExtra(EXTRA_SPINNER_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Open Custom Spinner Activity directly from here
     */
    public static void showSpinner(Context context, List<SpinnerModel> listSpinner, int request_code) {
        showSpinner(context, listSpinner, defaultSpinnerTitle, request_code);
    }

    public static void showSpinner(Context context, List<SpinnerModel> listSpinner, String spinnerTitle, int request_code) {
        itemList = listSpinner;
        Intent intent = new Intent(context, ActivitySpinner.class);
        intent.putExtra("title", spinnerTitle);
        ((Activity) context).startActivityForResult(intent, request_code);
    }

}