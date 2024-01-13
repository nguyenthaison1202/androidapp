package com.example.log_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterCar extends RecyclerView.Adapter<AdapterCar.ItemViewHolder> {
    private Context context;
    private ArrayList<ProductObject> mList;
    public AdapterCar(Context context) {
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
        ProductObject itemCar = mList.get(position);
        if (itemCar==null){
            return;
        }
        Glide.with(context).load(itemCar.getImageUrl()).into(holder.imgCar);
        holder.textTitle.setText(itemCar.getTenSanPham());
        holder.textDes.setText(itemCar.getDanhmucSanPham());
        holder.textPrice.setText(itemCar.getGiaSanPham());


        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(itemCar);
            }
        });

    }

    private void onClickGotoDetail(ProductObject itemCar) {
        Intent intent = new Intent(context, DetailProduct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_item", itemCar);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        if (mList==null)
            return 0;
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCar;
        private TextView textTitle , textDes, textPrice;
        private MaterialCardView materialCardView;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            materialCardView = itemView.findViewById(R.id.layout_item_car);
            imgCar = itemView.findViewById(R.id.img_itemCar);
            textTitle = itemView.findViewById(R.id.tv_item_Car);
            textDes = itemView.findViewById(R.id.tv_describe_car);
            textPrice = itemView.findViewById(R.id.tv_price_car);
        }
    }

}
