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

/* Simple trajectory plot for x and y position */
public class PositionDisplay extends GraphView {

    int maxDataPoints = 1000;
    int rangeX=20;
    int rangeY=20;
    LineGraphSeries<DataPoint> xy_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> x_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> y_coord = new LineGraphSeries<>();

    // PointsGraphSeries<DataPoint> xySeries;
    LineGraphSeries<DataPoint> xySeries;
    PointsGraphSeries<DataPoint> currentPoint;
    // LineGraphSeries<DataPoint> xySeries;


    public PositionDisplay(Context context) {
        super(context);
        plotXYSettings();
    }

    public PositionDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        plotXYSettings();
    }

    public PositionDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        plotXYSettings();
    }

    public void setMaxDataPoints(int max){
        maxDataPoints=max;
        resetGraph();
    }

    public void changeBackground(Bitmap bg){
        Drawable newbackground = new BitmapDrawable(getResources(), bg);
        this.setBackground(newbackground);

    }

    public void setRangeX(int range){
        rangeX=range;
        plotXYSettings();

    }

    public void setRangeY(int range){
        rangeY=range;
        plotXYSettings();
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

    public void resetGraph(){
        removeAllSeries();
        removeSeries(xySeries);
        removeSeries(currentPoint);
        xySeries = new LineGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        initGraph();
    }

    public void initGraph(){
        xySeries = new LineGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        drawGraph();
    }

    public void drawGraph(){
        this.post(new Runnable(){
            @Override
            public void run(){
                removeAllSeries();
                addSeries(xySeries);
                addSeries(currentPoint);
                //addSeries(currentPoint);
            }
        });
    }

    synchronized public void plotXY(Vector<double[]> position, double[] currentPosition){
        try {
            //xySeries = new PointsGraphSeries<>();

            // xySeries = new LineGraphSeries<>();
            xySeries.resetData(new DataPoint[] {});
            currentPoint.resetData(new DataPoint[] {});

            for (int i = 0; i < position.size(); i++) {
                try {
                    double x = position.get(i)[0];
                    double y = position.get(i)[1];
                    xySeries.appendData(new DataPoint(x, y), true, position.size());
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            currentPoint.appendData(new DataPoint(currentPosition[0], currentPosition[1]), true, 1);

            //set some properties
           // xySeries.setShape(PointsGraphSeries.Shape.POINT);
            xySeries.setColor(Color.BLUE);
           // xySeries.setAnimated(true);
            xySeries.setThickness(7);
            xySeries.setDrawDataPoints(true);
            xySeries.setDataPointsRadius(8f);
           // xySeries.setSize(7f);

            //set some properties
            currentPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);
            currentPoint.setColor(Color.RED);
            currentPoint.setSize(10f);

            plotXYSettings();

            //addSeries(xySeries);
        }
        catch (ConcurrentModificationException e){
            System.out.println("Ece");
        }
    }

    public void plotXYSettings(){

        //set Scrollable and Scaleable
        getViewport().setScalable(true);
        getViewport().setScalableY(true);
        getViewport().setScrollable(true);
        getViewport().setScrollableY(true);

        //set manual x bounds
        getViewport().setYAxisBoundsManual(true);
        getViewport().setMaxY(rangeY);
        getViewport().setMinY(-rangeY);

        //set manual y bounds
        getViewport().setXAxisBoundsManual(true);
        getViewport().setMaxX(rangeX);
        getViewport().setMinX(-rangeX);

        //getGridLabelRenderer().setHorizontalLabelsVisible(false);

        GridLabelRenderer gridLabel = getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle("Position (X,Y)");
    }

}
