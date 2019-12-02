package com.example.chat.adapter;


import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;

import java.util.ArrayList;

public class ImgRecyclerViewAdapter extends RecyclerView.Adapter<ImgRecyclerViewAdapter.ImgViewHolder> {
    private ArrayList<String> mList;


    public ImgRecyclerViewAdapter(ArrayList<String> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.img_list, viewGroup, false);

        ImgViewHolder viewHolder = new ImgViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder holder, int position) {
        holder.imgName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        holder.imgName.setGravity(Gravity.CENTER);

        holder.imgName.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public class ImgViewHolder extends RecyclerView.ViewHolder {
        protected TextView imgName;


        public ImgViewHolder(View view) {
            super(view);
            this.imgName = (TextView) view.findViewById(R.id.imgName1);
        }
    }


}
