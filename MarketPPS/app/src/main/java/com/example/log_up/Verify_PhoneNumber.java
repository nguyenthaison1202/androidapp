package com.example.log_up;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Verify_PhoneNumber extends AppCompatActivity {
    public static final String TAG = Verify_PhoneNumber.class.getName();
    private Button btnVerifyOTP;
    private String mPhoneNumber, mVerification;
    private EditText edOTP;
    private FirebaseAuth mAuth;
    private TextView tvResendOTP;
    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://detaicuoiki-7b040-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        getDataIntent();
        tvResendOTP = findViewById(R.id.tvOTPnew);
        edOTP = findViewById(R.id.edOTP);
        mAuth = FirebaseAuth.getInstance();

        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = edOTP.getText().toString();
                onClickSendOTP(OTP);
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendOTPAgain();
            }
        });
    }

    private void onClickSendOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, otp);
        signInWithPhoneAuthCredential(credential);
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("users").child(mPhoneNumber).child("verify").setValue("1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = new Intent(Verify_PhoneNumber.this, LogIn.class);
        startActivity(intent);
    }

    private void getDataIntent()
    {
        mPhoneNumber = getIntent().getStringExtra("phoneNumber");
        mVerification = getIntent().getStringExtra("verify_id");
    }
    private void onClickSendOTPAgain()
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mforceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Verify_PhoneNumber.this, "Verify Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verify_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verify_id, forceResendingToken);
                                mVerification = verify_id;
                                mforceResendingToken = forceResendingToken;

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                                Toast.makeText(Verify_PhoneNumber.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}