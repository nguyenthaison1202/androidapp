package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class LogIn extends AppCompatActivity {
    private TextView tvForgetPwd, tvDangky;
    private Button btnSignIn;
    private EditText edPhone, edPass;
    private ImageView imgMail;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        tvForgetPwd = findViewById(R.id.tvForgetPwd);
        tvDangky = findViewById(R.id.tvDangKy);
        edPhone = findViewById(R.id.ed_Phone);
        edPass = findViewById(R.id.ed_password);
        btnSignIn = findViewById(R.id.btnDangnhap);
        imgMail = findViewById(R.id.imgMailLogIn);
        tvDangky.setText(HtmlCompat.fromHtml("<a href=\\\"#\\\">Đăng ký</a>",HtmlCompat.FROM_HTML_MODE_LEGACY));
        tvForgetPwd.setText(HtmlCompat.fromHtml("<a href=\\\"#\\\">Bạn quên mật khẩu?</a>",HtmlCompat.FROM_HTML_MODE_LEGACY));
        tvDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, LogUp.class));
            }
        });
        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,GoogleAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("idTokenLogIn", "LogIn");
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+84"+edPhone.getText().toString().trim();
                String password = edPass.getText().toString();

                if(phone.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LogIn.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone))
                            {
                                final String getPassword = snapshot.child(phone).child("password").getValue(String.class);
                                final String getVerify= snapshot.child(phone).child("verify").getValue(String.class);
                                byte[] decodePass = Base64.getDecoder().decode(getPassword.trim());
                                String decPassword = decryptString(getPassword);
//                                try {
//                                    decPassword = new String(decodePass, "UTF-8");
//                                }catch(UnsupportedEncodingException ex)
//                                {
//                                    ex.printStackTrace();
//                                }

                                if(decPassword.equals(password) && getVerify.equals("1"))
                                {
                                    Toast.makeText(LogIn.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                                else{
                                    Toast.makeText(LogIn.this, "Bạn nhập sai thông tin", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(LogIn.this, "Bạn nhập sai thông tin", Toast.LENGTH_SHORT).show();
                            }

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