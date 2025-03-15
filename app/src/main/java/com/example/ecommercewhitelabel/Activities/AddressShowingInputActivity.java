package com.example.ecommercewhitelabel.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.AddressItemAdapter;
import com.example.ecommercewhitelabel.Model.AddressItemModel;
import com.example.ecommercewhitelabel.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddressShowingInputActivity extends AppCompatActivity {
    RecyclerView addressRecyclerView;
    CardView addAddressBtn;
    ImageView backBtn;
    RelativeLayout noDataLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_showing_input);

//        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        backBtn = findViewById(R.id.imgMenu);
        noDataLayout = findViewById(R.id.noDataLayout);
        addAddressBtn = findViewById(R.id.addAddressBtn);
        addressRecyclerView = findViewById(R.id.addressRecyclerView);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDialog(-1);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    Dialog drawerDialog;
    ImageView crossBtn;
    Button saveBtn;
    TextInputLayout firstNameLayout, lastNameLayout, emailLayout, phoneLayout, apartmentLayout, streetLayout, cityLayout, pincodeLayout;
    EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, apartmentEditText, streetEditText, cityEditText, pincodeEditText;
    Spinner stateSpinner, countrySpinner;
    ArrayList<AddressItemModel> addressItemList = new ArrayList<>();
    private final String[] stateArray = {
            "Select State","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
            "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan",
            "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
            "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
            "Delhi", "Puducherry"
    };
    private final String[] countryArray = {"Select City","UK", "USA", "India"};
    String stateStr, countryStr;
    @SuppressLint("ResourceAsColor")
    public void showAddAddressDialog(int position) {
        drawerDialog = new Dialog(AddressShowingInputActivity.this);
        drawerDialog.setContentView(R.layout.address_edit_dialog);
        drawerDialog.setCancelable(true);

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);

        saveBtn = drawerDialog.findViewById(R.id.saveBtn);
        firstNameLayout = drawerDialog.findViewById(R.id.firstNameLayout);
        lastNameLayout = drawerDialog.findViewById(R.id.lastNameLayout);
        emailLayout = drawerDialog.findViewById(R.id.emailNameLayout);
        phoneLayout = drawerDialog.findViewById(R.id.phoneLayout);
        apartmentLayout = drawerDialog.findViewById(R.id.apartmentLayout);
        streetLayout = drawerDialog.findViewById(R.id.streetAddressLayout);
        cityLayout = drawerDialog.findViewById(R.id.cityLayout);
        pincodeLayout = drawerDialog.findViewById(R.id.pinCodeLayout);

        firstNameEditText = drawerDialog.findViewById(R.id.firstNameEditText);
        lastNameEditText = drawerDialog.findViewById(R.id.lastNameEditText);
        emailEditText = drawerDialog.findViewById(R.id.emailEditText);
        phoneEditText = drawerDialog.findViewById(R.id.phoneEditText);
        apartmentEditText = drawerDialog.findViewById(R.id.apartmentEditText);
        streetEditText = drawerDialog.findViewById(R.id.streetAddressEditText);
        cityEditText = drawerDialog.findViewById(R.id.cityEditText);
        pincodeEditText = drawerDialog.findViewById(R.id.pinCodeEditText);

        stateSpinner = drawerDialog.findViewById(R.id.stateSpinner);
        countrySpinner = drawerDialog.findViewById(R.id.countrySpinner);

        if (!addressItemList.isEmpty()){
            firstNameEditText.setText(addressItemList.get(position).getFirstName());
            lastNameEditText.setText(addressItemList.get(position).getLastName());
            phoneEditText.setText(addressItemList.get(position).getPhone());
            emailEditText.setText(addressItemList.get(position).getEmail());
            apartmentEditText.setText(addressItemList.get(position).getApartment());
            streetEditText.setText(addressItemList.get(position).getStreet());
            cityEditText.setText(addressItemList.get(position).getCity());
            pincodeEditText.setText(addressItemList.get(position).getPincode());
            for (int i = 0; i < stateArray.length; i++) {
                if (stateArray[i].equals(addressItemList.get(position).getState())) {
                    stateSpinner.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < countryArray.length; i++) {
                if (countryArray[i].equals(addressItemList.get(position).getCountry())) {
                    countrySpinner.setSelection(i);
                    break;
                }
            }
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stateArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryStr = countryArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateStr = stateArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    emailLayout.setError(null);
                }else {
                    emailLayout.setError("Email is Required");
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
        apartmentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    apartmentLayout.setError(null);
                }else {
                    apartmentLayout.setError("Apartment Number is required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        streetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    streetLayout.setError(null);
                }else {
                    streetLayout.setError("Street address is required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    cityLayout.setError(null);
                }else {
                    cityLayout.setError("City is required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pincodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    pincodeLayout.setError(null);
                }else {
                    pincodeLayout.setError("Pin Code is required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    addressItemList.add(new AddressItemModel(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), phoneEditText.getText().toString(), emailEditText.getText().toString(), apartmentEditText.getText().toString(), streetEditText.getText().toString(), cityEditText.getText().toString(), pincodeEditText.getText().toString(), stateStr, countryStr));
                    addressRecyclerView.setAdapter(new AddressItemAdapter(addressItemList, AddressShowingInputActivity.this));
                    if (addressItemList.size() > 0) {
                        noDataLayout.setVisibility(View.GONE);
                        addressRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        noDataLayout.setVisibility(View.VISIBLE);
                        addressRecyclerView.setVisibility(View.GONE);
                    }
                    drawerDialog.dismiss();
                }
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
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
    Boolean allTrueOrFalse = true;
    private boolean checkValidation() {
        if (firstNameEditText.getText().toString().isEmpty()) {
            firstNameLayout.setError("First Name Required");
            allTrueOrFalse = false;
        }else {
            firstNameLayout.setErrorEnabled(false);
        }
        if (lastNameEditText.getText().toString().isEmpty()) {
            lastNameLayout.setError("Last Name Required");
            allTrueOrFalse = false;
        }else {
            lastNameLayout.setErrorEnabled(false);
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailLayout.setError("Email Required");
            allTrueOrFalse = false;
        }else {
            emailLayout.setErrorEnabled(false);
        }
        if (phoneEditText.getText().toString().isEmpty()) {
            phoneLayout.setError("Phone Required");
            allTrueOrFalse = false;
        }else {
            phoneLayout.setErrorEnabled(false);
        }
        if (apartmentEditText.getText().toString().isEmpty()) {
            apartmentLayout.setError("Apartment Required");
            allTrueOrFalse = false;
        }else {
            apartmentLayout.setErrorEnabled(false);
        }
        if (streetEditText.getText().toString().isEmpty()) {
            streetLayout.setError("Street Required");
            allTrueOrFalse = false;
        }else  {
            streetLayout.setErrorEnabled(false);
        }
        if (cityEditText.getText().toString().isEmpty()) {
            cityLayout.setError("City Required");
            allTrueOrFalse = false;
        }else {
            cityLayout.setErrorEnabled(false);
        }
        if (pincodeEditText.getText().toString().isEmpty()) {
            pincodeLayout.setError("Pin Code Required");
            allTrueOrFalse = false;
        }else {
            pincodeLayout.setErrorEnabled(false);
        }
        if (stateSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "State not selected", Toast.LENGTH_SHORT).show();
            allTrueOrFalse = false;
        }
        if (countrySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Country not selected", Toast.LENGTH_SHORT).show();
            allTrueOrFalse = false;
        }
        return allTrueOrFalse;
    }
    public void checkAddressArrayListSize(){
        if (addressItemList.isEmpty()) {
            noDataLayout.setVisibility(View.VISIBLE);
            addressRecyclerView.setVisibility(View.GONE);
        }
    }
}