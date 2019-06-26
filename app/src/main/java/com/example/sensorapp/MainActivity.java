package com.example.sensorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    AltitudeDisplay Screen;

    LineGraphSeries<DataPoint> series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();

    }

    //starts song, initializes buttons and gamethread
    protected void StartApp(){


        Screen = (AltitudeDisplay) findViewById(R.id.AltitudeDisplay);
        CreatePlot();

    }

    protected void CreatePlot(){
        double y,x;
        x=-5.0;
        PositionDisplay positionGraph = (PositionDisplay) findViewById(R.id.PositionDisplay);
        series = new LineGraphSeries<DataPoint>();

        for(int i=0; i<500; i++){
            x=x+0.1;
            y=Math.sin(x);
            series.appendData(new DataPoint(x,y), true, 500);
        }
        positionGraph.addSeries(series);
    }


}
