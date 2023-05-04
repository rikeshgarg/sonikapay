package com.codunite.sonikapay.spinner;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ImageLoading;
import com.codunite.commonutility.customfont.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssridharatiwari on 2021.
 */
public class PopulateSpinnerAdapter extends RecyclerView.Adapter<PopulateSpinnerAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<SpinnerModel> spinnerList;
    private List<SpinnerModel> spinnerListFiltered;
    private OnItemClickListener mOnItemClickListener;
    boolean isImageShow = false, isDescShow = false;

    public interface OnItemClickListener {
        void onItemClick(View view, SpinnerModel selectedSpinner, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView thumbnail;
        public RelativeLayout itemHead;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            thumbnail = view.findViewById(R.id.thumbnail);
            itemHead = view.findViewById(R.id.item_head);

            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setFont(itemHead, font);
            }

        }
    }

    public PopulateSpinnerAdapter(Context context, List<SpinnerModel> contactList, OnItemClickListener listener,
                                  boolean isImageShow, boolean isDescShow) {
        this.context = context;
        this.mOnItemClickListener = listener;
        this.spinnerList = contactList;
        this.spinnerListFiltered = contactList;
        this.isImageShow = isImageShow;
        this.isDescShow = isDescShow;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SpinnerModel contact = spinnerListFiltered.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                if (mOnItemClickListener != null) {
                    SpinnerModel spinnerModel = spinnerListFiltered.get(position);
                    mOnItemClickListener.onItemClick(view, spinnerModel, position);
                }
            }
        });

        holder.title.setText(contact.getTitle());
        if (isDescShow) {
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(contact.getDesc());
        } else {
            holder.desc.setVisibility(View.GONE);
        }

        if (isImageShow && contact.getStrImgUrl() != null) {
            holder.thumbnail.setVisibility(View.VISIBLE);
            ImageLoading.loadImages(contact.getStrImgUrl(), holder.thumbnail, 0);
        } else {
            holder.thumbnail.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return spinnerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    spinnerListFiltered = spinnerList;
                } else {
                    List<SpinnerModel> filteredList = new ArrayList<>();
                    for (SpinnerModel row : spinnerList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getId().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    spinnerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = spinnerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                spinnerListFiltered = (ArrayList<SpinnerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateListData(List<SpinnerModel> list) {
        spinnerList = list;
        spinnerListFiltered = list;
        notifyDataSetChanged();
    }

}