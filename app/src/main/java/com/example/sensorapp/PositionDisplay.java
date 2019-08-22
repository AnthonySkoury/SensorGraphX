package com.example.sensorapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.reflect.Constructor;
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
        rangeX=50;
        rangeY=50;
    }

    public void scale(){
        this.setScaleX(0.5f);
        this.setScaleY(2.0f);
    }

    public void resizeView(int newWidth, int newHeight) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = 130;
        this.setLayoutParams(params);
        /*
        try {
            Constructor<? extends ViewGroup.LayoutParams> ctor = this.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);
            this.setLayoutParams(ctor.newInstance(newWidth, newHeight));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * Calls settings with defined values
     */
    @Override
    public void SettingsHandler(){
        super.plotXYSettings(rangeY, -rangeY, rangeX, -rangeX, "Position (X,Y)", true);
    }



}
