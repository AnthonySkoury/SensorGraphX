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

import java.util.ConcurrentModificationException;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/* Simple trajectory plot for x and y position */
public class PositionDisplay extends GraphView {

    int maxDataPoints = 1000;
    int rangeX=60;
    int rangeY=60;
    LineGraphSeries<DataPoint> xy_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> x_coord = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> y_coord = new LineGraphSeries<>();

    PointsGraphSeries<DataPoint> xySeries;
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
        xySeries = new PointsGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        initGraph();
    }

    public void initGraph(){
        xySeries = new PointsGraphSeries<>();
        currentPoint = new PointsGraphSeries<>();
        drawGraph();
    }

    /*
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

    }*/

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
            xySeries.setShape(PointsGraphSeries.Shape.POINT);
            xySeries.setColor(Color.BLUE);
            xySeries.setSize(5f);

            //set some properties
            currentPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);
            currentPoint.setColor(Color.RED);
            currentPoint.setSize(8f);

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
