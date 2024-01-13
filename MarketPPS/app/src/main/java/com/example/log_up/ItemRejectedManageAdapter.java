package com.example.log_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ItemRejectedManageAdapter extends RecyclerView.Adapter<ItemRejectedManageAdapter.ViewHolder> {
    private Context context;
    private List<ProductObject> listItem;

    public ItemRejectedManageAdapter(Context context) {
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

        View view = inflater.inflate(R.layout.item_manage_reject, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductObject item = listItem.get(position);
//        Bitmap bitmap;
//        Drawable drawable;
//        drawable = ContextCompat.getDrawable(context, item.getImg());
//        bitmap = ((BitmapDrawable) drawable).getBitmap();
//        Bitmap newBitmap = convertImg(bitmap, 50);

        holder.name.setText(item.getTenSanPham());
        holder.price.setText(item.getGiaSanPham());
//        holder.image.setImageBitmap(newBitmap);
        Glide.with(context).load(item.getImageUrl()).into(holder.image);
        holder.imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.imageBtn);
                popupMenu.inflate(R.menu.option_menu_item_confirm);
                popupMenu.show();
            }
        });
    }

    private Bitmap convertImg(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price;
        private ImageView image, imageBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameStoryReject);
            price = itemView.findViewById(R.id.priceStoryReject);
            image = itemView.findViewById(R.id.imageReject);
            imageBtn = itemView.findViewById(R.id.btnMenuReject);
            image.setAlpha(80);
        }
    }

}
