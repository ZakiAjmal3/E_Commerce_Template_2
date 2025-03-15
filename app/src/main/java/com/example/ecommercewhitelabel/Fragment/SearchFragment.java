package com.example.ecommercewhitelabel.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.example.ecommercewhitelabel.R;

public class SearchFragment extends Fragment {
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the SearchView
        searchView = view.findViewById(R.id.searchView);

        // Set the SearchView to be expanded
        searchView.setIconified(false);

        // Add a small delay to ensure the SearchView is initialized before requesting focus
        new Handler().postDelayed(() -> {
            searchView.requestFocus();
            openKeyboard(searchView);
        }, 100);  // Adjust the delay if needed

        return view;
    }

    // Method to open the keyboard
    private void openKeyboard(View view) {
        // Get the InputMethodManager
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

        // Ensure the keyboard stays visible
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
