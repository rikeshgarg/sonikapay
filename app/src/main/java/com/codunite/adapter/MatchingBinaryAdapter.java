package com.codunite.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.codunite.model.MatchingBinaryModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class MatchingBinaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MatchingBinaryModel> items = new ArrayList<>();
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

    public MatchingBinaryAdapter(Context context, List<MatchingBinaryModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView level, member, membership, email, mobile, walletsettle;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            level = (TextView) v.findViewById(R.id.dl_level);
            member = (TextView) v.findViewById(R.id.dl_member);
            membership = (TextView) v.findViewById(R.id.dl_membership);
            email = (TextView) v.findViewById(R.id.dl_email);
            mobile = (TextView) v.findViewById(R.id.dl_mobile);
            walletsettle = (TextView) v.findViewById(R.id.waller_settle);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matchingbinary, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.level.setText(items.get(position).getStr_date());
            view.member.setText(items.get(position).getStr_income_amount());
            view.membership.setText(items.get(position).getStr_binary_amount());
            view.email.setText(items.get(position).getStr_tds_amount());
            view.mobile.setText(items.get(position).getStr_is_paid());
            view.walletsettle.setText(items.get(position).getStr_wallet_settle_amount());

            if (position >= 12) {
                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[0]));
            } else {
                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[1]));
            }

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