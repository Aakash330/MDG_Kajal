package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
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
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder> {
    private static final String TAG = "CartRecyclerViewAdapter";

    private static List<Cart_CartData_Data> cart_cartData_dataList;

    private Context context;

    private OnProductCartListener onProductCartListener;

    //    private static HashMap<Integer, Integer> prodIdOrderQtyHashMap;
    private static HashMap<Integer, Boolean> prodIdLoadingHashMap;

    public CartRecyclerViewAdapter(Context context) {
        Log.i(TAG, "CartRecyclerViewAdapter: Context Based Constructor fired!");

        this.context = context;
//        prodIdOrderQtyHashMap = new HashMap<>();
        prodIdLoadingHashMap = new HashMap<>();
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_row, parent,
                false), onProductCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartRecyclerViewAdapter.CartViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: CART, fired! position: " + position);

        displayCartData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (cart_cartData_dataList != null) {
            return cart_cartData_dataList.size();
        }
        return 0;
    }

    private void displayCartData(CartViewHolder holder, int position) {
        Log.i(TAG, "displayCartData: fired!");

        if (cart_cartData_dataList.get(position).getImage() != null && !cart_cartData_dataList.get(position).getImage().matches("")) {
            Glide.with(context)
                    .load(cart_cartData_dataList.get(position).getImage())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.productImageCartAppCompatImageView);
        } else {
            Log.i(TAG, "onBindViewHolder: Product Main Image was NULL or EMPTY at position = " + position);
            holder.productImageCartAppCompatImageView.setImageResource(R.drawable.ic_image_24);
        }

//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        spannableStringBuilder.append(String.valueOf(cart_cartData_dataList.get(position).getSale_price())).append(" INR").append(" |");
//        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableStringBuilder.toString().indexOf("|"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

//        spannableStringBuilder.append(" ").append(String.valueOf(cart_cartData_dataList.get(position).getMrp_price())).append(" INR");
//        spannableStringBuilder.setSpan(new StrikethroughSpan(), (spannableStringBuilder.toString().indexOf("|") + 2), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.productTitleCartMaterialTextView.setText(cart_cartData_dataList.get(position).getName());

        float totalPriceFloat = Float.parseFloat(cart_cartData_dataList.get(position).getTotalPrice());
        float singlePriceFloat = totalPriceFloat / cart_cartData_dataList.get(position).getQty();
        float mrpPrice = totalPriceFloat + Float.parseFloat(cart_cartData_dataList.get(position).getSaving());

//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        spannableStringBuilder.append(String.valueOf((totalPriceFloat / cart_cartData_dataList.get(position).getQty()))).append(" INR");
//        spannableStringBuilder.append(" | SubTotal: ").append(String.valueOf(totalPriceFloat)).append(" INR");
//        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), spannableStringBuilder.toString().indexOf(":"), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        holder.pricesMaterialTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder spannableStringBuilderPrice = new SpannableStringBuilder();
        SpannableStringBuilder spannableStringBuilderSubTotal = new SpannableStringBuilder();
        SpannableStringBuilder spannableStringBuilderWeight = new SpannableStringBuilder();

        spannableStringBuilderPrice.append(context.getResources().getString(R.string.price)).append(" ");
        spannableStringBuilderPrice.append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(singlePriceFloat));
