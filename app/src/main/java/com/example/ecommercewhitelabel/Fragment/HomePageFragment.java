package com.example.ecommercewhitelabel.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ecommercewhitelabel.Activities.MensCasualClothesActivity;
import com.example.ecommercewhitelabel.Adapter.HomePageAdapter.BrowseByDressStyleAdapter;
import com.example.ecommercewhitelabel.Adapter.HomePageAdapter.ProductDetailsForFragmentAdapter;
import com.example.ecommercewhitelabel.Model.HomePageBrowseByDStyleModel;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.Model.ProductImagesModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.MySingletonFragment;
import com.example.ecommercewhitelabel.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomePageFragment extends Fragment {
    RecyclerView newArrivalRecycler,topSellingRecycler,browseByDressRecycler;
    ArrayList<ProductDetailsModel> newArrivalList;
    ArrayList<HomePageBrowseByDStyleModel> browseByDressList;
    Button shopNowBtn;
    LinearLayout countingLLayout;
    RelativeLayout imagesRL,remainingRL;
    HorizontalScrollView brandLogoRL;
    LinearLayout brandLogoLinearLayout;
    final int SCROLL_DURATION = 30;
    ImageView starOne,starTwo;
    private Handler handler;
    private Handler handler2;
    private Runnable rotationRunnable;
    private Runnable rotationRunnable2;
    TextView headTxt1,headTxt2,viewAllDressTxtBtn,topSellingViewAllTxtBtn,brandNumCountTxt,brandNumCountBelowTxt,qualityNumCountTxt,customerNumCountTxt;
    SessionManager sessionManager;
    String authToken;
    ImageView rightIndicatorNewArrival,rightIndicatorTopSelling;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        sessionManager = new SessionManager(getContext());
        authToken = sessionManager.getUserData().get("authToken");

        shopNowBtn = view.findViewById(R.id.btnShopNow);
        viewAllDressTxtBtn = view.findViewById(R.id.viewAllTxtBtn);
        topSellingViewAllTxtBtn = view.findViewById(R.id.topSellingViewAllTxtBtn);
        headTxt1 = view.findViewById(R.id.headTxt1);
        headTxt2 = view.findViewById(R.id.headTxt2);
        headTxt2.setVisibility(View.GONE);
        brandNumCountTxt = view.findViewById(R.id.brandNumCountTxt);
        brandNumCountBelowTxt = view.findViewById(R.id.brandNumCountBelowTxt);
        qualityNumCountTxt = view.findViewById(R.id.qualityNumCountTxt);
        customerNumCountTxt = view.findViewById(R.id.customerNumCountTxt);

        imagesRL = view.findViewById(R.id.imagesRL);
        remainingRL = view.findViewById(R.id.remainingRL);

        countingLLayout = view.findViewById(R.id.countingLLayout);

        brandLogoRL = view.findViewById(R.id.brandLogoRL);
        brandLogoLinearLayout = view.findViewById(R.id.brandLogoLinearLayout);

        starOne = view.findViewById(R.id.starOne);
        starTwo = view.findViewById(R.id.starTwo);

        // Set up the handler to repeat the animation
        handler = new Handler();
        handler2 = new Handler();
        // Create a runnable that will perform the rotation
        rotationRunnable = new Runnable() {
            @Override
            public void run() {
                // Create the rotation animation (0 to 360 degrees)
                ObjectAnimator rotate1 = ObjectAnimator.ofFloat(starOne, "rotation", 0f, 360f);
                rotate1.setDuration(2000);
                rotate1.start();
                ObjectAnimator rotate2 = ObjectAnimator.ofFloat(starTwo, "rotation", 360f, 0f);
                rotate2.setDuration(2000);
                rotate2.start();
                // Post a new runnable to repeat the rotation after 500ms
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(rotationRunnable);

// Create a runnable for continuous translation animation
        rotationRunnable2 = new Runnable() {
            @Override
            public void run() {
                ObjectAnimator translateXAnim1 = ObjectAnimator.ofFloat(brandLogoLinearLayout, "translationX", 1000f, -1700f);
                translateXAnim1.setDuration(5000);

                // Add listener to restart animation on completion
                translateXAnim1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Restart animation immediately
                        handler.post(rotationRunnable2);
                    }
                });

                translateXAnim1.start();
            }
        };
