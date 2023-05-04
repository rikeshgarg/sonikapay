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
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.ActivityRequestWallet;
import com.codunite.sonikapay.ActivityWalletHistory;
import com.codunite.sonikapay.R;
import com.codunite.model.DashboardModel;
import com.codunite.commonutility.CheckInternet;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;
import com.codunite.commonutility.WebService;
import com.codunite.commonutility.WebServiceListener;
import com.codunite.commonutility.customfont.FontUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FragPersonProfile extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private List<DashboardModel> lstDashBoard = new ArrayList<>();
    public static String[] selectedItem = {"My QR Code","Personal Profile", "Change Password", "Change T-Pin"};
    private int[] allDrawable = {R.drawable.per_qr,R.drawable.per_profile, R.drawable.per_change_pass, R.drawable.per_change_pin};

    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};

    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};
    private LinearLayout cardRecharge;

    public FragPersonProfile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_personprofile, container, false);
        }
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            aiView = getView();
        }

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        resumeApp();
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

    public void switchContent(Fragment fragment) {
        hideFragmentkeyboard(svContext, aiView);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    public void resumeApp() {
        cardRecharge = (LinearLayout) aiView.findViewById(R.id.card_recharge);
        cardRecharge.setVisibility(View.VISIBLE);

        for (int j = 0; j < selectedItem.length; j++) {
            if ((selectedItem[j]).equals("M Transfer")) {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.IS_MONEY_TRANSFER_ACTIVE, false)) {
                    lstDashBoard.add(new DashboardModel(selectedItem[j], allDrawable[j], false));
                }
            } else {
                lstDashBoard.add(new DashboardModel(selectedItem[j], allDrawable[j], false));
            }

        }
        RecyclerView recyclerView = (RecyclerView) aiView.findViewById(R.id.rv_dashboard);

        root.setVisibility(View.VISIBLE);
        GridLayoutManager layoutManager = new GridLayoutManager(svContext, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
                        case R.id.request_money:
                            svIntent = new Intent(svContext, ActivityRequestWallet.class);
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