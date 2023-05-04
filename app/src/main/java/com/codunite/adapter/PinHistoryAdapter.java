package com.codunite.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.PinHistoryModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ItemAnimation;

import java.util.List;

import me.grantland.widget.AutofitHelper;

public class PinHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PinHistoryModel> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#388E3C", "#D32F2F"};

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PinHistoryAdapter(Context context, List<PinHistoryModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView amountRecharge, recharegDate, desc;
        public Button btnCopyToken;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            amountRecharge = (TextView) v.findViewById(R.id.amountrecharged);
            recharegDate = (TextView) v.findViewById(R.id.recahrge_datetime);
            btnCopyToken = (Button) v.findViewById(R.id.btn_copy_token);

            desc = (TextView) v.findViewById(R.id.rechargedesc);
            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinhistory, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.amountRecharge.setText(items.get(position).getPackage_amount());
            String strUseDate, strUsedDate;
            if (items.get(position).getUsed_by().equals("Not Available")) {
                strUseDate = "NA";
            }else{
                strUseDate = items.get(position).getUsed_by();
            }

            if (items.get(position).getUsed_date().equals("Not Available")) {
                strUsedDate = "NA";
            }else{
                strUsedDate = items.get(position).getUsed_date();
            }
            view.recharegDate.setText("Use before: " + strUseDate + "\n" + "Used date: " + strUsedDate);
            view.desc.setText("Package Name: " + items.get(position).getPackage_name() + "\n" +
                    "Token number: " + items.get(position).getToken() + "\n" +
                    "Is Used: " + items.get(position).getIs_used());

            AutofitHelper.create(view.desc);

            view.lyt_parent.setOnClickListener(view12 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view12, items.get(position).getPackage_name(), position);
                }
            });
            view.btnCopyToken.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position).getPackage_name(), position);
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