package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class RecentlyAddedRecyclerViewAdapter extends RecyclerView.Adapter<RecentlyAddedRecyclerViewAdapter.RecentlyAddedViewHolder> {
    private static final String TAG = "RecentlyAddedRecyclerVi";

    private Context context;

    private /*static*/ List<ProductsModel_Data> productsModel_dataList;

    //1ST Integer = Product ID
    //2nd Integer = Order Qty
    private /*static*/ HashMap<Integer, Integer> prodIdOrderQtyHashMap;

    private /*static*/ HashMap<Integer, Boolean> prodIdLoadingHashMap;

    private OnProductCartListener onProductCartListener;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public RecentlyAddedRecyclerViewAdapter(Context context) {
        Log.i(TAG, "RecentlyAddedRecyclerViewAdapter: Context Based Constructor fired!");

        this.context = context;
        prodIdOrderQtyHashMap = new HashMap<>();
        prodIdLoadingHashMap = new HashMap<>();
    }

    @NonNull
    @NotNull
    @Override
    public RecentlyAddedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new RecentlyAddedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_row, parent, false), onProductCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentlyAddedRecyclerViewAdapter.RecentlyAddedViewHolder holder, int position) {

        if (productsModel_dataList.get(position).getMain_image() != null && !productsModel_dataList.get(position).getMain_image().matches("")) {
            Glide.with(context)
                    .load(productsModel_dataList.get(position).getMain_image())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.productImageAppCompatImageView);
        } else {
            Log.i(TAG, "onBindViewHolder: Product Main Image was NULL or EMPTY at position = " + position);
            holder.productImageAppCompatImageView.setImageResource(R.drawable.ic_image_24);
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(productsModel_dataList.get(position).getSale_price())).append(" |");
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableStringBuilder.toString().indexOf("|"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (productsModel_dataList.get(position).getPercent_discount() != 0) {
            holder.discountMaterialTextView.setVisibility(View.VISIBLE);
            holder.discountMaterialTextView.setText(productsModel_dataList.get(position).getPercent_discount() + "%");

            spannableStringBuilder.append(" ").append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(productsModel_dataList.get(position).getMrp_price()));
            spannableStringBuilder.setSpan(new StrikethroughSpan(), (spannableStringBuilder.toString().indexOf("|") + 2), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            holder.discountMaterialTextView.setVisibility(View.INVISIBLE);
            holder.discountMaterialTextView.setText("0%");

            spannableStringBuilder.delete(spannableStringBuilder.length()-2, spannableStringBuilder.length());
        }

        holder.productTitleMaterialTextView.setText(productsModel_dataList.get(position).getName());
        holder.pricesMaterialTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        holder.prodWeightMaterialTextView.setText(productsModel_dataList.get(position).getWeight());

        if (prodIdOrderQtyHashMap.containsKey(productsModel_dataList.get(position).getId())) {
            holder.toggleButtonsVisibility(false/*, true, true, true*/);
            holder.prodQtyMaterialTextView.setText(String.valueOf(prodIdOrderQtyHashMap.get(productsModel_dataList.get(position).getId())));
        } else {
            holder.toggleButtonsVisibility(true/*, false, false, false*/);
        }

        if (prodIdLoadingHashMap.containsKey(productsModel_dataList.get(position).getId())) {
            if (prodIdLoadingHashMap.get(productsModel_dataList.get(position).getId())) {
                //API BEING EXECUTED - Loading
                holder.incrementMaterialButton.setEnabled(false);
                holder.decrementMaterialButton.setEnabled(false);
            } else {
                //API EXECUTED - Completed
                holder.incrementMaterialButton.setEnabled(true);
                holder.decrementMaterialButton.setEnabled(true);
            }
        } else {
            holder.incrementMaterialButton.setEnabled(true);
            holder.decrementMaterialButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        if (productsModel_dataList != null) {
            return productsModel_dataList.size();
        }
        return 0;
    }

    public void setProductsModel_dataList(List<ProductsModel_Data> productsModel_dataList) {
        Log.i(TAG, "setProductsModel_dataList: fired!");
        this.productsModel_dataList = productsModel_dataList;

        notifyDataSetChanged();
    }

    public void setOnProductCartListener(OnProductCartListener onProductCartListener) {
        this.onProductCartListener = onProductCartListener;
    }

    public void setProdIdOrderQtyHashMap(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        Log.i(TAG, "setProdIdOrderQtyHashMap: fired!");
        if (prodIdOrderQtyHashMap == null) {
            this.prodIdOrderQtyHashMap = new HashMap<>();
            return;
        }
        if (prodIdOrderQtyHashMap.size() != 0) {
            this.prodIdOrderQtyHashMap = prodIdOrderQtyHashMap;
            notifyDataSetChanged();
        } else {
            Log.i(TAG, "setProdIdOrderQtyHashMap: Not notifying the RecentlyAddedAdapter for prodIdQtyHashMap = 0.");
        }
    }

    public void notifyProductAdded(int productId, int qty, boolean isAdded) {
        Log.i(TAG, "notifyProductAdded: fired! productId: " + productId + "\tqty: " + qty + "\tisAdded: " + isAdded);

        //CAN TRY TO USE HASHCODE TO QUICKLY FIND THE RIGHT PRODUCT
        for (int i = 0; i < productsModel_dataList.size(); i++) {
            if (productsModel_dataList.get(i).getId() == productId) {
                Log.i(TAG, "notifyProductAdded: Match found at " + i + " position in RecentlyAddedRecyclerViewAdapter!");
                if (isAdded) {
                    prodIdOrderQtyHashMap.put(productId, qty);
                } else {
                    prodIdOrderQtyHashMap.put(productId, --qty);
                }
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void notifyProductRemoved(int productId, int qty, boolean isRemoved) {
        Log.i(TAG, "notifyProductRemoved: fired! productId: " + productId + "\tqty: " + qty + "\tisRemoved: " + isRemoved);

        //CAN TRY TO USE HASHCODE TO QUICKLY FIND THE RIGHT PRODUCT
        for (int i = 0; i < productsModel_dataList.size(); i++) {
            if (productsModel_dataList.get(i).getId() == productId) {
                Log.i(TAG, "notifyProductRemoved: Match found at " + i + " position in RecentlyAddedRecyclerViewAdapter!");
                if (isRemoved) {
//                    prodIdOrderQtyHashMap.put(productId, qty);
                    if (qty != 0) {
                        prodIdOrderQtyHashMap.put(productId, qty);
                    } else {
                        prodIdOrderQtyHashMap.remove(productId);
                    }
                } else {
                    prodIdOrderQtyHashMap.put(productId, ++qty);
                }
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void notifyProductCompletelyRemoved(int productId) {
        Log.i(TAG, "notifyProductCompletelyRemoved: fired!\tproductId: " + productId);

        for (int i = 0; i < productsModel_dataList.size(); i++) {
            if (productsModel_dataList.get(i).getId() == productId) {
                prodIdOrderQtyHashMap.remove(productId);
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
            }
        }
    }

    /*VIEWHOLDER*/
    /*static*/ class RecentlyAddedViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView productMaterialCardView;

        AppCompatImageView productImageAppCompatImageView;

        MaterialTextView productTitleMaterialTextView, pricesMaterialTextView, prodWeightMaterialTextView, prodQtyMaterialTextView,
                discountMaterialTextView;

        /*AppCompatImageButton*/ MaterialButton cartMaterialButton, incrementMaterialButton, decrementMaterialButton;

        OnProductCartListener onProductCartListener;

        public RecentlyAddedViewHolder(@NonNull @NotNull View itemView, OnProductCartListener onProductCartListener) {
            super(itemView);

            productMaterialCardView = itemView.findViewById(R.id.productMaterialCardView);

            productTitleMaterialTextView = itemView.findViewById(R.id.productTitleMaterialTextView);
            pricesMaterialTextView = itemView.findViewById(R.id.pricesMaterialTextView);
            prodWeightMaterialTextView = itemView.findViewById(R.id.prodWeightMaterialTextView);
            prodQtyMaterialTextView = itemView.findViewById(R.id.prodQtyMaterialTextView);
            discountMaterialTextView = itemView.findViewById(R.id.discountMaterialTextView);
            productImageAppCompatImageView = itemView.findViewById(R.id.productImageAppCompatImageView);
            cartMaterialButton = itemView.findViewById(R.id.cartMaterialButton);
            incrementMaterialButton = itemView.findViewById(R.id.incrementMaterialButton);
            decrementMaterialButton = itemView.findViewById(R.id.decrementMaterialButton);

            productMaterialCardView.setOnClickListener(onClickListener);
            cartMaterialButton.setOnClickListener(onClickListener);
            incrementMaterialButton.setOnClickListener(onClickListener);
            decrementMaterialButton.setOnClickListener(onClickListener);

            this.onProductCartListener = onProductCartListener;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Some view Clicked!");

                if (view.getId() == productMaterialCardView.getId()) {

                    onProductCartListener.productClicked(productsModel_dataList.get(getAdapterPosition()).getId());
                } else if (view.getId() == cartMaterialButton.getId()) {

                    onProductCartListener.addProductToCart(productsModel_dataList.get(getAdapterPosition()).getId(), 1);
                } else if (view.getId() == incrementMaterialButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyMaterialTextView.getText().toString());

                    incrementMaterialButton.setEnabled(false);
                    decrementMaterialButton.setEnabled(false);
                    prodIdLoadingHashMap.put(productsModel_dataList.get(getAdapterPosition()).getId(), true);

                    onProductCartListener.addProductToCart(productsModel_dataList.get(getAdapterPosition()).getId(), ++currentQty);
                } else if (view.getId() == decrementMaterialButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyMaterialTextView.getText().toString());

                    incrementMaterialButton.setEnabled(false);
                    decrementMaterialButton.setEnabled(false);
                    prodIdLoadingHashMap.put(productsModel_dataList.get(getAdapterPosition()).getId(), true);

                    onProductCartListener.removeProductFromCart(productsModel_dataList.get(getAdapterPosition()).getId(), --currentQty);
                }
            }
        };

        private void toggleButtonsVisibility(boolean cartVisible) {
            Log.i(TAG, "toggleButtonsVisibility: fired!");

            if (cartVisible) {
                cartMaterialButton.setVisibility(View.VISIBLE);

                incrementMaterialButton.setVisibility(View.GONE);
                decrementMaterialButton.setVisibility(View.GONE);
                prodQtyMaterialTextView.setVisibility(View.GONE);
            } else {
                cartMaterialButton.setVisibility(View.GONE);

                incrementMaterialButton.setVisibility(View.VISIBLE);
                decrementMaterialButton.setVisibility(View.VISIBLE);
                prodQtyMaterialTextView.setVisibility(View.VISIBLE);
            }
        }

    }
}
