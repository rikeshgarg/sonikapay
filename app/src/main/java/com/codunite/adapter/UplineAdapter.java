package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.codunite.model.UplineModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ItemAnimation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UplineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UplineModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
            "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};


    public interface OnItemClickListener {
        void onItemClick(View view, UplineModel obj, int position);
        void onItemMobileClick(View view, UplineModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public UplineAdapter(Context context, List<UplineModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView usercode, name, mobile;
        public CardView cardView;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);

            usercode = (TextView) v.findViewById(R.id.dl_usercode);
            name = (TextView) v.findViewById(R.id.dl_name);
            mobile = (TextView) v.findViewById(R.id.dl_mobile);


            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upline, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.usercode.setText(items.get(position).getStr_usercode());
            view.name.setText(items.get(position).getStr_name());
            view.mobile.setText(items.get(position).getStr_mobile());

//            Random random = new Random();
//            int randomNumber = random.nextInt(12 - 1) + 1;
//            view.cardView.setCardBackgroundColor(Color.parseColor(strColors[randomNumber]));

            view.mobile.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemMobileClick(v, items.get(position), position);
                }
            });

            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position), position);
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