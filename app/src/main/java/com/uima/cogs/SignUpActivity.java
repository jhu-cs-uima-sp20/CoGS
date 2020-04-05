package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private TextView confirmPass;
    private Button signUpBtn;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.emailSignUpText);
        password = findViewById(R.id.passwordSignUp);
        confirmPass = findViewById(R.id.passwordConfirm);
        signUpBtn = findViewById(R.id.signUpBtn);
        auth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStr = password.getText().toString();
                String passwordConfirmStr = confirmPass.getText().toString();
                String emailStr = email.getText().toString();
                if(password.length() == 0){
                    Toast.makeText(SignUpActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordStr.equals(passwordConfirmStr)){
                    Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    createUser(emailStr, passwordStr);

                }


            }
        });


    }

    void createUser(String emailStr, String passwordStr){

        auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(SignUpActivity.this, "Sign-Up Succeeded", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
