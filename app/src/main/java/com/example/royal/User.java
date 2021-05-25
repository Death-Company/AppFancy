package com.example.royal;

import android.widget.EditText;

public class User {

    public String first, last,email, pass,phone;

    public User(){

    }
    public User(String first, String last, String email, String pass, String phone){
        this.first = first.trim();
        this.last = last.trim();
        this.email=email.trim();
        this.pass = pass.trim();
        this.phone =phone.trim();
    }
}
