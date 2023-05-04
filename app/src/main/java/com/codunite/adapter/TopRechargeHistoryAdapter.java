package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.model.RechargeHistoryModel;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class TopRechargeHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RechargeHistoryModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private OnComplaintItemClickListener mOnComplaintItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#388E3C", "#D32F2F"};
    private boolean isComplaint;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnComplaintItemClickListener {
        void onComplaintItemClick(View view, String obj, int position);
    }

    public void setOnComplaintItemClickListener(final OnComplaintItemClickListener mOnComplaintItemClickListener) {
        this.mOnComplaintItemClickListener = mOnComplaintItemClickListener;
    }

    public TopRechargeHistoryAdapter(Context context, List<RechargeHistoryModel> items, int animation_type, boolean isComplaint) {
        this.isComplaint = isComplaint;
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView amountRecharge, recharegDateTime, status, txtID,txtaccno;
        public TextView txtOperator, txtMobile, txtType, memberDeatil, txtAfterBal, txtBeforeBal;
        public CardView cardView;
        public View lyt_parent;
        public View layaccountno,laymobile;
        public Button btnComplain;

        public OriginalViewHolder(View v) {
            super(v);
            amountRecharge = (TextView) v.findViewById(R.id.vm_memberid);
            recharegDateTime = (TextView) v.findViewById(R.id.vm_name);
            txtaccno = (TextView) v.findViewById(R.id.vm_accountno);
            status = (TextView) v.findViewById(R.id.vm_wallbal);
            txtOperator = (TextView) v.findViewById(R.id.operator);
            txtMobile = (TextView) v.findViewById(R.id.mobile);
            txtType= (TextView) v.findViewById(R.id.type);
            txtAfterBal= (TextView) v.findViewById(R.id.after_balance);
            txtBeforeBal= (TextView) v.findViewById(R.id.before_balance);
            txtID= (TextView) v.findViewById(R.id.txtId);
            btnComplain= (Button) v.findViewById(R.id.btn_complain);
            laymobile=(LinearLayout)v.findViewById(R.id.lay_mob);
            layaccountno=(LinearLayout)v.findViewById(R.id.accountno);
            memberDeatil= (TextView) v.findViewById(R.id.member_detail);
            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_rechargehistory, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.amountRecharge.setText(GlobalVariables.CURRENCYSYMBOL+items.get(position).getStr_amount());
            view.recharegDateTime.setText(items.get(position).getStr_datetime());
            view.txtOperator.setText(items.get(position).getOperator());
            view.txtMobile.setText(items.get(position).getMobile());
            view.txtType.setText(items.get(position).getType());
            view.memberDeatil.setText(items.get(position).getMemberDetail());
            view.txtAfterBal.setText(items.get(position).getAfterBalance());
            view.txtBeforeBal.setText(items.get(position).getBeforeBalance());
            view.txtaccno.setText(items.get(position).getStr_account_number());


            view.txtID.setText(items.get(position).getStr_order_id());

            if (items.get(position).getStr_status().equalsIgnoreCase("1")){
                view.status.setText("Pending");
                view.status.setTextColor(ctx.getResources().getColor(R.color.orange_900));
            }else if (items.get(position).getStr_status().equalsIgnoreCase("2")){
                view.status.setText("Success");
                view.status.setTextColor(ctx.getResources().getColor(R.color.green));
            }else {
                view.status.setText("Fail");
                view.status.setTextColor(ctx.getResources().getColor(R.color.red));
            }

            if (items.get(position).getStr_account_number().equalsIgnoreCase("")){
                view.layaccountno.setVisibility(View.GONE);
            }else {
                view.layaccountno.setVisibility(View.VISIBLE);

            }
            if (items.get(position).getMobile().equalsIgnoreCase("")){
                view.laymobile.setVisibility(View.GONE);
            }else {
                view.laymobile.setVisibility(View.VISIBLE);

            }

//            if ((items.get(position).getStr_status()).equalsIgnoreCase("1")) {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[0]));
//            } else {
//                view.cardView.setCardBackgroundColor(Color.parseColor(strColors[1]));
//            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position).getStr_datetime(), position);
                    }
                }
            });

            if (isComplaint) {
                view.btnComplain.setVisibility(View.GONE);
            }else{
                view.btnComplain.setVisibility(View.GONE);
            }
            view.btnComplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnComplaintItemClickListener != null) {
                        mOnComplaintItemClickListener.onComplaintItemClick(view, items.get(position).getStr_datetime(), position);
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