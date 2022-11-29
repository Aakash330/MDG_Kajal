package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techive.mydailygoodscustomer.Models.HomeModel_BannerData;
import com.techive.mydailygoodscustomer.R;

import java.util.List;

public class HomeBanner1SliderAdapter extends SliderViewAdapter<HomeBanner1SliderAdapter.HomeBanner1SliderViewHolder> {
    private static final String TAG = "HomeBanner1SliderAdapte";

    private Context context;

    private List<HomeModel_BannerData> homeModel_bannerDataList;
    //TEMP
//    ArrayList<Integer> integerArrayList;

    public HomeBanner1SliderAdapter(Context context) {
        Log.i(TAG, "HomeBanner1SliderAdapter: Empty Constructor fired!");

        this.context = context;
        //TEMP
//        integerArrayList = new ArrayList<>();
//        integerArrayList.add(R.drawable.ic_account_circle_24);
//        integerArrayList.add(R.drawable.ic_arrow_forward_24);
//        integerArrayList.add(R.drawable.banner1);
//        integerArrayList.add(R.drawable.banner2);
//        integerArrayList.add(R.drawable.banner3);
    }

    @Override
    public HomeBanner1SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.i(TAG, "onCreateViewHolder: fired!");
        return new HomeBanner1SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner1_row, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeBanner1SliderViewHolder viewHolder, int position) {
//        Log.i(TAG, "onBindViewHolder: fired! position: " + position);

        Glide.with(context)
                .load(homeModel_bannerDataList.get(position).getMobile_banner())
                .placeholder(R.drawable.ic_image_24)
                .error(R.drawable.ic_broken_image_24)
                .into(viewHolder.banner1AppCompatImageView);

        viewHolder.banner1Text1MaterialTextView.setText(homeModel_bannerDataList.get(position).getHeading());
        viewHolder.banner1Text2MaterialTextView.setText(homeModel_bannerDataList.get(position).getSub_heading());
    }

    @Override
    public int getCount() {
        if (homeModel_bannerDataList != null) {
            return homeModel_bannerDataList.size();
        }
        return 0;
    }

    public void setHomeModel_bannerDataList(List<HomeModel_BannerData> homeModel_bannerDataList) {
        Log.i(TAG, "setHomeModel_bannerDataList: fired!");
        this.homeModel_bannerDataList = homeModel_bannerDataList;
        notifyDataSetChanged();
    }

    public static class HomeBanner1SliderViewHolder extends SliderViewAdapter.ViewHolder {

        AppCompatImageView banner1AppCompatImageView;

        MaterialTextView banner1Text1MaterialTextView, banner1Text2MaterialTextView;

        public HomeBanner1SliderViewHolder(View itemView) {
            super(itemView);

            banner1AppCompatImageView = itemView.findViewById(R.id.banner1AppCompatImageView);
            banner1Text1MaterialTextView = itemView.findViewById(R.id.banner1Text1MaterialTextView);
            banner1Text2MaterialTextView = itemView.findViewById(R.id.banner1Text2MaterialTextView);
        }
    }
}
