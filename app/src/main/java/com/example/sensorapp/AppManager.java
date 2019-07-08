package com.example.sensorapp;

public class AppManager {

    private PositionDisplay positionDisplay;
    private AltitudeBar altitudeBar;
    private DataSaver dataSaver;

    public AppManager(PositionDisplay positionDisplay, AltitudeBar altitudeBar, DataSaver dataSaver){
        this.positionDisplay = positionDisplay;
        this.altitudeBar = altitudeBar;
        this.dataSaver = dataSaver;
    }


    public int updatePositionX(double x){
        dataSaver.updateXPos(x);
        return 0;
    }

    public int updatePositionY(double y){
        dataSaver.updateYPos(y);
        return 0;
    }

    public int updatePositionZ(double z){
        dataSaver.updateZPos(z);
        return 0;
    }

    public int updatePosition(){

        return 0;
    }

    public int updateAltitude(){

        return 0;
    }


}
