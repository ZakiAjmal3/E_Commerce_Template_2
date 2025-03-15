package com.example.ecommercewhitelabel.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Activities.CheckOutOrderActivity;
import com.example.ecommercewhitelabel.Adapter.CartItemAdapter;
import com.example.ecommercewhitelabel.Model.CartItemModel;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class CartItemFragment extends Fragment {
    ImageView backBtn;
    RecyclerView cartRecyclerView;
    CartItemAdapter cartItemAdapter;
    ArrayList<CartItemModel> cartItemModelArrayList;
    TextView subTotalDisplayTxt, discountDisplayTxt, deliveryFeeDisplayTxt, totalAmountDisplayTxt,promoErrorTxt;
    Button applyPromoBtn,checkOutBtn;
    EditText promoCodeET;
    RelativeLayout noDataLayout;
    NestedScrollView mainLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        noDataLayout = view.findViewById(R.id.noDataLayout);
        mainLayout = view.findViewById(R.id.mainLayout);

        // Cart item Setup
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItemModelArrayList = new ArrayList<>();
        cartItemModelArrayList.add(new CartItemModel("Product 1","300","Large","Black",null,"2"));
        cartItemModelArrayList.add(new CartItemModel("Product 2","250","Large","Black",null,"2"));
        cartItemModelArrayList.add(new CartItemModel("Product 3","150","Large","Black",null,"2"));
        cartItemModelArrayList.add(new CartItemModel("Product 4","200","Large","Black",null,"2"));
        cartItemModelArrayList.add(new CartItemModel("Product 5","1500","Large","Blue",null,"2"));

        cartItemAdapter = new CartItemAdapter(cartItemModelArrayList, CartItemFragment.this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        //Order Summary Setup
        subTotalDisplayTxt = view.findViewById(R.id.subTotalDisplayTxt);
        discountDisplayTxt = view.findViewById(R.id.discountDisplayTxt);
        deliveryFeeDisplayTxt = view.findViewById(R.id.sizeTxt);
        totalAmountDisplayTxt = view.findViewById(R.id.totalAmountDisplayTxt);
        promoErrorTxt = view.findViewById(R.id.invalidPromoTxt);
        applyPromoBtn = view.findViewById(R.id.applyCodeBtn);
        checkOutBtn = view.findViewById(R.id.goToCheckOutTxt);
        promoCodeET = view.findViewById(R.id.promoCodeET);

        setOrderSummaryDetails();

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CheckOutOrderActivity.class));
            }
        });

        applyPromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoErrorTxt.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Please enter a valid promo code", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setOrderSummaryDetails() {
        if (!cartItemModelArrayList.isEmpty()) {
            int totalAmount = 0, finalTotalAmount = 0, shippingCharge = 0, discount = 0;

            for (int i = 0; i < cartItemModelArrayList.size(); i++) {
                totalAmount += Integer.parseInt(cartItemModelArrayList.get(i).getProductPrice()) * Integer.parseInt(cartItemModelArrayList.get(i).getProductQuantity());
            }
            if (totalAmount > 500) {
                finalTotalAmount = totalAmount;
            } else {
                finalTotalAmount = totalAmount + 99;
            }
            subTotalDisplayTxt.setText("₹" + String.valueOf(totalAmount) + ".00");
            totalAmountDisplayTxt.setText("₹" + String.valueOf(finalTotalAmount) + ".00");
            discountDisplayTxt.setText("-₹" + String.valueOf(discount) + ".00");
            deliveryFeeDisplayTxt.setText("+₹" + String.valueOf(shippingCharge) + ".00");
        }
    }
    public void checkCartItemArraySize() {
        if (cartItemModelArrayList.isEmpty()){
            mainLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }else {
            mainLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }

    public ArrayList<CartItemModel> getCartItemModelArrayList() {
        return cartItemModelArrayList;
    }
}