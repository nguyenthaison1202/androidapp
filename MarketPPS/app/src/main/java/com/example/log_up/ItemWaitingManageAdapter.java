package com.example.log_up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemWaitingManageAdapter extends RecyclerView.Adapter<ItemWaitingManageAdapter.ViewHolder> {

    private Context context;
    private List<ProductObject> listItem;

    public ItemWaitingManageAdapter(Context context) {
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

        View view = inflater.inflate(R.layout.item_manage_wait, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductObject item = listItem.get(position);
        holder.name.setText(item.getTenSanPham());
        holder.price.setText(item.getGiaSanPham());
        Glide.with(context).load(item.getImageUrl()).into(holder.image);
        holder.tittle.setText(item.getDanhmucSanPham());
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

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, tittle;
        private ImageView image, imageBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameStoryWait);
            price = itemView.findViewById(R.id.priceStoryWait);
            image = itemView.findViewById(R.id.imageWait);
            imageBtn = itemView.findViewById(R.id.btnMenuWait);
            tittle = itemView.findViewById(R.id.tittleStoryWait);

        }
    }
}
