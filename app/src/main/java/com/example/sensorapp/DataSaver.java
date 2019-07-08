package com.example.sensorapp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class DataSaver {

    /*
    LinkedList<Double> x_position;
    LinkedList<Double> y_position;
    LinkedList<Double> z_position;
    */


    protected Vector<Double> x_position;
    protected Vector<Double> y_position;
    protected Vector<Double> z_position;


    public DataSaver(){

        /*
        this.x_position = new LinkedList<>();
        this.y_position = new LinkedList<>();
        this.z_position = new LinkedList<>();
       */

        this.x_position = new Vector<>();
        this.y_position = new Vector<>();
        this.z_position = new Vector<>();


    }

    public void updatePosition(double x, double y, double z){
        updateXPos(x);
        updateYPos(y);
        updateZPos(z);
    }

    public void updateXPos(double x){
        x_position.add(x);
    }

    public void updateYPos(double y){
        y_position.add(y);
    }

    public void updateZPos(double z){
        z_position.add(z);
    }

    /*
    public double getLastX(){
        return x_position.getLast();
    }

    public double getLastY(){
        return y_position.getLast();
    }

    public double getLastZ(){
        return z_position.getLast();
    }
    */

    public int SaveToFile(){
        Iterator<Double> iterX = x_position.iterator();
        Iterator<Double> iterY = y_position.iterator();
        Iterator<Double> iterZ = z_position.iterator();

        while (iterX.hasNext()) {
            //do the file writing here
            double elementX = iterX.next();
            double elementY = iterY.next();
            double elementZ = iterZ.next();

        }
        return 0;
    }

    /* iterator method to iterate through linkedlist with O(n)
    Iterator<E> iter = list.iterator();
    while (iter.hasNext()) {
        E element = iter.next();
        ...
    }
     */

}
