package com.example.sensorapp;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class DataReceiver extends Thread {

    public volatile Boolean mStop = false;
    protected String url;
    AppManager appManager;

    public DataReceiver(String url, AppManager appManager){
        this.url = url;
        this.appManager = appManager;
    }

    @Override
    public void run()
    {
        // This is all the same as before, except we will parse the double and
        // add
        // it to a chart.
        try
        {
            while (!mStop)
            {
                // First, open a connection to the machine where LabVIEW is
                // running
                URL url = new URL(this.url);
                URLConnection connection = url.openConnection();
                // Set resonable timeouts
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                // Create an XML document builder with the default settings
                DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                // Parse XML from the web service into a DOM tree
                Document doc = docbuilder.parse(connection.getInputStream());
                // Now, pull out the value attribute of the first channel element
                String value = doc.getElementsByTagName("channel")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("value")
                        .getNodeValue();
                // Create an array of data to add to the chart. There is only one
                // channel of data right now, so just add one point.
                Vector<Double> data = new Vector<Double>();
                data.add(Double.parseDouble(value));
                // Add the data to the chart.
                //mChart.addDataPoint(data);
                appManager.updatePosition();
                appManager.updateAltitude();

                System.out.println("Updated with no Errors");
            }
        }
        catch (Exception e)
        {
            System.out.println("Printing stack trace... ");
            e.printStackTrace();

        }
    }

}
