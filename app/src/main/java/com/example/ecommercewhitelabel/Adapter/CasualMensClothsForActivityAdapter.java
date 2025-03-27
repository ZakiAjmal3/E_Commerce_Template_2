package com.example.ecommercewhitelabel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommercewhitelabel.Activities.HomePageActivity;
import com.example.ecommercewhitelabel.Activities.SingleProductDetailsActivity;
import com.example.ecommercewhitelabel.Model.ProductDetailsModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;

import java.util.ArrayList;

public class CasualMensClothsForActivityAdapter extends RecyclerView.Adapter<CasualMensClothsForActivityAdapter.ViewHolder> {
    ArrayList<ProductDetailsModel> productDetailsList;
    Context context;
    SpannableStringBuilder spannableText;
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
        holder.productName.setText(productDetailsList.get(position).getProductTitle());
        holder.productName.setEllipsize(TextUtils.TruncateAt.END);
        holder.productName.setMaxLines(2);

        holder.productRating.setText(productDetailsList.get(position).getProductRating() + "/5");
//        holder.imageView.setImageResource(productDetailsList.get(position).getProductImage());
        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getProductRating()));

        if (!productDetailsList.get(position).getProductImagesModelsArrList().isEmpty()) {
            Glide.with(context).load(productDetailsList.get(position).getProductImagesModelsArrList().get(0).getProductImage()).into(holder.productImg);
        }else {
            Glide.with(context).load(R.drawable.no_image);
        }
        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getProductRating()));

        String originalPrice,disPercent,sellingPrice;
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

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductDetailsActivity.class);
                intent.putExtra("productId",productDetailsList.get(position).getProductId());
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
                    intent.putExtra("productId",productDetailsList.get(getAdapterPosition()).getProductId());
                    context.startActivity(intent);

                }
            });
        }
    }
}
