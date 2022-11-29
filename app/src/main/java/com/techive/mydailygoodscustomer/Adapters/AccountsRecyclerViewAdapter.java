package com.techive.mydailygoodscustomer.Adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountsRecyclerViewAdapter extends RecyclerView.Adapter<AccountsRecyclerViewAdapter.AccountsViewHolder> {
    private static final String TAG = "AccountsRecyclerViewAda";

    private static List<String> stringList;
    private static List<Drawable> drawableList;

    private AccountOptionsListener accountOptionsListener;

    public AccountsRecyclerViewAdapter(List<String> stringList, List<Drawable> drawableList) {
        Log.i(TAG, "AccountsRecyclerViewAdapter: String & Drawable Constructor fired!");

        AccountsRecyclerViewAdapter.stringList = stringList;
        AccountsRecyclerViewAdapter.drawableList = drawableList;
    }

    @NonNull
    @NotNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_account_row, parent, false), accountOptionsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccountsRecyclerViewAdapter.AccountsViewHolder holder, int position) {
        holder.accountOptionsMaterialTextView.setText(stringList.get(position));
        holder.accountOptionsMaterialTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableList.get(position), null, null);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void setAccountOptionsListener(AccountOptionsListener accountOptionsListener) {
        this.accountOptionsListener = accountOptionsListener;
    }

    /*VIEWHOLDER*/
    static class AccountsViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView accountOptionsMaterialCardView;

        MaterialTextView accountOptionsMaterialTextView;

        AccountOptionsListener accountOptionsListener;

        public AccountsViewHolder(@NonNull @NotNull View itemView, AccountOptionsListener accountOptionsListener) {
            super(itemView);

            accountOptionsMaterialTextView = itemView.findViewById(R.id.accountOptionsMaterialTextView);
            accountOptionsMaterialCardView = itemView.findViewById(R.id.accountOptionsMaterialCardView);

            accountOptionsMaterialCardView.setOnClickListener(view -> {
                Log.i(TAG, "AccountsViewHolder: clicked!");

                accountOptionsListener.accountOptionsClick(stringList.get(getAdapterPosition()));
            });

            this.accountOptionsListener = accountOptionsListener;
        }
    }

    public interface AccountOptionsListener {

        void accountOptionsClick(String optionName);
    }
}
