package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class DetailProduct extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView  tvNameDetailProduct, tvPriceDetailProduct, tvDiachiDetailProduct, chitietProduct, tvLienHe;
    private PhotoSliderDetailProductAdapter photoAdapter;
    private List<PhotoSliderDetailProduct> list;
    private Button btnChat, btnCall;
    private ProductObject product;
    private Bundle bundle;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        product = (ProductObject) bundle.get("object_item");
        viewPager = findViewById(R.id.imageDetailProduct);
        circleIndicator = findViewById(R.id.circleIndicatorDetailProduct);
        tvNameDetailProduct = findViewById(R.id.tv_nameDetailProduct);
        tvPriceDetailProduct = findViewById(R.id.tv_priceDetailProduct);
        tvDiachiDetailProduct = findViewById(R.id.tv_diachiDetailProduct);
        chitietProduct = findViewById(R.id.chitietProduct);
        btnChat = findViewById(R.id.btnChat);
        btnCall = findViewById(R.id.btnCall);
        tvLienHe = findViewById(R.id.lienhe);
        list = getListPhoto();
        photoAdapter = new PhotoSliderDetailProductAdapter(DetailProduct.this, getListPhoto());
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        getData();



    }
    private List<PhotoSliderDetailProduct> getListPhoto()
    {
        List<PhotoSliderDetailProduct> list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("product");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String getProductId = ""+product.getId();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(getProductId)){
                        for(DataSnapshot getParentImage : dataSnapshot.child("imageUrl").getChildren()){
                            for(DataSnapshot getImage:getParentImage.getChildren()){
                                PhotoSliderDetailProduct photo = new PhotoSliderDetailProduct();
                                photo.setPhoto(getImage.getValue().toString());
                                list.add(photo);
                            }


                        }
                    }


                }

                photoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return list;
    }
    private void getData()
    {
        if(bundle == null)
        {
            return;
        }
        tvNameDetailProduct.setText(product.getTenSanPham());
        tvPriceDetailProduct.setText(product.getGiaSanPham());
        tvDiachiDetailProduct.setText(product.getDiachiSanPham());
        chitietProduct.setText(product.getMotaSanPham());
        tvLienHe.setText("Liên hệ: "+product.getSodienthoaiUser().replace("+84",""));
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!product.getSodienthoaiUser().equalsIgnoreCase(MainActivity.getPhone))
                {
                    Intent transferMessage = new Intent(DetailProduct.this, MessageActivity.class);
                    transferMessage.putExtra("idPhoneUser", product.getSodienthoaiUser());
                    transferMessage.putExtra("idProduct", product.getId());
                    startActivity(transferMessage);
                }
                else
                {
                    Toast.makeText(DetailProduct.this, "Bạn phải click vào icon chat", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ product.getSodienthoaiUser().replace("+84","")));//change the number
                startActivity(callIntent);
            }
        });
    }

}