package com.example.sensorapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Vector;

import static android.content.ContentValues.TAG;

/* Simple trajectory plot for x and y position */
public class PositionDisplay extends GraphView {

    int maxDataPoints = 1000;
    LineGraphSeries<DataPoint> xy_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> x_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> y_coord = new LineGraphSeries<>();

    PointsGraphSeries<DataPoint> xySeries;
   // LineGraphSeries<DataPoint> xySeries;


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

    public void initGraph(){
        xySeries = new PointsGraphSeries<>();
    }

    synchronized public void plotXY(double[] currentPosition){
        try {
            xySeries.appendData(new DataPoint(currentPosition[0], currentPosition[1]), true, maxDataPoints);
        }catch (IllegalArgumentException e){
            Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage() );
        }

        //set some properties
        xySeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries.setColor(Color.BLUE);
        xySeries.setSize(5f);

        //set Scrollable and Scaleable
        this.getViewport().setScalable(true);
        this.getViewport().setScalableY(true);
        this.getViewport().setScrollable(true);
        this.getViewport().setScrollableY(true);

        //set manual x bounds
        this.getViewport().setYAxisBoundsManual(true);
        this.getViewport().setMaxY(50);
        this.getViewport().setMinY(-50);

        //set manual y bounds
        this.getViewport().setXAxisBoundsManual(true);
        this.getViewport().setMaxX(50);
        this.getViewport().setMinX(-50);

        this.addSeries(xySeries);

    }

    synchronized public void plotXY(Vector<double[]> position){

        xySeries = new PointsGraphSeries<>();

       // xySeries = new LineGraphSeries<>();


        for(int i = 0;i <position.size(); i++){
            try{
                double x = position.get(i)[0];
                double y = position.get(i)[1];
                xySeries.appendData(new DataPoint(x,y),true, maxDataPoints);
            }catch (IllegalArgumentException e){
                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage() );
            }
        }

        //set some properties
        xySeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries.setColor(Color.BLUE);
        xySeries.setSize(5f);

        //set Scrollable and Scaleable
        this.getViewport().setScalable(true);
        this.getViewport().setScalableY(true);
        this.getViewport().setScrollable(true);
        this.getViewport().setScrollableY(true);

        //set manual x bounds
        this.getViewport().setYAxisBoundsManual(true);
        this.getViewport().setMaxY(50);
        this.getViewport().setMinY(-50);

        //set manual y bounds
        this.getViewport().setXAxisBoundsManual(true);
        this.getViewport().setMaxX(50);
        this.getViewport().setMinX(-50);

        this.addSeries(xySeries);
    }

}
