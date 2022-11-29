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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.OrderHistory_Data;
import com.techive.mydailygoodscustomer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerViewAdapter.OrderHistoryViewHolder> {
    private static final String TAG = "OrderHistoryRecyclerVie";

    private static List<OrderHistory_Data> orderHistory_dataList;

    private Context context;

    private OrderHistoryClickListener orderHistoryClickListener;

    public OrderHistoryRecyclerViewAdapter(Context context) {
        Log.i(TAG, "OrderHistoryRecyclerViewAdapter: Context based constructor fired!");
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_order_history_row, parent, false), orderHistoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderHistoryRecyclerViewAdapter.OrderHistoryViewHolder holder, int position) {
        displayOrderHistoryData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (orderHistory_dataList != null) {
            return orderHistory_dataList.size();
        }
        return 0;
    }

    public void setOrderHistory_dataList(List<OrderHistory_Data> orderHistory_dataList) {
        Log.i(TAG, "setOrderHistory_dataList: fired!");
        OrderHistoryRecyclerViewAdapter.orderHistory_dataList = orderHistory_dataList;
        notifyDataSetChanged();
    }

    public void setOrderClickListener(OrderHistoryClickListener orderHistoryClickListener) {
        this.orderHistoryClickListener = orderHistoryClickListener;
    }

    private void displayOrderHistoryData(OrderHistoryRecyclerViewAdapter.OrderHistoryViewHolder holder, int position) {
        Log.i(TAG, "displayOrderHistoryData: fired!");

        Glide.with(context)
                .load(orderHistory_dataList.get(position).getImagepath() + orderHistory_dataList.get(position).getImage())
                .placeholder(R.drawable.ic_image_24)
                .error(R.drawable.ic_broken_image_24)
                .into(holder.orderItemMainAppCompatImageView);

        holder.orderInvoiceIdMaterialTextView.setText("Order InvoiceId #" + orderHistory_dataList.get(position).getInvoiceId());
        holder.orderDateMaterialTextView.setText(orderHistory_dataList.get(position).getCreated_at());

        if (orderHistory_dataList.get(position).getBuyerAddress() != null) {
            StringBuilder deliveryAddressStringBuilder = new StringBuilder();
            if (orderHistory_dataList.get(position).getBuyerAddress().getFirstname() != null) {
                deliveryAddressStringBuilder.append(orderHistory_dataList.get(position).getBuyerAddress().getFirstname()).append(" ");
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getLastname() != null) {
                deliveryAddressStringBuilder.append(orderHistory_dataList.get(position).getBuyerAddress().getLastname());
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getMobile() != null) {
                deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getMobile());
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getAddress() != null) {
                deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getAddress());
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getLandmark() != null) {
                deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getLandmark());
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getCity() != null) {
                deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getCity()).append(", ");
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getState() != null) {
                if (orderHistory_dataList.get(position).getBuyerAddress().getCity() != null) {
                    deliveryAddressStringBuilder.append(orderHistory_dataList.get(position).getBuyerAddress().getState()).append(", ");
                } else {
                    deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getState()).append(", ");
                }
            }
            if (orderHistory_dataList.get(position).getBuyerAddress().getPincode() != null) {
                if (orderHistory_dataList.get(position).getBuyerAddress().getCity() != null
                        || orderHistory_dataList.get(position).getBuyerAddress().getState() != null) {
                    deliveryAddressStringBuilder.append(orderHistory_dataList.get(position).getBuyerAddress().getPincode());
                } else {
                    deliveryAddressStringBuilder.append("\n").append(orderHistory_dataList.get(position).getBuyerAddress().getPincode());
                }
            }

