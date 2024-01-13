package com.example.log_up;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    private RecyclerView rcv_Cart;
    private AdapterCart adapter;
    private ArrayList<ProductObject> items;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rcv_Cart = view.findViewById(R.id.rcv_cart);
        adapter = new AdapterCart(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_Cart.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcv_Cart.addItemDecoration(itemDecoration);
        items= new ArrayList<>();
        adapter.setData(items);
        rcv_Cart.setAdapter(adapter);
        getItems();
        return view;
    }

    private void getItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MainActivity activity = (MainActivity) getActivity();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String verify = dataSnapshot.child("verify").getValue(String.class);
                    String numberPhone = dataSnapshot.child("sodienthoaiUser").getValue(String.class);
                    if (verify != null) {
                        if (verify.equals("1") && !MainActivity.getPhone.equals(numberPhone)) {
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
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}