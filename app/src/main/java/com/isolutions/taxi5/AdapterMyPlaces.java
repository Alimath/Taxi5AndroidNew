package com.isolutions.taxi5;

import android.content.Context;
import android.os.CountDownTimer;
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

import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class AdapterMyPlaces extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LocationData> mDataSource;

    public boolean isFavoriteAdapter;

    public AdapterMyPlaces(Context context, ArrayList<LocationData> items) {
        mContext = context;

        mDataSource = new ArrayList<LocationData>();
        for (LocationData item:items) {
            if(item.favoriteIsFavorite == isFavoriteAdapter) {
                mDataSource.add(item);
            }
        }
//        mDataSource = items;
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
            convertView = mInflater.inflate(R.layout.row_my_places, parent, false);

            ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.row_my_places_view_pager);
            PagerAdapterMyPlaces pagerAdapterMyPlaces = new PagerAdapterMyPlaces();
            viewPager.setAdapter(pagerAdapterMyPlaces);
            viewPager.setCurrentItem(0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    Log.d("taxi5", "Page scrolled: " + position);
                }

                @Override
                public void onPageSelected(int position) {
//                    Log.d("taxi5", "On Page Selected: " + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
//                    Log.d("taxi5", "On Page Scroll State Changed: " + state);
                }
            });

            holder = new ViewHolder();

            holder.titleTextView = (TextView) convertView.findViewById(R.id.row_my_places_title_text_view);
            holder.subTitleTextView= (TextView) convertView.findViewById(R.id.row_my_places_subtitle_text_view);
            holder.viewPager = viewPager;
            holder.selectPlaceButton = (Button) convertView.findViewById(R.id.row_my_places_select_place_button);
            holder.pagerAdapter = pagerAdapterMyPlaces;
            holder.pagerAdapter.isFavorite = isFavoriteAdapter;

            holder.viewPager.setCurrentItem(0);
            convertView.setTag(holder);

            if(isFavoriteAdapter) {
                holder.pagerAdapter.SetOnLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "edit place name click");
                    }
                });
                holder.pagerAdapter.SetOnRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "remove from favofite");
                    }
                });
            }
            else {
                holder.pagerAdapter.SetOnLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "add to favorite");
                    }
                });
                holder.pagerAdapter.SetOnRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "delete place");
                    }
                });
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            if(holder == null) {

            }

            holder.viewPager.setCurrentItem(0);
        }

        holder.fillData(mDataSource.get(position));

        return convertView;
    }

    public static class ViewHolder {
        PagerAdapterMyPlaces pagerAdapter;
        TextView titleTextView;
        TextView subTitleTextView;
        ViewPager viewPager;
        Button selectPlaceButton;
        private LocationData currentLocationData = null;

//        private OrderData orderData;

        void fillData(LocationData locationData) {
            currentLocationData = locationData;
            titleTextView.setText(locationData.getStringDescription());
            if(locationData.favoriteID != null) {
                if(!TextUtils.isEmpty(locationData.favoriteAlias)) {
                    subTitleTextView.setText(locationData.favoriteAlias);
                }
                else {
                    subTitleTextView.setText("без названия");
                }
            }
            else {
                subTitleTextView.setText("");
            }

            selectPlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(AppData.getInstance().leftDrawer != null) {
                        AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
                    }
                    AppData.getInstance().mainActivity.OpenClearMap();
                    FragmentMap.getMapFragment().statusCreateOrderFragment.ClearFields();

                    OrderData order = new OrderData();
                    order.from = currentLocationData;

                    AppData.getInstance().setCurrentOrder(order, true);
                    FragmentMap.getMapFragment().RefreshView();
                }
            });
        }
    }

    public void updateResource(ArrayList<LocationData> addressesData) {
//        this.mDataSource = addressesData;
        mDataSource = new ArrayList<LocationData>();
        if(addressesData != null) {
            for (LocationData item : addressesData) {
                if (item.favoriteIsFavorite == isFavoriteAdapter) {
                    mDataSource.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
