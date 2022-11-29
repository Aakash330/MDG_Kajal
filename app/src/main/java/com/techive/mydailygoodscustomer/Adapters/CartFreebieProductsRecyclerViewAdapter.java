package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Freebies_Products;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Fragments.CartFragment;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CartFreebieProductsRecyclerViewAdapter extends RecyclerView.Adapter<CartFreebieProductsRecyclerViewAdapter.CartFreebieViewHolder> {
    private static final String TAG = "CartFreebieProductsRecy";

    private List<Cart_CartData_Freebies_Products> cart_cartData_freebies_productsList;

    private Context context;

    private OnProductCartListener onProductCartListener;

    //    private HashMap<Integer, Integer> freebieIdQtyHashMap;
    private List<Integer> freebieQtyArrayList;

    public CartFreebieProductsRecyclerViewAdapter(Context context) {
        Log.i(TAG, "CartFreebieProductsRecyclerViewAdapter: Context Based Constructor fired!");
        this.context = context;
        freebieQtyArrayList = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public CartFreebieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartFreebieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartFreebieProductsRecyclerViewAdapter.CartFreebieViewHolder holder, int position) {
        displayFreebieProducts(holder, position);
    }

    private void displayFreebieProducts(CartFreebieProductsRecyclerViewAdapter.CartFreebieViewHolder holder, int position) {
        Log.i(TAG, "displayFreebieProducts: fired!");

        if (cart_cartData_freebies_productsList.get(position).getImage() != null && !cart_cartData_freebies_productsList.get(position).getImage().matches("")) {
            Glide.with(context)
                    .load(cart_cartData_freebies_productsList.get(position).getImage())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.productImageCartAppCompatImageView);
        } else {
            Log.i(TAG, "onBindViewHolder: Product Main Image was NULL or EMPTY at position = " + position);
            holder.productImageCartAppCompatImageView.setImageResource(R.drawable.ic_image_24);
        }

//        holder.productTitleCartMaterialTextView.setText(cart_cartData_freebies_productsList.get(position).getName() + "\n(FREEBIE)");
        holder.productTitleCartMaterialTextView.setText(cart_cartData_freebies_productsList.get(position).getName() + "\n" + context.getString(R.string.freebie));

        SpannableStringBuilder spannableStringBuilderPrice = new SpannableStringBuilder();
        SpannableStringBuilder spannableStringBuilderWeight = new SpannableStringBuilder();

        spannableStringBuilderPrice.append(context.getResources().getString(R.string.price)).append(" ");
        spannableStringBuilderPrice.append("FREE").append(" ");
        spannableStringBuilderPrice.append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(cart_cartData_freebies_productsList.get(position).getSale_price()));
        spannableStringBuilderPrice.setSpan(new StyleSpan(Typeface.BOLD), spannableStringBuilderPrice.toString().indexOf("F"),
                (spannableStringBuilderPrice.toString().indexOf("F") + 4), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilderPrice.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)), spannableStringBuilderPrice.toString().indexOf("F"),
                (spannableStringBuilderPrice.toString().indexOf("F") + 4), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilderPrice.setSpan(new StrikethroughSpan(), (spannableStringBuilderPrice.toString().indexOf("F") + 5),
                spannableStringBuilderPrice.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableStringBuilderWeight.append(context.getResources().getString(R.string.weight)).append(" ");
        if (cart_cartData_freebies_productsList.get(position).getWeight() != null) {
            spannableStringBuilderWeight.append(cart_cartData_freebies_productsList.get(position).getWeight());
        } else {
            spannableStringBuilderWeight.append("NA");
        }
        spannableStringBuilderWeight.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                spannableStringBuilderWeight.toString().indexOf(":"), spannableStringBuilderWeight.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.priceCartMaterialTextView.setText(spannableStringBuilderPrice, TextView.BufferType.SPANNABLE);
        holder.prodWeightCartMaterialTextView.setText(spannableStringBuilderWeight, TextView.BufferType.SPANNABLE);

        holder.subTotalPriceCartMaterialTextView.setVisibility(View.GONE);

        holder.prodQtyCartMaterialTextView.setText(String.valueOf(freebieQtyArrayList.get(position)));

//        if (holder.prodQtyCartMaterialTextView.getText().toString().matches("1")) {
        if (freebieQtyArrayList.get(position) == 1) {
            holder.incrementProdCartAppCompatImageButton.setEnabled(false);
        } else {
            holder.incrementProdCartAppCompatImageButton.setEnabled(true);
        }

//        if (holder.prodQtyCartMaterialTextView.getText().toString().matches("0")) {
        if (freebieQtyArrayList.get(position) == 0) {
            holder.decrementProdCartAppCompatImageButton.setEnabled(false);
        } else {
            holder.decrementProdCartAppCompatImageButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        if (cart_cartData_freebies_productsList != null) {
            return cart_cartData_freebies_productsList.size();
        }
        return 0;
    }

    public void setCart_cartData_freebies_productsList(List<Cart_CartData_Freebies_Products> cart_cartData_freebies_productsList) {
        this.cart_cartData_freebies_productsList = cart_cartData_freebies_productsList;

        if (cart_cartData_freebies_productsList != null) {
            freebieQtyArrayList.clear();
            for (int i = 0; i < cart_cartData_freebies_productsList.size(); i++) {
                freebieQtyArrayList.add(1);
            }
            Log.i(TAG, "setCart_cartData_freebies_productsList: freebieQtyArrayList.size(): " + freebieQtyArrayList.size());
        }
        notifyDataSetChanged();
    }

    public void setOnProductCartListener(OnProductCartListener onProductCartListener) {
        this.onProductCartListener = onProductCartListener;
    }

    public List<Integer> getFreebieQtyArrayList() {
        return freebieQtyArrayList;
    }

    /* VIEWHOLDER */
    /*static*/ class CartFreebieViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView productMaterialCardView;

        AppCompatImageView productImageCartAppCompatImageView;

        MaterialTextView productTitleCartMaterialTextView, priceCartMaterialTextView, prodWeightCartMaterialTextView,
                prodQtyCartMaterialTextView, subTotalPriceCartMaterialTextView;

        AppCompatImageButton incrementProdCartAppCompatImageButton, decrementProdCartAppCompatImageButton;

        public CartFreebieViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            productMaterialCardView = itemView.findViewById(R.id.productMaterialCardView);

            productTitleCartMaterialTextView = itemView.findViewById(R.id.productTitleCartMaterialTextView);
            priceCartMaterialTextView = itemView.findViewById(R.id.priceCartMaterialTextView);
            prodWeightCartMaterialTextView = itemView.findViewById(R.id.prodWeightCartMaterialTextView);
            prodQtyCartMaterialTextView = itemView.findViewById(R.id.prodQtyCartMaterialTextView);
            subTotalPriceCartMaterialTextView = itemView.findViewById(R.id.subTotalPriceCartMaterialTextView);
            productImageCartAppCompatImageView = itemView.findViewById(R.id.productImageCartAppCompatImageView);
            incrementProdCartAppCompatImageButton = itemView.findViewById(R.id.incrementProdCartAppCompatImageButton);
            decrementProdCartAppCompatImageButton = itemView.findViewById(R.id.decrementProdCartAppCompatImageButton);

            productMaterialCardView.setOnClickListener(onClickListener);
            incrementProdCartAppCompatImageButton.setOnClickListener(onClickListener);
            decrementProdCartAppCompatImageButton.setOnClickListener(onClickListener);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Some View clicked!");

                if (view.getId() == productMaterialCardView.getId()) {
                    Log.i(TAG, "onClick: Decrement Clicked!");

                    onProductCartListener.productClicked(cart_cartData_freebies_productsList.get(getAdapterPosition()).getId());
                } else if (view.getId() == decrementProdCartAppCompatImageButton.getId()) {
                    Log.i(TAG, "onClick: Decrement Clicked!");

                    freebieQtyArrayList.set(getAdapterPosition(), 0);
                    notifyItemChanged(getAdapterPosition());
                } else if (view.getId() == incrementProdCartAppCompatImageButton.getId()) {
                    Log.i(TAG, "onClick: Increment Clicked!");

                    freebieQtyArrayList.set(getAdapterPosition(), 1);
                    notifyItemChanged(getAdapterPosition());
                }
            }
        };
    }
}
