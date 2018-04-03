package com.yeeyuntech.newheader.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeeyuntech.newheader.R;

import java.util.List;

/**
 * Description: Jojo on 2018/4/3 ,Copyright YeeyunTech
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleHolder> {

    private List<String> mData;

    public SimpleAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public SimpleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_holder, parent, false);
        SimpleHolder holder = new SimpleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SimpleHolder extends RecyclerView.ViewHolder {
        TextView textView;

        SimpleHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
        }
    }
}
