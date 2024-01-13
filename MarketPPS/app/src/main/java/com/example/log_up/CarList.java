package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarList extends AppCompatActivity {
    private RecyclerView rcv_Car;
    private AdapterCar adapter;
    private ArrayList<ProductObject> items;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        rcv_Car = findViewById(R.id.rcv_car);
        adapter = new AdapterCar(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_Car.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_Car.addItemDecoration(itemDecoration);
        items= new ArrayList<>();
        adapter.setData(items);
        rcv_Car.setAdapter(adapter);
        getItems();

    }
    private void getItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String verify = dataSnapshot.child("verify").getValue(String.class);
                    String danhmuc = dataSnapshot.child("danhmucSanPham").getValue(String.class);

                    if(verify.equals("1") && danhmuc.equalsIgnoreCase("Xe cộ")){

                        ProductObject productObject = new ProductObject();
                        for(DataSnapshot getImage : dataSnapshot.child("imageUrl").getChildren()){
                            productObject.setImageUrl(getImage.child("link1").getValue().toString());
                        }
                        productObject.setId(Integer.parseInt(dataSnapshot.getKey()));
                        productObject.setTenSanPham(dataSnapshot.child("tenSanPham").getValue(String.class));
                        productObject.setDanhmucSanPham(dataSnapshot.child("danhmucSanPham").getValue(String.class));
                        productObject.setGiaSanPham(dataSnapshot.child("giaSanPham").getValue(String.class));
                        productObject.setDiachiSanPham(dataSnapshot.child("diachiSanPham").getValue(String.class));
                        productObject.setSodienthoaiUser(dataSnapshot.child("sodienthoaiUser").getValue(String.class));
                        productObject.setMotaSanPham(dataSnapshot.child("motaSanPham").getValue(String.class));
                        productObject.setVerify(dataSnapshot.child("verify").getValue(String.class));

                        items.add(productObject);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
