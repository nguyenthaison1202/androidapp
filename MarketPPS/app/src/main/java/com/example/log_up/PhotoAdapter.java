package com.example.log_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHodler>{
    private Context mContext;
    private ArrayList<Uri> mListPhoto;

    public PhotoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public  void setData(ArrayList<Uri> list){
        this.mListPhoto=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PhotoViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_in_fragment_load,parent,false);
        return new PhotoViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHodler holder, int position) {
        Uri uri = mListPhoto.get(position);
        if (uri == null) {
            return;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),uri);
            if (bitmap!=null){
                holder.img.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhoto!=null)
        {
            return mListPhoto.size();
        }
        return 0;
    }

    public class PhotoViewHodler extends RecyclerView.ViewHolder{
        ImageView img;
        public PhotoViewHodler(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_photo);
        }
    }
}
