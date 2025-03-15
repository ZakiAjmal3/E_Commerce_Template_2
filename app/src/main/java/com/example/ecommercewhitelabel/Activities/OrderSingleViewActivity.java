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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.SingleOrderPageItemsAdapter;
import com.example.ecommercewhitelabel.Adapter.TrackingOrderTimelineAdapter;
import com.example.ecommercewhitelabel.Model.SingleOrderPageItemsModel;
import com.example.ecommercewhitelabel.Model.TrackingOrderTimelineModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;
import java.util.List;

public class OrderSingleViewActivity extends AppCompatActivity {
    TextView orderIdTxt,orderDateTxt,totalAmountDisplayTxt,tFinalAmountDisplayTxt;
    String orderIdStr,orderStatusStr,orderDateStr,orderTitle,orderPrice;
    RecyclerView orderItemsRecyclerView;
    SingleOrderPageItemsAdapter sOrderItemsAdapter;
    SingleOrderPageItemsModel sOrderItemsModel;
    ArrayList<SingleOrderPageItemsModel> sOrderItemsModelList;
    ImageView backBtn;
    RelativeLayout trackOrderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_single_view);

//        getWindow().setStatusBarColor(ContextCompat.getColor(OrderSingleViewActivity.this, R.color.black));

        orderIdTxt = findViewById(R.id.orderIdTxt);
        orderDateTxt = findViewById(R.id.orderDateTxt);
        totalAmountDisplayTxt = findViewById(R.id.totalAmountDisplayTxt);
        tFinalAmountDisplayTxt = findViewById(R.id.tFinalAmountDisplayTxt);

        backBtn = findViewById(R.id.imgBack);
        backBtn.setOnClickListener(v -> onBackPressed());

        trackOrderBtn = findViewById(R.id.trackOrderBtn);
        trackOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrackingDialog();
            }
        });

        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderIdStr = getIntent().getStringExtra("orderId");
        orderStatusStr = getIntent().getStringExtra("orderStatus");
        orderDateStr = getIntent().getStringExtra("orderDate");
        orderTitle = getIntent().getStringExtra("orderProductTitle");
        orderPrice = getIntent().getStringExtra("orderPrice");

        orderIdTxt.setText("Order Id: " + orderIdStr);
        orderDateTxt.setText("Order on: " + orderDateStr);
        totalAmountDisplayTxt.setText("₹ " +orderPrice);
        tFinalAmountDisplayTxt.setText("₹ " +orderPrice);

        sOrderItemsModel = new SingleOrderPageItemsModel(null,orderTitle,orderPrice);
        sOrderItemsModelList = new ArrayList<>();
        sOrderItemsModelList.add(sOrderItemsModel);
        sOrderItemsAdapter = new SingleOrderPageItemsAdapter(sOrderItemsModelList, this);
        orderItemsRecyclerView.setAdapter(sOrderItemsAdapter);

    }
    Dialog drawerDialog;
    ImageView crossBtn;
    private RecyclerView recyclerView;
    private TrackingOrderTimelineAdapter adapter;
    private List<TrackingOrderTimelineModel> orderStages;
    @SuppressLint("ResourceAsColor")
    private void showTrackingDialog() {
        drawerDialog = new Dialog(this);
        drawerDialog.setContentView(R.layout.tracking_order_timeline_dialog);
        drawerDialog.setCancelable(true);

        recyclerView = drawerDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
            }
        });

        // Sample order stages
        orderStages = new ArrayList<>();
        orderStages.add(new TrackingOrderTimelineModel("Order Placed", "Your order has been placed", R.drawable.ic_order_placed));
        orderStages.add(new TrackingOrderTimelineModel("Processing", "Your order is being processed", R.drawable.ic_order_processing));
        orderStages.add(new TrackingOrderTimelineModel("Shipped", "Your order has been shipped", R.drawable.ic_order_shipped));
        orderStages.add(new TrackingOrderTimelineModel("Delivered", "Your order has been delivered", R.drawable.ic_order_delivered));

        // Set up the adapter
        adapter = new TrackingOrderTimelineAdapter(orderStages,OrderSingleViewActivity.this);
        recyclerView.setAdapter(adapter);

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
}