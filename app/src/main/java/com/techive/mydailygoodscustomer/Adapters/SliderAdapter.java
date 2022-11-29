package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techive.mydailygoodscustomer.R;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    private static final String TAG = "SliderAdapter";

    private List<String> sliderItems;
//    private List<Integer> intSliderItems;   //TEMP

    private Context context;

    private static final String vendorImageLocation = "https://www.mydailygoods.com/";

    public SliderAdapter() {
        Log.i(TAG, "SliderAdapter: Empty Constructor fired!");
    }

    public SliderAdapter(Context context) {
        Log.i(TAG, "SliderAdapter: Context based Constructor fired!");
        this.context = context;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.i(TAG, "onCreateViewHolder: fired!");
        return new SliderAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_slider_view_row, parent, false));
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
//        Log.i(TAG, "onBindViewHolder: fired! position: " + position);

//        viewHolder.sliderAppCompatImageView.setImageResource(intSliderItems.get(position));

        //NEED TO CHECK IF GLIDE IS CONSTANTLY RE-DOWNLOADING THE IMAGES EVERY TIME.
        //I DON'T THINK THE IMAGES ARE BEING CONSTANTLY DOWNLOADED, BECAUSE THEY ARE STILL VISIBLE AFTER THE FIRST DOWNLOAD,
        //WHEN THE INTERNET IS OFF. MOST PROBABLY, GLIDE KEEPS THOSE DOWNLOADED IMAGES EITHER IN THE LOCAL MEMORY OR SOME CACHE.
        Glide.with(context)
                .load(vendorImageLocation + sliderItems.get(position))
                .placeholder(R.drawable.ic_image_24)
                .error(R.drawable.ic_broken_image_24)
                .into(viewHolder.sliderAppCompatImageView);
    }

    @Override
    public int getCount() {
        if (sliderItems != null) {
            return sliderItems.size();
        }
        //TEMP
//        if (intSliderItems != null) {
//            return intSliderItems.size();
//        }
        return 0;
    }

    public void setSliderItems(List<String> sliderItems) {
        this.sliderItems = sliderItems;
        notifyDataSetChanged();
    }

    //TEMP
//    public void setIntSliderItems(List<Integer> intSliderItems) {
//        this.intSliderItems = intSliderItems;
//        notifyDataSetChanged();
//    }

    public static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        AppCompatImageView sliderAppCompatImageView;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);

            sliderAppCompatImageView = itemView.findViewById(R.id.sliderAppCompatImageView);
        }
    }
}
