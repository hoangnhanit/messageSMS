package com.example.thispc.database_sms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by This PC on 10/9/2017.
 */

public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.MyViewHolder> {
    List<contact> datalist = new ArrayList<>();


    MyContactAdapter(List<contact> data) {
        datalist = data;
    }

    @Override
    public MyContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);

        return new MyContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyContactAdapter.MyViewHolder holder, int position) {
        Bitmap photo = null;

        contact T = datalist.get(position);
        holder.mFrom.setText(String.valueOf(T.getName()));
        holder.mBody.setText(String.valueOf(T.getPhone()));

        if(T.getPic()!=null) {
            photo = BitmapFactory.decodeStream(T.getPic());
            holder.mPic.setImageBitmap(photo);
        }

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mFrom, mBody;
        ImageView mPic;
        public MyViewHolder(View view) {
            super(view);
            mFrom = (TextView) itemView.findViewById(R.id.txt_name);
            mBody = (TextView) itemView.findViewById(R.id.txt_number);
            mPic = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
