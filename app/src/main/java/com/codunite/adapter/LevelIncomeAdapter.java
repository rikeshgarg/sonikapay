package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.LevelIncomeModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class LevelIncomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LevelIncomeModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
            "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};


    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public LevelIncomeAdapter(Context context, List<LevelIncomeModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView level, member, paid, date, tdsamount, walletsettle, serviceTAxAmount;
        public CardView cardView;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            level = (TextView) v.findViewById(R.id.dl_level);
            member = (TextView) v.findViewById(R.id.dl_member);
            paid = (TextView) v.findViewById(R.id.dl_paid);
            date = (TextView) v.findViewById(R.id.dl_date);
            tdsamount = (TextView) v.findViewById(R.id.dl_tds_amount);
            walletsettle = (TextView) v.findViewById(R.id.waller_settle);

            serviceTAxAmount = (TextView) v.findViewById(R.id.service_tax_amount);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levelincome, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.level.setText(items.get(position).getStr_level_num());
            view.member.setText(items.get(position).getStr_by_member());
            view.paid.setText(items.get(position).getStr_is_paid());
            view.date.setText(items.get(position).getStr_date());
            view.tdsamount.setText(items.get(position).getStr_tds_amount());
            view.walletsettle.setText(items.get(position).getStr_wallet_settle_amount());

            view.serviceTAxAmount.setText(items.get(position).getService_tax_amount());

//            Random random = new Random();
//            int randomNumber = random.nextInt(12 - 1) + 1;
//            view.cardView.setCardBackgroundColor(Color.parseColor(strColors[randomNumber]));

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getStr_date(), position);
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