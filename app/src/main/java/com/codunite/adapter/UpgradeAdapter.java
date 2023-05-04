package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.PackageModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class UpgradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PackageModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private OnItemWalletClickListener mOnItemWalletListener;
    private OnItemUpgradeClickListener mOnUpgradeClickListener;

    private int animation_type = 0;
    private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
            "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};


    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }


    public interface OnItemWalletClickListener {
        void onItemWalletClick(View view, int position);
    }

    public interface OnItemUpgradeClickListener {
        void onItemUpgradeClick(View view, int position);
    }

    public void setmOnItemWalletListener(final OnItemWalletClickListener mOnItemWalletListener) {
        this.mOnItemWalletListener = mOnItemWalletListener;
    }

    public void setOnItemUpgradeClickListener(final OnItemUpgradeClickListener mOnUpgradeClickListener) {
        this.mOnUpgradeClickListener = mOnUpgradeClickListener;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public UpgradeAdapter(Context context, List<PackageModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, upgradePin, upgradeWallet;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            upgradePin = (TextView) v.findViewById(R.id.upgrade_pin);
            upgradeWallet = (TextView) v.findViewById(R.id.upgrade_wallet);

            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upgrade, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.name.setText(items.get(position).getName());
            view.price.setText(GlobalVariables.CURRENCYSYMBOL +
                    items.get(position).getPrice());

//            if (position >= 12) {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[position - 12]));
//            } else {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[position]));
//            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getName(), position);
                    }
                }
            });
            view.upgradePin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnUpgradeClickListener != null) {
                        mOnUpgradeClickListener.onItemUpgradeClick(view, position);
                    }
                }
            });

            view.upgradeWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemWalletListener != null) {
                        mOnItemWalletListener.onItemWalletClick(view, position);
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