package com.example.sensorapp;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;

/* Simple altitude bar graph display */
public class AltitudeBar extends GraphView {

    public AltitudeBar(Context context) {
        super(context);
    }

    public AltitudeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AltitudeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}