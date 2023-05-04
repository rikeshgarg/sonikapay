package com.codunite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codunite.sonikapay.R;
import com.codunite.model.ContactModel;
import com.codunite.commonutility.GlobalVariables;
import com.codunite.commonutility.ItemAnimation;
import com.codunite.commonutility.customfont.FontUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<ContactModel> items = new ArrayList<>();
    private List<ContactModel> contactListFiltered = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, ContactModel contactModel, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public ContactAdapter(Context context, List<ContactModel> items, int animation_type) {
        this.items = items;
        this.contactListFiltered = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public void updateData(List<ContactModel> data) {
        this.items = data;
        this.contactListFiltered = data;
        notifyDataSetChanged();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtNumber;
        public ViewGroup root;
        public CircularImageView imgAvatar;

        public OriginalViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.name);
            txtNumber = (TextView) v.findViewById(R.id.number);
            imgAvatar = (CircularImageView) v.findViewById(R.id.avatar);

            root = (ViewGroup) v.findViewById(R.id.lyt_parent);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setFont(root, font);
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        vh = new ContactAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            int finalPosition = position = holder.getAdapterPosition();
            OriginalViewHolder view = (OriginalViewHolder) holder;
            ContactModel contactData = contactListFiltered.get(position);

            view.txtName.setText(contactData.getContactName());
            view.txtNumber.setText(contactData.getContactNumber());

            String imgUri = contactData.getImageUri();
            if (imgUri != null) {
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.parse(imgUri));
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.users);
                }
                view.imgAvatar.setImageBitmap(bitmap);
            } else {
                view.imgAvatar.setImageResource(R.drawable.users);
            }

            view.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, contactData, finalPosition);
                    }
                }
            });

            //setAnimation(view.itemView, position);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = items;
                } else {
                    List<ContactModel> filteredList = new ArrayList<>();
                    for (ContactModel row : items) {
                        if (row.getContactName().toLowerCase().contains(charString.toLowerCase())
                                || row.getContactNumber().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (List<ContactModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
        return contactListFiltered.size();
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