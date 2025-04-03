package com.example.ecommercewhitelabel.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.ecommercewhitelabel.Adapter.CasualMensClothsForActivityAdapter;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.AllImagesRecyclerAdapter;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.TabLayoutAdapter;
import com.example.ecommercewhitelabel.Model.HomePageBrowseByDStyleModel;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.Model.ProductImagesModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;
import com.example.ecommercewhitelabel.Utils.MySingleton;
import com.example.ecommercewhitelabel.Utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleProductDetailsActivity extends AppCompatActivity {
    Button addToCartBtn;
    ImageView backBtn, quantityPlusIV, quantityMinusIV,productImgMain;
    RecyclerView productAllImagesRecycler,suggestionProductRV;
    ArrayList<ProductDetailsModel> newArrivalList;
    ArrayList<ProductDetailsModel> thisSingleProductItem;
    ArrayList<HomePageBrowseByDStyleModel> browseByDressList;
    String productTitleStr,productRatingStr,productPriceStr;
    TextView productTitleTxt,productPriceTxt,quantityTxt,smallTxt,mediumTxt,largeTxt,xLargeTxt;
    CustomRatingBar ratingBar;
    int quantityInt = 1;
    CardView colorCard1,colorCard2,colorCard3;
    ImageView colorImg1,colorImg2,colorImg3;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    String productIdIntent;
    SessionManager sessionManager;
    String authToken;
    Dialog progressBarDialog;
    NestedScrollView mainNestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_details);

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

        sessionManager = new SessionManager(SingleProductDetailsActivity.this);
        authToken = sessionManager.getUserData().get("authToken");

        mainNestedScrollView = findViewById(R.id.mainNestedLayout);
        mainNestedScrollView.setVisibility(View.GONE);
        progressBarDialog = new Dialog(SingleProductDetailsActivity.this);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.progress_bar_dialog);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressBarDialog.show();

        suggestionProductRV = findViewById(R.id.suggestionRV);
        backBtn = findViewById(R.id.imgMenu);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        productIdIntent = getIntent().getStringExtra("productId");

        // Suggestion RV setup
        suggestionProductRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newArrivalList = new ArrayList<>();
        thisSingleProductItem = new ArrayList<>();
        getProductById();
//        newArrivalList.add(new ProductDetailsModel("Product 1","200","2.5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 2","300","5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 3","250","3",0));
//        newArrivalList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 5","199","1.2",0));
//        suggestionProductRV.setAdapter(new CasualMensClothsForActivityAdapter(newArrivalList,SingleProductDetailsActivity.this));
        getSuggestionProducts();
        // product images recycler

        productAllImagesRecycler = findViewById(R.id.productMultipleImgRecycler);
        productAllImagesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        ArrayList<Integer> images = new ArrayList<>();
//        images.add(R.drawable.ic_casual_bg);
//        images.add(R.drawable.ic_formal_bg);
//        images.add(R.drawable.ic_party_bg);
//        images.add(R.drawable.ic_gym_bg);
//        productAllImagesRecycler.setAdapter(new AllImagesRecyclerAdapter(images, this));

        //product details

        productImgMain = findViewById(R.id.productImgMain);
        productTitleTxt = findViewById(R.id.productTitleTxt);
//        productTitleTxt.setText(productTitleStr);

        productPriceTxt = findViewById(R.id.productPriceTxt);
//        productPriceTxt.setText("₹ " + productPriceStr);


        ratingBar = findViewById(R.id.ratingBar);
//        ratingBar.setRating(Float.parseFloat(productRatingStr));

        //Quantity toggle

        quantityPlusIV = findViewById(R.id.quantityPlusTxt);
        quantityMinusIV = findViewById(R.id.quantityMinusTxt);
        quantityTxt = findViewById(R.id.quantityTxt);

