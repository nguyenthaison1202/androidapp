package com.example.log_up;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillBuyList extends AppCompatActivity {
    private RecyclerView rcv_bill;
    private AdapterBuyBill adapter;
    private ArrayList<ProductObject> items,mItems;
    ArrayList<ProductObject> arrayList = new ArrayList<>();
    String numberPhone = "0";
    private DatabaseReference databaseReference,reference;
    private String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_buy_list);

        rcv_bill = findViewById(R.id.rcv_bill);
        adapter = new AdapterBuyBill(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_bill.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_bill.addItemDecoration(itemDecoration);
        mItems= new ArrayList<>();
        items=new ArrayList<>();
        adapter.setData(mItems);
        rcv_bill.setAdapter(adapter);
        Intent getItem = getIntent();
        if(getItem.getStringExtra("keyProduct") == null)
        {
            getItems();
        }
        else
        {
            setItems();
            getItems();
        }

    }
    private void setItems()
    {
        reference = FirebaseDatabase.getInstance().getReference().child("buy_bill");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = getIntent();
                String idProRemove = intent.getStringExtra("keyProduct");
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    String idCheck = dataSnapshot.getKey();
                    String numberPhone  = dataSnapshot.child("sodienthoaiUser").getValue(String.class);
                    if(idProRemove.equals(idCheck) && MainActivity.getPhone.equals(numberPhone))
                    {
                        String id = "1";
                        String imgURL = null;
                        while(snapshot.hasChild(id))
                        {
                            int next_position = Integer.parseInt(id) + 1;
                            id = ""+next_position;
                        }
                        for(DataSnapshot getImage : dataSnapshot.child("imageUrl").getChildren()){
                            imgURL = getImage.child("link1").getValue().toString();
                        }

                        reference.child(id).child("tenSanPham").setValue(dataSnapshot.child("tenSanPham").getValue(String.class));
                        reference.child(id).child("danhmucSanPham").setValue(dataSnapshot.child("danhmucSanPham").getValue(String.class));
                        reference.child(id).child("giaSanPham").setValue(dataSnapshot.child("giaSanPham").getValue(String.class));
                        reference.child(id).child("diachiSanPham").setValue(dataSnapshot.child("diachiSanPham").getValue(String.class));
                        reference.child(id).child("sodienthoaiUser").setValue(dataSnapshot.child("sodienthoaiUser").getValue(String.class));
                        reference.child(id).child("motaSanPham").setValue(dataSnapshot.child("motaSanPham").getValue(String.class));
                        reference.child(id).child("verify").setValue(dataSnapshot.child("verify").getValue(String.class));
                        reference.child(id).child("imageUrl").setValue(imgURL);
                    }
                }
                DatabaseReference removeItem = FirebaseDatabase.getInstance().getReference().child("product").child(idProRemove);
                removeItem.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getItems()
    {
        reference = FirebaseDatabase.getInstance().getReference().child("buy_bill");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String numberPhone = dataSnapshot.child("sodienthoaiUser").getValue(String.class);
                    if(MainActivity.getPhone.equalsIgnoreCase(numberPhone)){

                        ProductObject productObject = new ProductObject();

                        productObject.setId(Integer.parseInt(dataSnapshot.getKey()));
                        productObject.setTenSanPham(dataSnapshot.child("tenSanPham").getValue(String.class));
                        productObject.setDanhmucSanPham(dataSnapshot.child("danhmucSanPham").getValue(String.class));
                        productObject.setGiaSanPham(dataSnapshot.child("giaSanPham").getValue(String.class));
                        productObject.setDiachiSanPham(dataSnapshot.child("diachiSanPham").getValue(String.class));
                        productObject.setSodienthoaiUser(dataSnapshot.child("sodienthoaiUser").getValue(String.class));
                        productObject.setMotaSanPham(dataSnapshot.child("motaSanPham").getValue(String.class));
                        productObject.setVerify(dataSnapshot.child("verify").getValue(String.class));
                        productObject.setImageUrl(dataSnapshot.child("imageUrl").getValue(String.class));
                        mItems.add(productObject);


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
