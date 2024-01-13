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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterElectronic extends RecyclerView.Adapter<AdapterElectronic.ItemViewHolder> {
    private Context context;
    private ArrayList<ProductObject> mList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    public AdapterElectronic(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_electronic, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,@SuppressLint("RecyclerView") int position) {
        ProductObject itemElectronic = mList.get(position);
        if (itemElectronic==null){
            return;
        }
        Glide.with(context).load(itemElectronic.getImageUrl()).into(holder.imgElectronic);
        holder.textTitle.setText(itemElectronic.getTenSanPham());
        holder.textDes.setText(itemElectronic.getDanhmucSanPham());
        holder.textPrice.setText(String.valueOf(itemElectronic.getGiaSanPham()));
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(itemElectronic);
            }
        });

    }
    private void onClickGotoDetail(ProductObject itemElectronic) {
        Intent intent = new Intent(context, DetailProduct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_item", itemElectronic);
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
        ImageView imgElectronic;
        TextView textTitle , textDes, textPrice;
        private MaterialCardView materialCardView;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            materialCardView = itemView.findViewById(R.id.layout_item_electronic);
            imgElectronic = itemView.findViewById(R.id.img_itemElectronic);
            textTitle = itemView.findViewById(R.id.tv_item_Electronic);
            textDes = itemView.findViewById(R.id.tv_describe_Electronic);
            textPrice = itemView.findViewById(R.id.tv_price_Electronic);

        }
    }

}

