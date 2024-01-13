package com.example.log_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterBuyBill extends RecyclerView.Adapter<AdapterBuyBill.ItemViewHolder> {
    private Context context;
    private ArrayList<ProductObject> mList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    private DatabaseReference reference;
    public AdapterBuyBill(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_car, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,@SuppressLint("RecyclerView") int position) {
        ProductObject itemBill = mList.get(position);
        if (itemBill==null){
            return;
        }
        Glide.with(context).load(itemBill.getImageUrl()).into(holder.imgBill);
        holder.textTitle.setText(itemBill.getTenSanPham());
        holder.textDes.setText(itemBill.getDanhmucSanPham());
        holder.textPrice.setText(itemBill.getGiaSanPham());



    }


    @Override
    public int getItemCount() {
        if (mList==null)
            return 0;
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBill;
        private TextView textTitle , textDes, textPrice;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            imgBill = itemView.findViewById(R.id.img_itemCar);
            textTitle = itemView.findViewById(R.id.tv_item_Car);
            textDes = itemView.findViewById(R.id.tv_describe_car);
            textPrice = itemView.findViewById(R.id.tv_price_car);

        }
    }

}
