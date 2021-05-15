package com.example.royal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button login,signup;
    private TextView getlink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.btnLogin);
        signup = (Button) findViewById(R.id.btnRegister);
        getlink = (TextView) findViewById(R.id.txtAbout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh1 = new Intent(getApplication(),Login.class);
                startActivity(mh1);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(getApplication(),SignUp.class);
                startActivity(mh2);
            }
        });
        getlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlink.setText("http://www.google.com");
                Linkify.addLinks(getlink,Linkify.WEB_URLS);
            }
        });
    }
}