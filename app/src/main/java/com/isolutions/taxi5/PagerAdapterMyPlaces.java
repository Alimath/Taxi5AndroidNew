package com.isolutions.taxi5;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by fedar.trukhan on 23.09.16.
 */

public class PagerAdapterMyPlaces extends PagerAdapter {

    List<View> pages = null;

    boolean isFavorite = false;

    ImageView leftButtonImage;
    ImageView rightButtonImage;
    Button leftButton;
    Button rightButton;

    private View.OnClickListener leftButtonOnClickListener;
    private View.OnClickListener rightButtonOnClickListener;

    public void SetOnLeftButtonClickListener(View.OnClickListener listener) {
        this.leftButtonOnClickListener = listener;
    }
    public void SetOnRightButtonClickListener(View.OnClickListener listener) {
        this.rightButtonOnClickListener = listener;
    }

    void MakePlaceFavoriteIcons() {
        if(leftButtonImage != null) {
            leftButtonImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.my_places_edit_icon));
        }
        if(rightButtonImage != null) {
            rightButtonImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.my_places_unsel_icon));
        }
    }

    void MakePlaceMyPlace() {
        if(leftButtonImage != null) {
            leftButtonImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.my_places_sel_icon));
        }
        if(rightButtonImage != null) {
            rightButtonImage.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.my_places_remove_icon));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position){
        int resID = 0;
        switch (position) {
            case 0:
                resID = R.id.row_my_places_main;
                break;
            case 1:
                resID = R.id.row_my_places_second;
                leftButton = (Button) collection.findViewById(resID).findViewById(R.id.row_my_places_left_button);
                leftButtonImage = (ImageView) collection.findViewById(resID).findViewById(R.id.row_my_places_left_button_icon);

//                leftButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.d("taxi5", "log edit button listener");
//                    }
//                });

                rightButton = (Button) collection.findViewById(resID).findViewById(R.id.row_my_places_right_button);
                rightButtonImage = (ImageView) collection.findViewById(resID).findViewById(R.id.row_my_places_right_button_icon);

//                rightButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.d("taxi5", "log remove from favorite button listener");
//                    }
//                });

                if(isFavorite) {
                    MakePlaceFavoriteIcons();
                }
                else {
                    MakePlaceMyPlace();
                }

                if(this.leftButtonOnClickListener != null) {
                    leftButton.setOnClickListener(this.leftButtonOnClickListener);
                }
                if(this.rightButtonOnClickListener != null) {
                    rightButton.setOnClickListener(this.rightButtonOnClickListener);
                }

                break;
        }
        return collection.findViewById(resID);
    }

    @Override
    public int getCount() {
        //TODO Сделать редактирования моих мест.
        return 1;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public float getPageWidth(int page) {
        if(page == 0) {
            return 1.f;
        }
        else {
            float neededWidth = 140.f;
            float windowWidth = AppData.getInstance().getScreenWidthInDP();

            return neededWidth/windowWidth;
        }
    }
}
