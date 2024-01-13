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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ItemViewHolder>{
    private Context context;
    private ArrayList<ProductObject> mList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    private DatabaseReference reference;


    public AdapterCart(Context context) {
        this.context = context;
    }
    public void setData(ArrayList<ProductObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cart, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ItemViewHolder holder, int position) {
        ProductObject itemCar = mList.get(position);

        if (itemCar==null){
            return;
        }
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String getUserName = snapshot.child(itemCar.getSodienthoaiUser()).child("fullname").getValue(String.class);
                holder.tvName.setText(getUserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.textTitle.setText(itemCar.getTenSanPham());
        holder.tvDanhmuc.setText(itemCar.getDanhmucSanPham());
        holder.textPrice.setText(String.valueOf(itemCar.getGiaSanPham()));
        Glide.with(context).load(itemCar.getImageUrl()).into(holder.imgCart);
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
        return mList.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCart;
        private TextView textTitle , textDes, textPrice, tvName, tvDanhmuc;
        private MaterialCardView materialCardView;
        private  ImageView icon_heart;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            materialCardView = itemView.findViewById(R.id.layout_item_cart);
            textTitle = itemView.findViewById(R.id.tv_item_Cart);
            textPrice = itemView.findViewById(R.id.tv_price_cart);
            tvName = itemView.findViewById(R.id.nameUserCart);
            tvDanhmuc = itemView.findViewById(R.id.TittleUserCart);
            imgCart = itemView.findViewById(R.id.img_itemCart);


        }
    }
}
