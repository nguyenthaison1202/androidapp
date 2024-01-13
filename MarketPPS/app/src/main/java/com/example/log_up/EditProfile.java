package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EditProfile extends AppCompatActivity {
    private TextView tv_Name;
    private TextInputEditText txt_input_fullname, txt_input_phone, txt_input_old_pass, txt_input_new_pass, txt_input_confirm_pass;
    private Button btnConfirm;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tv_Name = findViewById(R.id.tv_nameUser);
        txt_input_fullname = findViewById(R.id.full_name);
        txt_input_phone = findViewById(R.id.phone);
        txt_input_old_pass = findViewById(R.id.old_password);
        txt_input_new_pass = findViewById(R.id.new_password);
        txt_input_confirm_pass = findViewById(R.id.confirm_password);
        btnConfirm = findViewById(R.id.btn_confirm);
        getData();


    }
    private void getData()
    {
        Intent intent = getIntent();
        tv_Name.setText(intent.getStringExtra("old_nameUser"));
        txt_input_fullname.setText(intent.getStringExtra("old_nameUser"));
        txt_input_phone.setText(intent.getStringExtra("old_phoneUser").replace("+84",""));
        String oldPass = intent.getStringExtra("old_passUser");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(txt_input_old_pass.getText().toString() == null && txt_input_fullname.getText().toString() != null)
                {
                    String oldPhoneUser1 = intent.getStringExtra("old_phoneUser");
                    String newName1 = txt_input_fullname.getText().toString().trim();
                    databaseReference.child("users").child(oldPhoneUser1).child("fullname").setValue(newName1);
                }
                else if(!decryptString(oldPass).equals(txt_input_old_pass.getText().toString()))
                {
                    Toast.makeText(EditProfile.this, "Bạn nhập sai mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else if(txt_input_new_pass.getText().toString().length() < 6)
                {
                    Toast.makeText(EditProfile.this, "Mật khẩu bạn nhập quá ngắn", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_input_new_pass.getText().toString().equals(txt_input_confirm_pass.getText().toString()))
                {
                    Toast.makeText(EditProfile.this, "Mật khẩu bạn nhập không trùng khớp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String oldPhoneUser = intent.getStringExtra("old_phoneUser");
                            String newName = txt_input_fullname.getText().toString().trim();
                            String newPass = encryptString(txt_input_confirm_pass.getText().toString());
                            databaseReference.child("users").child(oldPhoneUser).child("fullname").setValue(newName);
                            databaseReference.child("users").child(oldPhoneUser).child("password").setValue(newPass);
                            Intent backMoreFragment = new Intent(EditProfile.this, MainActivity.class);
                            backMoreFragment.putExtra("phone", oldPhoneUser);
                            startActivity(backMoreFragment);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encryptString(String password)
    {
        String res = "";
        try {
            byte[] key = "123456789ABCDEFGHI".getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher  = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] input = password.getBytes("UTF-8");
            byte[] cipherTxt = cipher.doFinal(input);

            res = Base64.getEncoder().encodeToString(cipherTxt);

        }catch(NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchPaddingException ex)
        {
            ex.printStackTrace();
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        catch(BadPaddingException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalBlockSizeException ex)
        {
            ex.printStackTrace();
        }
        catch(InvalidKeyException ex)
        {
            ex.printStackTrace();
        }
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String decryptString(String password)
    {
        String res = "";
        try {
            byte[] key = "123456789ABCDEFGHI".getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher  = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] input = password.getBytes("UTF-8");
            byte[] cipherTxt = cipher.doFinal(Base64.getDecoder().decode(input));

            res = new String(cipherTxt);

        }catch(NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchPaddingException ex)
        {
            ex.printStackTrace();
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        catch(BadPaddingException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalBlockSizeException ex)
        {
            ex.printStackTrace();
        }
        catch(InvalidKeyException ex)
        {
            ex.printStackTrace();
        }
        return res;
    }
}