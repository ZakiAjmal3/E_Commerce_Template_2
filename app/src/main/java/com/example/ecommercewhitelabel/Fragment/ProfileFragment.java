package com.example.ecommercewhitelabel.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ecommercewhitelabel.Activities.AddressShowingInputActivity;
import com.example.ecommercewhitelabel.Activities.LoginActivity;
import com.example.ecommercewhitelabel.Activities.MainActivity;
import com.example.ecommercewhitelabel.Activities.MyOrdersActivity;
import com.example.ecommercewhitelabel.Model.CouponSelectingModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.MySingletonFragment;
import com.example.ecommercewhitelabel.Utils.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    ImageView editNameBtn,editPersonalBtn;
    TextView userFullNameTxt1,userEmailTxt1,userFullNameTxt2,userEmailTxt2;
    String fullNameStr,emailStr;
    LinearLayout myOrdersLL,addressLL,loginLL,logoutLL;
    SessionManager sessionManager;
    String authToken,userId;
    Dialog progressBarDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getContext());
        authToken = sessionManager.getUserData().get("authToken");

        editNameBtn = view.findViewById(R.id.editNameBtn);
        editPersonalBtn = view.findViewById(R.id.editNameBtn2);

        userFullNameTxt1 = view.findViewById(R.id.userNameTxt);
        userEmailTxt1 = view.findViewById(R.id.userEmailTxt);
        userFullNameTxt2 = view.findViewById(R.id.fullNameDisplayTxt);
        userEmailTxt2 = view.findViewById(R.id.emailDisplayTxt);

        fullNameStr = sessionManager.getUserData().get("fullName");
        emailStr = sessionManager.getUserData().get("email");
        userId = sessionManager.getUserData().get("userId");
        Log.d("ProfileFragment", "userId: " + userId);

        userFullNameTxt1.setText(fullNameStr);
        userFullNameTxt2.setText(fullNameStr);
        userEmailTxt1.setText(emailStr);
        userEmailTxt2.setText(emailStr);

        myOrdersLL = view.findViewById(R.id.myOrderLL);
        addressLL = view.findViewById(R.id.addressLL);
        loginLL = view.findViewById(R.id.loginLL);
        logoutLL = view.findViewById(R.id.logoutLL);

        if (sessionManager.isLoggedIn()){
            loginLL.setVisibility(View.GONE);
            logoutLL.setVisibility(View.VISIBLE);
        }else {
            loginLL.setVisibility(View.VISIBLE);
            logoutLL.setVisibility(View.GONE);
        }

        myOrdersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyOrdersActivity.class));
            }
        });
        addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddressShowingInputActivity.class));
            }
        });
        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Toast.makeText(getContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

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
        fullNameLayout = drawerDialog.findViewById(R.id.fullNameLayout);
        emailNameLayout = drawerDialog.findViewById(R.id.emailNameLayout);
//        phoneLayout = drawerDialog.findViewById(R.id.phoneLayout);
        fullNameEditTxt = drawerDialog.findViewById(R.id.fullNameEditText);
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
        String examCategoryURL = Constant.BASE_URL + "auth/customer/update";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fullName", fullNameEditTxt.getText().toString());
            jsonObject.put("email", emailEditText.getText().toString());
            jsonObject.put("userId", sessionManager.getUserData().get("userId"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, examCategoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String email = data.getString("email");
                            String role = data.getString("role");
                            String fullName = data.getString("fullName");
                            sessionManager.saveUserDetails(fullName,email,role);
                            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            userFullNameTxt1.setText(fullName);
                            userFullNameTxt2.setText(fullName);
                            userEmailTxt1.setText(email);
                            userEmailTxt2.setText(email);
                            progressBarDialog.dismiss();
                            drawerDialog.dismiss();
                        } catch (JSONException e) {
                            progressBarDialog.dismiss();
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarDialog.dismiss();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("ExamListError", errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        MySingletonFragment.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
