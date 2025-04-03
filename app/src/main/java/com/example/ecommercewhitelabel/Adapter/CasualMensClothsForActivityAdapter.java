package com.example.ecommercewhitelabel.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.ecommercewhitelabel.Activities.HomePageActivity;
import com.example.ecommercewhitelabel.Activities.MainActivity;
import com.example.ecommercewhitelabel.Activities.MensCasualClothesActivity;
import com.example.ecommercewhitelabel.Activities.MyOrdersActivity;
import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.Model.MyOrderModel;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.Model.ProductImagesModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;
import com.example.ecommercewhitelabel.Utils.MySingleton;
import com.example.ecommercewhitelabel.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CasualMensClothsForActivityAdapter extends RecyclerView.Adapter<CasualMensClothsForActivityAdapter.ViewHolder> {
    ArrayList<ProductDetailsModel> productDetailsList;
    Context context;
    SpannableStringBuilder spannableText;
    SessionManager sessionManager;
    String authToken;
    public CasualMensClothsForActivityAdapter(ArrayList<ProductDetailsModel> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }
    @NonNull
    @Override
    public CasualMensClothsForActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mens_casual_recycler_item_layout,parent,false);
        this.sessionManager = new SessionManager(context);
        authToken = sessionManager.getUserData().get("authToken");
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CasualMensClothsForActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(productDetailsList.get(position).getProductTitle());
        holder.productName.setEllipsize(TextUtils.TruncateAt.END);
        holder.productName.setMaxLines(1);

        holder.productRating.setText(productDetailsList.get(position).getProductRating() + "/5");
//        holder.imageView.setImageResource(productDetailsList.get(position).getProductImage());
        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getProductRating()));

        if (!productDetailsList.get(position).getProductImagesModelsArrList().isEmpty()) {
            Glide.with(context).load(productDetailsList.get(position).getProductImagesModelsArrList().get(0).getProductImage()).into(holder.productImg);
        }else {
            Glide.with(context).load(R.drawable.no_image);
        }
        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getProductRating()));

        if (!productDetailsList.get(position).getDiscountAmount().equals("0")) {
            String originalPrice, disPercent, sellingPrice;
            originalPrice = productDetailsList.get(position).getProductMRP();
            disPercent = productDetailsList.get(position).getDiscountPercentage();
            sellingPrice = productDetailsList.get(position).getProductPrice();

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

            holder.productPrice.setText(spannableText);
        }else {
            holder.productPrice.setText("₹" + productDetailsList.get(position).getProductPrice());
        }

        int wishlistState = productDetailsList.get(position).getWishListImgToggle();
        if (wishlistState == 1){
            holder.wishlistImg.setImageResource(R.drawable.ic_heart_red);
        }else {
            holder.wishlistImg.setImageResource(R.drawable.ic_heart_grey);
        }

        holder.wishlistImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWishlistCount();
                int state;
                state = productDetailsList.get(position).getWishListImgToggle();
                if (state == 0) {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_red);
                    productDetailsList.get(position).setWishListImgToggle(1);
                    addToWishList(position);
                }else {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_grey);
                    productDetailsList.get(position).setWishListImgToggle(0);
                    removeFromWishList(position);
                }
            }
        });
        if (context instanceof SingleProductDetailsActivity){
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            int screenWidth = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
            layoutParams.width = (int) (screenWidth / 2.2); // Divide screen width by 2 for two items
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    private void setWishlistCount() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomePageActivity obj = new HomePageActivity();
                obj.setWishlistCount();
            }
        }, 1500);  // Match the duration of the logo animation
    }
    private void removeFromWishList(int position) {
        String orderURL = Constant.BASE_URL + "wishlist/remove/" + productDetailsList.get(position).getProductId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orderURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                        sessionManager.removeWishListItem(productDetailsList.get(position).getProductId());
                        sessionManager.getWishlistFromServer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void addToWishList(int position) {
        String orderURL = Constant.BASE_URL + "wishlist";
        String productId = productDetailsList.get(position).getProductId();
        String userId = sessionManager.getUserData().get("userId");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orderURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Item added to wishlist", Toast.LENGTH_SHORT).show();
                        sessionManager.getWishlistFromServer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productRating;
        ImageView productImg,wishlistImg;
        CustomRatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTxt);
            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productRating = itemView.findViewById(R.id.ratingTxt);
            productImg = itemView.findViewById(R.id.productImg);
            wishlistImg = itemView.findViewById(R.id.wishlistImg);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleProductDetailsActivity.class);
                    intent.putExtra("productId",productDetailsList.get(getAdapterPosition()).getProductId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
