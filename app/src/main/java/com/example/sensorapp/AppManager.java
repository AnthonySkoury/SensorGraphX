package com.example.sensorapp;

public class AppManager {

    PositionDisplay positionDisplay;
    AltitudeBar altitudeBar;
    DataSaver dataSaver;

    public AppManager(PositionDisplay positionDisplay, AltitudeBar altitudeBar, DataSaver dataSaver){
        this.positionDisplay = positionDisplay;
        this.altitudeBar = altitudeBar;
        this.dataSaver = dataSaver;
    }

    public int updatePosition(){

        return 0;
    }

    public int updateAltitude(){

        return 0;
    }


}
