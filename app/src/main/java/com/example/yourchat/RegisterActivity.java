package com.example.yourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private final static String ROOT_USER_DERRICTORY = "Users";
    private final static String PROFILE_DERRICTORY = "Profile";

    private Button registerButton;
    private Button toLoginButton;
    private EditText loginText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private EditText emailText;
    private EditText nameText;
    private EditText familyNameText;

    private String login = null;
    private String password = null;
    private String confirmPassword = null;
    private String email = null;
    private String name = null;
    private String familyName = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeFields();
        setViewListeners();

    }

    private void initializeFields(){
        registerButton = findViewById(R.id.loginButton);
        toLoginButton = findViewById(R.id.toLoginButton);
        loginText = findViewById(R.id.reg_login);
        passwordText = findViewById(R.id.reg_password);
        confirmPasswordText = findViewById(R.id.reg_confirm_password);
        emailText = findViewById(R.id.reg_email);
        nameText = findViewById(R.id.reg_name);
        familyNameText = findViewById(R.id.reg_family_name);
    }


    private void setViewListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLogin();
            }
        });
    }

    private void launchLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void submit() {

        login = loginText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();
        email = emailText.getText().toString();
        name = nameText.getText().toString();
        familyName = familyNameText.getText().toString();

        if(validate()){
            register();
        }
        else{
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        FunctionsHelper.showToast(getApplicationContext(), "Ошибка в ведённых данных!");
    }

    private void register() {
        FirebaseHelper.AUTH.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DocumentReference documentReference = FirebaseHelper.FIRESTORE.collection("Users").document(task.getResult().getUser().getUid());
                        Map<String, Object> data = new HashMap<>();
                        data.put(FirebaseHelper.LOGIN_KEY, login);
                        data.put(FirebaseHelper.EMAIL_KEY, email);
                        data.put(FirebaseHelper.PASSWORD_KEY, password);
                        data.put(FirebaseHelper.FULLNAME_KEY, name + " " + familyName);
                        data.put(FirebaseHelper.IMAGESOURCE_KEY, "");
                        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FunctionsHelper.showToast(getApplicationContext(), "Успешная регистрация!");
                                launchLobby();
                            }
                        });
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

    private boolean validate() {
        return !email.isEmpty()
                && !password.isEmpty()
                && !confirmPassword.isEmpty()
                && !name.isEmpty()
                && !familyName.isEmpty()
                && !login.isEmpty()
                && password.equals(confirmPassword)
                && isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        String expressing = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expressing, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
