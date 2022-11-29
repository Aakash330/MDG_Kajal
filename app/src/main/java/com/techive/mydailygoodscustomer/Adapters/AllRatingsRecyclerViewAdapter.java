package com.techive.mydailygoodscustomer.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.ViewShopRating_Data;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllRatingsRecyclerViewAdapter extends RecyclerView.Adapter<AllRatingsRecyclerViewAdapter.AllRatingsViewHolder> {
    private static final String TAG = "AllRatingsRecyclerViewA";

    private List<ViewShopRating_Data> viewShopRating_dataList;

    @NonNull
    @NotNull
    @Override
    public AllRatingsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AllRatingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rating_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllRatingsRecyclerViewAdapter.AllRatingsViewHolder holder, int position) {
        displayStoreRatings(holder, position);
    }

    @Override
    public int getItemCount() {
        if (viewShopRating_dataList != null) {
            return viewShopRating_dataList.size();
        }
        return 0;
    }

    public void setViewShopRating_dataList(List<ViewShopRating_Data> viewShopRating_dataList) {
        Log.i(TAG, "setViewShopRating_dataList: fired!");
        this.viewShopRating_dataList = viewShopRating_dataList;
        notifyDataSetChanged();
    }

    private void displayStoreRatings(AllRatingsViewHolder holder, int position) {
        Log.i(TAG, "displayStoreRatings: fired!");

        holder.reviewerNameMaterialTextView.setText(viewShopRating_dataList.get(position).getName());
        holder.storeRatingAppCompatRatingBar.setRating(viewShopRating_dataList.get(position).getStar());
        holder.storeReviewMaterialTextView.setText(viewShopRating_dataList.get(position).getReviewMsg());
    }

    /*VIEWHOLDER*/
    static class AllRatingsViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView reviewerNameMaterialTextView, storeReviewMaterialTextView;

        AppCompatRatingBar storeRatingAppCompatRatingBar;

        public AllRatingsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            storeRatingAppCompatRatingBar = itemView.findViewById(R.id.storeRatingAppCompatRatingBar);
            reviewerNameMaterialTextView = itemView.findViewById(R.id.reviewerNameMaterialTextView);
            storeReviewMaterialTextView = itemView.findViewById(R.id.storeReviewMaterialTextView);
        }
    }
}
