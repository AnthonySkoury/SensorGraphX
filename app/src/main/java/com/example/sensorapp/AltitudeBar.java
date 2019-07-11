package com.example.sensorapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import static android.content.ContentValues.TAG;

/* Simple altitude bar graph display */
public class AltitudeBar extends GraphView {

    LineGraphSeries<DataPoint> zSeries;
    int rangeZ = 60;
    int maxDataPoints = 1000;

    public AltitudeBar(Context context) {
        super(context);
    }

    public AltitudeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AltitudeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initGraph(){
        zSeries = new LineGraphSeries<>();
        //drawGraph();
    }

    public void drawGraph(){
        this.post(new Runnable(){
            @Override
            public void run(){
                removeAllSeries();
                System.out.println(zSeries);
                addSeries(zSeries);
                //addSeries(currentPoint);
            }
        });
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

        for (int i = 0; i < rangeZ-10; i++) {
            try {
                // double x = i;
                // double y = z;
                zSeries.appendData(new DataPoint(i, z), true, rangeZ-10);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage());
            }
        }
        plotZSettings();
        addSeries(zSeries);
    }

}