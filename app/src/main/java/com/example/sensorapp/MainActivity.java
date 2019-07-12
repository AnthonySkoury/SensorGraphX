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
import java.util.concurrent.TimeUnit;

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
    boolean stop=true;

    TextView X_Coord;
    TextView Y_Coord;
    TextView Z_Coord;
    TextView Run_Time;

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
        CreateText();
        setupButtons();

    }

    protected void CreateText(){
        X_Coord = (TextView)findViewById(R.id.x_coordinate);
        Y_Coord = (TextView)findViewById(R.id.y_coordinate);
        Z_Coord = (TextView)findViewById(R.id.z_coordinate);
        Run_Time = (TextView)findViewById(R.id.time);
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

    }

    public void mainLooper(){

        while(!stop){
            appManager.updatePosition(dataReceiver.connectToDevice());
            appManager.tracePosition();
        }

    }

    protected void ScreenThread(){
        startTime();

        final Thread tGame=new Thread(){
            @Override
            public void run(){
                while(!stop && !isInterrupted()){
                    try {
                        Thread.sleep(1000);  //75% of 1sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tasks();
                                System.out.println(getName());


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

    public void Tasks(){
        appManager.updatePosition(dataReceiver.connectToDevice());
        appManager.tracePosition();
        String xPos = "X Position (in meters): "+String.valueOf(dataSaver.getCurrentX());
        String yPos = "Y Position (in meters): "+String.valueOf(dataSaver.getCurrentY());
        String zPos = "Z Position (in meters): "+String.valueOf(dataSaver.getCurrentZ());
        String runtime = "Elapsed Time: "+String.valueOf(getElapsedTimeSecs())+" s";
        X_Coord.setText(xPos);
        Y_Coord.setText(yPos);
        Z_Coord.setText(zPos);
        Run_Time.setText(runtime);
    }

    public void setupButtons(){
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    stop = true;
                    TimeUnit.SECONDS.sleep(1);
                    stop = false;
                    appManager.reset();
                    ScreenThread();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stop = true;
            }
        });

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    stop = true;
                    TimeUnit.SECONDS.sleep(1);
                    stop = false;
                    appManager.reset();
                    ScreenThread();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        });

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

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void startTime() {
        this.startTime = System.nanoTime();
        this.running = true;
    }


    public void stopTime() {
        this.stopTime = System.nanoTime();
        this.running = false;
    }


    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.nanoTime() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000000000);
        } else {
            elapsed = ((stopTime - startTime) / 1000000000);
        }
        return elapsed;
    }


}
