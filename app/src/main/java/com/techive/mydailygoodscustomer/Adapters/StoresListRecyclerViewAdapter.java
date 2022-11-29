package com.techive.mydailygoodscustomer.Adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.SliderView;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data_ReviewData;
import com.techive.mydailygoodscustomer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StoresListRecyclerViewAdapter extends RecyclerView.Adapter<StoresListRecyclerViewAdapter.StoresViewHolder> {
    private static final String TAG = "StoresListRecyclerViewA";

    private List<StoreListByCityName_Data> storeListByCityName_dataList;

    private Context context;

    private boolean hasDefaultRadioButton;

    private OnStoresListRecyclerViewAdapterListener onStoresListRecyclerViewAdapterListener;

    private /*static*/ int lastCheckedPosition = -1;

    public StoresListRecyclerViewAdapter(Context context, boolean hasDefaultRadioButton) {
        Log.i(TAG, "StoresListRecyclerViewAdapter: Context & default radioButton based constructor fired!");
        this.context = context;
        this.hasDefaultRadioButton = hasDefaultRadioButton;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public StoresViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: fired!");
        return new StoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_row, parent, false), onStoresListRecyclerViewAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull StoresListRecyclerViewAdapter.StoresViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: fired! position: " + position);

        SliderAdapter sliderAdapter = new SliderAdapter(context);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        holder.sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to setadapter to sliderview.
        holder.sliderView.setSliderAdapter(sliderAdapter);

        // below method is use to set scroll time in seconds.
        holder.sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically we use below method.
        holder.sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        holder.sliderView.startAutoCycle();

        //WILL SET THE ACTUAL SLIDER ADAPTER DATA OVER HERE.
        ArrayList<String> imageNamesArrayList = new ArrayList<>();

        if (storeListByCityName_dataList.get(position).getImage1() != null && !storeListByCityName_dataList.get(position).getImage1().matches("")) {
            imageNamesArrayList.add(storeListByCityName_dataList.get(position).getImage1());
        }
        if (storeListByCityName_dataList.get(position).getImage2() != null && !storeListByCityName_dataList.get(position).getImage2().matches("")) {
            imageNamesArrayList.add(storeListByCityName_dataList.get(position).getImage2());
        }
        if (storeListByCityName_dataList.get(position).getImage3() != null && !storeListByCityName_dataList.get(position).getImage3().matches("")) {
            imageNamesArrayList.add(storeListByCityName_dataList.get(position).getImage3());
        }
        if (storeListByCityName_dataList.get(position).getImage4() != null && !storeListByCityName_dataList.get(position).getImage4().matches("")) {
            imageNamesArrayList.add(storeListByCityName_dataList.get(position).getImage4());
        }
        if (storeListByCityName_dataList.get(position).getImage5() != null && !storeListByCityName_dataList.get(position).getImage5().matches("")) {
            imageNamesArrayList.add(storeListByCityName_dataList.get(position).getImage5());
        }

        sliderAdapter.setSliderItems(imageNamesArrayList);

        displayStoreData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (storeListByCityName_dataList != null) {
            return storeListByCityName_dataList.size();
        }
        return 0;
    }

    public void setStoreListByCityName_dataList(List<StoreListByCityName_Data> storeListByCityName_dataList) {
        Log.i(TAG, "setStoreListByCityName_dataList: fired, Store List Data set!");
        this.storeListByCityName_dataList = storeListByCityName_dataList;
        resetLastCheckedPosition();
        notifyDataSetChanged();
    }

    public void setOnStoresListRecyclerViewAdapterListener(OnStoresListRecyclerViewAdapterListener onStoresListRecyclerViewAdapterListener) {
        this.onStoresListRecyclerViewAdapterListener = onStoresListRecyclerViewAdapterListener;
    }

    private void displayStoreData(StoresListRecyclerViewAdapter.StoresViewHolder holder, int position) {
        Log.i(TAG, "displayStoreData: fired!");

        if (hasDefaultRadioButton) {
            //INITIALIZE LAST CHECKED POSITION WITH THE FAV STORE POSITION.
            if (storeListByCityName_dataList.get(position).getFav_status() == 1) {
                Log.i(TAG, "displayStoreData: Fav Store! position: " + position);
                lastCheckedPosition = position;
                holder.storeDefaultMaterialRadioButton.setChecked(true);
            } else {
                holder.storeDefaultMaterialRadioButton.setChecked(false);
            }
            holder.storeDefaultMaterialRadioButton.setVisibility(View.VISIBLE);
        }

        if (hasDefaultRadioButton) {
            if (position == lastCheckedPosition) {
                Log.i(TAG, "displayStoreData: position == lastCheckedPosition");
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append(storeListByCityName_dataList.get(position).getStore())
                        .append("   ").append("DEFAULT");
                spannableStringBuilder.setSpan(new BackgroundColorSpan(context.getColor(R.color.toolbar_dark_green)),
                        spannableStringBuilder.toString().indexOf("DEFAULT"), spannableStringBuilder.length(),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);  //BACKGROUND COLOR
                spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)),
                        spannableStringBuilder.toString().indexOf("DEFAULT"), spannableStringBuilder.length(),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);  //TEXT COLOR (FOREGROUND)

                //SHOW THE DEFAULT TEXT WITH THE NAME
                holder.storeNameMaterialTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            } else {
                Log.i(TAG, "displayStoreData: position != lastCheckedPosition");
                holder.storeNameMaterialTextView.setText(storeListByCityName_dataList.get(position).getStore());
            }
        } else {
            Log.i(TAG, "displayStoreData: Setting plain store name. position: " + position);
            holder.storeNameMaterialTextView.setText(storeListByCityName_dataList.get(position).getStore());
        }

        try {
            StringBuilder addressStringBuilder = new StringBuilder();
            addressStringBuilder.append(storeListByCityName_dataList.get(position).getAddress()).append(", ");
            addressStringBuilder.append(storeListByCityName_dataList.get(position).getCity()).append(", ");
            addressStringBuilder.append(storeListByCityName_dataList.get(position).getState());
            holder.storeAddressMaterialTextView.setText(addressStringBuilder.toString());
        } catch (Exception exception) {
            Log.i(TAG, "displayStoreData: Exception caught while building Store Address, exception.getMessage(): " + exception.getMessage());
            exception.printStackTrace();
            holder.storeAddressMaterialTextView.setText("Store Address");
        }

        //RATINGS
        if (!hasDefaultRadioButton) {       //STORE LIST BY LAT-LNG & BY CITY
            String averageRating = storeListByCityName_dataList.get(position).getAvg_rating();
            if (averageRating != null && !averageRating.matches("")) {
                float avgRatingFloat = Float.parseFloat(averageRating);
                holder.storeAppCompatRatingBar.setRating(avgRatingFloat);
            } else {
                holder.storeAppCompatRatingBar.setRating(0);
            }

            holder.viewAllRatingsMaterialTextView.setText(context.getString(R.string.view_all) + "  ("
                    + storeListByCityName_dataList.get(position).getTotal_rating() + ")");
        } else {            //FAV STORE PAGE
            holder.viewAllRatingsMaterialTextView.setText(context.getString(R.string.rate_us));

            if (storeListByCityName_dataList.get(position).getReviewData() != null) {
                holder.storeAppCompatRatingBar.setRating(storeListByCityName_dataList.get(position).getReviewData().getStar());
            } else {
                holder.storeAppCompatRatingBar.setRating(0);
            }
        }

        //SHOW OPEN/CLOSED FOR CURRENT DAY & TIME.
        displayStoreTiming(holder, position);

        if (storeListByCityName_dataList.get(position).getHomedeliver() == 1) {
            holder.homeDeliveryMaterialTextView.setVisibility(View.VISIBLE);
        } else {
            holder.homeDeliveryMaterialTextView.setVisibility(View.GONE);
        }
    }

    private void displayStoreTiming(StoresListRecyclerViewAdapter.StoresViewHolder holder, int position) {
        Log.i(TAG, "displayStoreTiming: fired!");

        SimpleDateFormat simpleDateFormatTime12Hours = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        Calendar currentDateCalendar = Calendar.getInstance();
        Date currentDate = currentDateCalendar.getTime();
        Log.i(TAG, "displayStoreTiming: currentDate with proper time segment: " + currentDate);
        //I/AddVisitActivity: compareVisitDates: currentDate with proper time segment: Fri Feb 18 18:12:30 GMT+05:30 2022

        String day = currentDate.toString().substring(0, currentDate.toString().indexOf(" "));
//        String day = "Sun";
        Log.i(TAG, "displayStoreTiming: day: " + day);

        int currentHour = currentDateCalendar.get(Calendar.HOUR);
        int currentMinute = currentDateCalendar.get(Calendar.MINUTE);
        int AMPM = currentDateCalendar.get(Calendar.AM_PM);
        String currentAMPM;
        if (AMPM == 0) {
            currentAMPM = "am";
        } else {
            currentAMPM = "pm";
        }

        switch (day) {
            case "Mon": {
                String monOpen = storeListByCityName_dataList.get(position).getMon_o();
                String monClose = storeListByCityName_dataList.get(position).getMon_c();

                if (monOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + monOpen);
                } else if (monOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, monOpen, monClose);
                }
                break;
            }
            case "Tue": {
                Log.i(TAG, "displayStoreTiming: Inside Tue.");
                String tueOpen = storeListByCityName_dataList.get(position).getTue_o();
                String tueClose = storeListByCityName_dataList.get(position).getTue_c();

                if (tueOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + tueOpen);
                } else if (tueOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, tueOpen, tueClose);
                }
                break;
            }
            case "Wed": {
                Log.i(TAG, "displayStoreTiming: Inside Wed.");
                String wedOpen = storeListByCityName_dataList.get(position).getWed_o();
                String wedClose = storeListByCityName_dataList.get(position).getWed_c();

                if (wedOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + wedOpen);
                } else if (wedOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, wedOpen, wedClose);
                }
                break;
            }
            case "Thu": {
                Log.i(TAG, "displayStoreTiming: Inside Thu.");
                String thuOpen = storeListByCityName_dataList.get(position).getThu_o();
                String thuClose = storeListByCityName_dataList.get(position).getThu_c();

                if (thuOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + thuOpen);
                } else if (thuOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, thuOpen, thuClose);
                }
                break;
            }
            case "Fri": {
                Log.i(TAG, "displayStoreTiming: Inside Fri.");
                String friOpen = storeListByCityName_dataList.get(position).getFri_o();
                String friClose = storeListByCityName_dataList.get(position).getFri_c();

                if (friOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + friOpen);
                } else if (friOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, friOpen, friClose);
                }
                break;
            }
            case "Sat": {
                Log.i(TAG, "displayStoreTiming: Inside Sat.");
                String satOpen = storeListByCityName_dataList.get(position).getSat_o();
                String satClose = storeListByCityName_dataList.get(position).getSat_c();

                if (satOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + satOpen);
                } else if (satOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, satOpen, satClose);
                }
                break;
            }
            case "Sun": {
                Log.i(TAG, "displayStoreTiming: Inside Sun.");
                String sunOpen = storeListByCityName_dataList.get(position).getSun_o();
                String sunClose = storeListByCityName_dataList.get(position).getSun_c();

                if (sunOpen.matches("24 hours")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Open " + sunOpen);
                } else if (sunOpen.matches("0")) {
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed!");
                } else {    //SOME TIMING
                    compareTiming(holder, simpleDateFormatTime12Hours, currentHour, currentMinute, currentAMPM, sunOpen, sunClose);
                }
                break;
            }
        }
    }

    private void compareTiming(StoresListRecyclerViewAdapter.StoresViewHolder holder,
                               SimpleDateFormat simpleDateFormatTime12Hours, int currentHour, int currentMinute,
                               String currentAMPM, String openTime, String closeTime) {
        Log.i(TAG, "compareTiming: fired!");

        try {
            Date curTime = simpleDateFormatTime12Hours.parse(currentHour + ":" + currentMinute + " " + currentAMPM);
            Log.i(TAG, "compareTiming: curTime: " + curTime);
//I/StoresRecyclerViewAdapt: compareTiming: curTime: Thu Jan 01 10:52:00 GMT+05:30 1970

            Date wedOpenDate = simpleDateFormatTime12Hours.parse(openTime);
            Date wedCloseDate = simpleDateFormatTime12Hours.parse(closeTime);

            if (curTime.before(wedOpenDate)) {
                //STORE CLOSED NOW, opens wed open date
                Log.i(TAG, "compareTiming: store closed now. will open later.");
                holder.storeOpenClosedMaterialTextView.setText("Store Closed Now! Opens " + openTime);
            } else if (curTime.equals(wedOpenDate)) {
                //STORE OPEN NOW
                Log.i(TAG, "compareTiming: timing equal, store open now.");
                holder.storeOpenClosedMaterialTextView.setText("Store Open Now!");
            } else {
                Log.i(TAG, "compareTiming: Open time satisfied, checking for close time.");
                //CHECKING IF AFTER CLOSING TIME.
                if (curTime.after(wedCloseDate)) {
                    //STORE CLOSED NOW, check timings.
                    Log.i(TAG, "compareTiming: Store closed now. Check timings");
                    holder.storeOpenClosedMaterialTextView.setText("Store Closed Now! Check timings.");
                } else {
                    //STORE OPEN NOW
                    Log.i(TAG, "compareTiming: Store Open now.");
                    holder.storeOpenClosedMaterialTextView.setText("Store Open Now!");
                }
            }
        } catch (ParseException e) {
            Log.i(TAG, "compareTiming: Exception Caught while parsing Time.\te.getMessage(): " + e.getMessage());
            e.printStackTrace();
            holder.storeOpenClosedMaterialTextView.setText("Click for Store Timings!");
        }
    }

    public void notifyFavStoreUpdate(int toBeNotifiedPosition, int lastCheckedPosition) {
        Log.i(TAG, "notifyFavStoreUpdate: fired! toBeNotifiedPosition: " + toBeNotifiedPosition + "\tlastCheckedPosition: " + lastCheckedPosition);

        Log.i(TAG, "notifyFavStoreUpdate: BEFORE, storeListByCityName_dataList.get(toBeNotifiedPosition): " + storeListByCityName_dataList.get(toBeNotifiedPosition));
        storeListByCityName_dataList.get(toBeNotifiedPosition).setFav_status(1);
        Log.i(TAG, "notifyFavStoreUpdate: AFTER, storeListByCityName_dataList.get(toBeNotifiedPosition): " + storeListByCityName_dataList.get(toBeNotifiedPosition));
        notifyItemChanged(toBeNotifiedPosition);

        this.lastCheckedPosition = toBeNotifiedPosition;

        Log.i(TAG, "notifyFavStoreUpdate: BEFORE, storeListByCityName_dataList.get(lastCheckedPosition): " + storeListByCityName_dataList.get(lastCheckedPosition));
        if (lastCheckedPosition != -1) {
            storeListByCityName_dataList.get(lastCheckedPosition).setFav_status(0);
            notifyItemChanged(lastCheckedPosition);
        }
        Log.i(TAG, "notifyFavStoreUpdate: AFTER, storeListByCityName_dataList.get(lastCheckedPosition): " + storeListByCityName_dataList.get(lastCheckedPosition));
    }

    public void notifyStoreReviewUpdate(int toBeNotifiedPosition, StoreListByCityName_Data_ReviewData storeListByCityName_data_reviewData) {
        Log.i(TAG, "notifyStoreReviewUpdate: fired! toBeNotifiedPosition: " + toBeNotifiedPosition);

        storeListByCityName_dataList.get(toBeNotifiedPosition).setReviewData(storeListByCityName_data_reviewData);
        notifyItemChanged(toBeNotifiedPosition);
    }

    public void resetLastCheckedPosition() {
        Log.i(TAG, "resetLastCheckedPosition: fired!");

        lastCheckedPosition = -1;
    }

    /* VIEWHOLDER */
    public /*static*/ class StoresViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView storeMaterialCardView;

        MaterialRadioButton storeDefaultMaterialRadioButton;

        SliderView sliderView;

        MaterialTextView storeNameMaterialTextView, storeAddressMaterialTextView, viewAllRatingsMaterialTextView,
                storeOpenClosedMaterialTextView, homeDeliveryMaterialTextView;

        AppCompatRatingBar storeAppCompatRatingBar;

        AppCompatImageView storeMobileAppCompatImageView, storeWhatsappAppCompatImageView, storeWebsiteAppCompatImageView;

        OnStoresListRecyclerViewAdapterListener onStoresListRecyclerViewAdapterListener;

        public StoresViewHolder(@NonNull View itemView, OnStoresListRecyclerViewAdapterListener onStoresListRecyclerViewAdapterListener) {
            super(itemView);

            this.onStoresListRecyclerViewAdapterListener = onStoresListRecyclerViewAdapterListener;

            storeMaterialCardView = itemView.findViewById(R.id.storeMaterialCardView);
            storeDefaultMaterialRadioButton = itemView.findViewById(R.id.storeDefaultMaterialRadioButton);
            sliderView = itemView.findViewById(R.id.sliderView);
            storeNameMaterialTextView = itemView.findViewById(R.id.storeNameMaterialTextView);
            storeAddressMaterialTextView = itemView.findViewById(R.id.storeAddressMaterialTextView);
            storeAppCompatRatingBar = itemView.findViewById(R.id.storeAppCompatRatingBar);
            viewAllRatingsMaterialTextView = itemView.findViewById(R.id.viewAllRatingsMaterialTextView);
            storeOpenClosedMaterialTextView = itemView.findViewById(R.id.storeOpenClosedMaterialTextView);
            homeDeliveryMaterialTextView = itemView.findViewById(R.id.homeDeliveryMaterialTextView);
            storeMobileAppCompatImageView = itemView.findViewById(R.id.storeMobileAppCompatImageView);
            storeWhatsappAppCompatImageView = itemView.findViewById(R.id.storeWhatsappAppCompatImageView);
            storeWebsiteAppCompatImageView = itemView.findViewById(R.id.storeWebsiteAppCompatImageView);
//            storeMobNoMaterialTextView = itemView.findViewById(R.id.storeMobNoMaterialTextView);
//            storeWhatsappNoMaterialTextView = itemView.findViewById(R.id.storeWhatsappNoMaterialTextView);
//            storeWebsiteMaterialTextView = itemView.findViewById(R.id.storeWebsiteMaterialTextView);
//            homeDeliveryAppCompatImageView = itemView.findViewById(R.id.homeDeliveryAppCompatImageView);
//            codAppCompatImageView = itemView.findViewById(R.id.codAppCompatImageView);

            storeMaterialCardView.setOnClickListener(onClickListener);

            storeDefaultMaterialRadioButton.setOnClickListener(onClickListener);
            storeMobileAppCompatImageView.setOnClickListener(onClickListener);
            storeWhatsappAppCompatImageView.setOnClickListener(onClickListener);
            storeWebsiteAppCompatImageView.setOnClickListener(onClickListener);
            storeOpenClosedMaterialTextView.setOnClickListener(onClickListener);
            viewAllRatingsMaterialTextView.setOnClickListener(onClickListener);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == storeMaterialCardView.getId()) {                        //STORE CARD
                    Log.i(TAG, "onClick: Store Card Clicked! getAdapterPosition(): " + getAdapterPosition());

                    onStoresListRecyclerViewAdapterListener.onStoreClick(storeListByCityName_dataList.get(getAdapterPosition()).getStore_id(), storeListByCityName_dataList.get(getAdapterPosition()).getStore());
                } else if (view.getId() == storeDefaultMaterialRadioButton.getId()) {         //DEFAULT
                    Log.i(TAG, "onClick: Store Default RadioButton Clicked! getAdapterPosition(): " + getAdapterPosition());

//                    Log.i(TAG, "onClick: lastCheckedPosition: " + lastCheckedPosition);
//                    int copyOfLastCheckedPosition = lastCheckedPosition;
//                    lastCheckedPosition = getAdapterPosition();
//                    notifyItemChanged(copyOfLastCheckedPosition);
//                    notifyItemChanged(lastCheckedPosition);

                    onStoresListRecyclerViewAdapterListener.onStoreSetFav(storeListByCityName_dataList.get(
                            getAdapterPosition()).getStore_id(), getAdapterPosition(), lastCheckedPosition);
                } else if (view.getId() == storeOpenClosedMaterialTextView.getId()) {       //STORE TIMING
                    Log.i(TAG, "onClick: Store Timing MaterialTextView Clicked! getAdapterPosition(): " + getAdapterPosition());

                    onStoresListRecyclerViewAdapterListener.onStoreTiming(
                            storeListByCityName_dataList.get(getAdapterPosition()).getStore_id());
                } else if (view.getId() == storeMobileAppCompatImageView.getId()) {         //MOBILE
                    Log.i(TAG, "onClick: Store Mobile clicked! getAdapterPosition(): " + getAdapterPosition());

                    onStoresListRecyclerViewAdapterListener.onStoreCall(new String[]
                            {storeListByCityName_dataList.get(getAdapterPosition()).getMob_no1(),
                                    storeListByCityName_dataList.get(getAdapterPosition()).getMob_no2()});
                } else if (view.getId() == storeWhatsappAppCompatImageView.getId()) {       //WHATSAPP
                    Log.i(TAG, "onClick: Store Whatsapp clicked! getAdapterPosition(): " + getAdapterPosition());

                    onStoresListRecyclerViewAdapterListener.onStoreWhatsapp(new String[]
                            {storeListByCityName_dataList.get(getAdapterPosition()).getWhap_no1(),
                                    storeListByCityName_dataList.get(getAdapterPosition()).getWhap_no2()});
                } else if (view.getId() == storeWebsiteAppCompatImageView.getId()) {        //WEBSITE
                    Log.i(TAG, "onClick: Store Website clicked! getAdapterPosition(): " + getAdapterPosition());

                    onStoresListRecyclerViewAdapterListener.onStoreWebsite(
                            storeListByCityName_dataList.get(getAdapterPosition()).getWebsite());
                } else if (view.getId() == viewAllRatingsMaterialTextView.getId()) {        //VIEW ALL RATINGS
                    Log.i(TAG, "onClick: View All Ratings clicked! getAdapterPosition(): " + getAdapterPosition());

                    if (!hasDefaultRadioButton) {       //STORE BY LAT-LNG OR CITY PAGE
                        onStoresListRecyclerViewAdapterListener.onViewAllRatings(storeListByCityName_dataList.get(
                                getAdapterPosition()).getStore_id());
                    } else {            //FAV STORES PAGE
                        if (storeListByCityName_dataList.get(getAdapterPosition()).getTotal_delivery_count() != null
                                && storeListByCityName_dataList.get(getAdapterPosition()).getTotal_delivery_count() > 0) {
                            onStoresListRecyclerViewAdapterListener.rateStore(storeListByCityName_dataList.get(
                                    getAdapterPosition()).getStore_id(), storeListByCityName_dataList.get(getAdapterPosition()).getStore(),
                                    storeListByCityName_dataList.get(getAdapterPosition()).getReviewData(), getAdapterPosition());
                        } else {
                            Toast.makeText(context, "You can't rate any store until you have at least 1 order successfully delivered from this store!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

//        RatingBar.OnRatingBarChangeListener onRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                Log.i(TAG, "onRatingChanged: v: " + v + "\tb: " + b + "\tratingBar.getRating(): " + ratingBar.getRating());
////                    //I/StoresListRecyclerViewA: onRatingChanged: v: 1.5	b: true	ratingBar.getRating(): 1.5
//
//                if (v > 0) {    //ONLY HIT API IF RATING IS GREATER THAN 0.
//                    if (storeListByCityName_dataList.get(getAdapterPosition()).getTotal_delivery_count() != null
//                            && storeListByCityName_dataList.get(getAdapterPosition()).getTotal_delivery_count() >= 1) {
//                        //ALLOW RATING OF STORE ONLY IF USER HAS AT LEAST ONE ORDER DELIVERED FROM THAT STORE.
//                        onStoresListRecyclerViewAdapterListener.rateStore(v, storeListByCityName_dataList.get(getAdapterPosition()).getStore_id());
//                    } else {
//                        Toast.makeText(context, "You are not allowed to rate any store until you have at least 1 order " +
//                                "delivered from that store!", Toast.LENGTH_SHORT).show();
//                        storeAppCompatRatingBar.setRating(0);
//                    }
//                } else {
//                    Log.i(TAG, "onRatingChanged: Not doing anything, since this is just zeroing out the unsuccessful rating.");
//                }
//            }
//        };
    }

    public interface OnStoresListRecyclerViewAdapterListener {

        void onStoreCall(String[] mobNoArray);

        void onStoreWhatsapp(String[] whatsappArray);

        void onStoreWebsite(String website);

        void onStoreTiming(int storeId);

        void onStoreSetFav(int storeId, int toBeDefaultStorePosition, int lastCheckedPosition);

        void onStoreClick(int storeId, String storeName);

        void onViewAllRatings(int storeId);

        void rateStore(int storeId, String storeName, StoreListByCityName_Data_ReviewData storeListByCityName_data_reviewData,
                       int toBeNotifiedPosition);
    }
}
