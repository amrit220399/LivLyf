package com.apsinnovations.livlyf.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.OrderDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderHolder> {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
    Context context;
    int resource;
    ArrayList<OrderDetails> orderDetailsArrayList;
    NavController navController;
    public MyOrderAdapter(Context context, int resource, ArrayList<OrderDetails> orderDetailsArrayList) {
        this.context = context;
        this.resource = resource;
        this.orderDetailsArrayList = orderDetailsArrayList;
        navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment);
    }

    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrderHolder(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int position) {
        OrderDetails orderDetails = orderDetailsArrayList.get(position);

        holder.txtOrderID.setText(orderDetails.getOrder().getOrderID());
        holder.txtOrderDate.setText(sdf.format(orderDetails.getOrder().getTimestamp().toDate()));
        holder.txtAmount.setText(String.valueOf(orderDetails.getOrder().getOrderValue()));
        holder.txtStatus.setText(getStringStatus(orderDetails.getOrder().getStatus()));
        holder.txtItemsCount.setText(String.valueOf(orderDetails.getOrder().getItems().size()));
        MyOrderItemsAdapter myOrderItemsAdapter = new MyOrderItemsAdapter(context, R.layout.card_order_items, orderDetails.getOrder().getItems());
        holder.recyclerItems.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerItems.setAdapter(myOrderItemsAdapter);
    }

    @Override
    public int getItemCount() {
        return orderDetailsArrayList.size();
    }


    private String getStringStatus(int statusVal) {
        switch (statusVal) {
            case 0:
                return "Failed";
            case 1:
                return "Pending";
            case 2:
                return "Preparing for Dispatch";
            case 3:
                return "Dispatched via LivLyf Couriers";
            case 4:
                return "Delivered";
            case 5:
                return "Cancelled";
            case 6:
                return "Seller Cancellation";
            case 7:
                return "Return Request Raised";
            case 8:
                return "Returned";
            default:
                return "Error Getting Status";
        }
    }

    public class MyOrderHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerItems;
        TextView txtOrderID, txtOrderDate, txtAmount, txtStatus, txtItemsCount;
        Button btnCancel, btnOrderAddress;

        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);
            recyclerItems = itemView.findViewById(R.id.recyclerItems);
            txtOrderID = itemView.findViewById(R.id.txt_OrderID);
            txtOrderDate = itemView.findViewById(R.id.txt_OrderDate);
            txtAmount = itemView.findViewById(R.id.txt_OrderAmount);
            txtStatus = itemView.findViewById(R.id.txt_OrderStatus);
            txtItemsCount = itemView.findViewById(R.id.txt_OrderItems);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
            btnOrderAddress = itemView.findViewById(R.id.btnOrderAddress);

            btnOrderAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address", orderDetailsArrayList.get(getLayoutPosition()).getAddress());
                    navController.navigate(R.id.action_nav_orders_to_orderAddressFragment, bundle);
                }
            });
        }
    }
}
