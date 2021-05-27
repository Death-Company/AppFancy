package com.example.royal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Login extends AppCompatActivity {
    private EditText userLogin,passLogin;
    private Button buttonLogin;
    FirebaseAuth fAuth;
    DatabaseReference post ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userLogin = findViewById(R.id.edtPhone);
        passLogin = findViewById(R.id.edtPass);
        fAuth = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.btnLoginAccount);
        String phone = userLogin.getText().toString().trim();
        String pass = userLogin.getText().toString().trim();
    }
    private Boolean validateUsername() {
        String val = userLogin.getText().toString();
        if (val.isEmpty()) {
            userLogin.setError("Field cannot be empty");
            return false;
        } else {
            userLogin.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = passLogin.getText().toString();
        if (val.isEmpty()) {
            passLogin.setError("Field cannot be empty");
            return false;
        } else {
            passLogin.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();

        }
    }
    private void isUser() {
        //progressBar.setVisibility(View.VISIBLE);

        final String userEnteredUsername = userLogin.getText().toString().trim();
        final String userEnteredPassword = passLogin.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredUsername);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(Login.this,"asdasdkasldkaj",Toast.LENGTH_LONG).show();
                if (dataSnapshot.exists()) {
                    userLogin.setError(null);
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("pass").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        userLogin.setError(null);
                        userLogin.setEnabled(false);
                        String firstnameFromDB = dataSnapshot.child(userEnteredUsername).child("first").getValue(String.class);
                        String lastnameFromDB = dataSnapshot.child(userEnteredUsername).child("last").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("name", firstnameFromDB);
                        intent.putExtra("username", lastnameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        //progressBar.setVisibility(View.GONE);
                        passLogin.setError("Wrong Password");
                        passLogin.requestFocus();
                    }
                } else {
                    //progressBar.setVisibility(View.GONE);
                    userLogin.setError("No such User exist");
                    userLogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}