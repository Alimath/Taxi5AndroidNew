package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isolutions.taxi5.APIAssist.AssistCardsHolder;
import com.isolutions.taxi5.APIAssist.Entities.AssistStoredCardData;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class AdapterPaymentCards extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AssistStoredCardData> mDataSource;

    public AdapterPaymentCards(Context context, ArrayList<AssistStoredCardData> items) {
        mContext = context;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public AssistStoredCardData getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AdapterPaymentCards.ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row_payment_cards, parent, false);

//            TextView cardNumber_1;
//            TextView cardNumber_4;
//            TextView cardHolderName;
//            TextView cardExpDate;
//
//            TextView errorTextView;
//            ImageView errorImageView;

            holder = new AdapterPaymentCards.ViewHolder();

            holder.cardNumber_1 = (TextView) convertView.findViewById(R.id.row_payment_cards_card_number_1);
            holder.cardNumber_4 = (TextView) convertView.findViewById(R.id.row_payment_cards_card_number_4);
            holder.cardHolderName = (TextView) convertView.findViewById(R.id.row_payment_cards_card_holder_name);
            holder.cardExpDate = (TextView) convertView.findViewById(R.id.row_payment_cards_card_exp_date);
            holder.errorBack = (ImageView) convertView.findViewById(R.id.row_payment_cards_card_back_error_image);
            holder.errorText = (TextView) convertView.findViewById(R.id.row_payment_cards_card_back_error_text_view);

            Button removeCardButton = (Button) convertView.findViewById(R.id.row_payment_cards_remove_card_button);
            removeCardButton.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    Log.d("taxi5", "remove card: " + getItem(position).initBillNumber) ;
                    AlertDialog.Builder builder;
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                    }
                    else {
                        builder = new AlertDialog.Builder(AppData.getInstance().mainActivity, android.R.style.Theme_Material_Dialog_Alert);
                    }
                    builder.setPositiveButton(R.string.remove_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AssistCardsHolder.RemoveCard(position);
                            if(AssistCardsHolder.GetCards() != null && !AssistCardsHolder.GetCards().isEmpty()) {
                                if(AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.isAdded()) {
                                    AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.UpdateListView();
                                }
                            }
                            else {
                                AppData.getInstance().mainActivity.OpenPayments();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.status_car_on_way_call_driver_dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.setTitle(AppData.getInstance().mainActivity.getString(R.string.payments_card_remove_card_alert_title));
                    builder.setMessage(AppData.getInstance().mainActivity.getString(R.string.payments_card_remove_card_alert_message));

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            convertView.setTag(holder);
        }
        else {
            holder = (AdapterPaymentCards.ViewHolder) convertView.getTag();
        }

        holder.fillData(mDataSource.get(position));

        return convertView;
    }


    public static class ViewHolder  {
        TextView cardNumber_1;
        TextView cardNumber_4;
        TextView cardHolderName;
        TextView cardExpDate;

        ImageView errorBack;
        TextView errorText;

//        private OrderData orderData;

        void fillData(AssistStoredCardData storedCardData) {
            String cardNumber = storedCardData.meanNumber;
            cardNumber_1.setText(cardNumber.substring(0,4));
            cardNumber_4.setText(cardNumber.substring(cardNumber.length()-4, cardNumber.length()));
            cardHolderName.setText(storedCardData.cardHolder);
            cardExpDate.setText(storedCardData.cardExpDate);

            if(storedCardData.initBillResponseCode.equalsIgnoreCase("AS000")) {
                errorBack.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.INVISIBLE);
            }
            else {
                errorBack.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.VISIBLE);

                errorText.setText(AppData.getInstance().getStringResourceByName("payments_card_row_error_message_"+storedCardData.initBillResponseCode));
            }
        }


    }

    public void updateResource(ArrayList<AssistStoredCardData> storedCardData) {
        this.mDataSource = storedCardData;
        notifyDataSetChanged();
    }
}
