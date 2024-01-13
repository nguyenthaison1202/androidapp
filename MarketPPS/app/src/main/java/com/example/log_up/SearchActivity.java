package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ProductObject> productObjects;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.rcvSearch);
        productObjects = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        searchAdapter = new SearchAdapter(this);
        searchAdapter.setData(productObjects);
        recyclerView.setAdapter(searchAdapter);
        getValue();
    }

    private void getValue()
    {
        Intent getIntentSearch = getIntent();
        String text_search = getIntentSearch.getStringExtra("search_key");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ProductObject> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String verify = dataSnapshot.child("verify").getValue(String.class);
                    String tenSanPham = dataSnapshot.child("tenSanPham").getValue(String.class);
                    if (verify != null) {
                        if (verify.equals("1") && tenSanPham.toLowerCase().contains(text_search.toLowerCase())) {
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

                            productObjects.add(productObject);
                        }
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}