package com.isolutions.taxi5;

import android.app.Fragment;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public interface StatusesInterface {
    void fillWithOrder(OrderData order);
}