//        quantityTxt.setText(String.valueOf(quantityInt));

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

        addToCartBtn = findViewById(R.id.addToCartBtn);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sessionManager.isLoggedIn()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleProductDetailsActivity.this);
                    AlertDialog dialog = builder.setTitle("Login Required")
                            .setMessage("Please login to add this item to your cart?")
                            .setPositiveButton("Proceed to login", (dialog1, which) -> startActivity(new Intent(SingleProductDetailsActivity.this, LoginActivity.class))) // Call addToCart() on "Yes"
                            .setNegativeButton("Cancel", (dialog1, which) ->
                                    dialog1.dismiss()) // Close dialog on "Cancel"
                            .create();

                    // Show the dialog before applying custom styles
                    dialog.show();

                    // Change background color
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // Yellow background

                    // Change text color of Title & Message
                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextColor(Color.BLACK); // Change message text color
                    }

                    // Change button text colors
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                }else {
                    addToCart();
                }
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

    private void addToCart() {
        String newArrivalURL = Constant.BASE_URL + "cart";
        Log.e("addTCartURL", newArrivalURL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productIdIntent);
            jsonObject.put("quantity", quantityInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, newArrivalURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString("message", null);
                        Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        sessionManager.getCartFromServer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void getProductById() {
        String newArrivalURL = Constant.BASE_URL + "product/productById/" + productIdIntent;
        Log.e("ProductsURL", newArrivalURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObj = response.optJSONObject("data");
                            if (dataObj != null) {

                                    String productId = dataObj.optString("_id", null);
                                    String title = dataObj.optString("title", null);
//
                                    JSONObject slugObj = dataObj.optJSONObject("meta");
                                    String slug = (slugObj != null) ? slugObj.optString("slug", null) : null;

                                    String MRP = dataObj.optString("MRP", null);
                                    String price = dataObj.optString("price", null);

                                    JSONObject discountObj = dataObj.optJSONObject("discount");
                                    String discountAmount = (discountObj != null) ? discountObj.optString("amount", null) : null;
                                    String discountPercentage = (discountObj != null) ? discountObj.optString("percentage", null) : null;

                                    String stock = dataObj.optString("stock", null);
                                    String description = dataObj.optString("description", null);

                                    JSONArray tagsArray = dataObj.optJSONArray("tags");
                                    String tags = (tagsArray != null) ? parseTags(tagsArray) : null;

                                    String SKU = dataObj.optString("SKU", null);

                                    // Handling Images
                                    ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                    JSONArray imageArray = dataObj.optJSONArray("images");
                                    if (imageArray != null) {
                                        for (int j = 0; j < imageArray.length(); j++) {
                                            String imageUrl = imageArray.optString(j, null);
                                            if (imageUrl != null) {
                                                Log.e("JSONIMG", imageUrl);
                                                imagesList.add(new ProductImagesModel(imageUrl));
                                            }
                                        }
                                    }

                                    String store = dataObj.optString("store", null);
                                    String category = dataObj.optString("category", null);
                                    String inputTag = dataObj.optString("inputTag", null);

                                    thisSingleProductItem.add(new ProductDetailsModel(
                                            productId, title, slug, MRP, price, discountAmount, discountPercentage,
                                            stock, description, tags, SKU, store, category, inputTag, "4", 0, imagesList
                                    ));
                                }
                            setProductDetails();
//                            }
//
//                            if (!casualDressArrayList.isEmpty()) {
//                                casualDressRecyclerView.setAdapter(new CasualMensClothsForActivityAdapter(casualDressArrayList, MensCasualClothesActivity.this));
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONParsingError", "Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    SpannableStringBuilder spannableText;
    private void setProductDetails() {
        productTitleStr = thisSingleProductItem.get(0).getProductTitle();
        productRatingStr = thisSingleProductItem.get(0).getProductRating();

        productTitleTxt.setText(productTitleStr);
        productTitleTxt.setEllipsize(TextUtils.TruncateAt.END);
        productTitleTxt.setMaxLines(2);

        Glide.with(this).load(thisSingleProductItem.get(0).getProductImagesModelsArrList().get(0).getProductImage()).error(R.drawable.no_image).into(productImgMain);
        if (!thisSingleProductItem.get(0).getDiscountAmount().equals("0")) {
            String originalPrice, disPercent, sellingPrice;
            originalPrice = thisSingleProductItem.get(0).getProductMRP();
            disPercent = thisSingleProductItem.get(0).getDiscountPercentage();
            sellingPrice = thisSingleProductItem.get(0).getProductPrice();

            // Create a SpannableString for the original price with strikethrough
            SpannableString spannableOriginalPrice = new SpannableString("₹" + originalPrice);
            spannableOriginalPrice.setSpan(new StrikethroughSpan(), 0, spannableOriginalPrice.length(), 0);
            // Create the discount text
            String discountText = "(-" + disPercent + "%)";
            spannableText = new SpannableStringBuilder();
            spannableText.append("₹" + sellingPrice + " ");
            spannableText.append(spannableOriginalPrice);
            spannableText.append(" " + discountText);
            // Set the color for the discount percentage
            int startIndex = spannableText.length() - discountText.length();
            spannableText.setSpan(new ForegroundColorSpan(Color.GREEN), startIndex, spannableText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            productPriceTxt.setText(spannableText);
        }else {
            productPriceTxt.setText("₹ " + thisSingleProductItem.get(0).getProductPrice());
        }

        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < thisSingleProductItem.get(0).getProductImagesModelsArrList().size(); i++) {
            images.add(thisSingleProductItem.get(i).getProductImagesModelsArrList().get(i).getProductImage());
        }

        productAllImagesRecycler.setAdapter(new AllImagesRecyclerAdapter(images, SingleProductDetailsActivity.this));

        progressBarDialog.dismiss();
        mainNestedScrollView.setVisibility(View.VISIBLE);
    }

    private String parseTags(JSONArray tagsArray) throws JSONException {
        StringBuilder tags = new StringBuilder();
        for (int j = 0; j < tagsArray.length(); j++) {
            tags.append(tagsArray.getString(j)).append(", ");
        }
        if (tags.length() > 0) {
            tags.setLength(tags.length() - 2); // Remove trailing comma and space
        }
        return tags.toString();
    }
    private void getSuggestionProducts() {
        String newArrivalURL = Constant.BASE_URL + "product/" + sessionManager.getStoreId() + "?pageNumber=1&pageSize=5";
        Log.e("ProductsURL",newArrivalURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                String productId = productObj.getString("_id");
                                String title = productObj.getString("title");

                                JSONObject slugObj = productObj.getJSONObject("meta");
                                String slug = slugObj.getString("slug");

                                String MRP = productObj.getString("MRP");
                                String price = productObj.getString("price");

                                JSONObject discountObj = productObj.getJSONObject("discount");
                                String discountAmount = discountObj.getString("amount");
                                String discountPercentage = discountObj.getString("percentage");

                                String stock = productObj.getString("stock");
                                String description = productObj.getString("description");

                                String tags = parseTags(productObj.getJSONArray("tags"));

                                String SKU = productObj.getString("SKU");

                                ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                JSONArray imageArray = productObj.getJSONArray("images");
                                for (int j = 0; j < imageArray.length(); j++) {
                                    String imageUrl = imageArray.getString(j);
                                    Log.e("JSONIMG",imageUrl);
                                    imagesList.add(new ProductImagesModel(imageUrl));
                                }

                                String store = productObj.getString("store");
                                String category = productObj.getString("category");
                                String inputTag = productObj.getString("inputTag");

                                newArrivalList.add(new ProductDetailsModel(productId,title,slug,MRP,price,
                                        discountAmount,discountPercentage,stock,description,tags,SKU,store,
                                        category,inputTag,"4",0,imagesList));
                            }
                            if (!newArrivalList.isEmpty()){
                                suggestionProductRV.setAdapter(new CasualMensClothsForActivityAdapter(newArrivalList, SingleProductDetailsActivity.this));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressBarDialog.dismiss();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public String getProductIdFromSingleProPage(){
        return productIdIntent;
    }
    public String getDescription(){
        return thisSingleProductItem.get(0).getDescription();
    }
}