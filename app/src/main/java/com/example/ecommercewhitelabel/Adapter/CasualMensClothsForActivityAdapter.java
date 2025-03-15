package com.example.ecommercewhitelabel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Activities.HomePageActivity;
import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;

import java.util.ArrayList;

public class CasualMensClothsForActivityAdapter extends RecyclerView.Adapter<CasualMensClothsForActivityAdapter.ViewHolder> {
    ArrayList<ProductDetailsModel> productDetailsList;
    Context context;
    public CasualMensClothsForActivityAdapter(ArrayList<ProductDetailsModel> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public CasualMensClothsForActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dress_item_layout_for_all,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CasualMensClothsForActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(productDetailsList.get(position).getProductName());
        holder.productPrice.setText("₹ " + productDetailsList.get(position).getProductPrice());
        holder.productRating.setText(productDetailsList.get(position).getProductRating() + "/5");
//        holder.imageView.setImageResource(productDetailsList.get(position).getProductImage());
        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getProductRating()));

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("LoadCartFrag",true);
                context.startActivity(intent);
            }
        });

        holder.wishlistImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state;
                state = productDetailsList.get(position).getWishListImgToggle();
                if (state == 0) {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_red);
                    productDetailsList.get(position).setWishListImgToggle(1);
                }else {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_grey);
                    productDetailsList.get(position).setWishListImgToggle(0);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productRating;
        ImageView productImg,wishlistImg;
        CustomRatingBar ratingBar;
        Button buyBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTxt);
            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productRating = itemView.findViewById(R.id.ratingTxt);
            productImg = itemView.findViewById(R.id.productImg);
            wishlistImg = itemView.findViewById(R.id.wishlistImg);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            buyBtn = itemView.findViewById(R.id.buy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleProductDetailsActivity.class);
                    intent.putExtra("productName",productDetailsList.get(getAdapterPosition()).getProductName());
                    intent.putExtra("productPrice",productDetailsList.get(getAdapterPosition()).getProductPrice());
                    intent.putExtra("productRating",productDetailsList.get(getAdapterPosition()).getProductRating());
                    context.startActivity(intent);

                }
            });
        }
    }
}
