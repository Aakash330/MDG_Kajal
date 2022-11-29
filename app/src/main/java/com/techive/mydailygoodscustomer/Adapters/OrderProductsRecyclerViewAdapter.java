package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.OrderHistory_Data_Products;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderProductsRecyclerViewAdapter extends RecyclerView.Adapter<OrderProductsRecyclerViewAdapter.OrderProductsViewHolder> {
    private static final String TAG = "OrderProductsRecyclerVi";

    private List<OrderHistory_Data_Products> orderHistory_data_productsList;

    private String imagePath;

    private Context context;

    public OrderProductsRecyclerViewAdapter(Context context) {
        Log.i(TAG, "OrderProductsRecyclerViewAdapter: Context based Constructor fired!");

        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public OrderProductsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new OrderProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_products_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderProductsRecyclerViewAdapter.OrderProductsViewHolder holder, int position) {
        displayOrderProducts(holder, position);
    }

    @Override
    public int getItemCount() {
        if (orderHistory_data_productsList != null) {
            return orderHistory_data_productsList.size();
        }
        return 0;
    }

    public void setOrderHistory_data_productsList(List<OrderHistory_Data_Products> orderHistory_data_productsList, String imagePath) {
        this.orderHistory_data_productsList = orderHistory_data_productsList;
        this.imagePath = imagePath;
        notifyDataSetChanged();
    }

    private void displayOrderProducts(OrderProductsViewHolder holder, int position) {
        Log.i(TAG, "displayOrderProducts: fired!");

        Glide.with(context)
                .load(imagePath + orderHistory_data_productsList.get(position).getImage())
                .placeholder(R.drawable.ic_image_24)
                .error(R.drawable.ic_broken_image_24)
                .into(holder.orderProductAppCompatImageView);

        holder.orderProductNameMaterialTextView.setText(orderHistory_data_productsList.get(position).getName());
        holder.orderProductQtyMaterialTextView.setText("Quantity: " + orderHistory_data_productsList.get(position).getNew_qty() + " Items");
        holder.orderProductPriceMaterialTextView.setText(context.getString(R.string.rupee_symbol) + " " + orderHistory_data_productsList.get(position).getSale_price() + " x "
                + orderHistory_data_productsList.get(position).getNew_qty());
        holder.orderProductTotalPriceMaterialTextView.setText(context.getString(R.string.rupee_symbol) + " " + orderHistory_data_productsList.get(position).getTotalPrice());
    }

    /*VIEWHOLDER*/
    static class OrderProductsViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView orderProductAppCompatImageView;

        MaterialTextView orderProductNameMaterialTextView, orderProductQtyMaterialTextView, orderProductPriceMaterialTextView,
                orderProductTotalPriceMaterialTextView;

        public OrderProductsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            orderProductAppCompatImageView = itemView.findViewById(R.id.orderProductAppCompatImageView);
            orderProductNameMaterialTextView = itemView.findViewById(R.id.orderProductNameMaterialTextView);
            orderProductQtyMaterialTextView = itemView.findViewById(R.id.orderProductQtyMaterialTextView);
            orderProductPriceMaterialTextView = itemView.findViewById(R.id.orderProductPriceMaterialTextView);
            orderProductTotalPriceMaterialTextView = itemView.findViewById(R.id.orderProductTotalPriceMaterialTextView);
        }
    }
}
