package com.example.royal;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity  {
    private Button regis;
    private EditText fisrtnameR,lastnameR,emailR,passwordR;

    FirebaseDatabase fAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        regis = findViewById(R.id.btnCreate);
        fisrtnameR = findViewById(R.id.edtFirstname);
        lastnameR = findViewById(R.id.edtLastname);
        emailR = findViewById(R.id.edtEmail);
        passwordR = findViewById(R.id.edtPassCreate);

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailR.getText().toString().trim();
                String pass = passwordR.getText().toString().trim();
                String first = fisrtnameR.getText().toString().trim();
                String last = lastnameR.getText().toString().trim();


                if(TextUtils.isEmpty(first)){
                    emailR.setError("Firstname is Required");
                    return;
                }
                if(TextUtils.isEmpty(last)){
                    emailR.setError("Lastname is Required");
                    return;
                }
                /*if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailR.setError("Email is Required");
                    return;
                }*/
                if(TextUtils.isEmpty(pass)){
                    passwordR.setError("Password is Required");
                    return;
                }
                if(pass.length()<6){
                    passwordR.setError("Password must be >=6");
                    return;
                }
                Intent mh = new Intent(getApplicationContext(),VerifyPhone.class);
                mh.putExtra("first",first);
                mh.putExtra("last",last);
                mh.putExtra("email",email);
                mh.putExtra("pass",pass);
                startActivity(mh);
            }
        });


    }
}