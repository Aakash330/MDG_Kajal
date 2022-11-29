package com.techive.mydailygoodscustomer.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeliveryAddressListRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryAddressListRecyclerViewAdapter.DeliveryAddressListViewHolder> {
    private static final String TAG = "DeliveryAddressListRecy";

    private List<Cart_CartData_BuyerAddress> cart_cartData_buyerAddressList;

    private int lastCheckedPosition, lastUncheckedPosition;

    private OnDeliveryAddressListener onDeliveryAddressListener;

    @NonNull
    @NotNull
    @Override
    public DeliveryAddressListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new DeliveryAddressListViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_delivery_address_row, parent, false), onDeliveryAddressListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeliveryAddressListRecyclerViewAdapter.DeliveryAddressListViewHolder holder, int position) {
        displayBuyerDeliveryAddress(holder, position);
    }

    @Override
    public int getItemCount() {
        if (cart_cartData_buyerAddressList != null) {
            return cart_cartData_buyerAddressList.size();
        }
        return 0;
    }

    public void setCart_cartData_buyerAddressList(List<Cart_CartData_BuyerAddress> cart_cartData_buyerAddressList) {
        Log.i(TAG, "setCart_cartData_buyerAddressList: fired!");
        this.cart_cartData_buyerAddressList = cart_cartData_buyerAddressList;
        notifyDataSetChanged();
    }

    public void setOnDeliveryAddressListener(OnDeliveryAddressListener onDeliveryAddressListener) {
        this.onDeliveryAddressListener = onDeliveryAddressListener;
    }

    private void displayBuyerDeliveryAddress(DeliveryAddressListRecyclerViewAdapter.DeliveryAddressListViewHolder holder, int position) {
        Log.i(TAG, "displayBuyerDeliveryAddress: fired!");

        holder.defaultDeliveryAddressMaterialRadioButton.setChecked(cart_cartData_buyerAddressList.get(position).getStatus() != 0);

        if (cart_cartData_buyerAddressList.get(position).getStatus() == 1) {
            lastCheckedPosition = position;
        }

        StringBuilder deliveryAddressStringBuilder = new StringBuilder();
        if (cart_cartData_buyerAddressList.get(position).getName() != null) {
            deliveryAddressStringBuilder.append(cart_cartData_buyerAddressList.get(position).getName()).append(" ");
        }
        if (cart_cartData_buyerAddressList.get(position).getLname() != null) {
            deliveryAddressStringBuilder.append(cart_cartData_buyerAddressList.get(position).getLname());
        }
        if (cart_cartData_buyerAddressList.get(position).getMobile() != null) {
            deliveryAddressStringBuilder.append("\n").append(cart_cartData_buyerAddressList.get(position).getMobile());
        }
        if (cart_cartData_buyerAddressList.get(position).getAddress() != null) {
            deliveryAddressStringBuilder.append("\n").append(cart_cartData_buyerAddressList.get(position).getAddress());
        }
        if (cart_cartData_buyerAddressList.get(position).getLandmark() != null) {
            deliveryAddressStringBuilder.append("\nLandmark: ").append(cart_cartData_buyerAddressList.get(position).getLandmark());
        }
        if (cart_cartData_buyerAddressList.get(position).getCity() != null) {
            deliveryAddressStringBuilder.append("\n").append(cart_cartData_buyerAddressList.get(position).getCity());
        }
        if (cart_cartData_buyerAddressList.get(position).getState() != null) {
            if (cart_cartData_buyerAddressList.get(position).getCity() != null) {
                deliveryAddressStringBuilder.append(", ").append(cart_cartData_buyerAddressList.get(position).getState());
            } else {
                deliveryAddressStringBuilder.append("\n").append(cart_cartData_buyerAddressList.get(position).getState());
            }
        }
        if (cart_cartData_buyerAddressList.get(position).getPincode() != null) {
            if (cart_cartData_buyerAddressList.get(position).getCity() != null || cart_cartData_buyerAddressList.get(position).getState() != null) {
                deliveryAddressStringBuilder.append(", ").append(cart_cartData_buyerAddressList.get(position).getPincode());
            } else {
                deliveryAddressStringBuilder.append("\n").append(cart_cartData_buyerAddressList.get(position).getPincode());
            }
        }
        holder.buyerDeliveryAddressMaterialTextView.setText(deliveryAddressStringBuilder.toString());
    }

    public void notifyDeliveryAddressDefault(int addressId, boolean defaultSuccess) {
        Log.i(TAG, "notifyDeliveryAddressDefault: fired!");

        for (int i = 0; i < cart_cartData_buyerAddressList.size(); i++) {
            if (cart_cartData_buyerAddressList.get(i).getId() == addressId) {
                if (defaultSuccess) {
                    lastUncheckedPosition = lastCheckedPosition;
                    cart_cartData_buyerAddressList.get(lastCheckedPosition).setStatus(0);
                    cart_cartData_buyerAddressList.get(i).setStatus(1);
                    notifyItemChanged(lastCheckedPosition);
                    notifyItemChanged(i);
                } else {
                    cart_cartData_buyerAddressList.get(lastUncheckedPosition).setStatus(1);
                    cart_cartData_buyerAddressList.get(i).setStatus(0);
                    notifyItemChanged(lastUncheckedPosition);
                    notifyItemChanged(i);
                }
                return;
            }
        }
    }

    /*VIEWHOLDER*/
    class DeliveryAddressListViewHolder extends RecyclerView.ViewHolder {

        MaterialRadioButton defaultDeliveryAddressMaterialRadioButton;

        MaterialTextView buyerDeliveryAddressMaterialTextView;

        MaterialButton editDeliveryAddressMaterialButton;

        OnDeliveryAddressListener onDeliveryAddressListener;

        public DeliveryAddressListViewHolder(@NonNull @NotNull View itemView, OnDeliveryAddressListener onDeliveryAddressListener) {
            super(itemView);

            defaultDeliveryAddressMaterialRadioButton = itemView.findViewById(R.id.defaultDeliveryAddressMaterialRadioButton);
            buyerDeliveryAddressMaterialTextView = itemView.findViewById(R.id.buyerDeliveryAddressMaterialTextView);
            editDeliveryAddressMaterialButton = itemView.findViewById(R.id.editDeliveryAddressMaterialButton);

            defaultDeliveryAddressMaterialRadioButton.setOnClickListener(onClickListener);
            editDeliveryAddressMaterialButton.setOnClickListener(onClickListener);

            this.onDeliveryAddressListener = onDeliveryAddressListener;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == defaultDeliveryAddressMaterialRadioButton.getId()) {
                    notifyDeliveryAddressDefault(cart_cartData_buyerAddressList.get(getAdapterPosition()).getId(), true);
                    onDeliveryAddressListener.setDefaultDeliveryAddress(cart_cartData_buyerAddressList.get(getAdapterPosition()).getId());
                } else if (view.getId() == editDeliveryAddressMaterialButton.getId()) {
                    Log.i(TAG, "onClick: Edit Delivery Address Material Button Clicked!");

                    onDeliveryAddressListener.editDeliveryAddress(cart_cartData_buyerAddressList.get(getAdapterPosition()));
                }
            }
        };
    }

    public interface OnDeliveryAddressListener {
        void setDefaultDeliveryAddress(int addressId);

        void editDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress);
    }
}