// Start the first animation
        handler.post(rotationRunnable2);

// Calling the animateCount function for each TextView
        animateCount(brandNumCountTxt, 200, 30); // Brand count with slower update
        animateCount(qualityNumCountTxt, 2000, 3); // Quality count with even slower update
        animateCount(customerNumCountTxt, 3000, 3); // Customer count with even slower update

// Animation for headTxt1 (Right corner tilt + move from middle to top)
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(headTxt1, "rotation", 45f, 0f); // Tilting effect
        ObjectAnimator translateYAnim1 = ObjectAnimator.ofFloat(headTxt1, "translationY", 1000f, 0f); // Move upwards
        ObjectAnimator translateXAnim1 = ObjectAnimator.ofFloat(headTxt1, "translationX", 500f, 0f); // Move from right corner to center

// Setting duration and interpolator
        rotateAnim.setDuration(500); // 1 second
        translateYAnim1.setDuration(500); // 1 second
        translateXAnim1.setDuration(500); // 1 second

// Create an AnimatorSet for headTxt1
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(rotateAnim, translateYAnim1, translateXAnim1); // Play together for smoother animation
        animatorSet1.setInterpolator(new DecelerateInterpolator()); // Smooth animation
        animatorSet1.start();

// When headTxt1 finishes, make headTxt2 visible and animate it
        animatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // Make headTxt2 visible after headTxt1 finishes its animation
                headTxt2.setVisibility(View.VISIBLE);

                // Animation for headTxt2 (Bottom to Top)
                ObjectAnimator translateYAnim2 = ObjectAnimator.ofFloat(headTxt2, "translationY", 500f, 0f); // Move upwards
                translateYAnim2.setDuration(500); // 1 second
                translateYAnim2.setInterpolator(new DecelerateInterpolator()); // Smooth animation
                translateYAnim2.start();

                // When headTxt2 finishes, make btnShopNow visible and animate it
                translateYAnim2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Make btnShopNow visible after headTxt2 finishes its animation
                        shopNowBtn.setVisibility(View.VISIBLE);

                        // Animation for btnShopNow (Bottom to Top)
                        ObjectAnimator translateYAnim3 = ObjectAnimator.ofFloat(shopNowBtn, "translationY", 500f, 0f); // Move upwards
                        translateYAnim3.setDuration(500); // 1 second
                        translateYAnim3.setInterpolator(new DecelerateInterpolator()); // Smooth animation
                        translateYAnim3.start();

                        // When headTxt2 finishes, make btnShopNow visible and animate it
                        translateYAnim3.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // Make btnShopNow visible after headTxt2 finishes its animation
                                countingLLayout.setVisibility(View.VISIBLE);

                                // Animation for btnShopNow (Bottom to Top)
                                ObjectAnimator translateYAnim4 = ObjectAnimator.ofFloat(countingLLayout, "translationY", 500f, 0f); // Move upwards
                                translateYAnim4.setDuration(500); // 1 second
                                translateYAnim4.setInterpolator(new DecelerateInterpolator()); // Smooth animation
                                translateYAnim4.start();

                                // When headTxt2 finishes, make btnShopNow visible and animate it
                                translateYAnim4.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {}

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        // Make btnShopNow visible after headTxt2 finishes its animation
                                        imagesRL.setVisibility(View.VISIBLE);

                                        // Animation for btnShopNow (Bottom to Top)
                                        ObjectAnimator translateYAnim5 = ObjectAnimator.ofFloat(imagesRL, "translationY", 500f, 0f); // Move upwards
                                        translateYAnim5.setDuration(500); // 1 second
                                        translateYAnim5.setInterpolator(new DecelerateInterpolator()); // Smooth animation
                                        translateYAnim5.start();

                                        // When headTxt2 finishes, make btnShopNow visible and animate it
                                        translateYAnim5.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {}

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                               remainingRL.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {}

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {}
                                        });
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {}

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {}
                                });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        rightIndicatorTopSelling = view.findViewById(R.id.rightIndicatorTopSelling);
        rightIndicatorNewArrival = view.findViewById(R.id.rightIndicatorNewArrival);
        newArrivalRecycler = view.findViewById(R.id.newArrivalRecyclerView);
        topSellingRecycler = view.findViewById(R.id.topSellingRecyclerView);
        browseByDressRecycler = view.findViewById(R.id.browseByDressRecyclerView);
        browseByDressRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        LinearLayoutManager newArrivalLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        newArrivalRecycler.setLayoutManager(newArrivalLayoutManager);
        LinearLayoutManager topSellingLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        topSellingRecycler.setLayoutManager(topSellingLayoutManager);

        newArrivalList = new ArrayList<>();
