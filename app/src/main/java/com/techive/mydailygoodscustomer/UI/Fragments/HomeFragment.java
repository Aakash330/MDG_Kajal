package com.techive.mydailygoodscustomer.UI.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.SliderView;
import com.techive.mydailygoodscustomer.Adapters.HomeCategoryRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.HomeTabPagerAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.HomeModel;
import com.techive.mydailygoodscustomer.Models.SearchProducts;
import com.techive.mydailygoodscustomer.Models.SearchProducts_Products_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.ProductDetailsActivity;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CartInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.LoginUtil;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements HomeCategoryRecyclerViewAdapter.HomeCategoryListenerInterface, OnProductCartListener {
    private static final String TAG = "HomeFragment";
    private static final String HOME_CART_TAG = "HOME_CART_TAG";
    private static final String HOME_BANNER_TAG = "HOME_BANNER_TAG";

    private LinearLayoutCompat rootLinearLayoutCompat;

    private SliderView banner1SliderView, couponsSliderView;

    private RecyclerView categoryRecyclerView, search_categoryRecyclerView;

    private AppCompatImageButton notificationAppCompatImageButton, reloadAppCompatImageButton;

    //USING FOR BANNER 2 ALSO
    private AppCompatImageView homeStoreLogoAppCompatImageView, banner2AppCompatImageView;

    //USING FOR BANNER 2 ALSO
    private MaterialTextView homeStoreTitleMaterialTextView, banner1Text1MaterialTextView, banner1Text2MaterialTextView;

    private SearchView homeSearchView;

    private TabLayout homeTabLayout;

    private ViewPager2 homeTabFragmentViewPager2;

    private View banner2Include;

    private NestedScrollView homeNestedScrollView, search_categoryNestedScrollView;

    private ArrayList<Fragment> homeTabFragmentsArrayList;

    private LinearLayoutManager horizontalLinearLayoutManager;

    private AlertDialog alertDialog;

    private CartInterface cartInterface;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
        Log.i(TAG, "HomeFragment: Empty Constructor!");
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: fired!");

        if (context instanceof CartInterface) {
            cartInterface = (CartInterface) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement CartInterface.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: fired!");

        //FRAGMENTS ADDED TO ARRAYLIST FOR TAB LAYOUT.
        homeTabFragmentsArrayList = new ArrayList<>();
        homeTabFragmentsArrayList.add(BestDealsFragment.newInstance());
        homeTabFragmentsArrayList.add(RecentlyAddedFragment.newInstance());
        homeTabFragmentsArrayList.add(HotSellingFragment.newInstance());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_24)
                .setTitle("RELOAD")
                .setMessage("All Data couldn't be loaded. Please try again!")
                .setPositiveButton("Try Again!", (dialogInterface, i) -> {
                    if (ApplicationData.getLoggedInBuyerId() != 0) {
                        Log.i(TAG, "displayReloadDialog: Buyer already logged in! Retrieving Cart for buyer on Home page: "
                                + ApplicationData.getLoggedInBuyerId());

                        if (!homeViewModel.getHomeCart()) {
                            DialogUtil.showNoInternetToast(requireActivity());
                        } else {
                            DialogUtil.showLoadingDialog1(requireActivity(), HOME_CART_TAG);
                        }
                    } else {
                        Log.i(TAG, "displayReloadDialog: Buyer NOT logged in! Not fetching cart!");
                    }

                    if (!homeViewModel.getHomeBannerCategoryData(ApplicationData.getDefaultStoreId())) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showLoadingDialog1(requireActivity(), HOME_BANNER_TAG);
                    }
                });
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        banner2AppCompatImageView.setImageResource(R.drawable.banner3);

        banner1Text1MaterialTextView.setText("BANNER 2 TITLE TEXT");
        banner1Text2MaterialTextView.setText("BANNER 2 SUB-TITLE TEXT");

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(homeTabLayout, homeTabFragmentViewPager2,
                true, true, tabConfigurationStrategy);
        tabLayoutMediator.attach();

        Log.i(TAG, "onViewCreated: ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireActivity());
        SharedPreferences sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

        if (sharedPreferences.getInt(SharedPreferencesManager.userId, 0) != 0) {
            Log.i(TAG, "onViewCreated: Buyer already logged in! Buyer: "
                    + sharedPreferences.getInt(SharedPreferencesManager.userId, 0));
            ApplicationData.setLoggedInBuyerId(sharedPreferences.getInt(SharedPreferencesManager.userId, 0));
        } else {
            Log.i(TAG, "onViewCreated: Buyer NOT logged in!");
        }

//        if (!homeViewModel.getHomeCart()) {
//            DialogUtil.showNoInternetToast(requireActivity());
//        } else {
//            DialogUtil.showLoadingDialog1(requireActivity(), HOME_CART_TAG);
//        }
//
//        if (!homeViewModel.getHomeBannerCategoryData(ApplicationData.getDefaultStoreId())) {
//            DialogUtil.showNoInternetToast(requireActivity());
//        } else {
//            DialogUtil.showLoadingDialog(requireActivity());
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: Home fired!");

        //PLANNING TO CHECK DEFAULT SHOP ID WITH THE SHOP ID IN HOME VIEWMODEL, SO AS TO REFRESH THE HOME DATA,
        // AS SOON AS WE COME BACK TO HOME FRAGMENT FROM STORELOCATOR ACTIVITY.
        //JUST DOING IT IN HOME FRAGMENT WONT WORK BECAUSE ONE CAN GO TO STORE LOCATOR ACTIVITY FROM ANY FRAGMENT.

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdHome(): " + ApplicationData.getOldStoreIdHome());
        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdHome()) {
            Log.i(TAG, "onResume: OldStoreIdHome != DefaultStoreId");

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearHomeData();

            if (!homeViewModel.getHomeBannerCategoryData(ApplicationData.getDefaultStoreId())) {
                DialogUtil.showNoInternetToast(requireActivity());
                checkLoadingData();
            } else {
                DialogUtil.showLoadingDialog1(requireActivity(), HOME_BANNER_TAG);
            }

            ApplicationData.setOldStoreIdHome(ApplicationData.getDefaultStoreId());
            Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdHome.");
        } else {
            Log.i(TAG, "onResume: OldStoreIdHome = DefaultStoreId");

            Log.i(TAG, "onResume: homeViewModel.getHomeModelMutableLiveData().getValue(): " + homeViewModel.getHomeModelMutableLiveData().getValue());
            Log.i(TAG, "onResume: homeViewModel.getHomeCartMutableLiveData().getValue(): " + homeViewModel.getHomeCartMutableLiveData().getValue());

            if (homeViewModel.getHomeModelMutableLiveData().getValue() == null) {
                Log.i(TAG, "onResume: Fetching Home Banners & Category.");

                if (!homeViewModel.getHomeBannerCategoryData(ApplicationData.getDefaultStoreId())) {
                    DialogUtil.showNoInternetToast(requireActivity());
                    checkLoadingData();
                } else {
                    DialogUtil.showLoadingDialog1(requireActivity(), HOME_BANNER_TAG);
                }
            } else {
                Log.i(TAG, "onResume: Re-displaying the banners & categories, cause HomeModelMutableLiveData is not null.");
                displayHomeData();
            }
        }

        //NO MATTER IF THE STORE CHANGED OR NOT, CART WILL BE FETCHED, EVERYTIME HOME PAGE IS RESUMED.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            Log.i(TAG, "onResume: Buyer already logged in! Retrieving Cart for buyer on Home page: "
                    + ApplicationData.getLoggedInBuyerId());

            if (!homeViewModel.getHomeCart()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog1(requireActivity(), HOME_CART_TAG);
            }
        } else {
            Log.i(TAG, "onResume: Buyer NOT logged in! Not fetching cart!");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        if (homeViewModel.getSearchProductsMutableLiveData().getValue() != null) {
            homeViewModel.setSearchProductsMutableLiveData(null);
        }
        if (homeViewModel.getAddToCartMutableLiveData().getValue() != null) {
            homeViewModel.setAddToCartMutableLiveData(null);
        }
        if (homeViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            homeViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (homeViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            homeViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        if (homeViewModel.getProductsByCategoryMutableLiveData().getValue() != null) {
            homeViewModel.setProductsByCategoryMutableLiveData(null);
        }

        if (homeViewModel.getProdAddAcceptResponseMutableLiveData().getValue() != null) {
            homeViewModel.setProdAddAcceptResponseMutableLiveData(null);
        }

        homeViewModel.currentCategoryId = 0;
        homeViewModel.homeCategoryRecyclerViewAdapter.resetCategorySelection();

        homeSearchView.setQuery("", false);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: fired!");

        homeViewModel.setHomeCartMutableLiveData(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: fired!");
    }

    private void initComponentViews(View view) {
        Log.i(TAG, "initComponentViews: fired!");

        rootLinearLayoutCompat = view.findViewById(R.id.rootLinearLayoutCompat);

        homeNestedScrollView = view.findViewById(R.id.homeNestedScrollView);
        search_categoryNestedScrollView = view.findViewById(R.id.search_categoryNestedScrollView);

        couponsSliderView = view.findViewById(R.id.couponsSliderView);
        banner1SliderView = view.findViewById(R.id.banner1SliderView);

        couponsSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        couponsSliderView.setScrollTimeInSec(3);
        couponsSliderView.setAutoCycle(true);
        couponsSliderView.startAutoCycle();

        banner1SliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        banner1SliderView.setScrollTimeInSec(3);
        banner1SliderView.setAutoCycle(true);
        banner1SliderView.startAutoCycle();

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        search_categoryRecyclerView = view.findViewById(R.id.search_categoryRecyclerView);

        notificationAppCompatImageButton = view.findViewById(R.id.notificationAppCompatImageButton);
        reloadAppCompatImageButton = view.findViewById(R.id.reloadAppCompatImageButton);

        homeStoreLogoAppCompatImageView = view.findViewById(R.id.homeStoreLogoAppCompatImageView);
        //USING FOR BANNER 2
        banner2AppCompatImageView = view.findViewById(R.id.banner1AppCompatImageView);
        //USING FOR BANNER 2
        banner1Text1MaterialTextView = view.findViewById(R.id.banner1Text1MaterialTextView);
        //USING FOR BANNER 2
        banner1Text2MaterialTextView = view.findViewById(R.id.banner1Text2MaterialTextView);
        homeStoreTitleMaterialTextView = view.findViewById(R.id.homeStoreTitleMaterialTextView);

        //USING FOR BANNER 2
        banner1Text1MaterialTextView.setTextColor(getResources().getColor(R.color.white, null));
        //USING FOR BANNER 2
        banner1Text2MaterialTextView.setTextColor(getResources().getColor(R.color.white, null));

        homeSearchView = view.findViewById(R.id.homeSearchView);

        homeTabLayout = view.findViewById(R.id.homeTabLayout);

        homeTabFragmentViewPager2 = view.findViewById(R.id.homeTabFragmentViewPager2);

        banner2Include = view.findViewById(R.id.banner2Include);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        //COUPONS
        couponsSliderView.setSliderAdapter(homeViewModel.homeCouponSliderAdapter);

        //BANNER 1
        banner1SliderView.setSliderAdapter(homeViewModel.homeBanner1SliderAdapter);

        //CATEGORY - START
        homeViewModel.homeCategoryRecyclerViewAdapter.setHomeCategoryListenerInterface(this);

        horizontalLinearLayoutManager = new LinearLayoutManager(requireActivity());
        horizontalLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
        categoryRecyclerView.setAdapter(homeViewModel.homeCategoryRecyclerViewAdapter);
        //CATEGORY - END

        HomeTabPagerAdapter homeTabPagerAdapter = new HomeTabPagerAdapter(getChildFragmentManager(), getLifecycle(), homeTabFragmentsArrayList);
        homeTabFragmentViewPager2.setAdapter(homeTabPagerAdapter);

        /*TESTING - START*/
        search_categoryRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        search_categoryRecyclerView.setAdapter(homeViewModel.searchProductsRecyclerViewAdapter);
        /*TESTING - END*/
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        homeViewModel.getHomeModelMutableLiveData().observe(this, homeModelObserver);
        homeViewModel.getHomeCartMutableLiveData().observe(this, homeCartObserver);
        homeViewModel.getSearchProductsMutableLiveData().observe(this, searchProductsObserver);
        homeViewModel.getAddToCartMutableLiveData().observe(this, addToCartObserver);
        homeViewModel.getRemoveFromCartMutableLiveData().observe(this, removeFromCartObserver);
        homeViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(this, completelyRemoveFromCartObserver);
        homeViewModel.getProductsByCategoryMutableLiveData().observe(this, categoryBasedProductsObserver);
        homeViewModel.getProdAddAcceptResponseMutableLiveData().observe(this, prodAddAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        homeTabLayout.addOnTabSelectedListener(onTabSelectedListener);
        homeNestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
        search_categoryNestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
        reloadAppCompatImageButton.setOnClickListener(onClickListener);
        homeSearchView.setOnQueryTextListener(onQueryTextListener);

        homeViewModel.searchProductsRecyclerViewAdapter.setOnProductCartListener(this);
    }

    private final TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
            Log.i(TAG, "onConfigureTab: position: " + position + "\ttab.getText(): " + tab.getText());

            //EXPLICITLY NEEDED, AS ON RUN-TIME THE TEXT OF THE TABS IS NOT VISIBLE.
            switch (position) {
                case 0: {
                    tab.setText(getString(R.string.best_deals));
                    break;
                }
                case 1: {
                    tab.setText(getString(R.string.recently_added));
                    break;
                }
                case 2: {
                    tab.setText(getString(R.string.hot_selling));
                    break;
                }
            }
        }
    };

    private final Observer<HomeModel> homeModelObserver = new Observer<HomeModel>() {
        @Override
        public void onChanged(HomeModel homeModel) {
            Log.i(TAG, "onChanged: HOME MODEL Observer fired! \nhomeModel: " + homeModel);

            DialogUtil.dismissLoadingDialog1(HOME_BANNER_TAG);

            if (homeModel.getError() == 200) {
                //ALTERNATIVE TO REMOVE SWIPE DOWN TO REFRESH.
                homeViewModel.homeModelSuccess = true;

                displayHomeData();
            } else {
                Log.i(TAG, "onChanged: Something went wrong while getting HomeModel data.");
                Toast.makeText(requireActivity(), homeModel.getMsg(), Toast.LENGTH_SHORT).show();

                homeViewModel.homeModelSuccess = false;
                checkLoadingData();
            }
        }
    };

    private final Observer<HomeCart> homeCartObserver = new Observer<HomeCart>() {
        @Override
        public void onChanged(HomeCart homeCart) {
            Log.i(TAG, "onChanged: HOME CART OBSERVER fired!\nhomeCart: " + homeCart);

            if (homeCart != null) {
                Log.i(TAG, "onChanged: Cart Dialog Showing: " + DialogUtil.isShowingLoadingDialog(HOME_CART_TAG));
                DialogUtil.dismissLoadingDialog1(HOME_CART_TAG);
                Log.i(TAG, "onChanged: Cart Dialog Showing: " + DialogUtil.isShowingLoadingDialog(HOME_CART_TAG));

                if (homeCart.getError() == 200) {
                    //WORKING ON ALTERNATIVE TO REMOVE SWIPE DOWN TO REFRESH.
                    homeViewModel.homeCartSuccess = true;

                    int totalQty = 0;

                    for (Cart_CartData_Data cart_cartData_data : homeCart.getData()) {
                        totalQty = totalQty + cart_cartData_data.getQty();
                    }
                    ApplicationData.setCartTotalQty(totalQty);
                    cartInterface.showBadgeOnCart(totalQty);

                    int selectedTabPosition = homeTabLayout.getSelectedTabPosition();
                    Log.i(TAG, "onChanged: selectedTabPosition: " + selectedTabPosition);

                    switch (selectedTabPosition) {
                        case 0: {
                            BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            selectedTabFragment.showCartDataInBestDeals(homeViewModel.computeProdIdOrderQtyHashMap());
                            break;
                        }
                        case 1: {
                            RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            selectedTabFragment.showCartDataInRecentlyAdded(homeViewModel.computeProdIdOrderQtyHashMap());
                            break;
                        }
                        case 2: {
                            HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            selectedTabFragment.showCartDataInHotSelling(homeViewModel.computeProdIdOrderQtyHashMap());
                            break;
                        }
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while loading cart on home page!");
                    if (homeCart.getError() == 400) {   //CART EMPTY
                        homeViewModel.homeCartSuccess = true;
                        ApplicationData.setCartTotalQty(0);
                        cartInterface.showBadgeOnCart(0);
                        return;
                    }
                    Toast.makeText(requireActivity(), homeCart.getMsg(), Toast.LENGTH_SHORT).show();

                    homeViewModel.homeCartSuccess = false;

                    checkLoadingData();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private final Observer<SearchProducts> searchProductsObserver = new Observer<SearchProducts>() {
        @Override
        public void onChanged(SearchProducts searchProducts) {
            Log.i(TAG, "onChanged: SEARCH PRODUCTS Observer fired!\nsearchProducts: " + searchProducts);

            if (searchProducts != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (searchProducts.getError() == 200) {
                    //SHOW BOTTOM SHEET DIALOG
                    homeViewModel.searchProductsCurrentPage = searchProducts.getProducts().getCurrent_page();
                    homeViewModel.searchProductsLastPage = searchProducts.getProducts().getLast_page();

                    homeViewModel.setPooledSearchProducts_products_dataList(searchProducts.getProducts().getData());

                    /*TESTING - START - 1-9-22*/
                    showSearch_CategoryResults(homeViewModel.getPooledSearchProducts_products_dataList());
                    /*TESTING - END*/
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while searching products!");
                    Toast.makeText(requireActivity(), searchProducts.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer firing!");
            }
        }
    };

    private final Observer<GeneralResponse> addToCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: ADD TO CART Observer (HomeFragment) fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())): "
                            + homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), true);

                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        updateCartCount(true);

                        homeViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not added from search bottomSheet dialog.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding product to cart!");
                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        //MOST PROBABLY NEED TO DECREMENT THE QTY.
                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "onChanged: Product not added from search bottomSheet dialog.");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> removeFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: REMOVE FROM CART (HomeFragment) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: homeViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        homeViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from search bottomSheet dialog.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart from search bottomSheet dialog!");

                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                false);
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from search bottomSheet dialog.");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> completelyRemoveFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART (HomeFragment) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: homeViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductCompletelyRemoved(Integer.parseInt(generalResponse.getProd_id()));

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), true);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        homeViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from search bottomSheet dialog.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart from Cart!");

                    if (homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                homeViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                false);
                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from search bottomSheet dialog.");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<SearchProducts> categoryBasedProductsObserver = new Observer<SearchProducts>() {
        @Override
        public void onChanged(SearchProducts searchProducts) {
            Log.i(TAG, "onChanged: CATEGORY BASED PRODUCTS Observer fired!\nsearchProducts: " + searchProducts);

            if (searchProducts != null) {
                DialogUtil.dismissLoadingDialog();

                if (searchProducts.getError() == 200) {
                    homeViewModel.categoryBasedProductsCurrentPage = searchProducts.getProducts().getCurrent_page();
                    homeViewModel.categoryBasedProductsLastPage = searchProducts.getProducts().getLast_page();

                    homeViewModel.setPooledCategoryBasedProducts_products_dataList(searchProducts.getProducts().getData());

//                    showSearchedProductsBottomSheetDialog(homeViewModel.getPooledCategoryBasedProducts_products_dataList(), false);
                    showSearch_CategoryResults(homeViewModel.getPooledCategoryBasedProducts_products_dataList());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while getting Category Based products!");
                    Toast.makeText(requireActivity(), searchProducts.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer firing!");
            }
        }
    };

    private final Observer<OrderAcceptResponse> prodAddAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
        @Override
        public void onChanged(OrderAcceptResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: PROD ADD ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() != 0) {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't add to cart.");
                        DialogUtil.showCustomSnackbar(requireActivity(), homeNestedScrollView, orderAcceptResponse.getMsg(), -1);

                        homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(homeViewModel.toBeAddedProductId, homeViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Prod Add Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(homeViewModel.toBeAddedProductId, homeViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.i(TAG, "onTabSelected: tab.getText(): " + tab.getText());

            switch (tab.getPosition()) {
                case 0: {
                    BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(0);
                    selectedTabFragment.showCartDataInBestDeals(ApplicationData.getProdIdOrderQtyHashMap());
                    break;
                }
                case 1: {
                    RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(1);
                    selectedTabFragment.showCartDataInRecentlyAdded(ApplicationData.getProdIdOrderQtyHashMap());
                    break;
                }
                case 2: {
                    HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(2);
                    selectedTabFragment.showCartDataInHotSelling(ApplicationData.getProdIdOrderQtyHashMap());
                    break;
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.i(TAG, "onTabUnselected: tab.getPosition(): " + tab.getPosition());

            /*Nullifying all the relevant MutableLiveData upon changing tabs.*/
            switch (tab.getPosition()) {
                case 0: {
                    BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(0);
                    selectedTabFragment.clearMutableLiveData();
                    break;
                }
                case 1: {
                    RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(1);
                    selectedTabFragment.clearMutableLiveData();
                    break;
                }
                case 2: {
                    HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(2);
                    selectedTabFragment.clearMutableLiveData();
                    break;
                }
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private final NestedScrollView.OnScrollChangeListener onScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.i(TAG, "onScrollChange: fired!");

            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                Log.i(TAG, "onScrollChange: Reached Page bottom!");

                if (homeViewModel.searchQuery.matches("") && homeViewModel.currentCategoryId == 0) {
                    //LOAD MORE RESULTS ACCORDING TO TAB SELECTED
                    int selectedTabPosition = homeTabLayout.getSelectedTabPosition();
                    Log.i(TAG, "onScrollChange: selectedTabPosition: " + selectedTabPosition);

                    switch (selectedTabPosition) {
                        case 0: {
                            BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            Log.i(TAG, "onScrollChange: selectedTabFragment: " + selectedTabFragment);
                            selectedTabFragment.loadMoreBestDealsProducts();
                            break;
                        }
                        case 1: {
                            RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            selectedTabFragment.loadMoreRecentlyAddedProducts();
                            break;
                        }
                        case 2: {
                            HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                            selectedTabFragment.loadMoreHotSellingProducts();
                            break;
                        }
                    }
                } else {
                    //LOAD MORE SEARCH OR CATEGORY RESULTS
                    if (homeViewModel.searchQuery.matches("")) {
                        //LOAD CATEGORY RESULTS
                        int nextPage = homeViewModel.categoryBasedProductsCurrentPage + 1;

                        if (nextPage <= homeViewModel.categoryBasedProductsLastPage) {
                            if (!homeViewModel.getProductsByCategory(homeViewModel.currentCategoryId, nextPage)) {
                                DialogUtil.showNoInternetToast(requireActivity());
                            } else {
                                DialogUtil.showProcessingInfoDialog(requireActivity());
                            }
                        } else {
                            Log.i(TAG, "onScrollChange: No More Products found in this category!");
                            Toast.makeText(requireActivity(), "No More Products found in this Category!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //LOAD SEARCH RESULTS
                        int nextPage = homeViewModel.searchProductsCurrentPage + 1;

                        if (nextPage <= homeViewModel.searchProductsLastPage) {
                            if (!homeViewModel.searchProducts(homeViewModel.searchQuery, nextPage)) {
                                DialogUtil.showNoInternetToast(requireActivity());
                            } else {
                                DialogUtil.showProcessingInfoDialog(requireActivity());
                            }
                        } else {
                            Log.i(TAG, "onScrollChange: No More Matching Products found!");
                            Toast.makeText(requireActivity(), "No More Matching Products found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: reloadAppCompatImageButton, clicked!");

            int selectedTabPosition = homeTabLayout.getSelectedTabPosition();
            Log.i(TAG, "onClick: selectedTabPosition: " + selectedTabPosition);

            switch (selectedTabPosition) {
                case 0: {
                    BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                    selectedTabFragment.reloadBestDeals();
                    break;
                }
                case 1: {
                    RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                    selectedTabFragment.reloadRecentlyAdded();
                    break;
                }
                case 2: {
                    HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(selectedTabPosition);
                    selectedTabFragment.reloadHotSelling();
                    break;
                }
            }
        }
    };

    private final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.i(TAG, "onQueryTextSubmit: query: " + query);

            if (homeViewModel.currentCategoryId != 0) {
                homeViewModel.currentCategoryId = 0;
                homeViewModel.homeCategoryRecyclerViewAdapter.resetCategorySelection();
            }

            //HIT THE SEARCH API TO GET THE PRODUCTS.
            if (!homeViewModel.searchProducts(query, 1)) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i(TAG, "onQueryTextChange: newText: " + newText);

            if (newText.matches("")) {
                homeViewModel.searchQuery = "";

                /*TESTING - START*/
                search_categoryNestedScrollView.setVisibility(View.GONE);
                homeNestedScrollView.setVisibility(View.VISIBLE);

                clearSearch_CategoryResults();
                /*TESTING - END*/
            }
            return false;
        }
    };

    private void displayHomeData() {
        Log.i(TAG, "displayHomeData: fired!");

        rootLinearLayoutCompat.setVisibility(View.VISIBLE);

        if (homeViewModel.getHomeModelMutableLiveData().getValue().getLogo() != null
                && !homeViewModel.getHomeModelMutableLiveData().getValue().getLogo().matches("")) {
            homeStoreLogoAppCompatImageView.setVisibility(View.VISIBLE);
            Glide.with(requireActivity())
                    .load(homeViewModel.getHomeModelMutableLiveData().getValue().getLogo())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(homeStoreLogoAppCompatImageView);
        } else {
            Log.i(TAG, "displayHomeData: LOGO was NULL or EMPTY!");
            homeStoreLogoAppCompatImageView.setVisibility(View.GONE);
        }

        homeStoreTitleMaterialTextView.setText(homeViewModel.getHomeModelMutableLiveData().getValue().getStore_name());

        homeViewModel.homeBanner1SliderAdapter.setHomeModel_bannerDataList(homeViewModel.getHomeModelMutableLiveData().getValue().getBanner_data());

        if (homeViewModel.getHomeModelMutableLiveData().getValue().getBanner_data() == null) {
            Log.i(TAG, "displayHomeData: Banner Data Null!");
            banner1SliderView.setVisibility(View.GONE);
        } else if (homeViewModel.getHomeModelMutableLiveData().getValue().getBanner_data().size() == 0) {
            Log.i(TAG, "displayHomeData: No Banners available for display!");
            banner1SliderView.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "displayHomeData: Banners available for display!");
            banner1SliderView.setVisibility(View.VISIBLE);
        }

        if (homeViewModel.getHomeModelMutableLiveData().getValue().getSection_banner() != null
                && !homeViewModel.getHomeModelMutableLiveData().getValue().getSection_banner().getMobile_banner().matches("")) {
            Log.i(TAG, "displayHomeData: Banner 2 Available!");
            banner2Include.setVisibility(View.VISIBLE);
            Glide.with(requireActivity())
                    .load(homeViewModel.getHomeModelMutableLiveData().getValue().getSection_banner().getMobile_banner())
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(banner2AppCompatImageView);
        } else {
            Log.i(TAG, "displayHomeData: Banner 2 was NULL or EMPTY!");
            banner2Include.setVisibility(View.GONE);
        }

        if (homeViewModel.getHomeModelMutableLiveData().getValue().getCoupon_data() != null) {
            couponsSliderView.setVisibility(View.VISIBLE);
            homeViewModel.homeCouponSliderAdapter.setHomeModel_couponDataArrayList(homeViewModel.getHomeModelMutableLiveData().getValue().getCoupon_data());
            if (homeViewModel.getHomeModelMutableLiveData().getValue().getCoupon_data().size() == 0) {
                Log.i(TAG, "onChanged: Coupon Data size = 0");
                couponsSliderView.setVisibility(View.GONE);
            } else {
                couponsSliderView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.i(TAG, "onChanged: Coupon Data was NULL!");
            couponsSliderView.setVisibility(View.GONE);
        }

        if (homeViewModel.getHomeModelMutableLiveData().getValue().getCategory_data() != null) {
            categoryRecyclerView.setVisibility(View.VISIBLE);

            if (homeViewModel.homeCategoryRecyclerViewAdapter.setHomeModel_categoryDataList(
                    homeViewModel.getHomeModelMutableLiveData().getValue().getCategory_data()) > 6) {
                categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int firstItemVisible = horizontalLinearLayoutManager.findFirstVisibleItemPosition();
                        if (firstItemVisible != 0 && firstItemVisible % homeViewModel.getHomeModelMutableLiveData()
                                .getValue().getCategory_data().size() == 0) {
                            recyclerView.getLayoutManager().scrollToPosition(0);
                        }
                    }
                });
            }
            if (homeViewModel.getHomeModelMutableLiveData().getValue().getCategory_data().size() == 0) {
                Log.i(TAG, "onChanged: Category Data size = 0");
                categoryRecyclerView.setVisibility(View.GONE);
            } else {
                categoryRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.i(TAG, "onChanged: Category Data was NULL!");
            categoryRecyclerView.setVisibility(View.GONE);
        }
    }

    public void toggleReloadIcon(boolean reloadVisible) {
        Log.i(TAG, "toggleReloadIcon: fired! reloadVisible: " + reloadVisible);

        if (reloadVisible) {
            reloadAppCompatImageButton.setVisibility(View.VISIBLE);
        } else {
            reloadAppCompatImageButton.setVisibility(View.INVISIBLE);
        }
    }

    public ArrayList<Fragment> getHomeTabFragmentsArrayList() {
        return homeTabFragmentsArrayList;
    }

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    private void updateCartData(int prodId, boolean completelyRemove) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: HOME_FRAGMENT, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (!completelyRemove) {
            prodIdQtyHashMap.put(prodId, homeViewModel.toBeNotifiedProdIdQtyHashMap.get(prodId));
        } else {
            prodIdQtyHashMap.remove(prodId);
        }
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "updateCartData: HOME_FRAGMENT, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
    }

    //TO UPDATE THE CART COUNT
    private void updateCartCount(boolean increment) {
        Log.i(TAG, "updateCartCount: fired!");

        Log.i(TAG, "onChanged: BEFORE, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
        if (increment) {
            ApplicationData.setCartTotalQty(ApplicationData.getCartTotalQty() + 1);
        } else {
            ApplicationData.setCartTotalQty(ApplicationData.getCartTotalQty() - 1);
        }
        Log.i(TAG, "onChanged: AFTER, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
        cartInterface.showBadgeOnCart(ApplicationData.getCartTotalQty());
    }

    /*TESTING - START*/
    private void showSearch_CategoryResults(List<SearchProducts_Products_Data> pooledSearchProducts_products_dataList) {
        Log.i(TAG, "showSearch_CategoryResults: fired!");

        homeNestedScrollView.setVisibility(View.GONE);
        search_categoryNestedScrollView.setVisibility(View.VISIBLE);

        homeViewModel.searchProductsRecyclerViewAdapter.setSearchProducts_products_dataList(pooledSearchProducts_products_dataList);
        homeViewModel.searchProductsRecyclerViewAdapter.setProdIdOrderQtyHashMap(ApplicationData.getProdIdOrderQtyHashMap());
    }
    /*TESTING - END*/

    private void clearSearch_CategoryResults() {
        Log.i(TAG, "clearSearch_CategoryResults: fired!");

        homeViewModel.setPooledSearchProducts_products_dataList(null);

        homeViewModel.setPooledCategoryBasedProducts_products_dataList(null);

        switch (homeTabLayout.getSelectedTabPosition()) {
            case 0: {
                BestDealsFragment selectedTabFragment = (BestDealsFragment) homeTabFragmentsArrayList.get(0);
                selectedTabFragment.showCartDataInBestDeals(ApplicationData.getProdIdOrderQtyHashMap());
                break;
            }
            case 1: {
                RecentlyAddedFragment selectedTabFragment = (RecentlyAddedFragment) homeTabFragmentsArrayList.get(1);
                selectedTabFragment.showCartDataInRecentlyAdded(ApplicationData.getProdIdOrderQtyHashMap());
                break;
            }
            case 2: {
                HotSellingFragment selectedTabFragment = (HotSellingFragment) homeTabFragmentsArrayList.get(2);
                selectedTabFragment.showCartDataInHotSelling(ApplicationData.getProdIdOrderQtyHashMap());
                break;
            }
        }
    }

    /*WILL BE USED TO CLEAR PREVIOUS DATA WHEN SWITCHING STORES.*/
    private void clearHomeData() {
        Log.i(TAG, "clearHomeData: fired!");

        homeStoreTitleMaterialTextView.setText("Store Name");

        homeViewModel.homeCouponSliderAdapter.setHomeModel_couponDataArrayList(null);
        homeViewModel.homeCategoryRecyclerViewAdapter.setHomeModel_categoryDataList(null);
        homeViewModel.homeBanner1SliderAdapter.setHomeModel_bannerDataList(null);

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
    }

    /* TO DISPLAY NON-DISMISSABLE RELOAD DIALOG WHEN BOTH DATA SETS HAVE NOT BEEN LOADED. */
    private synchronized void checkLoadingData() {
        Log.i(TAG, "checkLoadingData: fired!");

        if (homeViewModel.homeCartSuccess) {
            //Cart Data was Loaded.
            if (homeViewModel.homeModelSuccess) {
                //Do nothing, merely return
                return;
            } else {
                if (DialogUtil.isShowingLoadingDialog(HOME_BANNER_TAG)) {
                    //Do nothing, merely return
                    return;
                } else {
                    //Display Reload Alert Dialog
                    displayReloadDialog();
                }
            }
        } else {
            //Cart data NOT loaded.
            if (homeViewModel.homeModelSuccess) {
                displayReloadDialog();
            } else {
                if (DialogUtil.isShowingLoadingDialog(HOME_BANNER_TAG)) {
                    //Do nothing, merely return
                    return;
                } else {
                    //Display Reload Alert Dialog
                    displayReloadDialog();
                }
            }
        }
    }

    private void displayReloadDialog() {
        Log.i(TAG, "displayReloadDialog: fired!");

        if (!alertDialog.isShowing()) {
            Log.i(TAG, "displayReloadDialog: alertDialog.isShowing(): " + alertDialog.isShowing());
            alertDialog.show();
        }
    }

    @Override
    public void onCategoryClicked(int categoryId, boolean isSelected) {
        Log.i(TAG, "onCategoryClicked: fired! categoryId: " + categoryId + "\tisSelected: " + isSelected);

        if (!homeSearchView.getQuery().toString().matches("")) {
            //CLEAR ANY EXISTING QUERY
            homeSearchView.setQuery("", false);
        }

        if (isSelected) {
            //SEARCH FOR THE CATEGORY BASED PRODUCTS
            if (!homeViewModel.getProductsByCategory(categoryId, 1)) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog(requireActivity());
            }
        } else {
            //REMOVE THE CATEGORY BASED PRODUCTS DISPLAYED
            homeViewModel.currentCategoryId = 0;
            homeNestedScrollView.setVisibility(View.VISIBLE);
            search_categoryNestedScrollView.setVisibility(View.GONE);

            clearSearch_CategoryResults();
        }
    }

    /* FROM SEARCH RECYCLERVIEWADAPTER (USED FOR CATEGORY BASED PRODUCTS AS WELL) */
    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {

            //PROCEED TO CHECK FOR PROD ADD ACCEPTANCE
            if (!homeViewModel.checkVendorOrderAcceptance(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, homeViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + homeViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                homeViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, homeViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + homeViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 1) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, --qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }
        } else {
            homeViewModel.searchProductsRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    /* FROM SEARCH RECYCLERVIEWADAPTER */
    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            //PROCEED TO REMOVE PRODUCT FROM CART
            if (!homeViewModel.removeFromCartFromHome(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "removeProductFromCart: BEFORE, homeViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + homeViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                homeViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "removeProductFromCart: AFTER, homeViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + homeViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 0) {
                    prodIdQtyHashMap.remove(productId);
                    homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                } else {
                    prodIdQtyHashMap.put(productId, ++qty);
                    homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
            }
        } else {
            homeViewModel.searchProductsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void productClicked(int productId) {
        Log.i(TAG, "productClicked: fired! productId: " + productId);

        Intent productDetailsIntent = new Intent(requireActivity(), ProductDetailsActivity.class);
        productDetailsIntent.putExtra("PRODUCT_ID", productId);
        startActivity(productDetailsIntent);
    }
}