package com.example.ecommercewhitelabel.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.HomePageAdapter.ProductDetailsForFragmentAdapter;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class WishListFragment extends Fragment {
    RecyclerView wishListRecycler;
    ArrayList<ProductDetailsModel> newArrivalList;
    RelativeLayout noDataLayout,mainLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        noDataLayout = view.findViewById(R.id.noDataLayout);
        mainLayout = view.findViewById(R.id.mainLayout);
        wishListRecycler = view.findViewById(R.id.wishListRecyclerView);
        wishListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        newArrivalList = new ArrayList<>();
        newArrivalList.add(new ProductDetailsModel("Product 1","200","2.5",0));
        newArrivalList.add(new ProductDetailsModel("Product 2","300","5",0));
        newArrivalList.add(new ProductDetailsModel("Product 3","250","3",0));
        newArrivalList.add(new ProductDetailsModel("Product 4","3000","4.5",0));
        newArrivalList.add(new ProductDetailsModel("Product 5","199","1.2",0));

        wishListRecycler.setAdapter(new ProductDetailsForFragmentAdapter(newArrivalList, WishListFragment.this));
        checkWishListItemArraySize();
        return  view;
    }
    public void checkWishListItemArraySize() {
        if (newArrivalList.isEmpty()){
            mainLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }else {
            mainLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }
}
