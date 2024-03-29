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

/**
 * Has methods to connect to device and pull data
 */
public class DataReceiver{

    public volatile Boolean mStop = false;
    protected String url;
    AppManager appManager;
    double currentPosition[];
    double currentAcc[];
    double currentAlt[];
    double currentGyro[];
    double tempXYZ;
    int ZUPT_status;

    double originX = 0.0;
    double originY = 0.0;

    int reset_flag=0;
    int ZUPT_flag=1;
    int Altimeter_flag=1;

    DocumentBuilder docbuilder;
    Document doc;

    /* For simulator on computer */
    //String m_IP = "http://%s/Service/xyzDisplay";
    //String ip = "128.195.207.30:8001";
    //String URL_Upload = "http://%s/Service/xyzDisplay?ZUPT_control_test=%d&reset_data=%d&Altimeter_control_test=%d";

    /* For prototype physical device (different links) */
    String m_IP = "http://%s/WebService/xyzDisplay";
    String ip = "192.168.48.2:8001";
    String URL_Upload = "http://%s/WebService/xyzDisplay?ZUPT_control_test=%d&reset_data=%d&Altimeter_control_test=%d";

    String runtime="0";
    Timer timer;

    /**
     * Constructor taking in a default URL and AppManager object
     * @param url default url
     * @param appManager reference to AppManager to call update methods
     */
    public DataReceiver(String url, AppManager appManager){
        this.url = url;
        this.appManager = appManager;
        this.currentPosition  = new double[3];
        this.currentAcc  = new double[2];
        this.currentAlt  = new double[2];
        this.currentGyro  = new double[2];
        this.timer = new Timer();
        timer.startTime();
    }

    public void setOriginX(double x){
        originX = x;
    }

    public void setOriginY(double y){
        originY = y;
    }

    /**
     * Changes ip address
     * @param ip new ip address
     */
    public void changeURL(String ip){
        this.ip = ip;
    }

    /**
     * Connects to URL to create an XML document from web page
     * @throws Exception
     */
    public void ParseURL() throws  Exception{

            String url_to_upload = String.format(URL_Upload, ip, ZUPT_flag, reset_flag, Altimeter_flag);
            URL url = new URL(url_to_upload);
            URLConnection connection = url.openConnection();
            // Set resonable timeouts
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.getInputStream(); //test case
            // Create an XML document builder with the default settings

            docbuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            // Parse XML from the web service into a DOM tree
            //Document doc = docbuilder.parse(connection.getInputStream());
            //ParseXML(doc);
            doc = docbuilder.parse(connection.getInputStream());

    }

    /**
     * Parses XML document retrieved from URL
     * @param doc XML Document
     */
    public void ParseXML(Document doc){
        // Now, pull out the value attribute of the first channel element
        doc.getDocumentElement().normalize();
        //System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("Terminal");
        //System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            //System.out.println("\nCurrent Element : " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                String value = eElement.getElementsByTagName("Value").item(0).getTextContent();
                //System.out.println("Name : " + name);
                //System.out.println("Value : " + value);
                ParseData(name, value);
            }
        }
    }

    /**
     * Test method used when not connected to Wi-Fi
     */
    public void TestData(){
        ParseData("acc_test");
        ParseData("ALT_test");
        ParseData("gyro_test");
        ParseData("x");
        ParseData("y");
        ParseData("z");
    }

    /**
     * Method used when running simulation without Wi-Fi program.
     * Creates artificial values.
     * @param name Name for data type
     */
    public void ParseData(String name){
        switch(name){
            case "acc_test":
                currentAcc[0]= (double)timer.getElapsedTimeSecs();
                currentAcc[1]=genRandomPosDouble();
                break;
            case "ALT_test":
                currentAlt[0]=(double)timer.getElapsedTimeSecs();
                currentAlt[1]=genRandomPosDouble();
                break;
            case "gyro_test":
                currentGyro[0]=(double)timer.getElapsedTimeSecs();
                currentGyro[1]=genRandomPosDouble();
                break;
            case "run time":
                runtime= Long.toString(timer.getElapsedTimeSecs());
                break;
            case "x":
                currentPosition[0]=genRandomDouble()+originX;
                break;
            case "y":
                currentPosition[1]=genRandomDouble()+originY;
                break;
            case "z":
                currentPosition[2]=genRandomDouble();
                break;
            default:
                break;
        }
    }

    /**
     * Parses data read from a line in the XML file
     * @param name Name for data type
     * @param value Value for data
     */
    public void ParseData(String name, String value){
        switch(name){
            case "acc_test":
                currentAcc[0]=Double.parseDouble(runtime);
                currentAcc[1]=Double.parseDouble(value);
                break;
            case "ALT_test":
                currentAlt[0]=Double.parseDouble(runtime);
                currentAlt[1]=-Double.parseDouble(value);
                break;
            case "gyro_test":
                currentGyro[0]=Double.parseDouble(runtime);
                currentGyro[1]=Double.parseDouble(value);
                break;
            case "run time":
                runtime=value;
                appManager.updateRunTime(Integer.parseInt(value));
                break;
            case "x":
                currentPosition[0]=Double.parseDouble(value)+originX;
                break;
            case "y":
                currentPosition[1]=Double.parseDouble(value)+originY;
                break;
            case "z":
                currentPosition[2]=-Double.parseDouble(value);
                break;
            case "ZUPT_test":
                ZUPT_status=Integer.parseInt(value);
                break;
            default:
                break;
        }
    }

    /**
     * Generates random double between specified range for testing purposes
     * @return random double
     */
    public double genRandomDouble(){
        double rangeMin = -15.0;
        double rangeMax = 15.0;

        /*alternate way
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        */

        double randomDouble = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
        return randomDouble;
    }

    /**
     * Generates random positive double between specified range for testing purposes
     * @return random positive double
     */
    public double genRandomPosDouble(){
        double rangeMin = 0;
        double rangeMax = 15.0;

        /*alternate way
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        */

        double randomDouble = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
        return randomDouble;
    }

    /**
     * Connects to device then calls methods ParseURL and ParseXML
     * @return return code for error
     */
    public int connectToDevice(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
            ParseURL();
            ParseXML(doc);
            //TestData();
            return 0;

        }
        catch (Exception e)
        {
            //System.out.println("Printing stack trace... ");
            e.printStackTrace();
            return -1;

        }

    }

    /**
     * Uploads variables to program after update
     */
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


}
