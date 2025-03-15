package com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Model.RatingAndReviewModel;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.CustomRatingBar;

import java.util.ArrayList;

public class RatingAndReviewAdapter extends RecyclerView.Adapter<RatingAndReviewAdapter.ViewHolder> {
    ArrayList<RatingAndReviewModel> productDetailsList;
    Context context;
    public RatingAndReviewAdapter(ArrayList<RatingAndReviewModel> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAndReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler_item_layout,parent,false);
        return new RatingAndReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAndReviewAdapter.ViewHolder holder, int position) {

        holder.ratingBar.setRating(Float.parseFloat(productDetailsList.get(position).getRating()));
        holder.reviewerNameTxt.setText(productDetailsList.get(position).getReviewerName());
        holder.reviewContentTxt.setText(productDetailsList.get(position).getReviewContent());
        holder.reviewDateTxt.setText(productDetailsList.get(position).getReviewDate());

    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomRatingBar ratingBar;
        TextView reviewerNameTxt,reviewContentTxt,reviewDateTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewerNameTxt = itemView.findViewById(R.id.reviewerNameTxt);
            reviewContentTxt = itemView.findViewById(R.id.reviewContentTxt);
            reviewDateTxt = itemView.findViewById(R.id.reviewDateTxt);

        }
    }
}
