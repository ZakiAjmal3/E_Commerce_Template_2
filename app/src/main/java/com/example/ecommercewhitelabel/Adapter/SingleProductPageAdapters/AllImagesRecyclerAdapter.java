package com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class AllImagesRecyclerAdapter extends RecyclerView.Adapter<AllImagesRecyclerAdapter.ViewHolder> {
    ArrayList<String> productDetailsList;
    Context context;
    public AllImagesRecyclerAdapter(ArrayList<String> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllImagesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_page_product_image_recycler_item_layout,parent,false);
        return new AllImagesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllImagesRecyclerAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(productDetailsList.get(position)).error(R.drawable.no_image).into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }
}
