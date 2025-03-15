package com.example.ecommercewhitelabel.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ecommercewhitelabel.R;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);

//        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Animation logo_object = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_activity_logo_rotation);
        logo.startAnimation(logo_object);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();  // Finish the MainActivity to prevent back navigation
            }
        }, 1600);  // Match the duration of the logo animation
    }
}