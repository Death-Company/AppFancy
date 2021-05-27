package com.example.royal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {
    TextInputLayout fullName,email,phoneNo,password;
    TextView fullNameLabel,usernameLabel;

    //Global Vriable to hold user data inside this activity
    String _USERNAME, _NAME, _EMAIL, _PHONENO, _PASSWORD;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        reference = FirebaseDatabase.getInstance().getReference("users");

        //Hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.fullname_field);
        usernameLabel = findViewById(R.id.username_field);

        //ShowAllData
        showAllUserdata();
    }


    private void showAllUserdata() {

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("first");
        String user_name = intent.getStringExtra("last");
        String user_email = intent.getStringExtra("email");
        String user_phoneNo = intent.getStringExtra("phone");
        String user_password = intent.getStringExtra("password");

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);

    }

    public void update(View view){

        if(isNameChanged()  || isPasswordChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_LONG).show();

    }

    private boolean isPasswordChanged() {
        if(!_PASSWORD.equals(password.getEditText().getText().toString()))
        {
            reference.child(_USERNAME).child("pass").setValue(password.getEditText().getText().toString());
            _PASSWORD=password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }


    private boolean isNameChanged() {
        if(!_NAME.equals(fullName.getEditText().getText().toString())){
            reference.child(_USERNAME).child("last").setValue(fullName.getEditText().getText().toString());
            _NAME=fullName.getEditText().getText().toString();
            return true;
        }else {
            return false;
        }
    }
}