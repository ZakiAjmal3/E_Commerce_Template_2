package com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommercewhitelabel.R;

import java.io.File;
import java.util.ArrayList;

public class ReviewImagesListAdapter extends RecyclerView.Adapter<ReviewImagesListAdapter.ViewHolder> {
    ArrayList<File> imageFiles;
    ArrayList<String> imageURLList;
    Context context;
    public ReviewImagesListAdapter(Context context, ArrayList<File> imageFiles, ArrayList<String> imageURLList) {
        this.context = context;
        this.imageFiles = imageFiles;
        this.imageURLList = imageURLList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_images_item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (imageFiles != null) {
            if (position < imageFiles.size()) {
                File imageFile = imageFiles.get(position);
                Glide.with(context)
                        .load(imageFile) // Load the image from the file
                        .into(holder.productImg);  // Bind the image to the ImageView
            }
        }else {
            Glide.with(context)
                    .load(imageURLList.get(position)) // Load the image from the file
                    .into(holder.productImg);  // Bind the image to the ImageView
        }
        holder.crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageFiles != null) {
                    if (imageFiles.size() == 1) {
                        Toast.makeText(context, "Please Add More Images in order to remove this image", Toast.LENGTH_LONG).show();
                    } else {
                        imageFiles.remove(position);
                        notifyItemRemoved(position);
                    }
                }else {
                    imageURLList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (imageFiles != null)
            return imageFiles.size();
        else return imageURLList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        CardView crossBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.imgView);
            crossBtn = itemView.findViewById(R.id.crossBtn);
        }
    }
}
