package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.model.DownLineModel;
import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class DownLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DownLineModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private boolean isMobileSHow = false;
    private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
            "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};


    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DownLineAdapter(Context context, List<DownLineModel> items, int animation_type, boolean isMobileSHow) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
        this.isMobileSHow = isMobileSHow;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView level, member, membership, email, mobile, emailrem, mobrem;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            level = (TextView) v.findViewById(R.id.dl_level);
            member = (TextView) v.findViewById(R.id.dl_member);
            membership = (TextView) v.findViewById(R.id.dl_membership);
            email = (TextView) v.findViewById(R.id.dl_email);
            mobile = (TextView) v.findViewById(R.id.dl_mobile);
            emailrem = (TextView) v.findViewById(R.id.email_remove);
            mobrem = (TextView) v.findViewById(R.id.mobile_remove);
            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downline, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.level.setText(items.get(position).getStr_level());
            view.member.setText(items.get(position).getStr_name());
            view.membership.setText(items.get(position).getStr_membership());
            view.email.setText(items.get(position).getStr_email());
            view.mobile.setText("  " + items.get(position).getStr_mobile());

            view.email.setOnClickListener(v -> {
                //GlobalData.sendMail(ctx, "", "", items.get(position).getStr_email());
            });
            view.mobile.setOnClickListener(v -> {
                GlobalData.dialCall(ctx, items.get(position).getStr_mobile());
            });

            if (isMobileSHow) {
                view.mobile.setVisibility(View.VISIBLE);
                view.email.setVisibility(View.VISIBLE);
                view.emailrem.setVisibility(View.VISIBLE);
                view.mobrem.setVisibility(View.VISIBLE);

            } else {

                view.mobile.setVisibility(View.GONE);
                view.email.setVisibility(View.GONE);
                view.emailrem.setVisibility(View.GONE);
                view.mobrem.setVisibility(View.GONE);
//               view.mobile.setVisibility(View.VISIBLE);
//               view.mobile.setVisibility(View.VISIBLE);
//               view.mobile.setVisibility(View.VISIBLE);

            }


//            if (position >= 12) {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[position - 12]));
//            } else {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[position]));
//            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getStr_name(), position);
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