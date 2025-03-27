package com.example.ecommercewhitelabel.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ecommercewhitelabel.Adapter.CasualMensClothsForActivityAdapter;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.Model.ProductImagesModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.MySingleton;
import com.example.ecommercewhitelabel.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MensCasualClothesActivity extends AppCompatActivity {
    TextView headerTitleTxt;
    ImageView backBtn;
    CardView filterCardView;
    RecyclerView casualDressRecyclerView;
    ArrayList<ProductDetailsModel> casualDressArrayList;
    ArrayAdapter<String> dropdownArrayAdapter;
    AutoCompleteTextView autoCompleteDropdownTV;
    SessionManager sessionManager;
    String authToken;
    NestedScrollView dressNestedScroll;
    ProgressBar nextItemLoadingProgressBar;
    int itemPerPage = 5, totalPages = 1,currentPage = 1;
    Dialog progressBarDialog;
    RelativeLayout mainLayout,noDataLayout;
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

        sessionManager = new SessionManager(MensCasualClothesActivity.this);
        authToken = sessionManager.getUserData().get("authToken");

        progressBarDialog = new Dialog(MensCasualClothesActivity.this);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.progress_bar_dialog);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressBarDialog.show();

        mainLayout = findViewById(R.id.main);
        noDataLayout = findViewById(R.id.noDataLayout);
        mainLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);

        headerTitleTxt = findViewById(R.id.headerTitleTxt);
        backBtn = findViewById(R.id.imgMenu);
        filterCardView = findViewById(R.id.filterCardView);
        casualDressRecyclerView = findViewById(R.id.casualDressRecyclerView);
        autoCompleteDropdownTV = findViewById(R.id.autoCompleteTextView);

        nextItemLoadingProgressBar = findViewById(R.id.nextItemLoadingProgressBar);
        dressNestedScroll = findViewById(R.id.dressNestedScroll);

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
//        casualDressArrayList.add(new ProductDetailsModel("Product 1","200","2.5",0));
//        casualDressArrayList.add(new ProductDetailsModel("Product 2","300","5",0));
//        casualDressArrayList.add(new ProductDetailsModel("Product 3","250","3",0));
//        casualDressArrayList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
//        casualDressArrayList.add(new ProductDetailsModel("Product 5","199","1.2",0));


        casualDressRecyclerView.setAdapter(new CasualMensClothsForActivityAdapter(casualDressArrayList,this));

        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        dressNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if we are near the bottom, but leave a small threshold to avoid issues with small screens
                Log.e("ScrollDebug", "scrollY: " + scrollY +
                        " measuredHeight: " + v.getChildAt(0).getMeasuredHeight() +
                        " scrollHeight: " + v.getMeasuredHeight());
                int scrollThreshold = 50; // threshold to trigger load more data
                int diff = (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) - scrollY;
                Log.e("ScrollDebug", "diff: " + diff);
                // Check if we have scrolled to the bottom or near bottom
                if (diff <= scrollThreshold && currentPage <= totalPages) {
                    // Only increment the page and load more data if there's more data to load
                    currentPage++;
                    nextItemLoadingProgressBar.setVisibility(View.VISIBLE);
                    getNewArrivalProducts();
                    Log.e("Scroll","Scroll Happened");
                }
            }
        });

        getNewArrivalProducts();
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
        filterDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayBottomTop;
        filterDialog.getWindow().setGravity(Gravity.TOP);
        filterDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filterDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }
    private void getNewArrivalProducts() {
        String newArrivalURL = Constant.BASE_URL + "product/" + sessionManager.getStoreId() + "?pageNumber=" + currentPage + "&pageSize=" + itemPerPage;
        Log.e("ProductsURL", newArrivalURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            totalPages = response.optInt("totalPage", 0); // Default to 0 if not found
                            JSONArray dataArray = response.optJSONArray("data");

                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject productObj = dataArray.getJSONObject(i);

                                    String productId = productObj.optString("_id", null);
                                    String title = productObj.optString("title", null);

                                    JSONObject slugObj = productObj.optJSONObject("meta");
                                    String slug = (slugObj != null) ? slugObj.optString("slug", null) : null;

                                    String MRP = productObj.optString("MRP", null);
                                    String price = productObj.optString("price", null);

                                    JSONObject discountObj = productObj.optJSONObject("discount");
                                    String discountAmount = (discountObj != null) ? discountObj.optString("amount", null) : null;
                                    String discountPercentage = (discountObj != null) ? discountObj.optString("percentage", null) : null;

                                    String stock = productObj.optString("stock", null);
                                    String description = productObj.optString("description", null);

                                    JSONArray tagsArray = productObj.optJSONArray("tags");
                                    String tags = (tagsArray != null) ? parseTags(tagsArray) : null;

                                    String SKU = productObj.optString("SKU", null);

                                    // Handling Images
                                    ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                    JSONArray imageArray = productObj.optJSONArray("images");
                                    if (imageArray != null) {
                                        for (int j = 0; j < imageArray.length(); j++) {
                                            String imageUrl = imageArray.optString(j, null);
                                            if (imageUrl != null) {
                                                Log.e("JSONIMG", imageUrl);
                                                imagesList.add(new ProductImagesModel(imageUrl));
                                            }
                                        }
                                    }

                                    String store = productObj.optString("store", null);
                                    String category = productObj.optString("category", null);
                                    String inputTag = productObj.optString("inputTag", null);

                                    casualDressArrayList.add(new ProductDetailsModel(
                                            productId, title, slug, MRP, price, discountAmount, discountPercentage,
                                            stock, description, tags, SKU, store, category, inputTag, "4", 0, imagesList
                                    ));
                                }
                            }

                            if (!casualDressArrayList.isEmpty()) {
                                casualDressRecyclerView.setAdapter(new CasualMensClothsForActivityAdapter(casualDressArrayList, MensCasualClothesActivity.this));
                                nextItemLoadingProgressBar.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                progressBarDialog.dismiss();
                            } else {
                                noDataLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);
                                progressBarDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            progressBarDialog.dismiss();
                            e.printStackTrace();
                            Log.e("JSONParsingError", "Error parsing response: " + e.getMessage());
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
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                Toast.makeText(MensCasualClothesActivity.this, message, Toast.LENGTH_LONG).show();
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
}