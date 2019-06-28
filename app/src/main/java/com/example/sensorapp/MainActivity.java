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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();

    }


    protected void StartApp(){

        CreatePlots();
    }

    protected void CreatePlots(){
        CreatePlotXY();
        CreatePlotZ();
    }

    protected void CreatePlotXY(){
        LineGraphSeries<DataPoint> series;

        double y,x;
        x=-5.0;
        PositionDisplay positionGraph = (PositionDisplay) findViewById(R.id.PositionDisplay);
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
        AltitudeBar positionGraph = (AltitudeBar) findViewById(R.id.AltitudeBar);
        series = new BarGraphSeries<>();

        positionGraph.getViewport().setXAxisBoundsManual(true);
        positionGraph.getViewport().setMinX(0);
        positionGraph.getViewport().setMaxX(2);

        positionGraph.getViewport().setYAxisBoundsManual(true);
        positionGraph.getViewport().setMinY(-60);
        positionGraph.getViewport().setMaxY(60);

        positionGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        GridLabelRenderer gridLabel = positionGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Altitude (Z)");

        for(int i=0; i<1; i++){
            series.appendData(new DataPoint(0,30), true, 1);
        }
        positionGraph.addSeries(series);

        DataPoint[] dp = new DataPoint[]{new DataPoint(0,1)};


    }




}
