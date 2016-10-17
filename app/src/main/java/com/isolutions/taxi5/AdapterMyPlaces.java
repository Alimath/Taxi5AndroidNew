package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class AdapterMyPlaces extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LocationData> mDataSource;

    public FragmentMyPlaces parentFragment;


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
            holder.subTitleTextView = (CustomEditText) convertView.findViewById(R.id.row_my_places_subtitle_text_view);
            holder.viewPager = viewPager;
            holder.selectPlaceButton = (Button) convertView.findViewById(R.id.row_my_places_select_place_button);
            holder.pagerAdapter = pagerAdapterMyPlaces;
            holder.pagerAdapter.isFavorite = isFavoriteAdapter;

            holder.subTitleTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Log.d("taxi5", "focus is change: " + b);
                }
            });

            holder.viewPager.setCurrentItem(0);
            convertView.setTag(holder);

            final ViewHolder holderParent = holder;

            if(isFavoriteAdapter) {
                holder.pagerAdapter.SetOnLeftButtonClickListener(new View.OnClickListener() {
                    //Edit place
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                        }
                        else {
                            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
                        }
                        builder.setTitle(AppData.getInstance().mainActivity.getString(R.string.adapter_my_places_edit_place_comment_title));

                        final EditText input = new EditText(AppData.getInstance().mainActivity);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        input.setPadding(AppData.getInstance().dpToPx(16), AppData.getInstance().dpToPx(16), AppData.getInstance().dpToPx(16), 0);
                        input.setHint(AppData.getInstance().getAppContext().getString(R.string.adapter_my_places_place_has_no_name));
                        input.setLayoutParams(lp);
                        if(holderParent.currentLocationData.favoriteAlias != null) {
                            input.setText(holderParent.currentLocationData.favoriteAlias);
                        }

                        builder.setView(input);
                        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if(b) {
                                    ((EditText) view).setSelection(((EditText) view).getText().length());
                                    ((EditText) view).setCursorVisible(true);
                                }
                            }
                        });
                        input.setLines(1);
                        input.setMaxLines(1);
                        input.setMinLines(1);
                        input.setSingleLine(true);
                        input.setFocusableInTouchMode(true);
                        input.setFocusable(true);
                        input.setBackgroundColor(AppData.getInstance().getColor(R.color.clearColor));
                        builder.setPositiveButton(AppData.getInstance().mainActivity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holderParent.currentLocationData.favoriteAlias = input.getText().toString();
                                holderParent.fillData(holderParent.currentLocationData);
                                UploadPlace(holderParent.currentLocationData);
                            }
                        });
                        builder.setNegativeButton(AppData.getInstance().mainActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        builder.show();
                    }
                });
                holder.pagerAdapter.SetOnRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holderParent.currentLocationData.favoriteIsFavorite = false;

                        UploadPlace(holderParent.currentLocationData);
                    }
                });
            }
            else {
                holder.pagerAdapter.SetOnLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holderParent.currentLocationData.favoriteIsFavorite = true;
                        UploadPlace(holderParent.currentLocationData);
                    }
                });
                holder.pagerAdapter.SetOnRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeletePlace(holderParent.currentLocationData);
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

    void UploadPlace(LocationData place) {
        final Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }

        if(parentFragment != null) {
            parentFragment.ShowLoadingStub();
        }
        Call<Void> call = taxi5SDK.UpdateFavoritePlace(TokenData.getInstance().getToken(), place.favoriteID, place.favoriteAlias, place.favoriteIsFavorite);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (parentFragment != null) {
                    parentFragment.HideLoadingStub();
                }
                if(response.isSuccessful()) {
                    parentFragment.LoadMyPlaces();
                }
                else if(response.code() == 403) {
                    //TODO NEED REFRESHING TOKEN UPDATE
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(parentFragment != null) {
                    parentFragment.HideLoadingStub();
                }
                Log.d("taxi5", "upload my place failure: " + t.getLocalizedMessage());
            }
        });
    }

    void DeletePlace(LocationData place) {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }

        if(parentFragment != null) {
            parentFragment.ShowLoadingStub();
        }
        Call<Void> call = taxi5SDK.DeleteFavoritePlace(TokenData.getInstance().getToken(), place.favoriteID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (parentFragment != null) {
                    parentFragment.HideLoadingStub();
                }
                if(response.isSuccessful()) {
                    parentFragment.LoadMyPlaces();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (parentFragment != null) {
                    parentFragment.HideLoadingStub();
                }
                Toast.makeText(AppData.getInstance().mainActivity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("taxi5", "upload my place failure: " + t.getLocalizedMessage());
            }
        });
    }

    static class ViewHolder {
        PagerAdapterMyPlaces pagerAdapter;
        TextView titleTextView;
        CustomEditText subTitleTextView;
        ViewPager viewPager;
        Button selectPlaceButton;
        private LocationData currentLocationData = null;

//        private OrderData orderData;

        void fillData(LocationData locationData) {
            currentLocationData = locationData;
            titleTextView.setText(locationData.getStringDescription());
            if(locationData.favoriteID != null) {
                if(!TextUtils.isEmpty(locationData.favoriteAlias)) {
                    if(!subTitleTextView.hasFocus()) {
                        subTitleTextView.setText(locationData.favoriteAlias);
                    }
                }
                else {
                    if(!subTitleTextView.hasFocus()) {
                        subTitleTextView.setText(AppData.getInstance().getAppContext().getString(R.string.adapter_my_places_place_has_no_name));
                    }
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


