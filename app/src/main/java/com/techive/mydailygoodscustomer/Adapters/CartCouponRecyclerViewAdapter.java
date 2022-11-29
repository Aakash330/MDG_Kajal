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
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartCouponRecyclerViewAdapter extends RecyclerView.Adapter<CartCouponRecyclerViewAdapter.CartCouponViewHolder> {
    private static final String TAG = "CartCouponRecyclerViewA";

    private List<Cart_CartData_Coupons> cart_cartData_couponsList;

    private int lastCheckedPosition = -1;

    private OnCartCouponListener onCartCouponListener;

    public CartCouponRecyclerViewAdapter() {
        Log.i(TAG, "CartCouponRecyclerViewAdapter: Empty constructor fired!");
    }

    @NonNull
    @NotNull
    @Override
    public CartCouponViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartCouponViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_coupon_row, parent, false), onCartCouponListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartCouponRecyclerViewAdapter.CartCouponViewHolder holder, int position) {
        displayCouponData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (cart_cartData_couponsList != null) {
            return cart_cartData_couponsList.size();
        }
        return 0;
    }

    public void setCart_cartData_couponsList(List<Cart_CartData_Coupons> cart_cartData_couponsList) {
        Log.i(TAG, "setCart_cartData_couponsList: fired!");
        this.cart_cartData_couponsList = cart_cartData_couponsList;

        notifyDataSetChanged();
    }

    public void setOnCartCouponListener(OnCartCouponListener onCartCouponListener) {
        this.onCartCouponListener = onCartCouponListener;
    }

    public void setLastCheckedPosition(int lastCheckedPosition) {
        Log.i(TAG, "setLastCheckedPosition: fired! lastCheckedPosition: " + lastCheckedPosition);
        this.lastCheckedPosition = lastCheckedPosition;
        notifyDataSetChanged();
    }

    private void displayCouponData(CartCouponRecyclerViewAdapter.CartCouponViewHolder holder, int position) {
        Log.i(TAG, "displayCouponData: fired!");

        //CHECKING STATUS OF COUPON
        holder.couponMaterialRadioButton.setEnabled(cart_cartData_couponsList.get(position).getStatus().matches("1"));

        holder.couponMaterialRadioButton.setText(cart_cartData_couponsList.get(position).getName()
                + "  (" + cart_cartData_couponsList.get(position).getMsg() + ")");
        holder.couponDescMaterialTextView.setText(cart_cartData_couponsList.get(position).getDescription());

        holder.couponMaterialRadioButton.setChecked(lastCheckedPosition == position);
    }

    public void uncheckCurrentCoupon() {
        Log.i(TAG, "uncheckCurrentCoupon: fired!");

        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }

    /* VIEWHOLDER */
    /*static*/ class CartCouponViewHolder extends RecyclerView.ViewHolder {

        MaterialRadioButton couponMaterialRadioButton;

        MaterialTextView couponDescMaterialTextView;

        OnCartCouponListener onCartCouponListener;

        public CartCouponViewHolder(@NonNull @NotNull View itemView, OnCartCouponListener onCartCouponListener) {
            super(itemView);

            couponMaterialRadioButton = itemView.findViewById(R.id.couponMaterialRadioButton);
            couponDescMaterialTextView = itemView.findViewById(R.id.couponDescMaterialTextView);

            couponMaterialRadioButton.setOnClickListener(onClickListener);

            this.onCartCouponListener = onCartCouponListener;
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

                onCartCouponListener.onCouponClick(cart_cartData_couponsList.get(getAdapterPosition()));
            }
        };
    }

    public interface OnCartCouponListener {

        void onCouponClick(Cart_CartData_Coupons cart_cartData_coupons);
    }
}
