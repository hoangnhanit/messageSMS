package com.example.thispc.database_sms;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by This PC on 9/30/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<sms> datalist = new ArrayList<>();
    MyAdapter(List<sms> data){
        datalist = data;
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sms, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        sms  T = datalist.get(datalist.size()-position-1);
        holder.mName.setText(String.valueOf(T.getName()));
        holder.mPhone.setText(String.valueOf(T.getmPhone()));
        holder.mInfo.setText(String.valueOf(T.getInfo()));

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName,mInfo,mPhone;

        public MyViewHolder(View view) {
            super(view);
            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mPhone = (TextView) itemView.findViewById(R.id.txt_phone);
            mInfo = (TextView) itemView.findViewById(R.id.txt_info);

        }
    }

}
