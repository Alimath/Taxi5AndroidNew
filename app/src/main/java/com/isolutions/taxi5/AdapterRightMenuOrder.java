package com.isolutions.taxi5;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fedar.trukhan on 28.08.16.
 */

public class AdapterRightMenuOrder extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<OrderData> mDataSource;

    public boolean isActiveOrders = true;


    AdapterRightMenuOrder(Context context, ArrayList<OrderData> items) {
        mContext = context;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public OrderData getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row_active_order, parent, false);

            holder = new ViewHolder();

            holder.fromTextView = (TextView) convertView.findViewById(R.id.row_active_order_from_text_view);
            holder.toTextView = (TextView) convertView.findViewById(R.id.row_active_order_to_text_view);
            holder.statusTextView = (TextView) convertView.findViewById(R.id.row_active_order_status_text_view);
            holder.selectOrderArrow = (ImageView) convertView.findViewById(R.id.row_active_order_select_order_arrow);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderData order = getItem(position);

        holder.fillData(order, isActiveOrders);

        return convertView;
    }

    public static class ViewHolder {
        TextView fromTextView;
        TextView toTextView;
        TextView statusTextView;
        ImageView selectOrderArrow;

        private CountDownTimer timer;
        private OrderData orderData;

        void startUpdateTimer() {
            if(orderData != null && orderData.status != null && orderData.status.status != null &&
                    orderData.status.status == OrderStatusType.CarDelivering) {
                if (timer == null) {
                    timer = new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if (orderData != null) {
                                updateETATime(orderData.eta);
                                startUpdateTimer();
                            }
                        }
                    };
                    timer.start();
                } else {
                    timer.cancel();
                    timer.start();
                }
            }
        }

        void fillData(OrderData order, boolean isActiveOrder) {
            orderData = order;
            if(order != null) {
                if (order.from != null && !TextUtils.isEmpty(order.from.getStringDescription())) {
                    fromTextView.setText(order.from.getStringDescription());
                }
                if (order.to != null && !TextUtils.isEmpty(order.to.getStringDescription())) {
                    toTextView.setText(order.to.getStringDescription());
                }
                else {
                    toTextView.setText("");
                }
                if (order.status != null && order.status.status != null) {
                    if(order.status.status == OrderStatusType.CarDelivering && order.eta != null) {
                        updateETATime(order.eta);
                    }
                    else {
                        statusTextView.setText(ORDER_STATUSES_TEXT.get(order.status.status));
                        statusTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
                    }
                }
                else {
                    statusTextView.setText("");
                }


                if(isActiveOrder) {
                    selectOrderArrow.setVisibility(View.INVISIBLE);
                    startUpdateTimer();
                }
                else {
                    selectOrderArrow.setVisibility(View.VISIBLE);
                    statusTextView.setText("");
                }
            }
        }

        void updateETATime(Long etaTimeStamp) {
            if (statusTextView != null) {
                if (etaTimeStamp != null) {
                    Long time = new Date().getTime() / 1000L;
                    Long err = etaTimeStamp - time;

                    if (err < 0) {
                        statusTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultRed));
                    } else {
                        statusTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
                    }

                    Long minutes = err / 60L;
                    Long secundes = err - minutes * 60L;

                    minutes = Math.abs(minutes);
                    secundes = Math.abs(secundes);

                    String minutesString = "" + minutes;
                    String secundesString = "" + secundes;

                    if (minutes < 10) {
                        minutesString = "0" + minutesString;
                    }
                    if (secundes < 10) {
                        secundesString = "0" + secundesString;
                    }
                    statusTextView.setText(minutesString + ":" + secundesString);
                }
            }
        }
    }

    public void updateResource(ArrayList<OrderData> orderDatas) {
        this.mDataSource = orderDatas;
        notifyDataSetChanged();
    }

    private static final HashMap<OrderStatusType, String> ORDER_STATUSES_TEXT = new HashMap<OrderStatusType, String>() {{
        put(OrderStatusType.Registered, "");
        put(OrderStatusType.CarSearch, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_search));
        put(OrderStatusType.CarFound, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_found));
        put(OrderStatusType.CarNotFound, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_not_found));
        put(OrderStatusType.CarApproved, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_found));
        put(OrderStatusType.CarAssigned, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_found));
        put(OrderStatusType.ClientApproveTimeout, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_client_approve_timeout));
        put(OrderStatusType.ClientApproveReject, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_canceled));
        put(OrderStatusType.CarDelivering, "");
        put(OrderStatusType.CarDelivered, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_car_wait));
        put(OrderStatusType.ClientNotFound, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_client_not_found));
        put(OrderStatusType.OrderPostponed, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_postponed));
        put(OrderStatusType.OrderInProgress, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_in_progress));
        put(OrderStatusType.OrderCompleted, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_completed));
        put(OrderStatusType.OrderPaid, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_paid));
        put(OrderStatusType.OrderClosed, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_closed));
        put(OrderStatusType.OrderNotPaid, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_not_paid));
        put(OrderStatusType.ForceCanceled, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_canceled));
        put(OrderStatusType.OrderPendingPayment, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_order_pending_payment));
        put(OrderStatusType.Canceled, AppData.getInstance().getAppContext().getString(R.string.active_order_cell_status_canceled));
        put(null, "");
    }};
}
