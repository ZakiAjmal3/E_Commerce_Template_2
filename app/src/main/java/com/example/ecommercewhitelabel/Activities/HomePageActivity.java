package com.example.ecommercewhitelabel.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Fragment.CartItemFragment;
import com.example.ecommercewhitelabel.Fragment.HomePageFragment;
import com.example.ecommercewhitelabel.Fragment.ProfileFragment;
import com.example.ecommercewhitelabel.Fragment.SearchFragment;
import com.example.ecommercewhitelabel.Fragment.WishListFragment;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.SessionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePageActivity extends AppCompatActivity {
    ImageView imgMenu;
    static Fragment currentFragment;
    RecyclerView casualDressRecyclerView;
    BottomNavigationView bottomNavigationView;
    Boolean loadOtherFragment = false;
    SessionManager sessionManager;
    String storeId,authToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(HomePageActivity.this, R.color.black));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sessionManager = new SessionManager(this);
        storeId = sessionManager.getStoreId();
        loadFragment(new HomePageFragment());

        imgMenu = findViewById(R.id.imgMenu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setWishlistCount();
        setCartCount();

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawerDialog();
            }
        });

        if (getIntent() != null){
            loadOtherFragment = getIntent().getBooleanExtra("LoadCartFrag",false);
        }

        if (loadOtherFragment){
            loadFragment(new CartItemFragment());
            bottomNavigationView.setSelectedItemId(R.id.cart);
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    loadFragment(new HomePageFragment());
                }else if (item.getItemId() == R.id.search){
                    loadFragment(new SearchFragment());
                }else if (item.getItemId() == R.id.wishlist){
                    loadFragment(new WishListFragment());
                }else if (item.getItemId() == R.id.cart){
                    loadFragment(new CartItemFragment());
                }else if (item.getItemId() == R.id.profile){
                    if (sessionManager.isLoggedIn()) {
                        loadFragment(new ProfileFragment());
                    }else {
                        startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                    }
                }
                return true;
            }
        });
        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentFragment instanceof HomePageFragment) {
                    // If on HomeFragment, use the default behavior
                    setEnabled(false); // Disable this callback
                } else {
                    // If on another fragment, navigate back to HomeFragment
                    loadFragment(new HomePageFragment());
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
            }
        });
    }
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        currentFragment = fragment;
    }
    Dialog drawerDialog;
    CardView cardBack;
    RelativeLayout layoutShop, layoutOnSale, layoutNewArrivals, layoutEBrands, layoutMyAccount, layoutLogin, layoutLogout;
    TextView mensClothesTxt, womensClothesTxt, kidsClothesTxt, bagsAndShoesTxt, profileTxt, ordersTxt,
            addressTxt, wishListTxt;
    LinearLayout myAccountItemsDropdown, shopItemsDropdown;
    Boolean isShopItemsDropdownVisible = false, isMyAccountItemsDropdownVisible = false;

    private void showDrawerDialog() {
        drawerDialog = new Dialog(HomePageActivity.this);
        drawerDialog.setContentView(R.layout.custom_drawer_home_menu);
        drawerDialog.setCancelable(true);

        cardBack = drawerDialog.findViewById(R.id.cardBack);
        layoutShop = drawerDialog.findViewById(R.id.layoutShop);
        layoutOnSale = drawerDialog.findViewById(R.id.layoutOnSale);
        layoutNewArrivals = drawerDialog.findViewById(R.id.layoutNewArrivals);
        layoutEBrands = drawerDialog.findViewById(R.id.layoutEBrands);
        layoutMyAccount = drawerDialog.findViewById(R.id.layoutMyAccount);
        layoutLogin = drawerDialog.findViewById(R.id.layoutLogin);
        layoutLogout = drawerDialog.findViewById(R.id.layoutLogout);
        myAccountItemsDropdown = drawerDialog.findViewById(R.id.myAccountItemsDropdown);
        shopItemsDropdown = drawerDialog.findViewById(R.id.shopItemsDropdown);
        shopItemsDropdown.setVisibility(View.GONE);
        myAccountItemsDropdown.setVisibility(View.GONE);

        mensClothesTxt = drawerDialog.findViewById(R.id.mensClothesTxt);
        womensClothesTxt = drawerDialog.findViewById(R.id.womensClothesTxt);
        kidsClothesTxt = drawerDialog.findViewById(R.id.kidsClothesTxt);
        bagsAndShoesTxt = drawerDialog.findViewById(R.id.bagsAndShoesTxt);
        profileTxt = drawerDialog.findViewById(R.id.profileTxt);
        ordersTxt = drawerDialog.findViewById(R.id.ordersTxt);
        addressTxt = drawerDialog.findViewById(R.id.addressTxt);
        wishListTxt = drawerDialog.findViewById(R.id.wishListTxt);

        if (sessionManager.isLoggedIn()){
            layoutLogin.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
        }else {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.GONE);
        }

        cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
            }
        });
        profileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragment());
                bottomNavigationView.setSelectedItemId(R.id.profile);
                drawerDialog.dismiss();
            }
        });
        wishListTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WishListFragment());
                bottomNavigationView.setSelectedItemId(R.id.wishlist);
                drawerDialog.dismiss();
            }
        });
        layoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShopItemsDropdownVisible) {
                    shopItemsDropdown.setVisibility(View.GONE);
                    isShopItemsDropdownVisible = false;
                } else {
                    shopItemsDropdown.setVisibility(View.VISIBLE);
                    isShopItemsDropdownVisible = true;
                }
            }
        });

        layoutMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyAccountItemsDropdownVisible) {
                    myAccountItemsDropdown.setVisibility(View.GONE);
                    isMyAccountItemsDropdownVisible = false;
                } else {
                    myAccountItemsDropdown.setVisibility(View.VISIBLE);
                    isMyAccountItemsDropdownVisible = true;
                }
            }
        });
        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
            }
        });
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Toast.makeText(HomePageActivity.this, "Logged out SuccessFully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mensClothesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","Men's Clothes");
                startActivity(intent);
            }
        });
        womensClothesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","Women's Clothes");
                startActivity(intent);            }
        });
        kidsClothesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","Kid's Clothes");
                startActivity(intent);            }
        });
        bagsAndShoesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","Bags and Shoes");
                startActivity(intent);            }
        });
        layoutOnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","On Sale Items");
                startActivity(intent);            }
        });
        layoutNewArrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","New Arrivals");
                startActivity(intent);            }
        });
        layoutEBrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MensCasualClothesActivity.class);
                intent.putExtra("Title","Brands");
                startActivity(intent);            }
        });
        ordersTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(HomePageActivity.this, MyOrdersActivity.class));
                }else {
                    startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                }
            }
        });
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(HomePageActivity.this,AddressShowingInputActivity.class));
                }else {
                    startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                }
            }
        });

        drawerDialog.show();
        drawerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        drawerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayLeft;
        drawerDialog.getWindow().setGravity(Gravity.TOP);
        drawerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerDialog.getWindow().setStatusBarColor(getColor(R.color.white));
        }
    }
    public static Fragment getCurrentFragment(){
        return currentFragment;
    }

    public void setWishlistCount(){
        int count = sessionManager.getWishListCount();
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.wishlist);
        if (count == 0){
            badge.setVisible(false);
        }else {
            badge.setVisible(true);
            badge.setNumber(count);
            badge.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.red));
            badge.setBadgeTextColor(ContextCompat.getColor(HomePageActivity.this, R.color.white));
        }
    }
    public void setCartCount(){
        int count = sessionManager.getCartCount();
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.cart);
        if (count == 0){
            badge.setVisible(false);
        }else {
            badge.setVisible(true);
            badge.setNumber(count);
            badge.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.red));
            badge.setBadgeTextColor(ContextCompat.getColor(HomePageActivity.this, R.color.white));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setWishlistCount();
        setCartCount();
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof WishListFragment) {
            bottomNavigationView.setSelectedItemId(R.id.wishlist);
        }else if (fragment instanceof CartItemFragment){
            bottomNavigationView.setSelectedItemId(R.id.cart);
        }else if (fragment instanceof ProfileFragment){
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }else if (fragment instanceof HomePageFragment){
            bottomNavigationView.setSelectedItemId(R.id.home);
        }else if (fragment instanceof SearchFragment){
            bottomNavigationView.setSelectedItemId(R.id.search);
        }
    }
}