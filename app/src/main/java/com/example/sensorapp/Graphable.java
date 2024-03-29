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
 * Points based graph
 */
public class Graphable extends GraphView {

    int maxDataPoints = 1000;
    int rangeX;
    int rangeY;
    LineGraphSeries<DataPoint> xy_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> x_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> y_coord = new LineGraphSeries<>();

     PointsGraphSeries<DataPoint> xySeries;
    //LineGraphSeries<DataPoint> xySeries;
    PointsGraphSeries<DataPoint> currentPoint;
    // LineGraphSeries<DataPoint> xySeries;


    /**
     * Constructor
     * @param context
     */
    public Graphable(Context context) {
        super(context);
        setRange();
        plotXYSettings();
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public Graphable(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRange();
        plotXYSettings();
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public Graphable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setRange();
        plotXYSettings();
    }

    /**
     * Sets max data points displayed to value
     * @param max max data points
     */
    public void setMaxDataPoints(int max){
        maxDataPoints=max;
        plotXYSettings();
    }

    /**
     * Changes background to image
     * @param bg image resource
     */
    public void changeBackground(Bitmap bg){
        Drawable newbackground = new BitmapDrawable(getResources(), bg);
        this.setBackground(newbackground);

    }

    /**
     * Default range
     */
    public void setRange(){
        rangeX=10;
        rangeY=10;
    }

    /**
     * Set X range
     * @param range X range
     */
    public void setRangeX(int range){
        rangeX=range;
        plotXYSettings();

    }

    /**
     * Set Y range
     * @param range Y range
     */
    public void setRangeY(int range){
        rangeY=range;
        plotXYSettings();
    }

    public void updateXYPos(double x, double y){
        xy_coord.appendData(new DataPoint(x,y), true, maxDataPoints);
    }

    /**
     * Resets graph and reinitializes it
     */
    public void resetGraph(){
        removeAllSeries();
        removeSeries(xySeries);
        removeSeries(currentPoint);
        xySeries = new PointsGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        initGraph();
    }

    /**
     * Initializes graph
     */
    public void initGraph(){
        xySeries = new PointsGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        drawGraph();
    }

    /**
     * Creates thread to draw graph
     */
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

    /**
     * Updates graph data structure with new point and settings
     * @param position
     * @param currentPosition
     */
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
            xySeries.setSize(7f);
            //xySeries.setDrawDataPoints(true);
            //xySeries.setDataPointsRadius(6f);

            // xySeries.setSize(7f);

            //set some properties
            currentPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);
            currentPoint.setColor(Color.RED);
            currentPoint.setSize(9f);

            SettingsHandler();

            //addSeries(xySeries);
        }
        catch (ConcurrentModificationException e){
            System.out.println("Ece");
        }
    }

    /**
     * Calls settings method
     */
    public void SettingsHandler(){
        plotXYSettings();
    }

    /**
     * Settings method
     */
    public void plotXYSettings(){

        //set Scrollable and Scaleable
        getViewport().setScalable(true);
        getViewport().setScalableY(true);
        getViewport().setScrollable(true);
        getViewport().setScrollableY(true);

        //set manual x bounds
        getViewport().setYAxisBoundsManual(true);
        getViewport().setMaxY(rangeY);
        getViewport().setMinY(0);

        //set manual y bounds
        getViewport().setXAxisBoundsManual(true);
        getViewport().setMaxX(rangeX);
        getViewport().setMinX(0);

        //getGridLabelRenderer().setHorizontalLabelsVisible(false);

        GridLabelRenderer gridLabel = getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle("Graphable");
    }

    /**
     * Settings method with manual parameters
     * @param MaxY
     * @param MinY
     * @param MaxX
     * @param MinX
     * @param Title
     * @param manual
     */
    public void plotXYSettings(int MaxY, int MinY, int MaxX, int MinX, String Title, boolean manual){
        //set Scrollable and Scaleable
        getViewport().setScalable(true);
        getViewport().setScalableY(true);
        getViewport().setScrollable(true);
        getViewport().setScrollableY(true);

        getViewport().setBorderColor(Color.BLACK);
        getViewport().setDrawBorder(true);

        if(manual) {
            //set manual x bounds
            getViewport().setYAxisBoundsManual(true);
            getViewport().setMaxY(MaxY);
            getViewport().setMinY(MinY);

            //set manual y bounds
            getViewport().setXAxisBoundsManual(true);
            getViewport().setMaxX(MaxX);
            getViewport().setMinX(MinX);
        }


        GridLabelRenderer gridLabel = getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle(Title);
    }

}
