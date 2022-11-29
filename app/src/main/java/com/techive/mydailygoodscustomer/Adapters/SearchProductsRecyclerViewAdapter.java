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
import com.techive.mydailygoodscustomer.Models.SearchProducts_Products_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class    SearchProductsRecyclerViewAdapter extends RecyclerView.Adapter<SearchProductsRecyclerViewAdapter.BestDealsViewHolder> {
    private static final String TAG = "SearchProductsRecyclerV";

    private Context context;

    private List<SearchProducts_Products_Data> searchProducts_products_dataList;

    //1ST Integer = Product ID
    //2nd Integer = Order Qty
    private /*static*/ HashMap<Integer, Integer> prodIdOrderQtyHashMap;

    private /*static*/ HashMap<Integer, Boolean> prodIdLoadingHashMap;

    private OnProductCartListener onProductCartListener;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public SearchProductsRecyclerViewAdapter(Context context) {
        Log.i(TAG, "SearchProductsRecyclerViewAdapter: Context Based Constructor fired!");

        this.context = context;
        prodIdOrderQtyHashMap = new HashMap<>();
        prodIdLoadingHashMap = new HashMap<>();
    }

    @NonNull
    @NotNull
    @Override
    public BestDealsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new BestDealsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_row, parent, false), onProductCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchProductsRecyclerViewAdapter.BestDealsViewHolder holder, int position) {

        displaySearchedProducts(holder, position);
    }

    @Override
    public int getItemCount() {
        if (searchProducts_products_dataList != null) {
            return searchProducts_products_dataList.size();
        }
        return 0;
    }

    private void displaySearchedProducts(SearchProductsRecyclerViewAdapter.BestDealsViewHolder holder, int position) {
        Log.i(TAG, "displaySearchedProducts: fired!");

        if (searchProducts_products_dataList.get(position).getImage() != null && !searchProducts_products_dataList.get(position).getImage().matches("")) {
            Glide.with(context)
                    .load(searchProducts_products_dataList.get(position).getImage())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.productImageAppCompatImageView);
        } else {
            Log.i(TAG, "onBindViewHolder: Product Main Image was NULL or EMPTY at position = " + position);
            holder.productImageAppCompatImageView.setImageResource(R.drawable.ic_image_24);
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(String.valueOf(searchProducts_products_dataList.get(position).getSale_price())).append(" INR").append(" |");
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableStringBuilder.toString().indexOf("|"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        spannableStringBuilder.append(" ").append(String.valueOf(searchProducts_products_dataList.get(position).getMrp_price())).append(" INR");
        spannableStringBuilder.setSpan(new StrikethroughSpan(), (spannableStringBuilder.toString().indexOf("|") + 2), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.productTitleMaterialTextView.setText(searchProducts_products_dataList.get(position).getName());
        holder.pricesMaterialTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        holder.prodWeightMaterialTextView.setText(searchProducts_products_dataList.get(position).getWeight());

        if (prodIdOrderQtyHashMap.containsKey(searchProducts_products_dataList.get(position).getId())) {
            holder.toggleButtonsVisibility(false);
            holder.prodQtyMaterialTextView.setText(String.valueOf(prodIdOrderQtyHashMap.get(searchProducts_products_dataList.get(position).getId())));
        } else {
            holder.toggleButtonsVisibility(true);
        }

        if (prodIdLoadingHashMap.containsKey(searchProducts_products_dataList.get(position).getId())) {
            if (prodIdLoadingHashMap.get(searchProducts_products_dataList.get(position).getId())) {
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

        if (searchProducts_products_dataList.get(position).getMrp_price() != 0
                && searchProducts_products_dataList.get(position).getSale_price() != 0) {

            float discount = 100 - ((searchProducts_products_dataList.get(position).getSale_price() * 100) / searchProducts_products_dataList.get(position).getMrp_price());

            if (discount != 0) {
                holder.discountMaterialTextView.setVisibility(View.VISIBLE);
                holder.discountMaterialTextView.setText(df.format(discount) + "%");
            } else {
                holder.discountMaterialTextView.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.discountMaterialTextView.setVisibility(View.GONE);
        }
    }

    public void setSearchProducts_products_dataList(List<SearchProducts_Products_Data> searchProducts_products_dataList) {
        Log.i(TAG, "setSearchProducts_products_dataList: fired!");
        this.searchProducts_products_dataList = searchProducts_products_dataList;

        notifyDataSetChanged();
    }

    public void setOnProductCartListener(OnProductCartListener onProductCartListener) {
        this.onProductCartListener = onProductCartListener;
    }

    public void setProdIdOrderQtyHashMap(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        Log.i(TAG, "setProdIdOrderQtyHashMap: fired!");

        if (prodIdOrderQtyHashMap.size() != 0) {
            this.prodIdOrderQtyHashMap = prodIdOrderQtyHashMap;
            notifyDataSetChanged();
        } else {
            Log.i(TAG, "setProdIdOrderQtyHashMap: Not notifying the SearchProductsAdapter for prodIdQtyHashMap = 0.");
        }
    }

    public void notifyProductAdded(int productId, int qty, boolean isAdded) {
        Log.i(TAG, "notifyProductAdded: fired! productId: " + productId + "\tqty: " + qty + "\tisAdded: " + isAdded);

        //CAN TRY TO USE HASHCODE TO QUICKLY FIND THE RIGHT PRODUCT
        for (int i = 0; i < searchProducts_products_dataList.size(); i++) {
            if (searchProducts_products_dataList.get(i).getId() == productId) {
                Log.i(TAG, "notifyProductAdded: Match found at " + i + " position in BestDealsRecyclerViewAdapter!");
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
        for (int i = 0; i < searchProducts_products_dataList.size(); i++) {
            if (searchProducts_products_dataList.get(i).getId() == productId) {
                Log.i(TAG, "notifyProductRemoved: Match found at " + i + " position in SearchProductsRecyclerViewAdapter!");
                if (isRemoved) {
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

        for (int i = 0; i < searchProducts_products_dataList.size(); i++) {
            if (searchProducts_products_dataList.get(i).getId() == productId) {
                prodIdOrderQtyHashMap.remove(productId);
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
            }
        }
    }

    /*static*/ class BestDealsViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView productMaterialCardView;

        AppCompatImageView productImageAppCompatImageView;

        MaterialTextView productTitleMaterialTextView, pricesMaterialTextView, prodWeightMaterialTextView, prodQtyMaterialTextView,
                discountMaterialTextView;

        /*AppCompatImageButton*/ MaterialButton cartMaterialButton, incrementMaterialButton, decrementMaterialButton;

        OnProductCartListener onProductCartListener;

        public BestDealsViewHolder(@NonNull @NotNull View itemView, OnProductCartListener onProductCartListener) {
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

                    onProductCartListener.productClicked(searchProducts_products_dataList.get(getAdapterPosition()).getId());
                } else if (view.getId() == cartMaterialButton.getId()) {

                    onProductCartListener.addProductToCart(searchProducts_products_dataList.get(getAdapterPosition()).getId(), 1);
                } else if (view.getId() == incrementMaterialButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyMaterialTextView.getText().toString());

                    incrementMaterialButton.setEnabled(false);
                    decrementMaterialButton.setEnabled(false);
                    prodIdLoadingHashMap.put(searchProducts_products_dataList.get(getAdapterPosition()).getId(), true);

                    onProductCartListener.addProductToCart(searchProducts_products_dataList.get(getAdapterPosition()).getId(), ++currentQty);
                } else if (view.getId() == decrementMaterialButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyMaterialTextView.getText().toString());
//                    prodQtyMaterialTextView.setText(String.valueOf(--currentQty));

                    incrementMaterialButton.setEnabled(false);
                    decrementMaterialButton.setEnabled(false);
                    prodIdLoadingHashMap.put(searchProducts_products_dataList.get(getAdapterPosition()).getId(), true);

                    onProductCartListener.removeProductFromCart(searchProducts_products_dataList.get(getAdapterPosition()).getId(), --currentQty);
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
