package com.example.sensorapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import static android.content.ContentValues.TAG;

/**
 * Simple altitude bar graph display with methods
 */
public class AltitudeBar extends GraphView {

    LineGraphSeries<DataPoint> zSeries;
    int rangeZ = 20;
    int maxDataPoints = 1000;

    /**
     * Constructor
     * @param context
     */
    public AltitudeBar(Context context) {
        super(context);
        plotZSettings();
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public AltitudeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        plotZSettings();
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AltitudeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        plotZSettings();
    }


    public void resetGraph(){
        removeAllSeries();
        removeSeries(zSeries);
        zSeries = new LineGraphSeries<>();
        initGraph();
    }

    public void initGraph(){
        zSeries = new LineGraphSeries<>();
        drawGraph();
    }

    public void drawGraph(){
        this.post(new Runnable(){
            @Override
            public void run(){
                removeAllSeries();
                removeSeries(zSeries);
                addSeries(zSeries);
                //addSeries(currentPoint);
            }
        });
    }

    public void setRangeZ(int range){
        rangeZ=range;
        if(zSeries.isEmpty()){
            initGraph();
        }
    }

    public void updateZPos(){

    }

    public void plotZSettings(){
        getViewport().setXAxisBoundsManual(true);
        getViewport().setMinX(0);
        getViewport().setMaxX(rangeZ);

        getViewport().setYAxisBoundsManual(true);
        getViewport().setMinY(-rangeZ);
        getViewport().setMaxY(rangeZ);

        getGridLabelRenderer().setHorizontalLabelsVisible(false);

        GridLabelRenderer gridLabel = getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Altitude (Z)");
    }

    public void plotZ(double z){

        removeSeries(zSeries);
        zSeries = new LineGraphSeries<>();

        int range = rangeZ-(int)(rangeZ/4);
        if(rangeZ==1){
            range=2;
        }

        for (int i = 0; i < range; i++) {
            try {
                // double x = i;
                // double y = z;
                zSeries.appendData(new DataPoint(i, z), true, range);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage());
            }
        }
        zSeries.setColor(Color.BLUE);
        zSeries.setThickness(10);
        plotZSettings();
        addSeries(zSeries);
    }

}