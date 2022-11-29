package com.techive.mydailygoodscustomer.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeTabPagerAdapter extends FragmentStateAdapter {
    private static final String TAG = "HomeTabPagerAdapter";

    ArrayList<Fragment> fragmentArrayList;

    public HomeTabPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        Log.i(TAG, "HomeTabPagerAdapter: With FragmentActivity fired!");
    }

    public HomeTabPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
        Log.i(TAG, "HomeTabPagerAdapter: With Fragment fired!");
    }

    public HomeTabPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentManager, lifecycle);
        Log.i(TAG, "HomeTabPagerAdapter: With FragmentManager & Lifecycle fired!");

        this.fragmentArrayList = fragmentArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Log.i(TAG, "createFragment: fired!");

        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
