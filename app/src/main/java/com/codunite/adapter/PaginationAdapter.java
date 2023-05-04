package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.PaginationModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PaginationModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private int checkedPosition = -1;
    private boolean hasWhiteBg = false;
    private int textColor;
    private int unselectedBgColor;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PaginationAdapter(Context context, List<PaginationModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
        this.hasWhiteBg = false;
    }

    public PaginationAdapter(Context context, int pageSize, int animation_type) {
        for (int j = 1; j <= pageSize; j++) {
            items.add(new PaginationModel(j + ""));
        }
        ctx = context;
        this.animation_type = animation_type;
        this.hasWhiteBg = false;
    }

    public PaginationAdapter(Context context, int pageSize, int animation_type, boolean hasWhiteBg) {
        for (int j = 1; j <= pageSize; j++) {
            items.add(new PaginationModel(j + ""));
        }
        ctx = context;
        this.animation_type = animation_type;
        this.hasWhiteBg = hasWhiteBg;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CardView cardview;
        public TextView name;
        public ImageView imgSelected;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            imgSelected = (ImageView) v.findViewById(R.id.iv_selected);
            cardview = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);

            if (hasWhiteBg) {
                textColor = ctx.getResources().getColor(R.color.material_black);
                unselectedBgColor = ctx.getResources().getColor(R.color.white);
            } else {
                textColor = ctx.getResources().getColor(R.color.white);
                unselectedBgColor = ctx.getResources().getColor(R.color.material_black);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagination, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            PaginationModel model = items.get(position);
            view.imgSelected.setVisibility(model.isChecked() ? View.VISIBLE : View.GONE);

            if (checkedPosition == -1) {
                view.imgSelected.setVisibility(View.GONE);
                view.cardview.setCardBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
            } else {
                if (checkedPosition == view.getAdapterPosition()) {
                    view.imgSelected.setVisibility(View.VISIBLE);
                    view.cardview.setCardBackgroundColor(ctx.getResources().getColor(R.color.green_800));
                } else {
                    view.imgSelected.setVisibility(View.GONE);
                    view.cardview.setCardBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                }
            }

            view.name.setText(items.get(position).getName());

            view.cardview.setCardBackgroundColor(model.isChecked() ? ctx.getResources().getColor(R.color.green_600) : unselectedBgColor);
            view.name.setTextColor(textColor);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.imgSelected.setVisibility(View.VISIBLE);
                    view.cardview.setCardBackgroundColor(ctx.getResources().getColor(R.color.green_800));
                    if (checkedPosition != view.getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = view.getAdapterPosition();
                    }

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position).getName(), position);
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