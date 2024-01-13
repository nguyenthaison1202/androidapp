package com.example.log_up;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoadFragment extends Fragment {
    private TextInputLayout edt_category, edt_nameProduct, edt_titleProduct, edt_desProduct,edt_addressProduct;
    private TextInputEditText  edt_priceProduct;
    private AlertDialog dialog;
    private RecyclerView rcvPhoto;
    private PhotoAdapter photoAdapter;
    private AlertDialog.Builder builder;
    private CircleImageView upImage;
    private Uri imageUri;
    private StorageReference storageReference;
    private int count = 0;
    private CheckBox checkBox;
    private ArrayList<Uri> uri = new ArrayList<>();
    private String[] room={"Xe cộ","Đồ điện tử","Đồ gia dụng","Thời trang","Dụng cụ thể thao"};
    private String result="";
    private Button post;
    private static final int Read_Permission =101;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com");
    ActivityResultLauncher<Intent> activityResultLauncher;
    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        // Inflate the layout for this fragment
        edt_category = view.findViewById(R.id.edt_category);
        edt_nameProduct = view.findViewById(R.id.edt_nameProduct);
        edt_titleProduct = view.findViewById(R.id.edt_titleProduct);
        edt_desProduct = view.findViewById(R.id.edt_desProduct);
        edt_priceProduct = view.findViewById(R.id.edt_priceProduct);
        edt_addressProduct = view.findViewById(R.id.edt_addressProduct);
        edt_priceProduct.setRawInputType(Configuration.KEYBOARD_12KEY);
        edt_priceProduct.addTextChangedListener(new TextWatcher() {
            DecimalFormat dec = new DecimalFormat("0");
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().equals("₫"))
                {
                    current = "";
                    edt_priceProduct.setText("");
                    edt_priceProduct.addTextChangedListener(this);
                }
                else if(!charSequence.toString().equals(current)){
                    edt_priceProduct.removeTextChangedListener(this);
                    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vn", "VN"));
                    nf.setGroupingUsed(true);
                    String cleanString = charSequence.toString().replaceAll("[, ₫]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = nf.format(parsed);
                    String newEditText = formatted;
                    current = formatted;
                    edt_priceProduct.setText(formatted.replace("₫","")+" ₫");
                    edt_priceProduct.setSelection(formatted.length());
                    edt_priceProduct.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rcvPhoto = view.findViewById(R.id.rcv_imgUpload);
        upImage = view.findViewById(R.id.upload_img);
        builder = new AlertDialog.Builder(getContext());
        photoAdapter = new PhotoAdapter(getContext());
        post = view.findViewById(R.id.post);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setFocusable(false);
        rcvPhoto.setAdapter(photoAdapter);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        final String numberPhoneUser = mainActivity.getData();
                        if (result.getResultCode()==RESULT_OK && result.getData()!=null){
                            if (result.getData().getClipData()!=null){
                                int countOfImage = result.getData().getClipData().getItemCount();
                                for (int i =0; i< countOfImage;i++){
                                    if (uri.size()<11){
                                        imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageUri);
                                        uploadImg(numberPhoneUser);
                                    }
                                    else {
                                        Toast.makeText(getContext(), "NOT ALLOW to pick more than 11 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            else
                            {
                                if (uri.size()<11){
                                    imageUri = result.getData().getData();
                                    uri.add(imageUri);
                                    uploadImg(numberPhoneUser);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "NOT ALLOW to pick more than 11 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            photoAdapter.setData(uri);
                            photoAdapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getContext(),"You haven't pick ant Image",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setTitle("Chọn danh mục sản phẩm");
        edt_category.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setSingleChoiceItems(room, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result = room[which];
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_category.getEditText().setText(result);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                activityResultLauncher.launch(intent);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                final String nameProduct = edt_nameProduct.getEditText().getText().toString().trim();
                final String tittleProduct = edt_titleProduct.getEditText().getText().toString().trim();
                final String descProduct = edt_desProduct.getEditText().getText().toString().trim();
                final String priceProduct = edt_priceProduct.getText().toString().trim();
                final String danhmucProduct = edt_category.getEditText().getText().toString().trim();
                final String diachiProduct = edt_addressProduct.getEditText().getText().toString().trim();
                final String numberPhoneUser = mainActivity.getData();

                databaseReference.child("product").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = "1";
                        while(snapshot.hasChild(id))
                        {
                            int next_position = Integer.parseInt(id) + 1;
                            id = ""+next_position;
                        }
                        String getDanhMuc = edt_category.getEditText().getText().toString();
                        String getName = edt_nameProduct.getEditText().getText().toString();
                        String getPrice = edt_priceProduct.getText().toString();
                        String getAddress = edt_addressProduct.getEditText().getText().toString();

                        if(getDanhMuc.equals("") || getName.equals("") || getPrice.equals("") || getAddress.equals("") || uri.size() <= 0)
                        {
                            Toast.makeText(getContext(), "Nhap lai di ba", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            databaseReference.child("product").child(id).child("tenSanPham").setValue(nameProduct);
                            databaseReference.child("product").child(id).child("danhmucSanPham").setValue(danhmucProduct);
                            databaseReference.child("product").child(id).child("motaSanPham").setValue(descProduct);
                            databaseReference.child("product").child(id).child("giaSanPham").setValue(priceProduct);
                            databaseReference.child("product").child(id).child("diachiSanPham").setValue(diachiProduct);
                            databaseReference.child("product").child(id).child("sodienthoaiUser").setValue(numberPhoneUser);
                            databaseReference.child("product").child(id).child("verify").setValue("0");
                            databaseReference.child("product").child(id).child("imageUrl").push().setValue(hashMap);
                            Toast.makeText(getContext(), "Bạn đăng tin thành công. Chờ xét duyệt", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("phone", numberPhoneUser);


                            startActivity(intent);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        checkBox = view.findViewById(R.id.checked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )

                {
                    post.setEnabled(true);
                }
                else
                {
                    post.setEnabled(false);
                }
            }
        });
        return view;
    }
    private void uploadImg(String number_identification){
        byte[] bytes = new byte[0];
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri );
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        }catch(IOException e2){
            e2.printStackTrace();
        }

        final String randomName = UUID.randomUUID().toString();

        storageReference = FirebaseStorage.getInstance().getReference().child(number_identification+"/"+randomName);

        storageReference.putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uriItem) {
                                count++;
                                for(int i = 0; i< uri.size(); i++)
                                {
                                    String url = String.valueOf(uri.get(i));
                                    hashMap.put("link"+(i+1), url);
                                }

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}