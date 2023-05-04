package com.codunite.sonikapay.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.adapter.BbpsDashboardAdapter;
import com.codunite.model.DashboardModel;
import com.codunite.sonikapay.ActivityWalletHistory;
import com.codunite.sonikapay.R;
import com.codunite.sonikapay.bbps.ActivityBbpsAllServices;
import com.codunite.sonikapay.bbps.ActivityBbpsElectricity;
import com.codunite.sonikapay.bbps.ActivityBbpsTollTax;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
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

public class FragBBPSDashBoard extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private List<DashboardModel> lstDashBoard = new ArrayList<>();

    private String BBPSServices = "{\"status\":1,\"message\":\"Success\",\"data\":[" +
            "{\"service_id\":\"14\",\"title\":\"BROADBAND POSTPAID\"}," +
            "{\"service_id\":\"12\",\"title\":\"CABLE\"},{\"service_id\":\"24\",\"title\":\"CLUBS AND ASSOCIATIONS\"},{\"service_id\":\"22\",\"title\":\"CREDIT CARD\"}," +
            "{\"service_id\":\"7\",\"title\":\"EDUCATION\"},{\"service_id\":\"4\",\"title\":\"ELECTRICITY\"},{\"service_id\":\"9\",\"title\":\"ENTERTAINMENT\"}" +
            ",{\"service_id\":\"6\",\"title\":\"FASTAG\"},{\"service_id\":\"3\",\"title\":\"GAS\"},{\"service_id\":\"19\",\"title\":\"HOSPITAL\"},{\"service_id\":\"17\"," +
            "\"title\":\"HOUSING SOCIETY\"},{\"service_id\":\"2\",\"title\":\"INSURANCE\"},{\"service_id\":\"10\",\"title\":\"LANDLINE POSTPAID\"},{\"service_id\":\"1\",\"title\":\"LOAN\"}" +
            ",{\"service_id\":\"15\",\"title\":\"LPG GAS\"},{\"service_id\":\"11\",\"title\":\"MOBILE POSTPAID\"}," +
//            "{\"service_id\":\"5\",\"title\":\"MOBILE PREPAID\"}," +
            "{\"service_id\":\"18\",\"title\":\"MUNICIPAL SERVICES\"}" +
            ",{\"service_id\":\"16\",\"title\":\"MUNICIPAL TAXES\"},{\"service_id\":\"20\",\"title\":\"SUBSCRIPTION\"},{\"service_id\":\"23\",\"title\":\"TRANSIT CARD\"},{\"service_id\":\"21\",\"title\":\"TRAVEL-SUB\"}," +
            "{\"service_id\":\"8\",\"title\":\"WATER\"}," +
            "{\"service_id\":\"25\",\"title\":\"OTT\"}" +
            "]}";
    private int[] allDrawable = {R.drawable.bbps_landline, R.drawable.bbps_cable_tv, R.drawable.bbps_club_association,
            R.drawable.bbps_cradit_card, R.drawable.bbps_education,
            R.drawable.bbps_electricity, R.drawable.bbps_entertainment, R.drawable.bbps_fastag,
            R.drawable.bbps_lpg_gas,
            R.drawable.bbps_hospital, R.drawable.bbps_housing, R.drawable.bbps_insurance, R.drawable.bbps_landline,
            R.drawable.bbps_loan, R.drawable.bbps_gas, R.drawable.bbps_mobile_recharge,
            R.drawable.bbps_muncipal_tax, R.drawable.bbps_muncipal_tax, R.drawable.bbps_subscription,
            R.drawable.bbps_transit_card,
            R.drawable.bbps_travels, R.drawable.bbps_water, R.drawable.bbps_subscription};

    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};

    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};

    public FragBBPSDashBoard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_bbpslivehome, container, false);
        }
        return aiView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            aiView = getView();

            OnClickCombineDeclare(allViewWithClick);
            EditTextDeclare(edTexts);
            resumeApp();
        }
    }

    private void GetBbpsServices() {
        lstServices = new ArrayList<>();
        JSONObject json = null;
        try {
            json = new JSONObject(BBPSServices);

            JSONArray data = json.getJSONArray("data");
            for (int data_i = 0; data_i < data.length(); data_i++) {
                JSONObject data_obj = data.getJSONObject(data_i);
                String str_code = data_obj.getString("service_id");
                String str_name = data_obj.getString("title");

                lstServices.add(str_code + "#:#" + str_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int animation_type = ItemAnimation.NONE;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                break;
            default:
                break;
        }
    }

    public static String strServiceId = "";
    public void switchContent(Fragment fragment) {
        hideFragmentkeyboard(svContext, aiView);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private List<String> lstServices;
    public void resumeApp() {
        lstServices = new ArrayList<>();
        GetBbpsServices();

        lstDashBoard = new ArrayList<>();
        for (int j = 0; j < lstServices.size(); j++) {
            lstDashBoard.add(new DashboardModel((lstServices.get(j)).split("#:#")[1],
                    allDrawable[j], false));
        }
//        lstDashBoard.add(BBSPPOSITION, new DashboardModel(selectedItem[j], allDrawable[j], false));

        RecyclerView recyclerView = (RecyclerView) aiView.findViewById(R.id.rv_dashboard);

        root.setVisibility(View.VISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(svContext, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        BbpsDashboardAdapter mAdapter = new BbpsDashboardAdapter(svContext, lstDashBoard, animation_type);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, obj, position) -> {
            if ((lstDashBoard.get(position).getName()).equalsIgnoreCase("Electricity")) {
                Intent svIntent = new Intent(svContext, ActivityBbpsElectricity.class);
                startActivity(svIntent);
            } else if ((lstDashBoard.get(position).getName()).equalsIgnoreCase("Fastag")) {
                Intent svIntent = new Intent(svContext, ActivityBbpsTollTax.class);
                startActivity(svIntent);
            } else {
                strServiceId = (lstServices.get(position)).split("#:#")[0];
                Intent svIntent = new Intent(svContext, ActivityBbpsAllServices.class);
                svIntent.putExtra("servicetype", (lstServices.get(position)).split("#:#")[1]);
                startActivity(svIntent);
            }
        });
    }

    public void switchContent(Fragment fragment, String tag) {
        hideFragmentkeyboard(svContext, aiView);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = aiView.findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent svIntent;
                    switch (v.getId()) {
                        case R.id.view_passbook:
                            svIntent = new Intent(svContext, ActivityWalletHistory.class);
                            startActivity(svIntent);
                            break;
                        case R.id.btn_paytomerchant:
                            customToast.showCustomToast(svContext, "Comimg soon", customToast.ToastyInfo);
                            break;
                    }
                }
            });
        }
    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private ViewGroup root;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
//            FontUtils.setThemeColor(root, svContext, true);
        } else {
//            FontUtils.setThemeColor(root, svContext, false);
        }

        root.setVisibility(View.INVISIBLE);

        GlobalData.SetLanguage(svContext);
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
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }
}