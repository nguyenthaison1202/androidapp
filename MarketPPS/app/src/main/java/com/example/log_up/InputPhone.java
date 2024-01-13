package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
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

import java.util.concurrent.TimeUnit;

public class InputPhone extends AppCompatActivity {
    public static final String TAG = InputPhone.class.getName();
    private EditText edPhone, edName;
    private Button btnVerify;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone2);
        edName = findViewById(R.id.edInputName);
        edPhone = findViewById(R.id.edInputPhone);
        btnVerify = findViewById(R.id.confirm_phone_verify);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = "+84"+edPhone.getText().toString().trim();
                final String password = "userGoogle";
                final String name = edName.getText().toString().trim();
                final String verify = "0";

                if(phone.isEmpty()  || name.isEmpty())
                {
                    Toast.makeText(InputPhone.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <=6)
                {
                    Toast.makeText(InputPhone.this,"Mật khẩu bạn nhập quá ngắn",Toast.LENGTH_SHORT).show();

                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone))
                            {
                                Toast.makeText(InputPhone.this, "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                                edPhone.setText(null);
                            }
                            else
                            {
                                databaseReference.child("users").child(phone).child("fullname").setValue(name);
                                databaseReference.child("users").child(phone).child("password").setValue(password);
                                databaseReference.child("users").child(phone).child("email").setValue(email);
                                databaseReference.child("users").child(phone).child("verify").setValue(verify);
                                Toast.makeText(InputPhone.this, "Bạn đã đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InputPhone.this, Verify_PhoneNumber.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(InputPhone.this, ""+error, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(InputPhone.this, "Verify Failed", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(InputPhone.this, Verify_PhoneNumber.class);
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
                                Toast.makeText(InputPhone.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}