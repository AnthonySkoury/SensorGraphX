package com.example.sensorapp;

import android.os.StrictMode;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DataReceiver{

    public volatile Boolean mStop = false;
    protected String url;
    AppManager appManager;
    double currentPosition[];
    double tempXYZ;

    int reset_flag=0;
    int ZUPT_flag=0;
    int Altimeter_flag=0;

    String m_IP = "http://%s/WebService/xyzDisplay";
    String ip = "192.168.48.2:8001";
    String ip2 = "128.195.207.30:8001";
    String URL_Upload = "http://%s/WebService/xyzDisplay?ZUPT_control_test=%d&reset_data=%d&Altimeter_control_test=%d";
    String resetOnGlobal = "http://128.195.207.30:8001/Service/xyzDisplay?reset_data=1";
    String resetOffGlobal = "http://128.195.207.30:8001/Service/xyzDisplay?reset_data=0";
    String resetOn = "http://127.0.0.1:8001/Service/xyzDisplay?reset_data=1";
    String resetOff = "http://127.0.0.1:8001/Service/xyzDisplay?reset_data=0";

    public DataReceiver(String url, AppManager appManager){
        this.url = url;
        this.appManager = appManager;
        this.currentPosition  = new double[3];
    }

    public void changeURL(String ip){
        url = ip;
    }

    public Document ParseURL() throws Exception{
        URL url = new URL(this.url);
        URLConnection connection = url.openConnection();
        // Set resonable timeouts
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.getInputStream(); //test case
        // Create an XML document builder with the default settings

        DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        // Parse XML from the web service into a DOM tree
        //Document doc = docbuilder.parse(connection.getInputStream());
        //ParseXML(doc);
        return docbuilder.parse(connection.getInputStream());
    }

    public void ParseXML(Document doc){
        // Now, pull out the value attribute of the first channel element
        doc.getDocumentElement().normalize();
        System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("Terminal");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element : " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                String value = eElement.getElementsByTagName("Value").item(0).getTextContent();
                System.out.println("Name : " + name);
                System.out.println("Value : " + value);
                ParseData(name, value);
            }
        }
    }

    public void TestData(){
        ParseData("x");
        ParseData("y");
        ParseData("z");
    }

    public void ParseData(String name){
        switch(name){
            case "run time":
                break;
            case "x":
                currentPosition[0]=genRandomDouble();
                break;
            case "y":
                currentPosition[1]=genRandomDouble();
                break;
            case "z":
                currentPosition[2]=genRandomDouble();
                break;
            default:
                break;
        }
    }

    public void ParseData(String name, String value){
        switch(name){
            case "acc_test":
                break;
            case "ALT_test":
                break;
            case "gyro_test":
                break;
            case "run time":
                appManager.updateRunTime(Integer.parseInt(value));
                break;
            case "x":
                currentPosition[0]=Double.parseDouble(value);
                break;
            case "y":
                currentPosition[1]=Double.parseDouble(value);
                break;
            case "z":
                currentPosition[2]=Double.parseDouble(value);
                break;
            case "ZUPT_test":
                break;
            default:
                break;
        }
    }

    public void updateCurrentPosition(){
        appManager.updatePosition(currentPosition);
    }

    public double genRandomDouble(){
        double rangeMin = -50.0;
        double rangeMax = 50.0;

        /*alternate way
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        */

        double randomDouble = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
        return randomDouble;
    }

    public double genNextDouble(){
        if(tempXYZ>58){
            mStop=true;
            return tempXYZ;
        }
        tempXYZ+=1.5;
        return tempXYZ;
    }

    public double[] connectToDevice(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
               // ParseXML(ParseURL());
               TestData();

        }
        catch (Exception e)
        {
            System.out.println("Printing stack trace... ");
            e.printStackTrace();

        }
        finally {
            return currentPosition;
        }

    }

    public void UploadVariables(){
        try {
            String url_to_upload = String.format(URL_Upload, ip, ZUPT_flag, reset_flag, Altimeter_flag);
            URL url = new URL(url_to_upload);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.getInputStream(); //test case
            // Create an XML document builder with the default settings

            DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            // Parse XML from the web service into a DOM tree
            //Document doc = docbuilder.parse(connection.getInputStream());
            //ParseXML(doc);
            docbuilder.parse(connection.getInputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            /*
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            toggleResetOFF();
                        }
                    },2000
            );
            */
        }
    }


    public void toggleResetON(){
        try {
            URL url = new URL(resetOnGlobal);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.getInputStream(); //test case
            // Create an XML document builder with the default settings

            DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            // Parse XML from the web service into a DOM tree
            //Document doc = docbuilder.parse(connection.getInputStream());
            //ParseXML(doc);
            docbuilder.parse(connection.getInputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            /*
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            toggleResetOFF();
                        }
                    },2000
            );
            */
        }
    }

    public void toggleResetOFF(){
        try {
            URL url = new URL(resetOffGlobal);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.getInputStream(); //test case
            // Create an XML document builder with the default settings

            DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            // Parse XML from the web service into a DOM tree
            //Document doc = docbuilder.parse(connection.getInputStream());
            //ParseXML(doc);
            docbuilder.parse(connection.getInputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
    }



    public void toggleResetON2(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
            URL url = new URL(resetOnGlobal);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(250);
           // connection.setReadTimeout(1000);

            connection.connect();


        }
        catch (MalformedURLException e) {
            // new URL() failed
            // ...
            e.printStackTrace();
        }
        catch (IOException e) {
            // openConnection() failed
            // ...
            e.printStackTrace();

        }
        finally {

        }
    }

    public void toggleResetOFF2(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
            URL url = new URL(resetOffGlobal);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.connect();
        }
        catch (Exception e)
        {
            System.out.println("Printing stack trace... ");
            e.printStackTrace();

        }
        finally {

        }
    }


}