//            holder.orderDeliveryAddressMaterialTextView.setText("Delivery Address:\n" + orderHistory_dataList.get(position).getBuyerAddress());
            holder.orderDeliveryAddressMaterialTextView.setText("Delivery Address:\n" + deliveryAddressStringBuilder.toString());
        } else {
            holder.orderDeliveryAddressMaterialTextView.setText("Delivery Address: NA");
        }

        holder.orderQtyMaterialTextView.setText("No. of Items: " + orderHistory_dataList.get(position).getQuantity());
        holder.orderGrossTotalMaterialTextView.setText("Gross Total: " + context.getString(R.string.rupee_symbol) + " " +
                (orderHistory_dataList.get(position).getTotal() + orderHistory_dataList.get(position).getDelivery_change()));

        String onlinePayment = orderHistory_dataList.get(position).getOnline_payment();
        if (onlinePayment != null) {
            switch (onlinePayment) {
                case "0": {
                    holder.orderPaymentMaterialTextView.setText("Order Payment: COD");

                    break;
                }
                case "1": {
                    holder.orderPaymentMaterialTextView.setText("Order Payment: Payment Success");

                    break;
                }
                case "2": {
                    holder.orderPaymentMaterialTextView.setText("Order Payment: Payment Pending");

                    break;
                }
                default: {
                    holder.orderPaymentMaterialTextView.setText("Order Payment: NA");
                }
            }
        } else {
            holder.orderPaymentMaterialTextView.setText("Order Payment: NA");
        }

        /*ORDER-STATUS & STATUS*/
        String strOrderStatus = orderHistory_dataList.get(position).getOrder_status();
        int orderStatus = 0;
        if (strOrderStatus != null && !strOrderStatus.matches("")) {
            orderStatus = Integer.parseInt(strOrderStatus);
        }
        int selfPickup = orderHistory_dataList.get(position).getSelf_pickup();

        if (selfPickup == 0) {
            holder.orderDeliveryMaterialTextView.setText("Delivery Mode: Home Delivery");
        } else {
            holder.orderDeliveryMaterialTextView.setText("Delivery Mode: Self-Pickup");
        }

        holder.orderedFromMaterialTextView.setText("Ordered From: " + orderHistory_dataList.get(position).getStore_name());

        switch (orderStatus) {
            case 0: {
                //PENDING
                holder.orderStatusMaterialTextView.setText("Pending");
                break;
            }
            case 1: {
                //APPROVED
                String strStatus = orderHistory_dataList.get(position).getStatus();
                Log.i(TAG, "onBindViewHolder: strStatus: " + strStatus);
                if (strStatus != null && !strStatus.matches("")) {
                    int status = Integer.parseInt(strStatus);
                    Log.i(TAG, "onBindViewHolder: status: " + status);
                    if (selfPickup == 0) {  //HOME DELIVERY
                        if (status == 7) {
                            holder.orderStatusMaterialTextView.setText("Ready");
                        } else if (status == 2) {
                            holder.orderStatusMaterialTextView.setText("Dispatch");
                        } else if (status == 3) {
                            holder.orderStatusMaterialTextView.setText("Shipped");
                        } else if (status == 4) {
                            holder.orderStatusMaterialTextView.setText("Delivered");
                        } else {    // cancel = 5
                            holder.orderStatusMaterialTextView.setText("Cancel");
                        }
                    } else {    // SELF-PICKUP
                        if (status == 7) {
                            holder.orderStatusMaterialTextView.setText("Ready");
                        } else if (status == 0) {
                            holder.orderStatusMaterialTextView.setText("Self-Pickup");
                        } else {    // picked = 1
                            holder.orderStatusMaterialTextView.setText("Picked");
                        }
                    }
                } else {
                    holder.orderStatusMaterialTextView.setText("Approved");
                }
                break;
            }
            case 2: {
                //CANCELED
                holder.orderStatusMaterialTextView.setText("Canceled");
                break;
            }
            case 3: {
                //NOT APPROVED
                holder.orderStatusMaterialTextView.setText("Not Approved");
                break;
            }
        }
    }

    /*VIEWHOLDER*/
    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView orderMaterialCardView;

        MaterialTextView orderInvoiceIdMaterialTextView, orderDateMaterialTextView, orderDeliveryAddressMaterialTextView,
                orderStatusMaterialTextView, orderPaymentMaterialTextView, orderDeliveryMaterialTextView,
                orderedFromMaterialTextView, orderQtyMaterialTextView, orderGrossTotalMaterialTextView;

        AppCompatImageView orderItemMainAppCompatImageView;

        OrderHistoryClickListener orderHistoryClickListener;

        public OrderHistoryViewHolder(@NonNull @NotNull View itemView, OrderHistoryClickListener orderHistoryClickListener) {
            super(itemView);

            orderMaterialCardView = itemView.findViewById(R.id.orderMaterialCardView);

            orderInvoiceIdMaterialTextView = itemView.findViewById(R.id.orderInvoiceIdMaterialTextView);
            orderDateMaterialTextView = itemView.findViewById(R.id.orderDateMaterialTextView);
            orderDeliveryAddressMaterialTextView = itemView.findViewById(R.id.orderDeliveryAddressMaterialTextView);
            orderStatusMaterialTextView = itemView.findViewById(R.id.orderStatusMaterialTextView);
            orderPaymentMaterialTextView = itemView.findViewById(R.id.orderPaymentMaterialTextView);
            orderDeliveryMaterialTextView = itemView.findViewById(R.id.orderDeliveryMaterialTextView);
            orderedFromMaterialTextView = itemView.findViewById(R.id.orderedFromMaterialTextView);
            orderQtyMaterialTextView = itemView.findViewById(R.id.orderQtyMaterialTextView);
            orderGrossTotalMaterialTextView = itemView.findViewById(R.id.orderGrossTotalMaterialTextView);

            orderItemMainAppCompatImageView = itemView.findViewById(R.id.orderItemMainAppCompatImageView);

            orderMaterialCardView.setOnClickListener(view -> {
                Log.i(TAG, "onClick: Order Card clicked! " + getAdapterPosition());

                orderHistoryClickListener.orderClick(orderHistory_dataList.get(getAdapterPosition()));
            });
            this.orderHistoryClickListener = orderHistoryClickListener;
        }
    }

    public interface OrderHistoryClickListener {

        void orderClick(OrderHistory_Data orderHistory_data);

//        void orderClick(List<OrderHistory_Data_Products> orderHistory_data_products, String imagePath);
    }
}
