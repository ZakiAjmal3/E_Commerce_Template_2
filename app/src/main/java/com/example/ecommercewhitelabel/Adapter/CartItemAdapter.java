package com.example.ecommercewhitelabel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Activities.OrderSingleViewActivity;
import com.example.ecommercewhitelabel.Fragment.CartItemFragment;
import com.example.ecommercewhitelabel.Model.CartItemModel;
import com.example.ecommercewhitelabel.Model.MyOrderModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;


public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    ArrayList<CartItemModel> productDetailsList;
    Fragment context;
    int quantity = 1;
    public CartItemAdapter(ArrayList<CartItemModel> productDetailsList, Fragment context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.productTitleTxt.setText(productDetailsList.get(position).getProductTitle());
        holder.productPriceTxt.setText("₹ " +productDetailsList.get(position).getProductPrice());
        holder.productSizeTxt.setText(productDetailsList.get(position).getProductSize());
        holder.productColorTxt.setText(productDetailsList.get(position).getProductColor());
        holder.productQuantityTxt.setText(productDetailsList.get(position).getProductQuantity());
//        holder.productImg.setImageResource(productDetailsList.get(position).getProductImage());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailsList.remove(position);
                notifyDataSetChanged();
                ((CartItemFragment) context).setOrderSummaryDetails();
                ((CartItemFragment) context).checkCartItemArraySize();
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(productDetailsList.get(position).getProductQuantity());
                quantity++;
                productDetailsList.get(position).setProductQuantity(String.valueOf(quantity));
                holder.productQuantityTxt.setText(String.valueOf(quantity));
                ((CartItemFragment) context).setOrderSummaryDetails();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(productDetailsList.get(position).getProductQuantity());
                if (quantity > 1) {
                    quantity--;
                    productDetailsList.get(position).setProductQuantity(String.valueOf(quantity));
                    holder.productQuantityTxt.setText(String.valueOf(quantity));
                    ((CartItemFragment) context).setOrderSummaryDetails();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitleTxt, productPriceTxt, productSizeTxt, productColorTxt, productQuantityTxt;
        ImageView productImg,deleteBtn,plus,minus;;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitleTxt = itemView.findViewById(R.id.productTitleTxt);
            productPriceTxt = itemView.findViewById(R.id.productPriceTxt);
            productSizeTxt = itemView.findViewById(R.id.sizeTxt);
            productColorTxt = itemView.findViewById(R.id.colorTxt);
            productQuantityTxt = itemView.findViewById(R.id.quantityTxt);
            plus = itemView.findViewById(R.id.quantityPlusTxt);
            minus = itemView.findViewById(R.id.quantityMinusTxt);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            productImg = itemView.findViewById(R.id.productImg);

        }
    }
}
