package com.example.sensorapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ConcurrentModificationException;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/**
 * Simple trajectory plot for x and y position
 */
public class PositionDisplay extends Graphable {

    /**
     * Constructor
     * @param context
     */
    public PositionDisplay(Context context) {
        super(context);
        super.plotXYSettings(rangeY,-rangeY, rangeX, -rangeX, "Position (X,Y)", true);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public PositionDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.plotXYSettings(rangeY,-rangeY, rangeX, -rangeX, "Position (X,Y)", true);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public PositionDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.plotXYSettings(rangeY,-rangeY, rangeX, -rangeX, "Position (X,Y)", true);
    }

    @Override
    public void setRange(){
        rangeX=15;
        rangeY=15;
    }

    /**
     * Calls settings with defined values
     */
    @Override
    public void SettingsHandler(){
        super.plotXYSettings(rangeY, -rangeY, rangeX, -rangeX, "Position (X,Y)", true);
    }



}
