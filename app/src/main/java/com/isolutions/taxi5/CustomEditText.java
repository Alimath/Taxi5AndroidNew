package com.isolutions.taxi5;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by fedar.trukhan on 15.10.16.
 */

public class CustomEditText extends EditText {

    Context context;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            return super.dispatchKeyEvent(event);
//            Log.d("taxi5", "event back pressed");
            // do your stuff
//            return false;
        }
        return super.dispatchKeyEvent(event);

    }
}