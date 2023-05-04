package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.TicketListModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.ImageLoading;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class TicketListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TicketListModel> items = new ArrayList<>();
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

    public TicketListAdapter(Context context, List<TicketListModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView ticketId, subject, message, type, status, datetime;
        public CardView cardView;
        public ImageView imgTicket;
        public View lyt_parent;
        public OriginalViewHolder(View v) {
            super(v);
            ticketId = (TextView) v.findViewById(R.id.ticketid);
            subject = (TextView) v.findViewById(R.id.subject);
            message = (TextView) v.findViewById(R.id.message);
            type = (TextView) v.findViewById(R.id.complaintype);
            status = (TextView) v.findViewById(R.id.status);
            datetime = (TextView) v.findViewById(R.id.datetime);
            imgTicket = v.findViewById(R.id.img_ticket);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewticket, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.ticketId.setText(items.get(position).getStr_ticket_id());
            view.type.setText(items.get(position).getStr_type());
            view.subject.setText(items.get(position).getStr_subject());
            view.message.setText(items.get(position).getStr_message());
            view.status.setText(items.get(position).getStr_data_status());
            view.datetime.setText(items.get(position).getStr_datetime());

            String strImgUrl = items.get(position).getImgUrl();
            ImageLoading.loadImages(strImgUrl, view.imgTicket, 0);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getStr_ticket_id(), position);
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