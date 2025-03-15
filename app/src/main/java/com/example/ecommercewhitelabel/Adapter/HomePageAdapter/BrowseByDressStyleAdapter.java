package com.example.ecommercewhitelabel.Adapter.HomePageAdapter;

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

import com.example.ecommercewhitelabel.Activities.MensCasualClothesActivity;
import com.example.ecommercewhitelabel.Model.HomePageBrowseByDStyleModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class BrowseByDressStyleAdapter extends RecyclerView.Adapter<BrowseByDressStyleAdapter.ViewHolder> {
    ArrayList<HomePageBrowseByDStyleModel> dressStyleModels;
    Fragment context;
    public BrowseByDressStyleAdapter(ArrayList<HomePageBrowseByDStyleModel> dressStyleModels, Fragment context) {
        this.dressStyleModels = dressStyleModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BrowseByDressStyleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_by_dress_style_item_layout,parent,false);
        return new BrowseByDressStyleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseByDressStyleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dressName.setText(dressStyleModels.get(position).getDressName());
        holder.dressImg.setImageResource(dressStyleModels.get(position).getDressImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), MensCasualClothesActivity.class);
                intent.putExtra("Title",dressStyleModels.get(position).getDressName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dressStyleModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dressName;
        ImageView dressImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dressName = itemView.findViewById(R.id.dressTxt);
            dressImg = itemView.findViewById(R.id.dressImg);

        }
    }
}
