package com.loadapp.load.dialog.contact;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.loadapp.load.R;
import com.loadapp.load.bean.ContactBean;

import java.util.ArrayList;

/**
 * @author xu.wang
 * @date 2020/2/3 17:38
 * @desc
 */
public class SettingContactAdapter extends RecyclerView.Adapter<SettingContactHolder> {

    private static final String TAG = "SettingContactAdapter";

    private ArrayList<ContactBean> mList;

    public SettingContactAdapter(ArrayList<ContactBean> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public SettingContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_setting_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingContactHolder holder, int position) {
        ContactBean contactBean = mList.get(position);
        holder.tvContact1.setText(contactBean.contactName);
        holder.tvContact2.setText(contactBean.number);

        if (TextUtils.isEmpty(contactBean.photoUri)){
            holder.ivContact.setVisibility(View.GONE);
            holder.tvContactBg.setVisibility(View.VISIBLE);
            String str = contactBean.contactName.substring(0,1);
            holder.tvContactBg.setText(str);
        } else {
            holder.ivContact.setVisibility(View.VISIBLE);
            holder.tvContactBg.setVisibility(View.GONE);
            Glide.with(holder.ivContact).load(contactBean.photoUri).into(holder.ivContact);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null){
                    mListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onClick(int pos);
    }
}
