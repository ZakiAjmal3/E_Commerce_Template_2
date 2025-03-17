package com.example.ecommercewhitelabel.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.MyOrdersAdapter;
import com.example.ecommercewhitelabel.Model.MyOrderModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {
    ImageView backBtn;
    RecyclerView myOrderRecyclerView;
    ArrayList<MyOrderModel> myOrderArrayList;
    MyOrdersAdapter myOrdersAdapter;
    ArrayAdapter<String> dropdownStatusArrayAdapter;
    ArrayAdapter<String> dropdownFilterArrayAdapter;
    AutoCompleteTextView autoCompStatusTV,autoCompFilterTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

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

        backBtn = findViewById(R.id.imgBack);
        myOrderRecyclerView = findViewById(R.id.myOrderRecyclerView);

        autoCompStatusTV = findViewById(R.id.autoCompStatusTV);
        autoCompFilterTV = findViewById(R.id.autoCompFilterTV);

        setUpSpinners();

        myOrderArrayList = new ArrayList<>();
        myOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myOrderArrayList.add(new MyOrderModel("1233434564","Shipped","Mar 7, 2025","Product title 1","240"));
        myOrderArrayList.add(new MyOrderModel("65873434376","Delivered","Apr 7, 2020","Product title 2","150"));
        myOrderArrayList.add(new MyOrderModel("1233434564","Pending","Jun 7, 2022","Product title 3","400"));
        myOrderArrayList.add(new MyOrderModel("1233434564","Shipped","Oct 7, 2025","Product title 4","1500"));
        myOrderArrayList.add(new MyOrderModel("1233434564","Shipped","Dec 7, 2025","Product title 5","350"));

        myOrdersAdapter = new MyOrdersAdapter(myOrderArrayList,MyOrdersActivity.this);
        myOrderRecyclerView.setAdapter(myOrdersAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void setUpSpinners() {
        String[] languages = getResources().getStringArray(R.array.my_orders_status_sort_list);
        dropdownStatusArrayAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item_text_layout, languages);
        autoCompStatusTV.setAdapter(dropdownStatusArrayAdapter);
        autoCompStatusTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));

        String[] languages2 = getResources().getStringArray(R.array.my_orders_filter_sort_list);
        dropdownFilterArrayAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item_text_layout, languages2);
        autoCompFilterTV.setAdapter(dropdownStatusArrayAdapter);
        autoCompFilterTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));

    }
}