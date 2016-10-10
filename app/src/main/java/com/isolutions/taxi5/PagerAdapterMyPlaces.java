package com.isolutions.taxi5;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by fedar.trukhan on 23.09.16.
 */

public class PagerAdapterMyPlaces extends PagerAdapter {

    List<View> pages = null;

    @Override
    public Object instantiateItem(ViewGroup collection, int position){
        int resID = 0;
        switch (position) {
            case 0:
                resID = R.id.row_my_places_main;
                break;
            case 1:
                resID = R.id.row_my_places_second;
                Button b1 = (Button) collection.findViewById(resID).findViewById(R.id.row_my_places_edit_button);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "log edit button listener");
                    }
                });

                Button b2 = (Button) collection.findViewById(resID).findViewById(R.id.row_my_places_remove_from_favorite_button);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("taxi5", "log remove from favorite button listener");
                    }
                });
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
