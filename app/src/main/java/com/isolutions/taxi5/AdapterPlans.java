package com.isolutions.taxi5;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.AmountData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.PlanEntity;
import com.isolutions.taxi5.API.Taxi5SDKEntity.PlanResponseData;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class AdapterPlans extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList mDataSource;

    public AdapterPlans(Context context, ArrayList items) {
        mContext = context;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Map.Entry<PlanResponseData.PlanTypes, PlanEntity> getItem(int position) {
        return (Map.Entry<PlanResponseData.PlanTypes, PlanEntity>) mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row_plan_data, parent, false);

            holder = new ViewHolder();

            holder.iconImage = (ImageView) convertView.findViewById(R.id.row_plan_data_icon_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.row_plan_data_title_text_view);
            holder.currencyTextView = (TextView) convertView.findViewById(R.id.row_plan_data_currency_text_view);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.row_plan_data_amount_text_view);
            holder.metaTextView = (TextView) convertView.findViewById(R.id.row_plan_data_meta_text_view);
            holder.mainLayout = (ConstraintLayout) convertView.findViewById(R.id.row_plan_data_main);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map.Entry<PlanResponseData.PlanTypes, PlanEntity> planEntity = getItem(position);

        holder.fillData(planEntity);

        return convertView;
    }

    public void updateResource(ArrayList datas) {
        this.mDataSource = datas;
        notifyDataSetChanged();
    }
    public static class ViewHolder {
        ImageView iconImage;
        TextView titleTextView;
        TextView currencyTextView;
        TextView amountTextView;
        TextView metaTextView;

        ConstraintLayout mainLayout;

        private Map.Entry<PlanResponseData.PlanTypes, PlanEntity> planEntity;

        void fillData(Map.Entry<PlanResponseData.PlanTypes, PlanEntity> planItem) {
            planEntity = planItem;
            switch (planEntity.getKey()) {
                case CITY:
                    iconImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.row_plan_data_city_icon));
                    titleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.row_plan_data_city_title));
                    break;
                case BASE:
                    iconImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.row_plan_data_base_icon));
                    titleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.row_plan_data_base_title));
                    break;
                case AIRPORT:
                    iconImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.row_plan_data_airport_icon));
                    titleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.row_plan_data_airport_title));
                     break;
                case COUNTRY:
                    iconImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.row_plan_data_country_icon));
                    titleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.row_plan_data_country_title));
                    break;
                case IDLE:
                    iconImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.row_plan_data_idle_icon));
                    titleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.row_plan_data_idle_title));
                    break;
            }

            if (AppData.getInstance().selectedPlansCurrencyNew) {
                currencyTextView.setText(AppData.getInstance().getAppContext().getString(R.string.new_currency));
            }
            else {
                currencyTextView.setText(AppData.getInstance().getAppContext().getString(R.string.old_currency));
            }

            if(!TextUtils.isEmpty(planEntity.getValue().meta)) {
                metaTextView.setText(planEntity.getValue().meta);

                ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mainLayout.setLayoutParams(params);
            }
            else {
                metaTextView.setText("");

                ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, AppData.getInstance().getAppContext().getResources().getDisplayMetrics());;
                mainLayout.setLayoutParams(params);
            }

            if(planEntity.getValue().amount != null) {
                Long bynAmount = null;
                Long byrAmount = null;
                for (AmountData amountItem:planEntity.getValue().amount) {
                    if(amountItem.currency.equalsIgnoreCase("byr")) {
                        byrAmount = amountItem.value;
                    }
                    if(amountItem.currency.equalsIgnoreCase("byn")) {
                        bynAmount = amountItem.value;
                    }
                }

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

                    if(AppData.getInstance().selectedPlansCurrencyNew) {
                        String bynAmountString = rubs + "." + copString;
                        amountTextView.setText(bynAmountString);

                    }
                    else {
//                        String byrAmountString = (byrAmount / 1000) + " ";
//                        byrAmountString += (byrAmount - byrAmount / 1000 * 1000);

                        String byrAmountString = (byrAmount/1000) + " ";

                        String oldValueCopsSctring =""+(byrAmount - byrAmount/1000*1000);
                        while(oldValueCopsSctring.length() < 3) {
                            oldValueCopsSctring+="0";
                        }
                        byrAmountString += oldValueCopsSctring;

                        amountTextView.setText(byrAmountString);

                    }
                }
            }

        }
    }
}
