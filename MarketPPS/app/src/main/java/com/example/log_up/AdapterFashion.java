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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AdapterFashion extends RecyclerView.Adapter<AdapterFashion.ItemViewHolder> {
    private Context context;
    private ArrayList<ProductObject> mList;

    public AdapterFashion(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fashion, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,@SuppressLint("RecyclerView") int position) {
        ProductObject itemFashion = mList.get(position);
        if (itemFashion==null){
            return;
        }
        Glide.with(context).load(itemFashion.getImageUrl()).into(holder.imgCar);
        holder.textTitle.setText(itemFashion.getTenSanPham());
        holder.textDes.setText(itemFashion.getDanhmucSanPham());
        holder.textPrice.setText(String.valueOf(itemFashion.getGiaSanPham()));
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(itemFashion);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mList==null)
            return 0;
        return mList.size();
    }
    private void onClickGotoDetail(ProductObject itemCar) {
        Intent intent = new Intent(context, DetailProduct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_item", itemCar);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCar;
        private TextView textTitle , textDes, textPrice;
        private MaterialCardView materialCardView;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            imgCar = itemView.findViewById(R.id.img_itemFashion);
            textTitle = itemView.findViewById(R.id.tv_item_fashion);
            textDes = itemView.findViewById(R.id.tv_describe_fashion);
            textPrice = itemView.findViewById(R.id.tv_price_fashion);
            materialCardView = itemView.findViewById(R.id.layout_item_fashion);

        }
    }

}
