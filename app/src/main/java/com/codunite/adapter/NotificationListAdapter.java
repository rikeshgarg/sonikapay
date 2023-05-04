package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.NotificationListModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ImageLoading;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationListModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#388E3C", "#D32F2F"};

    public interface OnItemClickListener {
        void onItemClick(View view, NotificationListModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public NotificationListAdapter(Context context, List<NotificationListModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDesc;
        public TextView txtNotification;
        public ImageView imgNotification;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            txtNotification = v.findViewById(R.id.txt_letter);
            imgNotification = (ImageView) v.findViewById(R.id.img_notification);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.tvTitle.setText(items.get(position).getStrTitle());
            view.tvDesc.setText(items.get(position).getStrDesc());

            String strImgUrl = items.get(position).getImgUrl();
            if (strImgUrl.equals("") || strImgUrl.length() <= 30) {
                try {
                    String[] strings = items.get(position).getStrTitle().trim().split(" ");
                    String nameLetters = "";
                    if (strings.length == 1) {
                        nameLetters = ((Character) strings[0].trim().charAt(0)).toString().toUpperCase();
                    } else if (strings.length >= 2) {
                        String str1 = ((Character) strings[0].trim().charAt(0)).toString().toUpperCase();
                        String str2 = ((Character) strings[1].trim().charAt(0)).toString().toUpperCase();
                        nameLetters = str1 + " " + str2;
                    }
                    view.txtNotification.setText(nameLetters);
                    view.imgNotification.setVisibility(View.GONE);
                }catch (Exception e) {
                    view.imgNotification.setVisibility(View.VISIBLE);
                }
            } else {
                view.imgNotification.setVisibility(View.VISIBLE);
                ImageLoading.loadImages(strImgUrl, view.imgNotification, R.drawable.logo_round);
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
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