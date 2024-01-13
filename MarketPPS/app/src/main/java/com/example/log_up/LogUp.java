package com.example.log_up;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class LogUp extends AppCompatActivity {
    public static final String TAG = LogUp.class.getName();
    private TextView tvSignUp,tvForward;
    private Button btnDangky;
    private EditText edPhone, edPassword, edName;
    private FirebaseAuth mAuth;
    DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com");
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ImageView imgFB, imgMail;
    private static final String TAGFB = "FacebookAuthentication";
    private AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_up);
        btnDangky = findViewById(R.id.btnDangky);
        edPhone = findViewById(R.id.ed_Phone);
        edName = findViewById(R.id.ed_Name);
        edPassword = findViewById(R.id.ed_password);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForward = findViewById(R.id.tvChuyenToiDangNhap);
        tvSignUp.setText(HtmlCompat.fromHtml("Bằng việc đăng kí, bạn đã đồng ý với <span style = \"color:#01b2ff\">Điều khoản sử dụng</span> của PPS Market",HtmlCompat.FROM_HTML_MODE_LEGACY));
        tvForward.setText(HtmlCompat.fromHtml("<a href=\\\"#\\\">Đăng nhập</a>",HtmlCompat.FROM_HTML_MODE_LEGACY));
        imgFB = findViewById(R.id.imgFb);
        imgMail = findViewById(R.id.imgMail);
        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogUp.this,FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogUp.this,GoogleAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("idToken", "LogUp");
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        tvForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogUp.this, LogIn.class);
                startActivity(intent);
            }
        });
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = "+84"+edPhone.getText().toString().trim();
                final String password = edPassword.getText().toString();
                final String name = edName.getText().toString().trim();
                final String verify = "0";

                if(phone.isEmpty() || password.isEmpty() || name.isEmpty())
                {
                    Toast.makeText(LogUp.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <=6)
                {
                    Toast.makeText(LogUp.this,"Mật khẩu bạn nhập quá ngắn",Toast.LENGTH_SHORT).show();

                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone))
                            {
                                Toast.makeText(LogUp.this, "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                                edPhone.setText(null);
                            }
                            else
                            {
                                databaseReference.child("users").child(phone).child("fullname").setValue(name);
                                databaseReference.child("users").child(phone).child("password").setValue(encryptString(password));
                                databaseReference.child("users").child(phone).child("verify").setValue(verify);
                                Toast.makeText(LogUp.this, "Bạn đã đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogUp.this, Verify_PhoneNumber.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LogUp.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    onClickVerifyNumber(phone);
                }
            }
        });

    }

    private void onClickVerifyNumber(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LogUp.this, "Verify Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verify_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verify_id, forceResendingToken);
                                gotoEnterOTPActivity(phone, verify_id);

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void gotoEnterOTPActivity(String phone, String verify_id) {
        Intent intent = new Intent(LogUp.this, Verify_PhoneNumber.class);
        intent.putExtra("phoneNumber", phone);
        intent.putExtra("verify_id", verify_id);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LogUp.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
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


}