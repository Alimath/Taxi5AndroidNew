package com.isolutions.taxi5;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.AmountData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusPayment extends StatusesBaseFragment {

    @BindView(R.id.fragment_status_order_payment_price_progress_bar)
    ProgressBar pendingPriceProgressBar;

    @BindView(R.id.fragment_status_order_payment_price_new)
    TextView priceNewTextView;

    @BindView(R.id.fragment_status_order_payment_price_old)
    TextView priceOldTextView;

    @BindView(R.id.fragment_status_order_payment_from_text_view)
    TextView fromTextView;

    @BindView(R.id.fragment_status_order_payment_to_text_view)
    TextView toTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        super.onCreateView(inflater, container, savedInstanceState);

        View paymentWaiting = inflater.inflate(R.layout.fragment_status_payment, container, false);
        ButterKnife.bind(this, paymentWaiting);

        return paymentWaiting;
    }

    @Override
    public void fillWithOrder() {
        if(AppData.getInstance().getCurrentOrder() != null) {
            OrderData order = AppData.getInstance().getCurrentOrder();

            if(order.from != null) {
                fromTextView.setText(order.from.getStringDescription());
            }
            else {
                fromTextView.setText("");
            }
            if(order.to != null) {
                toTextView.setText(order.to.getStringDescription());
            }
            else {
                toTextView.setText("");
            }
            if(order.status != null && order.status.status != null) {
                if(order.status.status == OrderStatusType.OrderPendingPayment) {
                    if(order.amount != null) {
                        Long bynAmount = null;
                        Long byrAmount = null;
                        for (AmountData amountItem:order.amount) {
                            if(amountItem.currency.equalsIgnoreCase("byr")) {
                                byrAmount = amountItem.value;
                            }
                            if(amountItem.currency.equalsIgnoreCase("byn")) {
                                bynAmount = amountItem.value;
                            }
                        }
                        pendingPriceProgressBar.setVisibility(View.INVISIBLE);

                        if(bynAmount != null || byrAmount != null) {
                            if(bynAmount == null) {
                                bynAmount = byrAmount / 100;
                            }
                            if(byrAmount == null) {
                                byrAmount = bynAmount * 100;
                            }
//                            String bynAmountString = (bynAmount/100)+",";
                            Long rubs = bynAmount/100;
                            Long cop = bynAmount - (bynAmount/100)*100;
                            String copString;
                            if(cop <= 0) {
                                copString = "00";
                            }
                            else if(cop < 10) {
                                copString = "0" + cop;
                            }
                            else {
                                copString = "" + cop;
                            }

                            String bynAmountString = rubs + "," + copString + " " + getString(R.string.new_currency);
                            priceNewTextView.setText(bynAmountString);

                            String byrAmountString = (byrAmount/1000) + " ";
                            byrAmountString += (byrAmount - byrAmount/1000*1000) + " " + getString(R.string.old_currency);

                            priceOldTextView.setText(byrAmountString);
                        }
                        else {
                            pendingPriceProgressBar.setVisibility(View.VISIBLE);
                            priceNewTextView.setText("");
                            priceOldTextView.setText("");
                        }

                    }
                }
                else {
                    pendingPriceProgressBar.setVisibility(View.VISIBLE);

                }
            }
            else {
                pendingPriceProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}