//        newArrivalList.add(new ProductDetailsModel("Product 1","200","2.5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 2","300","5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 3","250","3",0));
//        newArrivalList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
//        newArrivalList.add(new ProductDetailsModel("Product 5","199","1.2",0));

        browseByDressList = new ArrayList<>();
        browseByDressList.add(new HomePageBrowseByDStyleModel("CASUAL",R.drawable.ic_casual_bg));
        browseByDressList.add(new HomePageBrowseByDStyleModel("FORMAL",R.drawable.ic_formal_bg));
        browseByDressList.add(new HomePageBrowseByDStyleModel("PARTY",R.drawable.ic_party_bg));
        browseByDressList.add(new HomePageBrowseByDStyleModel("GYM",R.drawable.ic_gym_bg));

        browseByDressRecycler.setAdapter(new BrowseByDressStyleAdapter(browseByDressList,HomePageFragment.this));
        newArrivalRecycler.setAdapter(new ProductDetailsForFragmentAdapter(newArrivalList,HomePageFragment.this));
        topSellingRecycler.setAdapter(new ProductDetailsForFragmentAdapter(newArrivalList,HomePageFragment.this));

        // Add Scroll Listener
        newArrivalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisible = newArrivalLayoutManager.findLastVisibleItemPosition();
                int totalItems = recyclerView.getAdapter().getItemCount();

                // Show right arrow if there are more items ahead
                if (lastVisible < totalItems - 1) {
                    rightIndicatorNewArrival.setVisibility(View.VISIBLE);
                } else {
                    rightIndicatorNewArrival.setVisibility(View.GONE);
                }
            }
        });
        // Add Scroll Listener
        topSellingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisible = topSellingLayoutManager.findLastVisibleItemPosition();
                int totalItems = recyclerView.getAdapter().getItemCount();

                // Show right arrow if there are more items ahead
                if (lastVisible < totalItems - 1) {
                    rightIndicatorTopSelling.setVisibility(View.VISIBLE);
                } else {
                    rightIndicatorTopSelling.setVisibility(View.GONE);
                }
            }
        });

        shopNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MensCasualClothesActivity.class));
            }
        });
        viewAllDressTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MensCasualClothesActivity.class);
                intent.putExtra("Title","Casual");
                startActivity(intent);
            }
        });
        topSellingViewAllTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MensCasualClothesActivity.class);
                intent.putExtra("Title","Casual");
                startActivity(intent);            }
        });

        getNewArrivalProducts();

        return  view;
    }

    private void getNewArrivalProducts() {
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
                                if (changingWishListIcon()) {
                                    newArrivalRecycler.setAdapter(new ProductDetailsForFragmentAdapter(newArrivalList, HomePageFragment.this));
                                }
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

    // Function to animate the count for brand, quality, and customer numbers
    private void animateCount(final TextView textView, final int targetValue, final int delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= targetValue; i++) {
                    final int finalI = i;
                    // Post the Runnable to the main thread to update the UI
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.valueOf(finalI) + "+");
                        }
                    }, i * delay);  // Delay based on the current count
                }
            }
        }).start();
    }
    private boolean changingWishListIcon() {
        ArrayList<ProductDetailsModel> wishlistItem = new ArrayList<>();
        wishlistItem = sessionManager.getWishList();
        Log.e("wishlist",wishlistItem.toString());
        for (int i = 0; i < newArrivalList.size(); i++) {
            for (int j = 0; j < wishlistItem.size(); j++) {
                if (newArrivalList.get(i).getProductId().equals(wishlistItem.get(j).getProductId())) {
                    newArrivalList.get(i).setWishListImgToggle(1);
                    Log.e("changing","true");
                }
            }
        }
        return true;
    }
}
