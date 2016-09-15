package com.isolutions.taxi5;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class AdapterSearchAddress extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LocationData> mDataSource;

    public AdapterSearchAddress(Context context, ArrayList<LocationData> items) {
        mContext = context;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public LocationData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.row_address_search, parent, false);

            holder = new ViewHolder();

            holder.titleTextView = (TextView) convertView.findViewById(R.id.row_address_search_title_text_view);
            holder.subTitleTextView= (TextView) convertView.findViewById(R.id.row_address_search_subtitle_text_view);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fillData(mDataSource.get(position));

        return convertView;
    }

    public static class ViewHolder {
        TextView titleTextView;
        TextView subTitleTextView;

//        private OrderData orderData;

        void fillData(LocationData locationData) {
            titleTextView.setText(locationData.getStringDescription());
            if(locationData.favoriteID != null) {
                if(!TextUtils.isEmpty(locationData.favoriteAlias)) {
                    subTitleTextView.setText(locationData.favoriteAlias);
                }
                else {
                    subTitleTextView.setText("Имя не указано");
                }
            }
            else {
                subTitleTextView.setText("");
            }
        }
    }

    public void updateResource(ArrayList<LocationData> addressesData) {
        this.mDataSource = addressesData;
        notifyDataSetChanged();
    }
}
