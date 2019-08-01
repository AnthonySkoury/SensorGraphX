package com.example.sensorapp;

import android.graphics.Bitmap;

public class AppManager {

    private PositionDisplay positionDisplay;
    private AltitudeBar altitudeBar;
    private AccelerometerDisplay accelerometerDisplay;
    private AltimeterDisplay altimeterDisplay;
    private GyroDisplay gyroDisplay;
    protected DataSaver dataSaver;

    public AppManager(PositionDisplay positionDisplay, AltitudeBar altitudeBar, AccelerometerDisplay accelerometerDisplay, AltimeterDisplay altimeterDisplay, GyroDisplay gyroDisplay){
        this.positionDisplay = positionDisplay;
        this.altitudeBar = altitudeBar;
        this.accelerometerDisplay = accelerometerDisplay;
        this.altimeterDisplay = altimeterDisplay;
        this.gyroDisplay = gyroDisplay;
        this.dataSaver = new DataSaver();
    }


    public int updatePosition(double currentPos[]){
        dataSaver.updatePosition(currentPos);
        return 0;
    }

    public void updateAcc(double currentAcc[]){
        dataSaver.updateAccData(currentAcc);
    }

    public void updateAlt(double currentAlt[]){
        dataSaver.updateAltData(currentAlt);
    }

    public void updateGyro(double currentGyro[]){
        dataSaver.updateGyroData(currentGyro);
    }

    public void updateZUPTStatus(int Zupt){
        dataSaver.updateZUPTData(Zupt);
    }

    public void tracePosition(){
        positionDisplay.updateXPos();
        positionDisplay.plotXYPos();

        altitudeBar.updateZPos();

        // positionDisplay.plotXY(dataSaver.getCurrentPosition());
        positionDisplay.plotXY(dataSaver.getPosition(), dataSaver.getCurrentPosition());
        altitudeBar.plotZ(dataSaver.getCurrentZ());
        accelerometerDisplay.plotXY(dataSaver.getAccelorometer(), dataSaver.getCurrentAcc());
        altimeterDisplay.plotXY(dataSaver.getAltimeter(), dataSaver.getCurrentAlt());
        gyroDisplay.plotXY(dataSaver.getGyro(), dataSaver.getCurrentGyro());
    }


    public void printPositionHistory(){
        for(int i =0; i<dataSaver.getDataLength(); i++){
            System.out.println(dataSaver.getXPos(i));
            System.out.println(dataSaver.getYPos(i));
            System.out.println(dataSaver.getZPos(i));
            System.out.println("--------------------");
        }
    }

    public void initGraphs(){
        positionDisplay.initGraph();
        altitudeBar.initGraph();
        accelerometerDisplay.initGraph();
        altimeterDisplay.initGraph();
        gyroDisplay.initGraph();

    }

    public void reset(){
        dataSaver.resetData();
        positionDisplay.resetGraph();
        altitudeBar.resetGraph();
    }

    public void resetExtras(){
        accelerometerDisplay.resetGraph();
        altimeterDisplay.resetGraph();
        gyroDisplay.resetGraph();
    }

    public void setIP(String ip){

    }

    public void setMaxDatapoints(int max){
        dataSaver.setThreshold(max);
        positionDisplay.setMaxDataPoints(max);
    }

    public void setBackground(Bitmap bg){
        positionDisplay.changeBackground(bg);
    }

    public void updateRunTime(int time){
        dataSaver.runtime=time;
    }


}
