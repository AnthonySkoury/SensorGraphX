package com.example.sensorapp;

import android.os.Environment;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class DataSaver {

    private Vector<double[]> unordered_position = new Vector<>();
    private Vector<double[]> position = new Vector<>();

    private Vector<double[]> unordered_accelorometer = new Vector<>();
    private Vector<double[]> accelorometer = new Vector<>();

    private Vector<double[]> unordered_altimeter = new Vector<>();
    private Vector<double[]> altimeter = new Vector<>();

    private Vector<double[]> unordered_gyro = new Vector<>();
    private Vector<double[]> gyro = new Vector<>();

    private int threshold=100;
    int runtime;

    public DataSaver(){

    }

    public void resetData(){
        unordered_position = new Vector<>();
        position = new Vector<>();
    }

    public void updatePosition(double[] currentPos){

        unordered_position.add(currentPos.clone());
        position = spliceData(calcThreshold());
        position = sortData(position);

    }

    public void updateAccData(double[] currentAcc){
        unordered_accelorometer.add(currentAcc.clone());
        //accelorometer = spliceData(calcThreshold());
        accelorometer.add(currentAcc.clone());
        accelorometer = sortData(accelorometer);
    }

    public void updateAltData(double currentAlt[]){
        unordered_altimeter.add(currentAlt.clone());
        //altimeter = spliceData(calcThreshold());
        altimeter.add(currentAlt.clone());
        altimeter = sortData(altimeter);
    }

    public void updateGyroData(double currentGyro[]){
        unordered_gyro.add(currentGyro.clone());
        //altimeter = spliceData(calcThreshold());
        gyro.add(currentGyro.clone());
        gyro = sortData(gyro);
    }


    public void setThreshold(int new_threshold){
        threshold=new_threshold;
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

    synchronized public double getCurrentX(){
        double last = unordered_position.lastElement()[0];
        //return unordered_position.lastElement()[2];
        return last;
    }

    synchronized public double getCurrentY(){
        double last = unordered_position.lastElement()[1];
        //return unordered_position.lastElement()[2];
        return last;
    }

    synchronized public double getCurrentZ(){
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

    public Vector<double[]> getAccelorometer(){
        return accelorometer;
    }

    public Vector<double[]> getAltimeter(){
        return altimeter;
    }

    public Vector<double[]> getGyro(){
        return gyro;
    }

    public double[] getCurrentPosition(){
        return unordered_position.lastElement();
    }

    public double[] getCurrentAcc(){
        return unordered_accelorometer.lastElement();
    }

    public double[] getCurrentAlt(){
        return unordered_altimeter.lastElement();
    }

    public double[] getCurrentGyro(){
        return unordered_gyro.lastElement();
    }

    public int calcThreshold(){
        if(unordered_position.size()>threshold){
            return (unordered_position.size()-threshold);
        }
        else{
            return 0;
        }
    }

    public Vector<double[]> spliceData(int startindex){
        Vector<double[]> splicedData = new Vector<double[]>();

        for(int i=startindex; i<unordered_position.size(); i++){
            splicedData.add(unordered_position.get(i));
        }
        return splicedData;
    }

    public Vector<double[]> sortData(Vector<double[]> sortedposition){

        Collections.sort(sortedposition,new Comparator<double[]>() {
            public int compare(double[] strings, double[] otherStrings) {
                if(strings[0]<otherStrings[0])
                    return -1;
                else if(otherStrings[0]<strings[0])
                    return 1;
                return 0;
            }
        });

        return sortedposition;

    }

    public String SaveToFile(String name){
        //String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName = name+".csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;
        FileWriter mFileWriter;
        // File exist
        try {
            if (f.exists() && !f.isDirectory()) {
                mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }

            for(double[] position: unordered_position){

                String[] data = {Double.toString(position[0]), Double.toString(position[1]), Double.toString(position[2])};
                writer.writeNext(data);
            }

            writer.close();
            //Toast.makeText(this, "Saved to "+filePath, Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return filePath;
    }

    private static final String SAMPLE_CSV_FILE_PATH = "./users.csv";

    public void readFile(String name) throws IOException {
        resetData();
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName = name+".csv";
        String filePath = baseDir + File.separator + fileName;
        try (

                CSVReader csvReader = new CSVReader(new FileReader(filePath))
        ) {
            // Reading Records One by One in a String array
            String[] data;
            while ((data = csvReader.readNext()) != null) {
                double[] temp = {Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2])};
                updatePosition(temp);
                /*
                System.out.println("X : " + data[0]);
                System.out.println("Y : " + data[1]);
                System.out.println("Z : " + data[2]);
                System.out.println("==========================");
                */
            }
        }
    }


}
