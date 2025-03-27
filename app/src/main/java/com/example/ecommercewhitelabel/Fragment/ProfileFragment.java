package com.example.ecommercewhitelabel.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommercewhitelabel.Activities.LoginActivity;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {
    ImageView editNameBtn,editPersonalBtn;
    TextView userFullNameTxt1,userEmailTxt1,userFullNameTxt2,userEmailTxt2;
    String fullNameStr,emailStr;
    SessionManager sessionManager;
    Dialog progressBarDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getContext());

        editNameBtn = view.findViewById(R.id.editNameBtn);
        editPersonalBtn = view.findViewById(R.id.editNameBtn2);

        userFullNameTxt1 = view.findViewById(R.id.userNameTxt);
        userEmailTxt1 = view.findViewById(R.id.userEmailTxt);
        userFullNameTxt2 = view.findViewById(R.id.fullNameDisplayTxt);
        userEmailTxt2 = view.findViewById(R.id.emailDisplayTxt);

        fullNameStr = sessionManager.getUserData().get("fullName");
        emailStr = sessionManager.getUserData().get("email");

        userFullNameTxt1.setText(fullNameStr);
        userFullNameTxt2.setText(fullNameStr);
        userEmailTxt1.setText(emailStr);
        userEmailTxt2.setText(emailStr);

        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });
        editPersonalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });
        return view;
    }
    Dialog drawerDialog;
    ImageView crossBtn;
    Button saveBtn;
    TextInputLayout fullNameLayout, emailNameLayout;
    EditText fullNameEditTxt, emailEditText;
    @SuppressLint("ResourceAsColor")
    public void openEditDialog() {
        drawerDialog = new Dialog(ProfileFragment.this.getContext());
        drawerDialog.setContentView(R.layout.profile_edit_dialog);
        drawerDialog.setCancelable(true);

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);

        saveBtn = drawerDialog.findViewById(R.id.saveBtn);
        fullNameLayout = drawerDialog.findViewById(R.id.firstNameLayout);
        emailNameLayout = drawerDialog.findViewById(R.id.emailNameLayout);
//        phoneLayout = drawerDialog.findViewById(R.id.phoneLayout);
        fullNameEditTxt = drawerDialog.findViewById(R.id.firstNameEditText);
        fullNameEditTxt.setText(fullNameStr);
        emailEditText = drawerDialog.findViewById(R.id.emailEditText);
        emailEditText.setText(emailStr);
//        phoneEditText = drawerDialog.findViewById(R.id.phoneEditText);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDialog = new Dialog(getContext());
                progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressBarDialog.setContentView(R.layout.progress_bar_dialog);
                progressBarDialog.setCancelable(false);
                progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
                progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
                progressBarDialog.show();
                checkValidation();
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
            }
        });

        fullNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    fullNameLayout.setError(null);
                }else {
                    fullNameLayout.setError("First Name Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    emailNameLayout.setError(null);
                }else {
                    emailNameLayout.setError("Email is Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        phoneEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!charSequence.equals("")){
//                    phoneLayout.setError(null);
//                }else {
//                    phoneLayout.setError("Phone is Required");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        drawerDialog.show();
        drawerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        drawerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayRight;
        drawerDialog.getWindow().setGravity(Gravity.TOP);
        drawerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }

    private void checkValidation() {
        if (fullNameEditTxt.getText().toString().isEmpty()) {
            fullNameLayout.setError("Please enter your first name");
            return;
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailNameLayout.setError("Please enter your email");
            return;
        }
        updateUser();
        drawerDialog.dismiss();
    }

    private void updateUser() {

    }
}
