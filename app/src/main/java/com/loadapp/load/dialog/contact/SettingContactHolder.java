package com.loadapp.load.dialog.contact;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;


/**
 * @author xu.wang
 * @date 2020/2/3 17:40
 * @desc
 */
public class SettingContactHolder extends RecyclerView.ViewHolder {

    public TextView tvContact1;
    public TextView tvContact2;
    public TextView tvContactBg;
    public ImageView ivContact;


    public SettingContactHolder(@NonNull View itemView) {
        super(itemView);
        ivContact = itemView.findViewById(R.id.iv_setting_contact);

        tvContact1 = itemView.findViewById(R.id.tv_setting_contact_1);
        tvContact2 = itemView.findViewById(R.id.tv_setting_contact_2);

        tvContactBg = itemView.findViewById(R.id.tv_setting_contact_bg);
    }
}
