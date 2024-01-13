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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class waitStory extends Fragment {
    private RecyclerView recyclerView;
    private ItemWaitingManageAdapter waitingManageAdapter;
    private ArrayList<ProductObject> listItem;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait_manage, container, false);
        recyclerView = view.findViewById(R.id.recycleViewWait);
        listItem = new ArrayList<>();
        waitingManageAdapter = new ItemWaitingManageAdapter(getContext());
        waitingManageAdapter.setData(listItem);
        layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(waitingManageAdapter);
        recyclerView.setLayoutManager(layoutManager);
        initData();
        return view;
    }
    private void initData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MainActivity activity = (MainActivity) getActivity();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String verify = dataSnapshot.child("verify").getValue(String.class);
                    String numberPhone = dataSnapshot.child("sodienthoaiUser").getValue(String.class);
                    if (verify != null) {
                        if (verify.equals("0") && MainActivity.getPhone.equalsIgnoreCase(numberPhone)) {
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
                            productObject.setCheckHeart(dataSnapshot.child("checkHeart").getValue(String.class));
                            listItem.add(productObject);
                        }
                    }
                }
                waitingManageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}