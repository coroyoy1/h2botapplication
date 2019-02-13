package com.google.group.h2botapplication.waterdealer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.group.h2botapplication.R;

import java.util.List;

public class WPInProgressAdapter extends RecyclerView.Adapter<WPInProgressAdapter.ViewHolder>{
    private Context mContext;
    private List<WPTransactionModel> mUploads;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public WPInProgressAdapter(Context context, List<WPTransactionModel> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public WPInProgressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.wpinprogressxml, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull WPInProgressAdapter.ViewHolder viewHolder, int i) {
        final WPTransactionModel currentData = mUploads.get(i);
                viewHolder.transactionNo.setText(currentData.getTransactionNo());
                viewHolder.status.setText(currentData.getStatus());
                viewHolder.customername.setText(currentData.getCustomerName());
                viewHolder.address.setText(currentData.getAddress());
                viewHolder.contactno.setText(currentData.getContactNo());
                viewHolder.deliveryfee.setText(currentData.getDeliveryFee());
                viewHolder.itemquantity.setText(currentData.getItemQuantity());
                viewHolder.pricepergallon.setText(currentData.getPricePerGallon());
                viewHolder.service.setText(currentData.getService());
                viewHolder.totalprice.setText(currentData.getTotalPrice());
                viewHolder.watertype.setText(currentData.getWaterType());
                viewHolder.details.setText(">>");

//        String transactno= currentData.getTransactionNo();
//        String status= currentData.getStatus();
//        String customername= currentData.getCustomerName();
//        String address= currentData.getAddress();
//        String contactno= currentData.getContactNo();
//        String deliveryfee= currentData.getDeliveryFee();
//        String itemquantity= currentData.getItemQuantity();
//        String pricepergallon= currentData.getPricePerGallon();
//        String service= currentData.getService();
//        String totalprice= currentData.getTotalPrice();
//        String watertype= currentData.getWaterType();


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WPPendingTransactionFragment detail = new WPPendingTransactionFragment();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_wp, detail).addToBackStack(null).commit();
                Bundle args = new Bundle();
                args.putString("transactionno", currentData.getTransactionNo());
                args.putString("status", currentData.getStatus());
                args.putString("customername", currentData.getCustomerName());
                args.putString("address", currentData.getAddress());
                args.putString("contactno", currentData.getContactNo());
                args.putString("deliveryfee", currentData.getDeliveryFee());
                args.putString("itemquantity", currentData.getItemQuantity());
                args.putString("pricepergallon", currentData.getPricePerGallon());
                args.putString("service", currentData.getService());
                args.putString("totalprice", currentData.getTotalPrice());
                args.putString("watertype", currentData.getWaterType());
                detail.setArguments(args);
            }
        });

//                WPTransactionModel uploadCurrent = mUploads.get(i);
//                viewHolder.transactionNo.setText(uploadCurrent.getTransactionNo());
//                viewHolder.status.setText(uploadCurrent.getStatus());
//                viewHolder.customername.setText(uploadCurrent.getCustomerName());
//                viewHolder.address.setText(uploadCurrent.getAddress());
//                viewHolder.contactno.setText(uploadCurrent.getContactNo());
//                viewHolder.deliveryfee.setText(uploadCurrent.getDeliveryFee());
//                viewHolder.itemquantity.setText(uploadCurrent.getItemQuantity());
//                viewHolder.pricepergallon.setText(uploadCurrent.getPricePerGallon());
//                viewHolder.service.setText(uploadCurrent.getService());
//                viewHolder.totalprice.setText(uploadCurrent.getTotalPrice());
//                viewHolder.watertype.setText(uploadCurrent.getWaterType());
//                viewHolder.details.setText(">>");




    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transactionNo, status, details,address, customername, contactno, deliveryfee,itemquantity, pricepergallon,service,totalprice,watertype;

        public ViewHolder(View itemView) {
            super(itemView);

            transactionNo = itemView.findViewById(R.id.transactionNo);
            status = itemView.findViewById(R.id.status);
            details = itemView.findViewById(R.id.details);
            address = itemView.findViewById(R.id.address);
            customername = itemView.findViewById(R.id.customername);
            contactno = itemView.findViewById(R.id.contactno);
            deliveryfee = itemView.findViewById(R.id.deliveryfee);
            itemquantity = itemView.findViewById(R.id.itemquantity);
            pricepergallon = itemView.findViewById(R.id.pricepergallon);
            service = itemView.findViewById(R.id.service);
            totalprice = itemView.findViewById(R.id.totalprice);
            watertype = itemView.findViewById(R.id.watertype);


//            itemView.setOnClickListener(new View    .OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
