package com.example.sensorapp;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/* Simple trajectory plot for x and y position */
public class PositionDisplay extends GraphView {

    int maxDataPoints = 1000;
    LineGraphSeries<DataPoint> xy_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> x_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> y_coord = new LineGraphSeries<>();

    public PositionDisplay(Context context) {
        super(context);
    }

    public PositionDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PositionDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void updateXYPos(double x, double y){
        xy_coord.appendData(new DataPoint(x,y), true, maxDataPoints);
    }

    public void updateXPos(){

    }

    public void updateYPos(){

    }

    public void plotXYPos(){

    }

    public void plotXPos(){

    }

    public void plotYPos(){

    }

}
