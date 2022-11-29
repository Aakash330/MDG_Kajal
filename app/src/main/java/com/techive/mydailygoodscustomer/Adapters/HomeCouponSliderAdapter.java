package com.techive.mydailygoodscustomer.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techive.mydailygoodscustomer.Models.HomeModel_CouponData;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeCouponSliderAdapter extends SliderViewAdapter<HomeCouponSliderAdapter.HomeCouponViewHolder> {
    private static final String TAG = "HomeCouponRecyclerViewA";

    private List<HomeModel_CouponData> homeModel_couponDataArrayList;

    public HomeCouponSliderAdapter() {
        Log.i(TAG, "HomeCouponSliderAdapter: Empty Constructor fired!");
    }

    @Override
    public HomeCouponViewHolder onCreateViewHolder(ViewGroup parent) {
        return new HomeCouponViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coupon_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeCouponSliderAdapter.HomeCouponViewHolder holder, int position) {
//        Log.i(TAG, "onBindViewHolder: fired! position: " + position);
        holder.couponNameMaterialTextView.setText(homeModel_couponDataArrayList.get(position).getCoupon());
    }

    @Override
    public int getCount() {
        if (homeModel_couponDataArrayList != null) {
            return homeModel_couponDataArrayList.size();
        }
        return 0;
    }

    public void setHomeModel_couponDataArrayList(List<HomeModel_CouponData> homeModel_couponDataArrayList) {
        Log.i(TAG, "setHomeModel_couponDataArrayList: fired!");
        this.homeModel_couponDataArrayList = homeModel_couponDataArrayList;

        notifyDataSetChanged();
    }

    public static class HomeCouponViewHolder extends SliderViewAdapter.ViewHolder {

        MaterialTextView couponNameMaterialTextView;

        public HomeCouponViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            couponNameMaterialTextView = itemView.findViewById(R.id.couponNameMaterialTextView);
        }
    }
}
