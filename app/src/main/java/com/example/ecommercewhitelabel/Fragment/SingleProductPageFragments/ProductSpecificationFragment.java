package com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.R;

public class ProductSpecificationFragment extends Fragment {
    WebView descriptionWebView;
    String productDescription;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_product_details_item_layout, container, false);

        descriptionWebView = view.findViewById(R.id.productDescriptionTxt);

        SingleProductDetailsActivity obj = (SingleProductDetailsActivity) getActivity();
        productDescription = obj.getDescription();
        descriptionWebView.loadData(productDescription, "text/html", "UTF-8");

        return view;
    }
}
