package com.example.sensorapp;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;

/* Simple trajectory plot for x and y position */
public class PositionDisplay extends GraphView {

    public PositionDisplay(Context context) {
        super(context);
    }

    public PositionDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PositionDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
