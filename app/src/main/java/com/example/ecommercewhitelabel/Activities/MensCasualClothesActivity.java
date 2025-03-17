package com.example.ecommercewhitelabel.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecommercewhitelabel.Adapter.CasualMensClothsForActivityAdapter;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class MensCasualClothesActivity extends AppCompatActivity {
    TextView headerTitleTxt;
    ImageView backBtn;
    CardView filterCardView;
    RecyclerView casualDressRecyclerView;
    ArrayList<ProductDetailsModel> casualDressArrayList;
    ArrayAdapter<String> dropdownArrayAdapter;
    AutoCompleteTextView autoCompleteDropdownTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens_casual_clothes);

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

        headerTitleTxt = findViewById(R.id.headerTitleTxt);
        backBtn = findViewById(R.id.imgMenu);
        filterCardView = findViewById(R.id.filterCardView);
        casualDressRecyclerView = findViewById(R.id.casualDressRecyclerView);
        autoCompleteDropdownTV = findViewById(R.id.autoCompleteTextView);

        if (getIntent() != null){
            headerTitleTxt.setText(getIntent().getStringExtra("Title"));
        }else {
            headerTitleTxt.setText("Casual");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpSpinner();

        casualDressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        casualDressArrayList = new ArrayList<>();
        casualDressArrayList.add(new ProductDetailsModel("Product 1","200","2.5",0));
        casualDressArrayList.add(new ProductDetailsModel("Product 2","300","5",0));
        casualDressArrayList.add(new ProductDetailsModel("Product 3","250","3",0));
        casualDressArrayList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
        casualDressArrayList.add(new ProductDetailsModel("Product 5","199","1.2",0));


        casualDressRecyclerView.setAdapter(new CasualMensClothsForActivityAdapter(casualDressArrayList,this));

        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });
    }

    private void setUpSpinner() {
        String[] languages = getResources().getStringArray(R.array.mens_casual_sort_by_item);
        dropdownArrayAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item_text_layout, languages);
        autoCompleteDropdownTV.setAdapter(dropdownArrayAdapter);
        autoCompleteDropdownTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }
    Dialog filterDialog;
    @SuppressLint("ResourceAsColor")
    private void openFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_product_layout);

        filterDialog.show();
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayLeft;
        filterDialog.getWindow().setGravity(Gravity.TOP);
        filterDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filterDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }
}