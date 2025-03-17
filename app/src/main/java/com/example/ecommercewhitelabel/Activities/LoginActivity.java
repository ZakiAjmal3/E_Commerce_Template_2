package com.example.ecommercewhitelabel.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercewhitelabel.R;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditTxt, passwordEditTxt;
    Button loginBtn;
    TextView signUpTxt;
    RelativeLayout continueWithGoogleRLBTN;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTxt = findViewById(R.id.signUpTxt);
        continueWithGoogleRLBTN = findViewById(R.id.continueWithGoogleRLBTN);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
        continueWithGoogleRLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        passwordEditTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the user clicked on the drawableEnd (eye icon)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= passwordEditTxt.getWidth() - passwordEditTxt.getCompoundDrawables()[2].getBounds().width()) {
                        // Toggle password visibility when clicked on the eye icon
                        togglePasswordVisibility();
                        return true; // Consume the touch event
                    }
                }
                return false; // Don't consume other touch events
            }
        });
    }
    // Method to toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Set password to hidden
            passwordEditTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close, 0); // Change icon to "eye_open"
        } else {
            // Set password to visible
            passwordEditTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open, 0); // Change icon to "eye_closed"
        }

        // Move the cursor to the end of the EditText after changing input type
        passwordEditTxt.setSelection(passwordEditTxt.getText().length());

        // Toggle the state of visibility
        isPasswordVisible = !isPasswordVisible;
    }
    private void signInUser() {
        if (emailEditTxt.getText().toString().isEmpty()) {
            emailEditTxt.setError("Email is required");
            return;
        }
        if (passwordEditTxt.getText().toString().isEmpty()) {
            passwordEditTxt.setError("Password is required");
            return;
        }
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        startActivity(intent);
        Toast.makeText(LoginActivity.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}