//        spannableStringBuilderPrice.setSpan(new StyleSpan(Typeface.BOLD), spannableStringBuilderPrice.toString().indexOf(":"),
//                spannableStringBuilderPrice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        spannableStringBuilderPrice.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
//                spannableStringBuilderPrice.toString().indexOf(":"), spannableStringBuilderPrice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableStringBuilderSubTotal.append(context.getResources().getString(R.string.subTotal)).append(" ");
        spannableStringBuilderSubTotal.append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(totalPriceFloat));
        if (cart_cartData_dataList.get(position).getSaving().matches("0")) {
            spannableStringBuilderSubTotal.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                    spannableStringBuilderSubTotal.toString().indexOf(":"), spannableStringBuilderSubTotal.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            spannableStringBuilderPrice.setSpan(new StyleSpan(Typeface.BOLD), spannableStringBuilderPrice.toString().indexOf(":"),
                    spannableStringBuilderPrice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringBuilderPrice.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                    spannableStringBuilderPrice.toString().indexOf(":"), spannableStringBuilderPrice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            spannableStringBuilderSubTotal.append(" | ").append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(mrpPrice));
            spannableStringBuilderSubTotal.setSpan(new StrikethroughSpan(), (spannableStringBuilderSubTotal.toString().indexOf("|") + 2),
                    spannableStringBuilderSubTotal.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringBuilderSubTotal.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                    spannableStringBuilderSubTotal.toString().indexOf(":"), spannableStringBuilderSubTotal.toString().indexOf("|"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilderPrice.append(" | ").append(context.getString(R.string.rupee_symbol)).append(" ").append(String.valueOf((mrpPrice / cart_cartData_dataList.get(position).getQty())));
            spannableStringBuilderPrice.setSpan(new StrikethroughSpan(), (spannableStringBuilderPrice.toString().indexOf("|") + 2),
                    spannableStringBuilderPrice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringBuilderPrice.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                    spannableStringBuilderPrice.toString().indexOf(":"), spannableStringBuilderPrice.toString().indexOf("|"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        spannableStringBuilderWeight.append(context.getResources().getString(R.string.weight)).append(" ");
        if (cart_cartData_dataList.get(position).getWeight() != null) {
            spannableStringBuilderWeight.append(cart_cartData_dataList.get(position).getWeight());
        } else {
            spannableStringBuilderWeight.append("NA");
        }
        spannableStringBuilderWeight.setSpan(new ForegroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                spannableStringBuilderWeight.toString().indexOf(":"), spannableStringBuilderWeight.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.priceCartMaterialTextView.setText(spannableStringBuilderPrice, TextView.BufferType.SPANNABLE);
//        holder.subTotalPriceCartMaterialTextView.setText(context.getResources().getString(R.string.subTotal) +  " " + totalPriceFloat);
        holder.subTotalPriceCartMaterialTextView.setText(spannableStringBuilderSubTotal, TextView.BufferType.SPANNABLE);

//        holder.prodWeightCartMaterialTextView.setText(context.getResources().getString(R.string.weight) + " " + cart_cartData_dataList.get(position).getWeight());
        holder.prodWeightCartMaterialTextView.setText(spannableStringBuilderWeight, TextView.BufferType.SPANNABLE);

        holder.toggleButtonsVisibility(false);
        holder.prodQtyCartMaterialTextView.setText(String.valueOf((cart_cartData_dataList.get(position).getQty())));

//        prodIdOrderQtyHashMap.put(cart_cartData_dataList.get(position).getProd_id(), cart_cartData_dataList.get(position).getQty());
//        if (prodIdOrderQtyHashMap.containsKey(cart_cartData_dataList.get(position).getProd_id())) {
//            holder.toggleButtonsVisibility(false);
//            holder.prodQtyMaterialTextView.setText(String.valueOf(prodIdOrderQtyHashMap.get(cart_cartData_dataList.get(position).getProd_id())));
//        } else {
//            holder.toggleButtonsVisibility(true);
//        }

        if (prodIdLoadingHashMap.containsKey(cart_cartData_dataList.get(position).getProd_id())) {
            if (prodIdLoadingHashMap.get(cart_cartData_dataList.get(position).getProd_id())) {
                //API BEING EXECUTED - Loading
                holder.incrementProdCartAppCompatImageButton.setEnabled(false);
                holder.decrementProdCartAppCompatImageButton.setEnabled(false);
            } else {
                //API EXECUTED - Completed
                holder.incrementProdCartAppCompatImageButton.setEnabled(true);
                holder.decrementProdCartAppCompatImageButton.setEnabled(true);
            }
        } else {
            holder.incrementProdCartAppCompatImageButton.setEnabled(true);
            holder.decrementProdCartAppCompatImageButton.setEnabled(true);
        }
    }

    public void setCart_cartData_dataList(List<Cart_CartData_Data> cart_cartData_dataList) {
        Log.i(TAG, "setCart_cartData_dataList: fired!");
        CartRecyclerViewAdapter.cart_cartData_dataList = cart_cartData_dataList;

        notifyDataSetChanged();
    }

    public void setOnProductCartListener(OnProductCartListener onProductCartListener) {
        this.onProductCartListener = onProductCartListener;
    }

    public void notifyProductAddedInCart(int productId, int qty, boolean isAdded, Cart_CartData_Data cart_cartData_data) {
        Log.i(TAG, "notifyProductAddedInCart: fired!");

        for (int i = 0; i < cart_cartData_dataList.size(); i++) {
            if (cart_cartData_dataList.get(i).getProd_id() == productId) {
                Log.i(TAG, "notifyProductAdded: Match found at " + i + " position in CartRecyclerViewAdapter!");
//                prodIdOrderQtyHashMap.put(productId, qty);
                if (isAdded) {
                    if (cart_cartData_data != null) {
                        cart_cartData_dataList.set(i, cart_cartData_data);
                    } else {
                        cart_cartData_dataList.get(i).setQty(--qty);
                    }
                } /*else {
                    prodIdLoadingHashMap.remove(productId);
                }*/
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void notifyProductRemovedFromCart(int productId, int qty, boolean isRemoved, Cart_CartData_Data cart_cartData_data) {
        Log.i(TAG, "notifyProductRemovedFromCart: fired!");

        for (int i = 0; i < cart_cartData_dataList.size(); i++) {
            if (cart_cartData_dataList.get(i).getProd_id() == productId) {
                Log.i(TAG, "notifyProductRemovedFromCart: Match found at " + i + " position in CartRecyclerViewAdapter!");
                if (isRemoved) {
                    if (cart_cartData_data != null) {
                        cart_cartData_dataList.set(i, cart_cartData_data);
                    } else {
                        cart_cartData_dataList.get(i).setQty(++qty);
                    }
                } /*else {
                    prodIdLoadingHashMap.remove(productId);
                }*/
                prodIdLoadingHashMap.remove(productId);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void notifyProductCompletelyRemovedFromCart() {
        Log.i(TAG, "notifyProductCompletelyRemovedFromCart: fired!");

        Log.i(TAG, "notifyProductCompletelyRemovedFromCart: cart_cartData_dataList.size(): " + cart_cartData_dataList.size());

        notifyDataSetChanged();
    }

    /* VIEWHOLDER */
    static class CartViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView productMaterialCardView;

        AppCompatImageView productImageCartAppCompatImageView;

        MaterialTextView productTitleCartMaterialTextView, priceCartMaterialTextView, prodWeightCartMaterialTextView,
                prodQtyCartMaterialTextView, subTotalPriceCartMaterialTextView;

        AppCompatImageButton incrementProdCartAppCompatImageButton, decrementProdCartAppCompatImageButton;

        OnProductCartListener onProductCartListener;

        public CartViewHolder(@NonNull @NotNull View itemView, OnProductCartListener onProductCartListener) {
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

            this.onProductCartListener = onProductCartListener;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Some view Clicked!");

                if (view.getId() == productMaterialCardView.getId()) {

                    onProductCartListener.productClicked(cart_cartData_dataList.get(getAdapterPosition()).getProd_id());
                } else if (view.getId() == incrementProdCartAppCompatImageButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyCartMaterialTextView.getText().toString());

                    //WILL MERELY DISABLE +- BUTTONS UNTIL PRODUCT GETS ADDED & WILL SHOW A PROGRESS BAR.
                    incrementProdCartAppCompatImageButton.setEnabled(false);
                    decrementProdCartAppCompatImageButton.setEnabled(false);
                    prodIdLoadingHashMap.put(cart_cartData_dataList.get(getAdapterPosition()).getProd_id(), true);

                    onProductCartListener.addProductToCart(cart_cartData_dataList.get(getAdapterPosition()).getProd_id(), ++currentQty);
                } else if (view.getId() == decrementProdCartAppCompatImageButton.getId()) {
                    int currentQty = Integer.parseInt(prodQtyCartMaterialTextView.getText().toString());

                    incrementProdCartAppCompatImageButton.setEnabled(false);
                    decrementProdCartAppCompatImageButton.setEnabled(false);
                    prodIdLoadingHashMap.put(cart_cartData_dataList.get(getAdapterPosition()).getProd_id(), true);

                    onProductCartListener.removeProductFromCart(cart_cartData_dataList.get(getAdapterPosition()).getProd_id(), --currentQty);
                }
            }
        };

        private void toggleButtonsVisibility(boolean cartVisible/*, boolean incrementVisible, boolean decrementVisible, boolean qtyVisible*/) {
            Log.i(TAG, "toggleButtonsVisibility: fired!");

            if (cartVisible) {
//                cartMaterialButton.setVisibility(View.VISIBLE);

                incrementProdCartAppCompatImageButton.setVisibility(View.GONE);
                decrementProdCartAppCompatImageButton.setVisibility(View.GONE);
                prodQtyCartMaterialTextView.setVisibility(View.GONE);
            } else {
//                cartMaterialButton.setVisibility(View.GONE);

                incrementProdCartAppCompatImageButton.setVisibility(View.VISIBLE);
                decrementProdCartAppCompatImageButton.setVisibility(View.VISIBLE);
                prodQtyCartMaterialTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
