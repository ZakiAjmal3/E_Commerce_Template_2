package com.example.ecommercewhitelabel.Activities;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ecommercewhitelabel.R;

public class SignUpActivity extends AppCompatActivity {
    EditText firstNameEditTxt,lastNameEditTxt,emailEditTxt, passwordEditTxt;
    Button signUpBtn;
    TextView loginInTxt;
    RelativeLayout continueWithGoogleRLBTN;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.black));

        firstNameEditTxt = findViewById(R.id.firstNameEditTxt);
        lastNameEditTxt = findViewById(R.id.lastNameEditTxt);
        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginInTxt = findViewById(R.id.loginTxt);
        continueWithGoogleRLBTN = findViewById(R.id.continueWithGoogleRLBTN);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
        continueWithGoogleRLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
        loginInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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
    private void signUpUser() {
        if (firstNameEditTxt.getText().toString().isEmpty()) {
            firstNameEditTxt.setError("Email is required");
            return;
        }
        if (lastNameEditTxt.getText().toString().isEmpty()) {
            lastNameEditTxt.setError("Email is required");
            return;
        }
        if (emailEditTxt.getText().toString().isEmpty()) {
            emailEditTxt.setError("Email is required");
            return;
        }
        if (passwordEditTxt.getText().toString().isEmpty()) {
            passwordEditTxt.setError("Password is required");
            return;
        }
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
        startActivity(intent);
        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}