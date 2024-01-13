package com.example.log_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreFragment extends Fragment {
    private RelativeLayout setting_account;
    LinearLayout profile_User;
    private CardView logout,help, cardViewSale;
    TextView tvNameUser;
    private SharedPreferences sharedPreferences;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    private String getFullName;
    private String getPhoneUser;
    private String getPassUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_more, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        profile_User = view.findViewById(R.id.profile_user);
        tvNameUser = view.findViewById(R.id.tv_nameUser);
        setting_account = view.findViewById(R.id.setting_account);
        help = view.findViewById(R.id.cardViewHelp);
        cardViewSale = view.findViewById(R.id.cardViewSale);
        String getPhone = MainActivity.getPhone;
        getPhoneUser = getPhone;
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(getPhone)) {
                    final String getName = snapshot.child(getPhone).child("fullname").getValue(String.class);
                    getFullName = getName;
                    getPassUser = snapshot.child(getPhone).child("password").getValue(String.class);

                    tvNameUser.setText(getName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout = view.findViewById(R.id.cardViewLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogIn.class);
                startActivity(intent);
            }
        });
        setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingAccount();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(),HelpFragment.class);
                startActivity(intent);
            }
        });
        cardViewSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BillBuyList.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void SettingAccount()
    {
        Intent settingaccount = new Intent(getActivity(), EditProfile.class);
        settingaccount.putExtra("old_phoneUser", getPhoneUser);
        settingaccount.putExtra("old_nameUser", getFullName);
        settingaccount.putExtra("old_passUser", getPassUser);
        startActivity(settingaccount);

    }

}