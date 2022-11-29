package com.techive.mydailygoodscustomer.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Coupons;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Freebies;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Freebies_Products;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartFreebieRecyclerViewAdapter extends RecyclerView.Adapter<CartFreebieRecyclerViewAdapter.CartFreebieViewHolder> {
    private static final String TAG = "CartFreebieRecyclerView";

    private List<Cart_CartData_Freebies> cart_cartData_freebiesList;

    private int lastCheckedPosition = -1;

    private float cartTotalPrice;
    
    private OnCartFreebieListener onCartFreebieListener;

    public CartFreebieRecyclerViewAdapter() {
        Log.i(TAG, "CartFreebieRecyclerViewAdapter: Empty constructor fired!");
    }

    @NonNull
    @NotNull
    @Override
    public CartFreebieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartFreebieViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_cart_coupon_row, parent, false), onCartFreebieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartFreebieRecyclerViewAdapter.CartFreebieViewHolder holder, int position) {
        displayFreebieData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (cart_cartData_freebiesList != null) {
            return cart_cartData_freebiesList.size();
        }
        return 0;
    }

    public void setCart_cartData_freebiesList(List<Cart_CartData_Freebies> cart_cartData_freebiesList) {
        Log.i(TAG, "setCart_cartData_freebiesList: fired!");
        this.cart_cartData_freebiesList = cart_cartData_freebiesList;

        notifyDataSetChanged();
    }

    public void setOnCartFreebieListener(OnCartFreebieListener onCartFreebieListener) {
        this.onCartFreebieListener = onCartFreebieListener;
    }

    public void setLastCheckedPosition(int lastCheckedPosition) {
        Log.i(TAG, "setLastCheckedPosition: fired! lastCheckedPosition: " + lastCheckedPosition);
        this.lastCheckedPosition = lastCheckedPosition;
        notifyDataSetChanged();
    }

    private void displayFreebieData(CartFreebieRecyclerViewAdapter.CartFreebieViewHolder holder, int position) {
        Log.i(TAG, "displayFreebieData: fired!");

        holder.couponMaterialRadioButton.setText(cart_cartData_freebiesList.get(position).getName());
        holder.couponDescMaterialTextView.setText(cart_cartData_freebiesList.get(position).getDescription());

        holder.couponMaterialRadioButton.setChecked(lastCheckedPosition == position);
    }

    public void uncheckCurrentFreebie() {
        Log.i(TAG, "uncheckCurrentFreebie: fired!");

        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }

    /* VIEWHOLDER */
    /*static*/ class CartFreebieViewHolder extends RecyclerView.ViewHolder {

        MaterialRadioButton couponMaterialRadioButton;

        MaterialTextView couponDescMaterialTextView;

        OnCartFreebieListener onCartFreebieListener;

        public CartFreebieViewHolder(@NonNull @NotNull View itemView, OnCartFreebieListener onCartFreebieListener) {
            super(itemView);

            couponMaterialRadioButton = itemView.findViewById(R.id.couponMaterialRadioButton);
            couponDescMaterialTextView = itemView.findViewById(R.id.couponDescMaterialTextView);

            couponMaterialRadioButton.setOnClickListener(onClickListener);

            this.onCartFreebieListener = onCartFreebieListener;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Some view clicked!");

                Log.i(TAG, "onClick: lastCheckedPosition: " + lastCheckedPosition);
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = getAdapterPosition();
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);

                onCartFreebieListener.onFreebieClick(cart_cartData_freebiesList.get(getAdapterPosition()).getId(),
                        cart_cartData_freebiesList.get(getAdapterPosition()).getMin_cart_value(),
                        cart_cartData_freebiesList.get(getAdapterPosition()).getProducts());
            }
        };
    }

    public interface OnCartFreebieListener {

        void onFreebieClick(int freebieId, int minCartValue, List<Cart_CartData_Freebies_Products> cart_cartData_freebies_productsList);
    }
}
