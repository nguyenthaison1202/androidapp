package com.example.log_up;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleAuthActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private String getPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google Sign In...");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        createRequest();
        signIn();
    }
    private void createRequest()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn()
    {
        Intent signInIntent =  mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch(ApiException ex)
            {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(firebaseCredential) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Intent getToken = getIntent();
                    String token = getToken.getStringExtra("idToken");
                    String tokenLogIn = getToken.getStringExtra("idTokenLogIn");
                    if(token !=null)
                    {
                        if(token.equalsIgnoreCase("LogUp"))
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,account.getEmail());
                        }
                    }
                    if(tokenLogIn !=null)
                    {
                        if(tokenLogIn.equalsIgnoreCase("LogIn"))
                        {

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String email = dataSnapshot.child("email").getValue(String.class);
                                        getPhone = dataSnapshot.getKey();
                                        if(email != null)
                                        {
                                            if(email.equalsIgnoreCase(account.getEmail()))
                                            {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                gotoMain(user);
                                            }
                                            else
                                            {
                                                Intent gotoLogIn = new Intent(GoogleAuthActivity.this, LogIn.class);
                                                Toast.makeText(GoogleAuthActivity.this, "Email này chưa được đăng ký", Toast.LENGTH_SHORT).show();
                                                startActivity(gotoLogIn);
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }


                } else {
                    // If sign in fails, display a message to the user
                    progressDialog.dismiss();
                    Toast.makeText(GoogleAuthActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user, String email) {
        Intent intent = new Intent(GoogleAuthActivity.this, InputPhone.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("email", email);
        startActivity(intent);
    }
    private void gotoMain(FirebaseUser user) {
        Intent intent = new Intent(GoogleAuthActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("phone", getPhone);
        startActivity(intent);
    }

}