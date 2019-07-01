package com.example.sensorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Document;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DataReceiver dataReceiver;
    PositionDisplay positionGraph;
    AltitudeBar altitudeBar;
    AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();
    }


    protected void StartApp(){

        CreatePlots();
        appManager = new AppManager(positionGraph, altitudeBar);
        dataReceiver = new DataReceiver("http://XXX.XXX.XXX.XXX:8080/MobileAPI/SampleTemp", appManager);

    }

    protected void CreatePlots(){
        CreatePlotXY();
        CreatePlotZ();
    }

    protected void CreatePlotXY(){
        LineGraphSeries<DataPoint> series;

        double y,x;
        x=-5.0;
        positionGraph = (PositionDisplay) findViewById(R.id.PositionDisplay);
        series = new LineGraphSeries<DataPoint>();
        GridLabelRenderer gridLabel = positionGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Position (X, Y)");

        for(int i=0; i<500; i++){
            x=x+0.1;
            y=Math.sin(x);
            series.appendData(new DataPoint(x,y), true, 500);
        }
        positionGraph.addSeries(series);
    }

    protected void CreatePlotZ(){
        BarGraphSeries<DataPoint> series;

        double y,x;
        x=-5.0;
        altitudeBar = (AltitudeBar) findViewById(R.id.AltitudeBar);
        series = new BarGraphSeries<>();

        altitudeBar.getViewport().setXAxisBoundsManual(true);
        altitudeBar.getViewport().setMinX(0);
        altitudeBar.getViewport().setMaxX(2);

        altitudeBar.getViewport().setYAxisBoundsManual(true);
        altitudeBar.getViewport().setMinY(-60);
        altitudeBar.getViewport().setMaxY(60);

        altitudeBar.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        GridLabelRenderer gridLabel = altitudeBar.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Altitude (Z)");

        for(int i=0; i<1; i++){
            series.appendData(new DataPoint(0,30), true, 1);
        }
        altitudeBar.addSeries(series);

        DataPoint[] dp = new DataPoint[]{new DataPoint(0,1)};


    }




}
