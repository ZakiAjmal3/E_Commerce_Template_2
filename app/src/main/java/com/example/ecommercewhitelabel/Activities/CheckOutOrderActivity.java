package com.example.ecommercewhitelabel.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.CheckOutAdapter;
import com.example.ecommercewhitelabel.Adapter.CheckOutAddressChangingRVAdapter;
import com.example.ecommercewhitelabel.Fragment.CartItemFragment;
import com.example.ecommercewhitelabel.Fragment.ProfileFragment;
import com.example.ecommercewhitelabel.Model.AddressItemModel;
import com.example.ecommercewhitelabel.Model.CartItemModel;
import com.example.ecommercewhitelabel.Model.CheckOutModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class CheckOutOrderActivity extends AppCompatActivity {
    ImageView backBtn;
    RecyclerView checkoutRecyclerView;
    TextView subTotalDisplayTxt, discountDisplayTxt, deliveryFeeDisplayTxt, totalAmountDisplayTxt,changeAddressTxtBtn;
    TextView addressLine1Txt, addressLine2Txt, addressLine3Txt;
    ArrayList<CheckOutModel> checkOutModelArrayList;
    ArrayList<CartItemModel> cartItemModelArrayList;
    RelativeLayout noDataLayout;
    NestedScrollView mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_order);

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

        noDataLayout = findViewById(R.id.noDataLayout);
        mainLayout = findViewById(R.id.mainLayout);

        backBtn = findViewById(R.id.imgMenu);
        checkoutRecyclerView = findViewById(R.id.checkoutRecyclerView);

        backBtn.setOnClickListener(v -> {
            finish();
        });
        //Order Summary Setup
        subTotalDisplayTxt = findViewById(R.id.subTotalDisplayTxt);
        discountDisplayTxt = findViewById(R.id.discountDisplayTxt);
        deliveryFeeDisplayTxt = findViewById(R.id.sizeTxt);
        totalAmountDisplayTxt = findViewById(R.id.totalAmountDisplayTxt);

        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkOutModelArrayList = new ArrayList<>();
        cartItemModelArrayList = new ArrayList<>();


        CartItemFragment fragment = (CartItemFragment) HomePageActivity.getCurrentFragment();
        if (fragment != null) {
            cartItemModelArrayList = fragment.getCartItemModelArrayList();
        } else {
            Log.e("Activity", "Fragment not found.");
        }

        for (int i = 0; i < cartItemModelArrayList.size(); i++) {
            String name = cartItemModelArrayList.get(i).getProductTitle();
            String price = cartItemModelArrayList.get(i).getProductPrice();
            checkOutModelArrayList.add(new CheckOutModel(name,price,null));
        }

        checkoutRecyclerView.setAdapter(new CheckOutAdapter(checkOutModelArrayList,this));
        setOrderSummaryDetails();

        // Change Address Setup
        changeAddressTxtBtn = findViewById(R.id.changeAddressTxt);
        changeAddressTxtBtn.setOnClickListener(v -> {
            openEditDialog();
        });
        checkOutItemArraySize();

        // Address Showing Setup
        addressLine1Txt = findViewById(R.id.addressLine1);
        addressLine2Txt = findViewById(R.id.addressLine2);
        addressLine3Txt = findViewById(R.id.addressLine3);
    }
    Dialog drawerDialog;
    ImageView crossBtn;
    RecyclerView addressRV;
    ArrayList<AddressItemModel> addressItemModelArrayList;
    public void openEditDialog() {
        drawerDialog = new Dialog(CheckOutOrderActivity.this);
        drawerDialog.setContentView(R.layout.checkout_change_address_dialog);
        drawerDialog.setCancelable(true);

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);
        crossBtn.setOnClickListener(v -> {
            drawerDialog.dismiss();
        });

        addressRV = drawerDialog.findViewById(R.id.addressItemLayout);
        addressRV.setLayoutManager(new LinearLayoutManager(CheckOutOrderActivity.this));
        addressItemModelArrayList = new ArrayList<>();
        addressItemModelArrayList.add(new AddressItemModel("Amanda","Qwerty" ,"9876543210","amanda@example.com","123","home","city","123456","state","country"));
        addressItemModelArrayList.add(new AddressItemModel("Amanda2","Qwerty2" ,"9876543210","amanda@example.com","124","home2","city2","123456","state2","country2"));

        addressRV.setAdapter(new CheckOutAddressChangingRVAdapter(addressItemModelArrayList,CheckOutOrderActivity.this));

        drawerDialog.show();
        drawerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        drawerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayPopUp;
        drawerDialog.getWindow().setGravity(Gravity.TOP);
        drawerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerDialog.getWindow().setStatusBarColor(ContextCompat.getColor(CheckOutOrderActivity.this,R.color.white));
        }
    }
    public void setOrderSummaryDetails() {
        int totalAmount = 0,finalTotalAmount = 0,shippingCharge = 0,discount = 0;

        for (int i = 0; i < checkOutModelArrayList.size(); i++) {
            totalAmount += Integer.parseInt(checkOutModelArrayList.get(i).getProductPrice()) * Integer.parseInt(cartItemModelArrayList.get(i).getProductQuantity());
        }
        if (totalAmount > 500){
            finalTotalAmount = totalAmount;
        }else {
            finalTotalAmount = totalAmount + 99;
        }
        subTotalDisplayTxt.setText("₹" + String.valueOf(totalAmount) + ".00");
        totalAmountDisplayTxt.setText("₹" + String.valueOf(finalTotalAmount) + ".00");
        discountDisplayTxt.setText("-₹" + String.valueOf(discount) + ".00");
        deliveryFeeDisplayTxt.setText("+₹" + String.valueOf(shippingCharge) + ".00");
    }
    public void checkOutItemArraySize() {
        if (checkOutModelArrayList.isEmpty()){
            mainLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }else {
            mainLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }
    public void setAddress(String addressLine1,String addressLine2,String addressLine3) {
        addressLine1Txt.setText(addressLine1);
        addressLine2Txt.setText(addressLine2);
        addressLine3Txt.setText(addressLine3);
        drawerDialog.dismiss();
    }
}