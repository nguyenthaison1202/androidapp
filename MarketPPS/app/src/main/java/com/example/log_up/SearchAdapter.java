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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<ProductObject> mList;

    public SearchAdapter(Context context)
    {
        this.context = context;
    }
    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ProductObject productObject = mList.get(position);

        holder.tvNameSearch.setText(productObject.getTenSanPham());
        holder.tvDesSearch.setText(productObject.getDanhmucSanPham());
        holder.tvPriceSearch.setText(productObject.getGiaSanPham());
        Glide.with(context).load(productObject.getImageUrl()).into(holder.imgSearch);
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(productObject);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void onClickGotoDetail(ProductObject searchItem) {
        Intent intent = new Intent(context, DetailProduct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_item", searchItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSearch, tvDesSearch, tvPriceSearch;
        private ImageView imgSearch;
        private MaterialCardView materialCardView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSearch = itemView.findViewById(R.id.tv_item_name_search);
            tvDesSearch = itemView.findViewById(R.id.tv_describe_search);
            tvPriceSearch = itemView.findViewById(R.id.tv_price_search);
            imgSearch = itemView.findViewById(R.id.img_itemSearch);
            materialCardView = itemView.findViewById(R.id.layout_item_search);
        }
    }
}
