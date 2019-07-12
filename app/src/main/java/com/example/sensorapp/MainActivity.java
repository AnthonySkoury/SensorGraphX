package com.example.sensorapp;

import android.provider.ContactsContract;
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
    DataSaver dataSaver;
    int count;

    boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();
    }


    protected void StartApp(){

        CreatePlots();
        CreateConnection();
        //FlowThread();
        ScreenThread();
    }

    protected void CreatePlots(){
        CreatePlotXY();
        CreatePlotZ();

        dataSaver = new DataSaver();
        appManager = new AppManager(positionGraph, altitudeBar, dataSaver);
        appManager.initGraphs();
    }

    protected void CreateConnection(){
        dataReceiver = new DataReceiver("http://128.195.207.30:8001/Service/xyzDisplay", appManager);
    }

    protected void CreatePlotXY(){

        positionGraph = (PositionDisplay) findViewById(R.id.PositionDisplay);

    }

    protected void CreatePlotZ(){

        altitudeBar = (AltitudeBar) findViewById(R.id.AltitudeBar);

        /*
        //rest is random stuff
        BarGraphSeries<DataPoint> series;

        double y,x;
        x=-5.0;
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
        */

    }

    public void mainLooper(){

        while(!stop){
            appManager.updatePosition(dataReceiver.connectToDevice());
            appManager.tracePosition();
        }

    }

    protected void ScreenThread(){

        Thread tGame=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //75% of 1sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appManager.updatePosition(dataReceiver.connectToDevice());
                                appManager.tracePosition();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tGame.start();

    }

    /*
    @Override
    protected void onResume(){
        super.onResume();
        Runnable run = new Runnable() {
            public void run() {
                try {
                    while(!stop) {
                        Thread.sleep(1000);  //75% of 1sec
                        appManager.updatePosition(dataReceiver.connectToDevice());
                        appManager.tracePosition();
                    }

                } catch (Exception e) {
                    System.out.println(" interrupted");
                }
            }
        };
        new Thread(run).start();
    }
    */



}
