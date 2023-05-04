package com.codunite.sonikapay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retrofit.ApiInterface;
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

public class ActivityLevelList extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_TYPE = "type";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    private RecyclerView wallethistoryrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rechargecommision);
        StartApp();

        resumeApp();
    }

    public void resumeApp() {
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);

        CardView cvAddWallet = (CardView) findViewById(R.id.card_addwallet);
        CardView cardShowBalance = (CardView) findViewById(R.id.card_wallbal);
        cvAddWallet.setVisibility(View.GONE);
        cardShowBalance.setVisibility(View.GONE);

        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        callWebService(ApiInterface.GET_LEVEL_LIST, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityLevelList.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
            // FontUtils.setThemeColor(root, svContext, true);
        } else {
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
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Level List");

        TextView toolbar_txt_walletbal = (TextView) findViewById(R.id.toolbar_txt_walletbal);
        toolbar_txt_walletbal.setText(ActivityMain.ShowBalance(svContext));

        TextView toolbar_txt_ewalletbal = (TextView) findViewById(R.id.toolbar_txt_ewalletbal);
        toolbar_txt_ewalletbal.setText(ActivityMain.ShoweBalance(svContext));

        LinearLayout imgToolBarWallet = (LinearLayout) findViewById(R.id.img_wallet);
        imgToolBarWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpenWalletActivity(svContext);
            }
        });

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgToolBareWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySplash.OpeneWalletActivity(svContext);
            }
        });
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

    private List<LevelListModel> lstItems = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GET_LEVEL_LIST)) {
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
                        String str_level_id = data_obj.getString("level_id");
                        String str_title = data_obj.getString("title");
                        String str_total_user = data_obj.getString("total_user");
                        lstItems.add(new LevelListModel(str_level_id, str_title, str_total_user));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            wallethistoryrv.setLayoutManager(layoutManager);
            wallethistoryrv.setHasFixedSize(true);
            int animation_type = ItemAnimation.LEFT_RIGHT;
            LevelListAdapter mAdapter = new LevelListAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new LevelListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, LevelListModel obj, int position) {
                    Intent intent = new Intent(svContext, ActivityLevelWiseTeamList.class);
                    intent.putExtra("rank_id", obj.getLevelId());
                    intent.putExtra("rank_title", obj.getTitle());
                    svContext.startActivity(intent);
                }
            });
        }
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

    public class LevelListModel {

        private String title, totalUsers, levelId;

        public LevelListModel(String rankId, String title, String totalUsers) {
            this.levelId = rankId;
            this.title = title;
            this.totalUsers = totalUsers;
        }

        public String getLevelId() {
            return levelId;
        }

        public void setLevelId(String levelId) {
            this.levelId = levelId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(String totalUsers) {
            this.totalUsers = totalUsers;
        }

    }

    public static class LevelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<LevelListModel> items;
        private Context context;
        private OnItemClickListener mOnItemClickListener;
        private int animation_type = 0;
        private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38",
                "#03a9f4", "#00bcd4", "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};

        public interface OnItemClickListener {
            void onItemClick(View view, LevelListModel obj, int position);
        }

        public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mOnItemClickListener = mItemClickListener;
        }

        public LevelListAdapter(Context context, List<LevelListModel> items, int animation_type) {
            this.items = items;
            this.context = context;
            this.animation_type = animation_type;
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {
            public TextView txtRank, txtTotalAchiever;
            public View lyt_parent;
            public Button btnAchieverList;

            public OriginalViewHolder(View v) {
                super(v);
                txtRank = (TextView) v.findViewById(R.id.txt_rank);
                txtTotalAchiever = (TextView) v.findViewById(R.id.txt_total_achiever);

                btnAchieverList = v.findViewById(R.id.btn_achieverlist);
                lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levellist, parent, false);
            vh = new OriginalViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof OriginalViewHolder) {
                OriginalViewHolder view = (OriginalViewHolder) holder;

                LevelListModel model = items.get(position);
                view.txtRank.setText(model.getTitle());
                view.txtTotalAchiever.setText(model.getTotalUsers());

                view.btnAchieverList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, items.get(position), position);
                        }
                    }
                });
                setAnimation(view.itemView, position);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private int lastPosition = -1;
        private boolean on_attach = true;

        private void setAnimation(View view, int position) {
            if (position > lastPosition) {
                ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
                lastPosition = position;
            }
        }

    }

}