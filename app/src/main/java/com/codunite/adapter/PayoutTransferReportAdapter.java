package com.codunite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.model.PayoutTransferReportModel;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class PayoutTransferReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PayoutTransferReportModel> items = new ArrayList<>();
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

    public PayoutTransferReportAdapter(Context context, List<PayoutTransferReportModel> items, int animation_type, boolean isComplaint) {
        this.isComplaint = isComplaint;
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txtmember, txtMobile,txtname,txtifsc,txtamount, txttransasctionid,txtrrn, txtstatus,txtdate;
        public CardView cardView;
        public View lyt_parent;
      //  public Button btnComplain;

        public OriginalViewHolder(View v) {
            super(v);
            txtmember = (TextView) v.findViewById(R.id.member_detail);
            txtname = (TextView) v.findViewById(R.id.name);
            txtMobile = (TextView) v.findViewById(R.id.mobile);
            txtifsc = (TextView) v.findViewById(R.id.tv_ifsc);
            txtamount = (TextView) v.findViewById(R.id.tv_amount);
            txttransasctionid = (TextView) v.findViewById(R.id.tv_trnsc_id);
            txtrrn = (TextView) v.findViewById(R.id.tv_rrn);
            txtstatus = (TextView) v.findViewById(R.id.tv_status);
            txtdate = (TextView) v.findViewById(R.id.tv_datetime);


          //  btnComplain= (Button) v.findViewById(R.id.btn_complain);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payouttranferreport, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.txtamount.setText(GlobalVariables.CURRENCYSYMBOL+items.get(position).getStr_amount());
            view.txtdate.setText(items.get(position).getStr_datetime());
            view.txtrrn.setText(items.get(position).getStr_rrn());
            view.txtMobile.setText(items.get(position).getMobile());
            view.txttransasctionid.setText(items.get(position).getStr_txn_id());
            view.txtname.setText(items.get(position).getStr_name());
            view.txtifsc.setText(items.get(position).getStr_ifsc());
            view.txtmember.setText(items.get(position).getStr_member_id());

            if (items.get(position).getStr_status().equalsIgnoreCase("Pending")){
                view.txtstatus.setText("Pending");
                view.txtstatus.setTextColor(ctx.getResources().getColor(R.color.orange_900));
            }else if (items.get(position).getStr_status().equalsIgnoreCase("Success")){
                view.txtstatus.setText("Success");
                view.txtstatus.setTextColor(ctx.getResources().getColor(R.color.green));
            }else {
                view.txtstatus.setText("Fail");
                view.txtstatus.setTextColor(ctx.getResources().getColor(R.color.red));
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

    //        if (isComplaint) {
      //          view.btnComplain.setVisibility(View.VISIBLE);
     //       }else{
       //         view.btnComplain.setVisibility(View.GONE);
      //      }
     //       view.btnComplain.setOnClickListener(new View.OnClickListener() {
      //          @Override
      //          public void onClick(View view) {
        //            if (mOnComplaintItemClickListener != null) {
         //               mOnComplaintItemClickListener.onComplaintItemClick(view, items.get(position).getStr_datetime(), position);
        //            }
        //        }
       //     });
       //     setAnimation(view.itemView, position);
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