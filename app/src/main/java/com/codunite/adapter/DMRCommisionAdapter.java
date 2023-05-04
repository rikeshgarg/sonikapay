package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.model.DMRCommisionModel;
import com.codunite.sonikapay.R;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class DMRCommisionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DMRCommisionModel> items = new ArrayList<>();
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

    public DMRCommisionAdapter(Context context, List<DMRCommisionModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txtstartrange, txtendrange,txtflat,txtsurcharge;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            txtstartrange = (TextView) v.findViewById(R.id.startrange_commision);
            txtendrange = (TextView) v.findViewById(R.id.endrange_commision);
            txtflat = (TextView) v.findViewById(R.id.flat_dmr_commision);
            txtsurcharge= (TextView) v.findViewById(R.id.surcharge_dmr_commision);
            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dmrcommision, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.txtstartrange.setText(GlobalVariables.CURRENCYSYMBOL+items.get(position).getStrstartrange());
            view.txtendrange.setText(items.get(position).getStrendrange());
            view.txtflat.setText(items.get(position).getStrflat());
            view.txtsurcharge.setText(items.get(position).getStrsurcharge());



//            if ((items.get(position).getStr_status()).equalsIgnoreCase("1")) {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[0]));
//            } else {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[1]));
//            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getStrendrange(), position);
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