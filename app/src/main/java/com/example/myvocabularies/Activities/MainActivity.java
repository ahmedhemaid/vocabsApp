package com.example.myvocabularies.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myvocabularies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    TextView reset;
    private FirebaseAuth mAuth;
    EditText email,password;
    String sEmail,sPassword;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        reset=findViewById(R.id.tv_reset_password);
        reset.setPaintFlags(reset.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        email=findViewById(R.id.ed_email);
        password=findViewById(R.id.ed_password);
        sEmail=email.getText().toString();
        sPassword=password.getText().toString();
        //shared preferences
        preferences=getSharedPreferences("Prefs",MODE_PRIVATE);
        if (preferences.getBoolean("is_logged_in", false)) {
            // Go to home activity (user logged in)
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    public void onclickSignup(View view) {
        sEmail=email.getText().toString();
        sPassword=password.getText().toString();
        Intent intent=new Intent(MainActivity.this, SignUp.class);
        intent.putExtra("email",sEmail);
        intent.putExtra("password",sPassword);
        startActivity(intent);
    }

    public void onclickReset(View view) {
        sEmail=email.getText().toString();
        Intent intent=new Intent(MainActivity.this, ResetPassword.class);
        intent.putExtra("emailToReset",sEmail);
        startActivity(intent);
    }

    public void onclickSignin(View view) {
        (mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            preferences=getSharedPreferences("Prefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putBoolean("is_logged_in",true);
                            editor.apply();
                            Intent intent = new Intent(MainActivity.this , HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Log.e("Error",task.getException().toString());
                            Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    }

