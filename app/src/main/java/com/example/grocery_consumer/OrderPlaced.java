package com.example.grocery_consumer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrderPlaced extends AppCompatActivity {

    Button doneBTN;
    TextView timeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setTitle("");
        setContentView(R.layout.activity_order_placed);

        popup();
        Calendar calendar = Calendar.getInstance();
        timeTV = findViewById(R.id.time);
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        calendar.add(Calendar.MINUTE, 10);
        String newTime = dateFormat.format(calendar.getTime());
        timeTV.setText(newTime);
        doneBTN = findViewById(R.id.done);
        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShopList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
finish();
            }
        });

    }
    private void popup() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        getWindow().setAttributes(params);
    }
}