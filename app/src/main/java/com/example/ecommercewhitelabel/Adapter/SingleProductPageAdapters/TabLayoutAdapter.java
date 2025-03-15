package com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments.FAQFragment;
import com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments.ProductSpecificationFragment;
import com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments.RatingAndReviewFragment;


public class TabLayoutAdapter extends FragmentStateAdapter {
    public TabLayoutAdapter(SingleProductDetailsActivity activity) {
        super(activity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("ViewPagerAdapter", "Creating fragment for position: " + position);
        switch (position) {
            case 1: {
                return new RatingAndReviewFragment();
            } case 2: {
                return new FAQFragment();
            }
            default:
                return new ProductSpecificationFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
