package com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ecommercewhitelabel.Activities.ReviewWritingActivity;
import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.Adapter.CasualMensClothsForActivityAdapter;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.RatingAndReviewAdapter;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.Model.ProductImagesModel;
import com.example.ecommercewhitelabel.Model.RatingAndReviewModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.MySingleton;
import com.example.ecommercewhitelabel.Utils.MySingletonFragment;
import com.example.ecommercewhitelabel.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingAndReviewFragment extends Fragment {
    Button writeReviewBtn;
    RecyclerView reviewRecyclerView;
    private final String[] statusArrayItems = {"Latest","Most Relevant", "Oldest"};
    ArrayList<RatingAndReviewModel> ratingAndReviewArrayList;
    ArrayAdapter<String> dropdownArrayAdapter;
    AutoCompleteTextView autoCompleteDropdownTV;
    SessionManager sessionManager;
    String authToken;
    String productId;
    TextView allReviewCountTxt;
    Dialog progressDialog;
    RelativeLayout mainLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_ratings_and_review_item_layout, container, false);

        sessionManager = new SessionManager(getContext());
        authToken = sessionManager.getUserData().get("authToken");

        mainLayout = view.findViewById(R.id.main);
        mainLayout.setVisibility(View.GONE);

        progressDialog = new Dialog(getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_bar_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressDialog.show();


        writeReviewBtn = view.findViewById(R.id.writeReviewBtn);
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        autoCompleteDropdownTV = view.findViewById(R.id.autoCompleteTextView);
        allReviewCountTxt = view.findViewById(R.id.allReviewCountTxt);

        String[] languages = getResources().getStringArray(R.array.rating_and_review_sort_by_item);
        dropdownArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item_text_layout, languages);
        autoCompleteDropdownTV.setAdapter(dropdownArrayAdapter);
        autoCompleteDropdownTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));
        SingleProductDetailsActivity obj = (SingleProductDetailsActivity) getActivity();
        productId = obj.getProductIdFromSingleProPage();
        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewWritingActivity.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratingAndReviewArrayList = new ArrayList<>();
//        ratingAndReviewArrayList.add(new RatingAndReviewModel("3.5","Alex k.","Posted on August 14,2024","Product description"));
//        ratingAndReviewArrayList.add(new RatingAndReviewModel("1.5","Zaki Ajmal","Posted on October 14,2024","Product description"));
//        ratingAndReviewArrayList.add(new RatingAndReviewModel("5","Alex k.","Posted on August 14,2024","Product description"));

//        reviewRecyclerView.setAdapter(new RatingAndReviewAdapter(ratingAndReviewArrayList, getContext()));

        getAllReviews();

        return view;
    }

    private void getAllReviews() {
        String getReviewURL = Constant.BASE_URL + "review?productId=" + productId;
        Log.e("ProductsURL",getReviewURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getReviewURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                String reviewId = dataObj.getString("_id");
                                String rating = dataObj.getString("rating");
                                String createdAt = dataObj.getString("createdAt");
                                String review = dataObj.getString("review");
                                JSONObject createdByObj = dataObj.getJSONObject("createdBy");
                                String fullName = createdByObj.getString("fullName");
                                ratingAndReviewArrayList.add(new RatingAndReviewModel(reviewId,rating,fullName,createdAt,review));
                            }
                            allReviewCountTxt.setText(String.valueOf(ratingAndReviewArrayList.size()));
                            reviewRecyclerView.setAdapter(new RatingAndReviewAdapter(ratingAndReviewArrayList, getContext()));
                            mainLayout.setVisibility(View.GONE);
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
