package com.example.chetakdriverapp;


// this is a rejected file not used any where



import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }
    

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            	AtPickUpPointMapWithList.mMapIsTouched = true;
                  break;

            case MotionEvent.ACTION_UP:
            	AtPickUpPointMapWithList.mMapIsTouched = false;
                  break;
        }
        return super.dispatchTouchEvent(event);
    }*/
}