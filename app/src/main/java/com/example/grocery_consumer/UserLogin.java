package com.example.grocery_consumer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserLogin extends AppCompatActivity {
    private static final String TAG = "Login";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private TextView signUpTv;
    private EditText phoneET;
    private Button registerBT;
    private String uPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);
        signUpTv = findViewById(R.id.signupTv);
        phoneET = findViewById(R.id.phone);
        registerBT = findViewById(R.id.button8);
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
            }
        });
        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uPhone = "+91"+phoneET.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(),OTP.class);
                intent.putExtra("phoneNo", uPhone);
                startActivity(intent);
            }
        });


    }

}