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

    public DataReceiver(String url, AppManager appManager){
        this.url = url;
        this.appManager = appManager;
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
            }
        }
    }

    public void ParseURL(){

    }

    public void AppendData(String name, String value){
        switch(name){
            case "run time":
                break;
            case "x":
                appManager.updatePositionX(Double.parseDouble(value)+genRandomDouble());
                break;
            case "y":
                appManager.updatePositionY(Double.parseDouble(value)+genRandomDouble());
                break;
            case "z":
                appManager.updatePositionZ(Double.parseDouble(value)+genRandomDouble());
                break;
            default:
                break;
        }
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
                /*
                URL url = new URL("http://128.195.207.30:8001/Service/xyzDisplay");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    System.out.println("it worked");
                } finally {
                    urlConnection.disconnect();
                }
                */

                // First, open a connection to the machine where LabVIEW is
                // running

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
                /*
                String value = doc.getElementsByTagName("Value")
                        .item(0)
                       // .getAttributes()
                       // .getNamedItem("value")
                        .getNodeValue();

                System.out.println(value);

                // Create an array of data to add to the chart. There is only one
                // channel of data right now, so just add one point.
                // will need to add more things to parse for in the xml file because there's 3 variables
                Vector<Double> data = new Vector<>();
                data.add(Double.parseDouble(value));
                // Add the data to the chart.
                //mChart.addDataPoint(data);
                appManager.updatePosition();
                appManager.updateAltitude();
                */
                System.out.println("Updated with no Errors");
                System.out.println(genRandomDouble());
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
