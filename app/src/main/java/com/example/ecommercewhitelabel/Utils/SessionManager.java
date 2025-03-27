package com.example.ecommercewhitelabel.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import java.util.HashMap;

public class SessionManager {
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private static final String PREF_NAME = "SessionPrefs";
    private static final String DEFAULT_VALUE = "DEFAULT";
    private static final String KEY_WISHLIST = "wishlist";
    private static final String KEY_CART = "cart";
    private static final String KEY_STORE_ID = "storeId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_AUTH_TOKEN = "authToken";
    public static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    public SessionManager(Context context) {
        this.ctx = context;
        this.sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
    }

    public HashMap<String, String> getUserData() {
        HashMap<String, String> user = new HashMap<>();
        user.put("userId", sharedPreferences.getString(KEY_USER_ID, DEFAULT_VALUE));
        user.put("authToken", sharedPreferences.getString(KEY_AUTH_TOKEN, DEFAULT_VALUE));
        user.put("fullName", sharedPreferences.getString(KEY_FULL_NAME, DEFAULT_VALUE));
        user.put("email", sharedPreferences.getString(KEY_EMAIL, DEFAULT_VALUE));
        user.put("role", sharedPreferences.getString(KEY_ROLE, DEFAULT_VALUE));
        return user;
    }

    public String getStoreId() {
        return sharedPreferences.getString(KEY_STORE_ID, "67d2b3da82e71e00672df277");
    }

    public void saveAuthToken(String userId, String authToken) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.apply();
    }

    public void saveUserDetails(String fullName, String email, String role) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        Log.d("SessionManager", "IsLoggedIn: " + isLoggedIn);
        return isLoggedIn;
    }

    public void logout() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_FULL_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_ROLE);
        editor.remove(IS_LOGGED_IN);
        editor.apply();
        Log.d("SessionManager", "Logged out, session cleared.");
    }
}
