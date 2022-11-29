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

public class ProductDetailsImagesSliderAdapter extends SliderViewAdapter<ProductDetailsImagesSliderAdapter.ProductDetailsImagesViewHolder> {
    private static final String TAG = "ProductDetailsImagesSli";

    private List<String> productImagesList;

    private Context context;

    public ProductDetailsImagesSliderAdapter(Context context) {
        Log.i(TAG, "ProductDetailsImagesSliderAdapter: Empty constructor fired!");
        this.context = context;
    }

    @Override
    public ProductDetailsImagesViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ProductDetailsImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_images_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductDetailsImagesViewHolder viewHolder, int position) {
        Glide.with(context)
                .load(productImagesList.get(position))
                .placeholder(R.drawable.ic_image_24)
                .error(R.drawable.ic_broken_image_24)
                .into(viewHolder.productImageAppCompatImageView);
    }

    @Override
    public int getCount() {
        if (productImagesList != null) {
            return productImagesList.size();
        }
        return 0;
    }

    public void setProductImagesList(List<String> productImagesList) {
        Log.i(TAG, "setProductImagesList: fired!");
        this.productImagesList = productImagesList;
        notifyDataSetChanged();
    }

    /* VIEWHOLDER */
    public static class ProductDetailsImagesViewHolder extends SliderViewAdapter.ViewHolder {

        AppCompatImageView productImageAppCompatImageView;

        public ProductDetailsImagesViewHolder(View itemView) {
            super(itemView);

            productImageAppCompatImageView = itemView.findViewById(R.id.productImageAppCompatImageView);
        }
    }
}
