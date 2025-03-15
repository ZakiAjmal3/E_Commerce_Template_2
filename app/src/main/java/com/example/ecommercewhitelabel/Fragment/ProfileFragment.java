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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommercewhitelabel.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {
    ImageView editNameBtn,editPersonalBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editNameBtn = view.findViewById(R.id.editNameBtn);
        editPersonalBtn = view.findViewById(R.id.editNameBtn2);

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
    TextInputLayout firstNameLayout, lastNameLayout, emailNameLayout, phoneLayout, ageLayout;
    EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, ageEditText;
    @SuppressLint("ResourceAsColor")
    public void openEditDialog() {
        drawerDialog = new Dialog(ProfileFragment.this.getContext());
        drawerDialog.setContentView(R.layout.profile_edit_dialog);
        drawerDialog.setCancelable(true);

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);

        saveBtn = drawerDialog.findViewById(R.id.saveBtn);
        firstNameLayout = drawerDialog.findViewById(R.id.firstNameLayout);
        lastNameLayout = drawerDialog.findViewById(R.id.lastNameLayout);
        emailNameLayout = drawerDialog.findViewById(R.id.emailNameLayout);
        phoneLayout = drawerDialog.findViewById(R.id.phoneLayout);
        ageLayout = drawerDialog.findViewById(R.id.ageLayout);
        firstNameEditText = drawerDialog.findViewById(R.id.firstNameEditText);
        lastNameEditText = drawerDialog.findViewById(R.id.lastNameEditText);
        emailEditText = drawerDialog.findViewById(R.id.emailEditText);
        phoneEditText = drawerDialog.findViewById(R.id.phoneEditText);
        ageEditText = drawerDialog.findViewById(R.id.ageEditText);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
            }
        });

        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    firstNameLayout.setError(null);
                }else {
                    firstNameLayout.setError("First Name Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    lastNameLayout.setError(null);
                }else {
                    lastNameLayout.setError("Last Name Required");
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
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    phoneLayout.setError(null);
                }else {
                    phoneLayout.setError("Phone is Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    ageLayout.setError(null);
                }else {
                    ageLayout.setError("Age is required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        if (firstNameEditText.getText().toString().isEmpty()) {
            firstNameLayout.setError("Please enter your first name");
        }
        if (lastNameEditText.getText().toString().isEmpty()) {
            lastNameLayout.setError("Please enter your last name");
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailNameLayout.setError("Please enter your email");
        }
        if (phoneEditText.getText().toString().isEmpty()) {
            phoneLayout.setError("Please enter your phone number");
        }
        if (ageEditText.getText().toString().isEmpty()) {
            ageLayout.setError("Please enter your age");
        } else {
            drawerDialog.dismiss();
        }
    }
}
