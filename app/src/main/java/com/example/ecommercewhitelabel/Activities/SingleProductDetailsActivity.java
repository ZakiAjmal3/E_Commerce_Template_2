package com.example.ecommercewhitelabel.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecommercewhitelabel.Adapter.CasualMensClothsForActivityAdapter;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.AllImagesRecyclerAdapter;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.TabLayoutAdapter;
import com.example.ecommercewhitelabel.Model.HomePageBrowseByDStyleModel;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class SingleProductDetailsActivity extends AppCompatActivity {
    ImageView backBtn, quantityPlusIV, quantityMinusIV;
    RecyclerView productAllImagesRecycler,suggestionProductRV;
    ArrayList<ProductDetailsModel> newArrivalList;
    ArrayList<HomePageBrowseByDStyleModel> browseByDressList;
    String productTitleStr,productRatingStr,productPriceStr;
    TextView productTitleTxt,productRatingTxt,productPriceTxt,quantityTxt,smallTxt,mediumTxt,largeTxt,xLargeTxt;
    CustomRatingBar ratingBar;
    int quantityInt = 1;
    CardView colorCard1,colorCard2,colorCard3;
    ImageView colorImg1,colorImg2,colorImg3;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_details);

//        getWindow().setStatusBarColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));

        suggestionProductRV = findViewById(R.id.suggestionRV);
        backBtn = findViewById(R.id.imgMenu);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        // Suggestion RV setup
        suggestionProductRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        newArrivalList = new ArrayList<>();
        newArrivalList.add(new ProductDetailsModel("Product 1","200","2.5",0));
        newArrivalList.add(new ProductDetailsModel("Product 2","300","5",0));
        newArrivalList.add(new ProductDetailsModel("Product 3","250","3",0));
        newArrivalList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
        newArrivalList.add(new ProductDetailsModel("Product 5","199","1.2",0));
        suggestionProductRV.setAdapter(new CasualMensClothsForActivityAdapter(newArrivalList,SingleProductDetailsActivity.this));

        // product images recycler

        productAllImagesRecycler = findViewById(R.id.productMultipleImgRecycler);
        productAllImagesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_casual_bg);
        images.add(R.drawable.ic_formal_bg);
        images.add(R.drawable.ic_party_bg);
        images.add(R.drawable.ic_gym_bg);
        productAllImagesRecycler.setAdapter(new AllImagesRecyclerAdapter(images, this));

        //product details

        productTitleStr = getIntent().getStringExtra("productName");
        productRatingStr = getIntent().getStringExtra("productRating");
        productPriceStr = getIntent().getStringExtra("productPrice");

        productTitleTxt = findViewById(R.id.productTitleTxt);
        productTitleTxt.setText(productTitleStr);
        productRatingTxt = findViewById(R.id.ratingBarTxt);
        productRatingTxt.setText(productRatingStr);

        productPriceTxt = findViewById(R.id.productPriceTxt);
        productPriceTxt.setText("₹ " + productPriceStr);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(productRatingStr));

        //Quantity toggle

        quantityPlusIV = findViewById(R.id.quantityPlusTxt);
        quantityMinusIV = findViewById(R.id.quantityMinusTxt);
        quantityTxt = findViewById(R.id.quantityTxt);

        quantityTxt.setText(String.valueOf(quantityInt));

        quantityPlusIV.setOnClickListener(v -> {
            quantityInt++;
            quantityTxt.setText(String.valueOf(quantityInt));
        });
        quantityMinusIV.setOnClickListener(v -> {
            if (quantityInt > 1) {
                quantityInt--;
                quantityTxt.setText(String.valueOf(quantityInt));
            }
        });

        // Select Color

        colorCard1 = findViewById(R.id.colorCard1);
        colorCard2 = findViewById(R.id.colorCard2);
        colorCard3 = findViewById(R.id.colorCard3);

        colorImg1 = findViewById(R.id.colorImg1);
        colorImg2 = findViewById(R.id.colorImg2);
        colorImg3 = findViewById(R.id.colorImg3);

        colorCard1.setOnClickListener(v -> {
            colorImg1.setImageResource(R.drawable.ic_tick_for_color);
            colorImg2.setImageResource(0);
            colorImg3.setImageResource(0);
        });
        colorCard2.setOnClickListener(v -> {
            colorImg2.setImageResource(R.drawable.ic_tick_for_color);
            colorImg1.setImageResource(0);
            colorImg3.setImageResource(0);
        });
        colorCard3.setOnClickListener(v -> {
            colorImg3.setImageResource(R.drawable.ic_tick_for_color);
            colorImg2.setImageResource(0);
            colorImg1.setImageResource(0);
        });

        // Choosing size
        smallTxt = findViewById(R.id.smallTxt);
        mediumTxt = findViewById(R.id.mediumTxt);
        largeTxt = findViewById(R.id.largeTxt);
        xLargeTxt = findViewById(R.id.xLargeTxt);

        smallTxt.setOnClickListener(v -> {
            smallTxt.setBackgroundResource(R.drawable.rounded_corner_box_black_bg);
            smallTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.white));
            mediumTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            mediumTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            largeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            largeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            xLargeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            xLargeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));

        });
        mediumTxt.setOnClickListener(v -> {
            mediumTxt.setBackgroundResource(R.drawable.rounded_corner_box_black_bg);
            mediumTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.white));
            smallTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            smallTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            largeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            largeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            xLargeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            xLargeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
        });
        largeTxt.setOnClickListener(v -> {

            largeTxt.setBackgroundResource(R.drawable.rounded_corner_box_black_bg);
            largeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.white));
            smallTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            smallTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            mediumTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            mediumTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            xLargeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            xLargeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));

        });
        xLargeTxt.setOnClickListener(v -> {

            xLargeTxt.setBackgroundResource(R.drawable.rounded_corner_box_black_bg);
            xLargeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.white));
            smallTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            smallTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            mediumTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            mediumTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));
            largeTxt.setBackgroundResource(R.drawable.rounded_corner_box_grey_bg);
            largeTxt.setTextColor(ContextCompat.getColor(SingleProductDetailsActivity.this,R.color.black));

        });

        //Tab Layout SetUP
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        TabLayoutAdapter adapter = new TabLayoutAdapter(SingleProductDetailsActivity.this);
        viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Product Details");
//                        bookTypeTabSelected = "books";
                        break;
                    }
                    case 1: {
                        tab.setText("Rating & Review");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 2: {
                        tab.setText("FAQs");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                }
                // Log when a tab is clicked
                tab.view.setOnClickListener(v -> {
                    Log.d("TabClicked", "Tab " + position + " clicked");
                });
            }
        }).attach();
    }
}