package com.loadapp.load.ui.profile.banklist;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.dialog.SelectDataHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BankListsAdapter extends RecyclerView.Adapter<SelectDataHolder> {

    private ArrayList<Pair<String, String>> mLists;
    private OnItemClickListener mItemClickListener;

    public BankListsAdapter(ArrayList<Pair<String, String>> lists) {
        mLists = lists;
    }

    @NonNull
    @NotNull
    @Override
    public SelectDataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SelectDataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectDataHolder holder, int position) {
        Pair<String, String> pair = mLists.get(position);
        holder.tvSelectData.setText(pair.first);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.onItemClick(pair, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(Pair<String, String> content, int pos);

    }
}
