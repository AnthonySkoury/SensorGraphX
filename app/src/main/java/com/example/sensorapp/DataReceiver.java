package com.example.sensorapp;

import android.os.StrictMode;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
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


public class DataReceiver extends Thread {

    public volatile Boolean mStop = false;
    protected String url;
    AppManager appManager;
    double currentPosition[];

    public DataReceiver(String url, AppManager appManager){
        this.url = url;
        this.appManager = appManager;
        this.currentPosition  = new double[3];
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
        updateCurrentPosition();
    }

    public void ParseURL(){

    }

    public void ParseData(String name, String value){
        switch(name){
            case "run time":
                break;
            case "x":
                currentPosition[0]=Double.parseDouble(value)+genRandomDouble();
                //appManager.updatePositionX(Double.parseDouble(value)+genRandomDouble());
                break;
            case "y":
                currentPosition[1]=Double.parseDouble(value)+genRandomDouble();
                //appManager.updatePositionY(Double.parseDouble(value)+genRandomDouble());
                break;
            case "z":
                currentPosition[2]=Double.parseDouble(value)+genRandomDouble();
                //appManager.updatePositionZ(Double.parseDouble(value)+genRandomDouble());
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

    @Override
    public void run()
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // This is all the same as before, except we will parse the double and
        // add
        // it to a chart.
        try
        {
            while (!mStop)
            {

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
                Document doc = docbuilder.parse(connection.getInputStream());
                ParseXML(doc);

                System.out.println("Updated with no Errors");
                appManager.printPositionHistory();
                this.sleep(10); //adjust delay in milliseconds, 1000=1s, 10=0.01s
            }
        }
        catch (Exception e)
        {
            System.out.println("Printing stack trace... ");
            e.printStackTrace();

        }
    }

}
