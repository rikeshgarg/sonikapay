package com.codunite.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.customfont.FontUtils;
import com.codunite.sonikapay.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAmount extends RecyclerView.Adapter<AdapterAmount.SingleViewHolder> {
    private int layout;
    private Context context;
    private List<String> employees;
    private int checkedPosition = 0;


    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, String obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterAmount(Context context, List<String> employees, int layout) {
        this.context = context;
        this.employees = employees;
        this.layout = layout;
    }

    public void setEmployees(ArrayList<String> employees) {
        this.employees = new ArrayList<>();
        this.employees = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAmount;
        private RelativeLayout layHeadChange;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            layHeadChange = itemView.findViewById(R.id.lay_head_change);
        }

        void bind(final String model) {
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setFont(layHeadChange, font);
            }

            txtAmount.setText(model);

            itemView.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, model);
                }
            });
        }
    }
}