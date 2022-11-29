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
import com.techive.mydailygoodscustomer.Models.HomeModel_CategoryData;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeCategoryRecyclerViewAdapter extends RecyclerView.Adapter<HomeCategoryRecyclerViewAdapter.HomeCategoryViewHolder> {
    private static final String TAG = "HomeCategoryRecyclerVie";

    private Context context;

    private static List<HomeModel_CategoryData> homeModel_categoryDataList;

    private int selectedCategoryId;

    private HomeCategoryListenerInterface homeCategoryListenerInterface;

    public HomeCategoryRecyclerViewAdapter(Context context) {
        Log.i(TAG, "HomeCategoryRecyclerViewAdapter: Empty Constructor fired!");

        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new HomeCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_row, parent, false), homeCategoryListenerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeCategoryRecyclerViewAdapter.HomeCategoryViewHolder holder, int position) {

        if (homeModel_categoryDataList.size() <= 6) {
            Log.i(TAG, "onBindViewHolder: homeModel_categoryDataList size is = 0");
            if (selectedCategoryId == homeModel_categoryDataList.get(position).getId()) {
                holder.itemView.setBackground(context.getDrawable(R.drawable.linear_layout_border));
            } else {
                holder.itemView.setBackground(null);
            }
            holder.categoryTitleMaterialTextView.setText(homeModel_categoryDataList.get(position).getName());
            Glide.with(context)
                    .load(homeModel_categoryDataList.get(position).getImage())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.categoryAppCompatImageView);
        } else {
            Log.i(TAG, "onBindViewHolder: homeModel_categoryDataList size is > 0.");
            if (selectedCategoryId == homeModel_categoryDataList.get((position % homeModel_categoryDataList.size())).getId()) {
                holder.itemView.setBackground(context.getDrawable(R.drawable.linear_layout_border));
            } else {
                holder.itemView.setBackground(null);
            }
            holder.categoryTitleMaterialTextView.setText(homeModel_categoryDataList.get((position % homeModel_categoryDataList.size())).getName());
            Glide.with(context)
                    .load(homeModel_categoryDataList.get((position % homeModel_categoryDataList.size())).getImage())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(holder.categoryAppCompatImageView);
        }
    }

    @Override
    public int getItemCount() {
        //WORKING, BUT THIS WILL KEEP ON INCREASING THE SIZE OF THE RECYCLER VIEWS ITEMS BEING DISPLAYED.
//        return Integer.MAX_VALUE;
        if (homeModel_categoryDataList != null) {
            if (homeModel_categoryDataList.size() > 6) {  //IF MORE THAN 6 ITEMS THEN WE'LL HAVE THE REPEAT SCROLL FUNCTIONALITY.
                return (homeModel_categoryDataList.size() * 2);
            } else {
                return homeModel_categoryDataList.size();
            }
        }
        return 0;
    }

    //SETTER FOR THE RECYCLER VIEW DATA, WILL RETURN THE SIZE BACK TO THE ACTIVITY, SO THAT ON SCROLL LISTENER CAN BE SET.
    public int setHomeModel_categoryDataList(List<HomeModel_CategoryData> homeModel_categoryDataList) {
        Log.i(TAG, "setHomeModel_categoryDataList: fired!");
        HomeCategoryRecyclerViewAdapter.homeModel_categoryDataList = homeModel_categoryDataList;
        notifyDataSetChanged();

        if (homeModel_categoryDataList == null) {
            return 0;
        }
        return homeModel_categoryDataList.size();
    }

    public void resetCategorySelection() {
        Log.i(TAG, "resetCategorySelection: fired!");

        selectedCategoryId = 0;
        notifyDataSetChanged();
    }

    public void setHomeCategoryListenerInterface(HomeCategoryListenerInterface homeCategoryListenerInterface) {
        this.homeCategoryListenerInterface = homeCategoryListenerInterface;
    }

    public /*static*/ class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView categoryAppCompatImageView;

        MaterialTextView categoryTitleMaterialTextView;

        HomeCategoryListenerInterface homeCategoryListenerInterface;

        public HomeCategoryViewHolder(@NonNull @NotNull View itemView, HomeCategoryListenerInterface homeCategoryListenerInterface) {
            super(itemView);

            categoryAppCompatImageView = itemView.findViewById(R.id.categoryAppCompatImageView);
            categoryTitleMaterialTextView = itemView.findViewById(R.id.categoryTitleMaterialTextView);

            itemView.setOnClickListener(onClickListener);

            this.homeCategoryListenerInterface = homeCategoryListenerInterface;

        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: clicked!");

                if (homeModel_categoryDataList.size() > 6) {
                    if (selectedCategoryId == homeModel_categoryDataList.get((getAdapterPosition() % homeModel_categoryDataList.size())).getId()) {
                        //DESELECT CURRENT CATEGORY
                        view.setBackground(null);
                        selectedCategoryId = 0;
                        homeCategoryListenerInterface.onCategoryClicked(homeModel_categoryDataList.get((getAdapterPosition() % homeModel_categoryDataList.size())).getId(), false);
                    } else {
                        //SELECT CURRENT CATEGORY
                        view.setBackground(context.getDrawable(R.drawable.linear_layout_border));
                        selectedCategoryId = homeModel_categoryDataList.get((getAdapterPosition() % homeModel_categoryDataList.size())).getId();
                        homeCategoryListenerInterface.onCategoryClicked(homeModel_categoryDataList.get((getAdapterPosition() % homeModel_categoryDataList.size())).getId(), true);
                    }
                } else {
                    if (selectedCategoryId == homeModel_categoryDataList.get(getAdapterPosition()).getId()) {
                        //DESELECT CURRENT CATEGORY
                        view.setBackground(null);
                        selectedCategoryId = 0;
                        homeCategoryListenerInterface.onCategoryClicked(homeModel_categoryDataList.get(getAdapterPosition()).getId(), false);
                    } else {
                        //SELECT CURRENT CATEGORY
                        view.setBackground(context.getDrawable(R.drawable.linear_layout_border));
                        selectedCategoryId = homeModel_categoryDataList.get(getAdapterPosition()).getId();
                        homeCategoryListenerInterface.onCategoryClicked(homeModel_categoryDataList.get(getAdapterPosition()).getId(), true);
                    }
                }
                notifyDataSetChanged();
            }
        };
    }

    public interface HomeCategoryListenerInterface {
        void onCategoryClicked(int categoryId, boolean isSelected);
    }
}
