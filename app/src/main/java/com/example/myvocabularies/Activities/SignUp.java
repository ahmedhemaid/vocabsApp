package com.example.myvocabularies.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myvocabularies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    EditText email,password;
    String sEmail,sPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        sEmail=getIntent().getStringExtra("email");
        sPassword=getIntent().getStringExtra("password");
        email=findViewById(R.id.ed_email1);
        password=findViewById(R.id.ed_password1);
        email.setText(sEmail);
        password.setText(sPassword);
    }

    public void onclickSignup1(View view) {
        ( mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Sign up Successfully", Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, task.getException().getLocalizedMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
