package com.example.log_up;

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

public class AdapterSport extends RecyclerView.Adapter<AdapterSport.ItemViewHolder>{
    private Context context;
    private ArrayList<ProductObject> mList;

    public AdapterSport(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_sport, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ProductObject itemSport = mList.get(position);
        if (itemSport == null) {
            return;
        }
        Glide.with(context).load(itemSport.getImageUrl()).into(holder.imgSport);
        holder.textTitle.setText(itemSport.getTenSanPham());
        holder.textPrice.setText(itemSport.getGiaSanPham());
        holder.textDes.setText(itemSport.getDanhmucSanPham());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(itemSport);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mList.size()==0)
        {
            return 0;
        }
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
        private ImageView imgSport;
        private TextView textTitle , textDes, textPrice;
        private MaterialCardView materialCardView;

        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            imgSport = itemView.findViewById(R.id.img_itemSport);
            textTitle = itemView.findViewById(R.id.tv_item_sport);
            textDes = itemView.findViewById(R.id.tv_describe_sport);
            textPrice = itemView.findViewById(R.id.tv_price_sport);
            materialCardView = itemView.findViewById(R.id.layout_item_sport);
        }
    }
}
