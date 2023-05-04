package com.codunite.sonikapay;

import android.content.Context;
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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.adapter.PaginationAdapter;
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
import com.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityRankAchieverList extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public static final String TAG_DATA = "data";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_TYPE = "type";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";

    private String strRankId = "";
    private RecyclerView wallethistoryrv, rvPagination;
    private boolean isFirstLoad = true;
    private NestedScrollView layNestedScroll;
    private int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rechargecommision);
        StartApp();

        resumeApp();
    }

    public void resumeApp() {
        layNestedScroll = (NestedScrollView) findViewById(R.id.lay_nestedscroll);
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);
        rvPagination = (RecyclerView) findViewById(R.id.rv_pagination);
        rvPagination.setVisibility(View.VISIBLE);


        CardView cvAddWallet = (CardView) findViewById(R.id.card_addwallet);
        CardView cardShowBalance = (CardView) findViewById(R.id.card_wallbal);
        cvAddWallet.setVisibility(View.GONE);
        cardShowBalance.setVisibility(View.GONE);

        isFirstLoad = true;
        loadListData();
    }

    private void loadListData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINEDUSERID, ""));
        lstUploadData.add(strRankId);
        lstUploadData.add("" + pageNumber);
        callWebService(ApiInterface.GET_RANKWISE_TEAMLIST, lstUploadData);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActivityRankAchieverList.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISDARKTHEME, false)) {
            // FontUtils.setThemeColor(root, svContext, true);
        } else {
            // FontUtils.setThemeColor(root, svContext, false);
        }

        if (getIntent().hasExtra("rank_id")) {
            strRankId = getIntent().getStringExtra("rank_id");
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
        if (getIntent().hasExtra("rank_title")) {
            String strRankTitle = getIntent().getStringExtra("rank_title");
            txtHeading.setText(strRankTitle + " Rank Achiever List");
        } else {
            txtHeading.setText("Achiever Rank List");
        }

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

    private List<RankAchieverListModel> lstItems = new ArrayList<>();

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        if (url.contains(ApiInterface.GET_RANKWISE_TEAMLIST)) {
            int strPageCount = 0;
            try {
                lstItems = new ArrayList<>();

                JSONObject json = new JSONObject(result);
                String str_message = json.getString(TAG_MESSAGE);
                String str_status = json.getString(TAG_STATUS);

                try {
                    strPageCount = Integer.parseInt(json.getString("pages"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < ((JSONArray) data).length(); data_i++) {
                        //{"user_code":"SNPM591063","name":"CHANDA RANI","old_rank":"Bronze","new_rank":"Silver","datetime":"18-Oct-2021"}
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_user_code = data_obj.getString("user_code");
                        String str_name = data_obj.getString("name");
                        String str_old_rank = data_obj.getString("old_rank");
                        String str_new_rank = data_obj.getString("new_rank");
                        String str_date = data_obj.getString("datetime");
                        lstItems.add(new RankAchieverListModel(str_user_code, str_name, str_old_rank, str_new_rank, str_date));
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
            RankAchieverListAdapter mAdapter = new RankAchieverListAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RankAchieverListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RankAchieverListModel obj, int position) {

                }
            });
            layNestedScroll.scrollTo(0, 0);
            if (isFirstLoad) {
                isFirstLoad = false;
                LoadPaginationView(strPageCount);
            }
        }
    }

    void LoadPaginationView(int paginationSize) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPagination.setLayoutManager(layoutManager);
        rvPagination.setHasFixedSize(true);
        int animation_type = ItemAnimation.LEFT_RIGHT;
        PaginationAdapter mAdapter = new PaginationAdapter(this, paginationSize, animation_type);
        rvPagination.setNestedScrollingEnabled(false);
        rvPagination.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PaginationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                pageNumber = Integer.parseInt(obj);
                loadListData();
            }
        });
        if (paginationSize <= 1) {
            rvPagination.setVisibility(View.GONE);
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

    public static class RankAchieverListModel {

        private String memberID, name, oldRank, newRank, date;

        public RankAchieverListModel(String memberID, String name, String oldRank, String newRank, String date) {
            this.memberID = memberID;
            this.name = name;
            this.oldRank = oldRank;
            this.newRank = newRank;
            this.date = date;
        }

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOldRank() {
            return oldRank;
        }

        public void setOldRank(String oldRank) {
            this.oldRank = oldRank;
        }

        public String getNewRank() {
            return newRank;
        }

        public void setNewRank(String newRank) {
            this.newRank = newRank;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public static class RankAchieverListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<RankAchieverListModel> items;
        private Context context;
        private OnItemClickListener mOnItemClickListener;
        private int animation_type = 0;
        private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
                "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};

        public interface OnItemClickListener {
            void onItemClick(View view, RankAchieverListModel obj, int position);
        }

        public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mOnItemClickListener = mItemClickListener;
        }

        public RankAchieverListAdapter(Context context, List<RankAchieverListModel> items, int animation_type) {
            this.items = items;
            this.context = context;
            this.animation_type = animation_type;
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {
            public TextView txtMemberId, txtName, txtOldRank, txtNewRank, txtDate;
            public View lyt_parent;
            public Button btnAchieverList;

            public OriginalViewHolder(View v) {
                super(v);
                txtMemberId = (TextView) v.findViewById(R.id.txt_memberid);
                txtName = (TextView) v.findViewById(R.id.txt_name);
                txtOldRank = (TextView) v.findViewById(R.id.txt_oldrank);
                txtNewRank = (TextView) v.findViewById(R.id.txt_newrank);
                txtDate = (TextView) v.findViewById(R.id.txt_datetime);

                btnAchieverList = v.findViewById(R.id.btn_achieverlist);
                btnAchieverList.setVisibility(View.GONE);
                lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rankachiverslist, parent, false);
            vh = new OriginalViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof OriginalViewHolder) {
                OriginalViewHolder view = (OriginalViewHolder) holder;

                RankAchieverListModel model = items.get(position);
                view.txtMemberId.setText(model.getMemberID());
                view.txtName.setText(model.getName());
                view.txtOldRank.setText(model.getOldRank());
                view.txtNewRank.setText(model.getNewRank());
                view.txtDate.setText(model.getDate());

//                view.btnAchieverList.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (mOnItemClickListener != null) {
//                            mOnItemClickListener.onItemClick(view, items.get(position), position);
//                        }
//                    }
//                });
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