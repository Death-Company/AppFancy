package com.example.royal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.FirebaseApp;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
//import com.google.firebase.appcheck.FirebaseAppCheck;
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    private Button verify,sendotp,resend;
    private EditText phoneNumber,otpCode;
    String userPhoneNum,verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public String removeLeadingZeroes(String str) {
        String strPattern = "^0+(?!$)";
        str = str.replaceFirst(strPattern, "");
        return str;
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        verify =  findViewById(R.id.btnVerify);
        sendotp =  findViewById(R.id.btnSendOTP);
        resend =  findViewById(R.id.btnResendOTP);
        resend.setEnabled(false);
        phoneNumber =  findViewById(R.id.edtPhoneNumberVerify);
        fAuth = FirebaseAuth.getInstance();


        otpCode = findViewById(R.id.edtOTPcode);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().toString().trim().isEmpty())
                {
                    phoneNumber.setError("not null");
                    return;
                }
                userPhoneNum = "+84" + removeLeadingZeroes(phoneNumber.getText().toString().trim());
                verifyPhoneNumber(userPhoneNum);
                Toast.makeText(VerifyPhone.this,userPhoneNum,Toast.LENGTH_SHORT).show();

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpCode.getText().toString().trim().isEmpty())
                {
                    otpCode.setError("Enter OTP Code First");
                    return;
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpCode.getText().toString().trim());
                    authenticateUser(credential);
                }
            }
        });
       callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           @Override
           public void onVerificationCompleted(@NonNull @org.jetbrains.annotations.NotNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
           }

           @Override
           public void onVerificationFailed(@NonNull @org.jetbrains.annotations.NotNull FirebaseException e) {
                Toast.makeText(VerifyPhone.this,e.getMessage(),Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               super.onCodeSent(s, forceResendingToken);
               verificationId = s;
               token = forceResendingToken;
               phoneNumber.setVisibility(View.GONE);
               sendotp.setVisibility(View.GONE);
//               otpCode.setVisibility(View.VISIBLE);
               verify.setVisibility(View.VISIBLE);
               resend.setVisibility(View.VISIBLE);
               resend.setEnabled(false);
           }

           @Override
           public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
               super.onCodeAutoRetrievalTimeOut(s);
               resend.setEnabled(true);
           }
       };
    }

    public void verifyPhoneNumber(String phoneNum){
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                    .setActivity(this)
                    .setPhoneNumber(phoneNum)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void authenticateUser(PhoneAuthCredential credential){
        String first = getIntent().getExtras().getString("first");
        String last = getIntent().getExtras().getString("last");
        String email = getIntent().getExtras().getString("email");
        String pass = getIntent().getExtras().getString("pass");
        String phone = phoneNumber.getText().toString().trim();
            fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User usercreate = new User(first,last,email,pass,phone);
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(phone)
                            .setValue(usercreate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(VerifyPhone.this,"User has been created !",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(VerifyPhone.this,"Faild to Register !",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(VerifyPhone.this,"Success",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(VerifyPhone.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

    }

}