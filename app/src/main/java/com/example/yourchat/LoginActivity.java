package com.example.yourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yourchat.Helpers.FunctionsHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    Button loginButton;
    Button toRegisterButton;
    EditText emailText;
    EditText passwordText;
    String email = null;
    String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_login);
        initializeFields();
        setViewListeners();
    }

    private void initializeFields(){
        loginButton = findViewById(R.id.loginButton);
        toRegisterButton = findViewById(R.id.toLoginButton);
        emailText = findViewById(R.id.reg_login);
        passwordText = findViewById(R.id.enter_auto_password);
    }

    private void setViewListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegister();
            }
        });
    }

    private void launchRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void submit() {
        loginButton.setEnabled(false);
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if(validate()){
            login();
        }
        else{
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        FunctionsHelper.showToast(getApplicationContext(), "Неверная почта или пароль");
        loginButton.setEnabled(true);
    }

    private void login() {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        launchLobby();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showErrorMessage();
                    }
                });
    }

    private void launchLobby() {
        FunctionsHelper.openActivity(getApplicationContext(), MainActivity.class);
    }

    private boolean validate(){
        return !email.isEmpty()
                && !password.isEmpty()
                && isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        String expressing = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expressing, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}