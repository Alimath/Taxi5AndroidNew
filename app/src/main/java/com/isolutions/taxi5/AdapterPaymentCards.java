package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
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

    public boolean isChoosingPaymentCard = false;
    public boolean hasOneClick = false;

    public AdapterPaymentCards(Context context, ArrayList<AssistStoredCardData> items) {
        mContext = context;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        int addCount = 0;
        if(hasOneClick) {
            addCount = 1;
        }

        if(mDataSource != null) {
            return mDataSource.size()+addCount;
        }
        else {
            return addCount;
        }
    }

    @Override
    @Nullable
    public AssistStoredCardData getItem(int position) {
        AssistStoredCardData card = null;
        try{
            card = mDataSource.get(position);
            return card;
        }
        catch (Exception ex) {
            return null;
        }

//        Log.d("taxi5", "POS: " + position + ", SIZE: " + mDataSource.size());
//        if(mDataSource != null && position > mDataSource.size()) {
//            return null;
//        }
//        else {
//            return mDataSource.get(position);
//        }
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

            holder = new AdapterPaymentCards.ViewHolder();

            holder.cardNumber_1 = (TextView) convertView.findViewById(R.id.row_payment_cards_card_number_1);
            holder.cardNumber_4 = (TextView) convertView.findViewById(R.id.row_payment_cards_card_number_4);
            holder.cardHolderName = (TextView) convertView.findViewById(R.id.row_payment_cards_card_holder_name);
            holder.cardExpDate = (TextView) convertView.findViewById(R.id.row_payment_cards_card_exp_date);
            holder.reccurentCardBack = (ImageView) convertView.findViewById(R.id.row_payment_cards_card_back_image);

            holder.errorBack = (ImageView) convertView.findViewById(R.id.row_payment_cards_card_back_error_image);
            holder.errorText = (TextView) convertView.findViewById(R.id.row_payment_cards_card_back_error_text_view);

            holder.oneClickBack = (ImageView) convertView.findViewById(R.id.row_payment_cards_card_back_one_click_image);
            holder.oneClickTitle = (TextView) convertView.findViewById(R.id.row_payment_cards_one_click_title);
            holder.oneClickMessage = (TextView) convertView.findViewById(R.id.row_payment_cards_one_click_message);
            holder.cardTypeImage = (ImageView) convertView.findViewById(R.id.row_payment_cards_card_type_image);


            Button removeCardButton = (Button) convertView.findViewById(R.id.row_payment_cards_remove_card_button);
            ImageView removeCardButtonIcon = (ImageView) convertView.findViewById(R.id.row_payment_cards_remove_card_icon);
            if(!isChoosingPaymentCard) {
                removeCardButton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
//                        Log.d("taxi5", "remove card: " + getItem(position).initBillNumber);
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                        } else {
                            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity, android.R.style.Theme_Material_Dialog_Alert);
                        }
                        builder.setPositiveButton(R.string.remove_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(getItem(position) == null) {
                                    AssistCardsHolder.RemoveOneClick();
                                    if ((AssistCardsHolder.GetCards() != null && !AssistCardsHolder.GetCards().isEmpty()) || AssistCardsHolder.GetOneClickState()) {
                                        if (AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.isAdded()) {
                                            AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.UpdateListView();
                                        }
                                        else {
                                            AppData.getInstance().mainActivity.OpenPayments();
                                        }
                                    } else {
                                        AppData.getInstance().mainActivity.OpenPayments();
                                    }
                                }
                                else {
                                    AssistCardsHolder.RemoveCard(position);
                                    if ((AssistCardsHolder.GetCards() != null && !AssistCardsHolder.GetCards().isEmpty()) || AssistCardsHolder.GetOneClickState()) {
                                        if (AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.isAdded()) {
                                            AppData.getInstance().mainActivity.fragmentPaymentHasStoredCards.UpdateListView();
                                        }
                                        else {
                                            AppData.getInstance().mainActivity.OpenPayments();
                                        }
                                    } else {
                                        AppData.getInstance().mainActivity.OpenPayments();
                                    }
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
            }
            else {
                removeCardButton.setFocusable(false);
                removeCardButton.setVisibility(View.INVISIBLE);
                removeCardButtonIcon.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(holder);
        }
        else {
            holder = (AdapterPaymentCards.ViewHolder) convertView.getTag();
        }

        if(mDataSource != null && position < mDataSource.size()) {
            holder.fillData(mDataSource.get(position));
        }
        else {
            AssistStoredCardData oneClickData = new AssistStoredCardData();
            oneClickData.isOneClickCard = true;
            oneClickData.initBillResponseCode = "AS000";
            holder.fillData(oneClickData);
        }

        return convertView;
    }


    public static class ViewHolder {
        TextView cardNumber_1;
        TextView cardNumber_4;
        TextView cardHolderName;
        TextView cardExpDate;
        ImageView cardTypeImage;
        ImageView reccurentCardBack;

        ImageView errorBack;
        TextView errorText;

        ImageView oneClickBack;
        TextView oneClickTitle;
        TextView oneClickMessage;

        void fillData(AssistStoredCardData storedCardData) {
            if(storedCardData.initBillResponseCode.equalsIgnoreCase("AS000")) {
                errorBack.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.INVISIBLE);

                if(!storedCardData.isOneClickCard) {
                    oneClickBack.setVisibility(View.INVISIBLE);
                    oneClickTitle.setVisibility(View.INVISIBLE);
                    oneClickMessage.setVisibility(View.INVISIBLE);

                    cardNumber_1.setVisibility(View.VISIBLE);
                    cardNumber_4.setVisibility(View.VISIBLE);
                    cardHolderName.setVisibility(View.VISIBLE);
                    cardExpDate.setVisibility(View.VISIBLE);
                    cardTypeImage.setVisibility(View.VISIBLE);
                    reccurentCardBack.setVisibility(View.VISIBLE);


                    String cardNumber = storedCardData.meanNumber;
                    cardNumber_1.setText(cardNumber.substring(0,4));
                    cardNumber_4.setText(cardNumber.substring(cardNumber.length()-4, cardNumber.length()));
                    cardHolderName.setText(storedCardData.cardHolder);
                    cardExpDate.setText(storedCardData.cardExpDate);

                    if(!TextUtils.isEmpty(storedCardData.meanType)) {
                        switch (storedCardData.meanType) {
                            case "VISA":
                                cardTypeImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.visa_icon));
                                break;
                            case "MasterCard":
                                cardTypeImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.mastercard_icon));
                                break;
                            case "MC":
                                cardTypeImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.mastercard_icon));
                                break;
                            case "AMEX":
                                cardTypeImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.mastercard_icon));
                                break;
                            case "BelCard":
                                cardTypeImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.belcard_icon));
                                break;
                            default:
                                cardTypeImage.setImageDrawable(null);
                        }
                    }
                    else {
                        cardTypeImage.setImageDrawable(null);
                    }
                }
                else {
                    cardNumber_1.setVisibility(View.INVISIBLE);
                    cardNumber_4.setVisibility(View.INVISIBLE);
                    cardHolderName.setVisibility(View.INVISIBLE);
                    cardExpDate.setVisibility(View.INVISIBLE);
                    cardTypeImage.setVisibility(View.INVISIBLE);
                    reccurentCardBack.setVisibility(View.INVISIBLE);

                    oneClickBack.setVisibility(View.VISIBLE);
                    oneClickTitle.setVisibility(View.VISIBLE);
                    oneClickMessage.setVisibility(View.VISIBLE);
                }
            }
            else {
                cardNumber_1.setVisibility(View.INVISIBLE);
                cardNumber_4.setVisibility(View.INVISIBLE);
                cardHolderName.setVisibility(View.INVISIBLE);
                cardExpDate.setVisibility(View.INVISIBLE);
                cardTypeImage.setVisibility(View.INVISIBLE);
                reccurentCardBack.setVisibility(View.INVISIBLE);

                oneClickBack.setVisibility(View.INVISIBLE);
                oneClickTitle.setVisibility(View.INVISIBLE);
                oneClickMessage.setVisibility(View.INVISIBLE);

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
