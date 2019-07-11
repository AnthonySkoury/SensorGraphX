package com.example.sensorapp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class DataSaver {

    private final Vector<double[]> unordered_position = new Vector<>();
    private final Vector<double[]> position = new Vector<>();

    public DataSaver(){

    }

    public void updatePosition(double currentPos[]){

        unordered_position.add(currentPos.clone());
        position.add(currentPos.clone());

    }

    public double getXPos(int index){
        return position.get(index)[0];
    }

    public double getYPos(int index){
        return position.get(index)[1];
    }

    public double getZPos(int index){
        return position.get(index)[2];
    }

    public double getCurrentZ(){
        double last = unordered_position.lastElement()[2];
        //return unordered_position.lastElement()[2];
        return last;
    }

    public int getDataLength(){
        return position.size();
    }

    public Vector<double[]> getPosition(){
        return position;
    }

    public double[] getCurrentPosition(){
        return position.lastElement();
    }

    public void sortData(){

    }

    public int SaveToFile(){

        return 0;
    }


}
