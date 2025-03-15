package com.example.ecommercewhitelabel.Fragment.SingleProductPageFragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.RatingAndReviewAdapter;
import com.example.ecommercewhitelabel.Model.RatingAndReviewModel;
import com.example.ecommercewhitelabel.R;

import java.util.ArrayList;

public class RatingAndReviewFragment extends Fragment {
    Button writeReviewBtn;
    RecyclerView reviewRecyclerView;
    private final String[] statusArrayItems = {"Latest","Most Relevant", "Oldest"};
    ArrayList<RatingAndReviewModel> ratingAndReviewArrayList;
    ArrayAdapter<String> dropdownArrayAdapter;
    AutoCompleteTextView autoCompleteDropdownTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ratings_and_review_tab_item_layout, container, false);

        writeReviewBtn = view.findViewById(R.id.writeReviewBtn);
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        autoCompleteDropdownTV = view.findViewById(R.id.autoCompleteTextView);

        String[] languages = getResources().getStringArray(R.array.rating_and_review_sort_by_item);
        dropdownArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item_text_layout, languages);
        autoCompleteDropdownTV.setAdapter(dropdownArrayAdapter);
        autoCompleteDropdownTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));

        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratingAndReviewArrayList = new ArrayList<>();
        ratingAndReviewArrayList.add(new RatingAndReviewModel("3.5","Alex k.","Posted on August 14,2024","Product description"));
        ratingAndReviewArrayList.add(new RatingAndReviewModel("1.5","Zaki Ajmal","Posted on October 14,2024","Product description"));
        ratingAndReviewArrayList.add(new RatingAndReviewModel("5","Alex k.","Posted on August 14,2024","Product description"));
        ratingAndReviewArrayList.add(new RatingAndReviewModel("3.5","Alex k.","Posted on August 14,2024","Product description"));
        ratingAndReviewArrayList.add(new RatingAndReviewModel("3.5","Alex k.","Posted on August 14,2024","Product description"));

        reviewRecyclerView.setAdapter(new RatingAndReviewAdapter(ratingAndReviewArrayList, getContext()));

        return view;
    }
}
