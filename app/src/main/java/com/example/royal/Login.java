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

public class Login extends AppCompatActivity {
    private EditText userLogin,passLogin;
    private Button buttonLogin;
    FirebaseAuth fAuth;

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
    private Boolean validatePhone(){
        String val = userLogin.getText().toString();
        String noWhiteSpace = "\\A\\w[4,20]\\z";
        if (val.isEmpty()){
            userLogin.setError("Not null");
            return false;
        }
        else {
            userLogin.setError(null);
            return true;
        }
    }
    private Boolean validatePass(){
        String val = passLogin.getText().toString();
        String passval = "^"+
                "(?=.*[a-zA-Z])"+
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$)"+
                ".{4,}"+
                "$";
        if (val.isEmpty()){
            passLogin.setError("Password cannot Empty");
            return false;
        } /*else if(!val.matches(passval)){
            passLogin.setError("Password is too weak");
            return false;
        }*/else{
            passLogin.setError(null);
            return true;
        }
    }

    public void loginUser(View view){
        if(!validatePhone() | !validatePass()){
            return;

        }
        else
        {
            isuser();
        }
    }

    private void isuser() {
        String UsernameEntered = userLogin.getText().toString().trim();
        String PasswordEntered = passLogin.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query userQuery = reference.orderByChild("phone").equalTo(UsernameEntered);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userLogin.setVisibility(View.GONE);
                if(snapshot.exists()){
                    userLogin.setVisibility(View.GONE);
                    String passDB = snapshot.child(UsernameEntered).child("pass").getValue(String.class);
                    if(passDB.equals(PasswordEntered)){

                        //String emailDB = snapshot.child(UsernameEntered).child("email").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),Profile.class);

                        startActivity(intent);
                    }
                    else {
                        passLogin.setError("Wronggg Password");
                        passLogin.requestFocus();
                    }
                }
                else {
                    userLogin.setError("User not exist");
                    userLogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Login.this,"Faild",Toast.LENGTH_LONG).show();
            }
        });
    }
}