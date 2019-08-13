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

/**
 * Contains data structures and methods for data for graphs
 */
public class DataSaver {

    private Vector<double[]> unordered_position = new Vector<>();
    private Vector<double[]> position = new Vector<>();

    private Vector<double[]> unordered_accelorometer = new Vector<>();
    private Vector<double[]> accelorometer = new Vector<>();

    private Vector<double[]> unordered_altimeter = new Vector<>();
    private Vector<double[]> altimeter = new Vector<>();

    private Vector<double[]> unordered_gyro = new Vector<>();
    private Vector<double[]> gyro = new Vector<>();

    String ZUPT_Status="Off";

    private int threshold=100;
    int runtime;

    public DataSaver(){

    }

    /**
     * Resets data
     */
    public void resetData(){
        unordered_position = new Vector<>();
        position = new Vector<>();
    }

    /**
     * Resets extra sensor graphs
     */
    public void resetExtras(){
        unordered_accelorometer = new Vector<>();
        accelorometer = new Vector<>();

        unordered_altimeter = new Vector<>();
        altimeter = new Vector<>();

        unordered_gyro = new Vector<>();
        gyro = new Vector<>();
    }

    /**
     * Updates position data structures
     * @param currentPos current position in XYZ format
     */
    public void updatePosition(double[] currentPos){

        unordered_position.add(currentPos.clone());
        position = spliceData(calcThreshold());
        position = sortData(position);

    }

    /**
     * Updates accelerometer data structure
     * @param currentAcc current XY with runtime and value
     */
    public void updateAccData(double[] currentAcc){
        unordered_accelorometer.add(currentAcc.clone());
        //accelorometer = spliceData(calcThreshold());
        accelorometer.add(currentAcc.clone());
        //accelorometer = sortData(accelorometer);
    }

    /**
     * Updates altimeter data structure
     * @param currentAlt current XY with runtime and value
     */
    public void updateAltData(double currentAlt[]){
        unordered_altimeter.add(currentAlt.clone());
        //altimeter = spliceData(calcThreshold());
        altimeter.add(currentAlt.clone());
        //altimeter = sortData(altimeter);
    }

    /**
     * Updates Gyro data structure
     * @param currentGyro current XY with runtime and value
     */
    public void updateGyroData(double currentGyro[]){
        unordered_gyro.add(currentGyro.clone());
        //altimeter = spliceData(calcThreshold());
        gyro.add(currentGyro.clone());
        //gyro = sortData(gyro);
    }

    /**
     * Sets ZUPT string on or off depending status
     * @param status boolean, 1 representing on and 0 off
     */
    public void updateZUPTData(int status){
        if(status==1)
            ZUPT_Status="On";
        else
            ZUPT_Status="Off";
    }

    /**
     * Sets threshold for max data points at a time
     * @param new_threshold new threshold size
     */
    public void setThreshold(int new_threshold){
        threshold=new_threshold;
    }

    /**
     * Gets a specific X position
     * @param index desired index
     * @return X position at index
     */
    public double getXPos(int index){
        return position.get(index)[0];
    }

    /**
     * Gets a specific Y position
     * @param index desired index
     * @return Y position at index
     */
    public double getYPos(int index){
        return position.get(index)[1];
    }

    /**
     * Gets a specific Z position
     * @param index desired index
     * @return Z position at index
     */
    public double getZPos(int index){
        return position.get(index)[2];
    }

    /**
     * Gets latest X
     * @return current X value
     */
    synchronized public double getCurrentX(){
        double last = unordered_position.lastElement()[0];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gets latest Y
     * @return current Y value
     */
    synchronized public double getCurrentY(){
        double last = unordered_position.lastElement()[1];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gets latest Z
     * @return current Z value
     */
    synchronized public double getCurrentZ(){
        double last = unordered_position.lastElement()[2];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gets latest Accelerometer value
     * @return current Acc value
     */
    synchronized public double getCurrentAccValue(){
        double last = unordered_accelorometer.lastElement()[1];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gets latest Altimeter value
     * @return current Alt value
     */
    synchronized public double getCurrentAltValue(){
        double last = unordered_altimeter.lastElement()[1];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gets latest Gyroscope value
     * @return current Gyro value
     */
    synchronized public double getCurrentGyroValue(){
        double last = unordered_gyro.lastElement()[1];
        //return unordered_position.lastElement()[2];
        return last;
    }

    /**
     * Gives length of position data structure
     * @return length
     */
    public int getDataLength(){
        return position.size();
    }

    /**
     * Gives position data structure
     * @return position data structure
     */
    public Vector<double[]> getPosition(){
        return position;
    }

    /**
     * Gives Accelerometer data structure
     * @return Accelerometer data structure
     */
    public Vector<double[]> getAccelorometer(){
        return accelorometer;
    }

    /**
     * Gives Altimeter data structure
     * @return Altimeter data structure
     */
    public Vector<double[]> getAltimeter(){
        return altimeter;
    }

    /**
     * Gives Gyro data structure
     * @return Gyro data structure
     */
    public Vector<double[]> getGyro(){
        return gyro;
    }

    /**
     * Gives current position
     * @return current position
     */
    public double[] getCurrentPosition(){
        return unordered_position.lastElement();
    }

    /**
     * Gives current acc
     * @return current acc
     */
    public double[] getCurrentAcc(){
        return unordered_accelorometer.lastElement();
    }

    /**
     * Gives current alt
     * @return current alt
     */
    public double[] getCurrentAlt(){
        return unordered_altimeter.lastElement();
    }

    /**
     * Gives current gyro
     * @return current gyro
     */
    public double[] getCurrentGyro(){
        return unordered_gyro.lastElement();
    }

    /**
     * Calculates threshold
     * @return threshold
     */
    public int calcThreshold(){
        if(unordered_position.size()>threshold){
            return (unordered_position.size()-threshold);
        }
        else{
            return 0;
        }
    }

    /**
     * Gives a portion of data structure
     * @param startindex index to start splice at
     * @return portion of data structure
     */
    public Vector<double[]> spliceData(int startindex){
        Vector<double[]> splicedData = new Vector<double[]>();

        for(int i=startindex; i<unordered_position.size(); i++){
            splicedData.add(unordered_position.get(i));
        }
        return splicedData;
    }

    /**
     * Sorts data structure (needed because graph needs to be plotted in order of X for real time)
     * @param sortedposition data structure to be sorted
     * @return sorted position
     */
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

    /**
     * Saves positon values to a csv file
     * @param name name of file
     * @return filepath to be displayed
     */
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

    /**
     * Reads uploaded csv file and stores into data structure
     * @param name name of file
     * @throws IOException
     */
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
