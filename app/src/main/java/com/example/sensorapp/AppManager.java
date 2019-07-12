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


    public int updatePosition(double currentPos[]){
        dataSaver.updatePosition(currentPos);
        return 0;
    }

    public void tracePosition(){
        positionDisplay.updateXPos();
        positionDisplay.plotXYPos();

        altitudeBar.updateZPos();

        // positionDisplay.plotXY(dataSaver.getCurrentPosition());
        positionDisplay.plotXY(dataSaver.getPosition(), dataSaver.getCurrentPosition());
        altitudeBar.plotZ(dataSaver.getCurrentZ());
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
    }


}
