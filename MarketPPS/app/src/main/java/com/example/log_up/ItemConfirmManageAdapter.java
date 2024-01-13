package com.example.log_up;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemConfirmManageAdapter extends RecyclerView.Adapter<ItemConfirmManageAdapter.ViewHolder> {
    private Context context;
    private List<ProductObject> listItem;

    public ItemConfirmManageAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ProductObject> listItem) {
        this.listItem = listItem;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_manage_story, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductObject item = listItem.get(position);
        holder.name.setText(item.getTenSanPham());
        holder.danhmuc.setText(item.getDanhmucSanPham());
        holder.price.setText(item.getGiaSanPham());
        Glide.with(context).load(item.getImageUrl()).into(holder.image);
        holder.layout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGotoDetail(item);
            }
        });
        holder.btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                Intent intent = new Intent(context, BillBuyList.class);
                String key = ""+item.getId();
                intent.putExtra("keyProduct",key);
                context.startActivity(intent);
            }
        });
        holder.imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.imageBtn);
                popupMenu.inflate(R.menu.option_menu_item_confirm);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        listItem.clear();
                        switch (menuItem.getItemId())
                        {
                            case R.id.hide:
                                DatabaseReference removeItem = FirebaseDatabase.getInstance().getReference("product").child(""+item.getId());
                                removeItem.removeValue();
                                return true;
                        }
                        return true;
                    }
                });
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
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price,danhmuc;
        private ImageView image, imageBtn;
        private LinearLayout layout_confirm;
        private Button btnSold ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            danhmuc = itemView.findViewById(R.id.tittleStoryConfirmed);
            name = itemView.findViewById(R.id.nameStoryConfirmed);
            price = itemView.findViewById(R.id.priceStoryConfirmed);
            image = itemView.findViewById(R.id.imageConfirmed);
            imageBtn = itemView.findViewById(R.id.btnMenu);
            layout_confirm = itemView.findViewById(R.id.layout_confirm);
            btnSold = itemView.findViewById(R.id.btnSold);
        }
    }
}
