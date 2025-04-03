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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        String orderDate = productDetailsList.get(position).getReviewDate();
        String formattedDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        try {
            Date date = simpleDateFormat.parse(orderDate); // Parse timestamp
            formattedDate = outputFormat.format(date); // Convert to required format
            System.out.println(formattedDate); // Output: March 27, 2025
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.reviewDateTxt.setText(formattedDate);

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

            if (productDetailsList.size() >2){
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                if (params != null) {
                    params.width = (int) (250 * itemView.getContext().getResources().getDisplayMetrics().density);
                    itemView.setLayoutParams(params);
                }
            }else {
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                if (params != null) {
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;;
                    itemView.setLayoutParams(params);
                }
            }
        }
    }
}
