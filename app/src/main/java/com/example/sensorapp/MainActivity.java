package com.example.sensorapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Document;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;

/**
 * Instantiates all objects and has main control flow as well as buttons and menu/options functionality
 */
public class MainActivity extends AppCompatActivity {
    int finalHeight;
    int finalWidth;
    /* Objects Used */
    LayoutWrapContentUpdater resizer;
    ScrollView scrollView;
    LinearLayout scrollLayout;
    DataReceiver dataReceiver;
    RelativeLayout positionLayout;
    PositionDisplay positionGraph;
    ImageView backgroundView;
    AltitudeBar altitudeBar;
    AccelerometerDisplay accelerometerDisplay;
    AltimeterDisplay altimeterDisplay;
    GyroDisplay gyroDisplay;
    AppManager appManager;
    Bitmap background;
    TextView X_Coord;
    TextView Y_Coord;
    TextView Z_Coord;
    TextView Run_Time;
    TextView Accelerometer;
    TextView Altimeter;
    TextView Gyro;
    TextView ZUPT_Status;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /* State Variables */
    boolean stop=true;
    private boolean isCheckedZUPT = false;
    private boolean isCheckedAltitude = false;
    boolean resize;

    /* Permissions Variables */
    int GET_FROM_GALLERY = 1;

    /* Options Variables */
    private String m_Title = "";
    private int m_Type;
    private String m_Text = "";
    //private String m_IP = "http://192.168.48.2:8001/WebService/xyzDisplay";
    private String m_IP = "http://128.195.207.30:8001/Service/xyzDisplay";
    private int m_Max_RangeX=60;
    private int m_Max_RangeY=60;
    private int m_Max_RangeZ=60;
    private long m_Sample_Rate=1000;
    private int m_Max_Points=100;
    private String m_File_Upload = "";
    private String m_File_Download = "";

    private int m_Max_RangeX2=20;
    private int m_Max_RangeY2=20;
    private int m_Max_RangeX3=20;
    private int m_Max_RangeY3=20;
    private int m_Max_RangeX4=20;
    private int m_Max_RangeY4=20;

    String xPos="";
    String yPos ="";
    String zPos = "";
    String runtime = "";
    String gyrotxt = "";
    String acctxt = "";
    String alttxt = "";
    String zupttxt = "";

    /**
     * Creates this activity and asks user for storage permissions then calls method StartApp.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        } else {
            // Permission has already been granted
        }

        StartApp();

    }

    /**
     * Creates options menu
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_bar, menu);
        //return super.onCreateOptionsMenu(menu);
       // MenuItem item = menu.findItem(R.id.myswitch);
       // item.setActionView(R.layout.switch_layout);
        return true;
    }

    /**
     * Creates checkables for ZUPT controls.
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.ZUPT);
        checkable.setChecked(isCheckedZUPT);
        MenuItem checkable2 = menu.findItem(R.id.Altitude);
        checkable2.setChecked(isCheckedAltitude);
        return true;
    }

    /**
     * Checks what option user selected and calls what is needed depending if it requires an input.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_more:

                return true;

            case R.id.action_set_ip:
                itemAction("Enter IP Address", R.id.action_set_ip);
                return true;

            case R.id.action_graph_settings:
                return true;
            case R.id.action_set_sampling:
                itemAction("Enter Sample Rate", R.id.action_set_sampling);
                return true;
            case R.id.action_set_maxpoints:
                itemAction("Enter Max Points Displayed at a Time", R.id.action_set_maxpoints);
                return true;
            case R.id.action_set_background:
                handleBackground();
                return true;
            case R.id.ZUPT:
                isCheckedZUPT = !item.isChecked();
                item.setChecked(isCheckedZUPT);
                if(dataReceiver.ZUPT_flag==0)
                    dataReceiver.ZUPT_flag=1;
                else
                    dataReceiver.ZUPT_flag=0;
                handleToggleZUPT();
                return true;
            case R.id.Altitude:
                isCheckedAltitude = !item.isChecked();
                item.setChecked(isCheckedAltitude);
                if(dataReceiver.Altimeter_flag==0)
                    dataReceiver.Altimeter_flag=1;
                else
                    dataReceiver.Altimeter_flag=0;
                handleToggleAltitude();
                return true;

            case R.id.action_set_range:
                positionGraph.resizeView(500,1000);
                return true;
            case R.id.action_set_x_range:
                itemAction("Enter X Range Limit (in meters)", R.id.action_set_x_range);
                return true;
            case R.id.action_set_y_range:
                itemAction("Enter Y Range Limit (in meters)", R.id.action_set_y_range);
                return true;
            case R.id.action_set_z_range:
                itemAction("Enter Altitude Range Limit (in meters)", R.id.action_set_z_range);
                return true;

            case R.id.action_set_acc_range:
                return true;
            case R.id.action_set_x2_range:
                itemAction("Enter Time Range Limit (in seconds)", R.id.action_set_x2_range);
                return true;
            case R.id.action_set_y2_range:
                itemAction("Enter Y Range Limit ", R.id.action_set_y2_range);
                return true;

            case R.id.action_set_alt_range:
                return true;
            case R.id.action_set_x3_range:
                itemAction("Enter Time Range Limit (in seconds)", R.id.action_set_x3_range);
                return true;
            case R.id.action_set_y3_range:
                itemAction("Enter Y Range Limit ", R.id.action_set_y3_range);
                return true;

            case R.id.action_set_gyro_range:
                return true;
            case R.id.action_set_x4_range:
                itemAction("Enter Time Range Limit (in seconds)", R.id.action_set_x4_range);
                return true;
            case R.id.action_set_y4_range:
                itemAction("Enter Y Range Limit ", R.id.action_set_y4_range);
                return true;

            case R.id.action_file:

                return true;
            case R.id.action_download:
                itemAction("Enter File Name For Download", R.id.action_download);
                return true;
            case R.id.action_upload:
                itemAction("Enter File Name For Upload", R.id.action_upload);
                return true;

            case R.id.action_help:
                return true;
            case R.id.action_guide:
                Toast.makeText(this, "Tutorial", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Guide.class));
                return true;
            case R.id.action_about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, About.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Action dialogue popup prompting user for a text input after option is pressed.
     * @param title Description for action dialogue
     * @param id Identifier for which menu option was selected
     */
    protected void itemAction(String title, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        m_Type = id;
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                handleInput(m_Text, m_Type);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Converts input to appropriate type then directs to specific handler.
     * @param input Variable to store user input
     * @param id Identifier for which menu option was selected
     */
    protected void handleInput(String input, int id){
        switch (id){
            case R.id.action_set_ip:
                m_IP=input;
                handleIP();
                break;

            case R.id.action_set_x_range:
                m_Max_RangeX=Integer.parseInt(input);
                handleRangeX();
                break;
            case R.id.action_set_y_range:
                m_Max_RangeY=Integer.parseInt(input);
                handleRangeY();
                break;
            case R.id.action_set_z_range:
                m_Max_RangeZ=Integer.parseInt(input);
                handleRangeZ();
                break;

            case R.id.action_set_x2_range:
                m_Max_RangeX2=Integer.parseInt(input);
                handleRangeAccX();
                break;
            case R.id.action_set_y2_range:
                m_Max_RangeY2=Integer.parseInt(input);
                handleRangeAccY();
                break;

            case R.id.action_set_x3_range:
                m_Max_RangeX3=Integer.parseInt(input);
                handleRangeAltX();
                break;
            case R.id.action_set_y3_range:
                m_Max_RangeY3=Integer.parseInt(input);
                handleRangeAltY();
                break;

            case R.id.action_set_x4_range:
                m_Max_RangeX4=Integer.parseInt(input);
                handleRangeGyroX();
                break;
            case R.id.action_set_y4_range:
                m_Max_RangeY4=Integer.parseInt(input);
                handleRangeGyroY();
                break;

            case R.id.action_set_sampling:
                m_Sample_Rate=Long.parseLong(input);
                handleSampling();
                break;
            case R.id.action_set_maxpoints:
                m_Max_Points=Integer.parseInt(input);
                handleMaxPoints();
                break;
            case R.id.action_set_background:
                handleBackground();
                break;
            case R.id.action_download:
                m_File_Download=input;
                handleDownload();
                break;
            case R.id.action_upload:
                m_File_Upload=input;
                handleUpload();
                break;
            default:
                return;
        }
    }

    /* Handler Functions */

    /**
     * Calls DataReceiver method to change IP address in the URL.
     */
    protected void handleIP(){
        //appManager.setIP(m_IP);
        dataReceiver.changeURL(m_IP);
    }

    /**
     * Calls PositionGraph method to change the X range.
     */
    protected void handleRangeX(){
        positionGraph.setRangeX(m_Max_RangeX);
    }

    /**
     * Calls PositionGraph method to change the Y range.
     */
    protected void handleRangeY(){
        positionGraph.setRangeY(m_Max_RangeY);
    }

    /**
     * Calls AltitudeBar method to change the Z range.
     */
    protected void handleRangeZ(){
        altitudeBar.setRangeZ(m_Max_RangeZ);
    }

    protected void handleRangeAccX(){
        accelerometerDisplay.setRangeX(m_Max_RangeX2);
    }

    protected void handleRangeAccY(){
        accelerometerDisplay.setRangeY(m_Max_RangeY2);
    }

    protected void handleRangeAltX(){
        altimeterDisplay.setRangeX(m_Max_RangeX3);
    }

    protected void handleRangeAltY(){
        altimeterDisplay.setRangeY(m_Max_RangeY3);
    }

    protected void handleRangeGyroX(){
        gyroDisplay.setRangeX(m_Max_RangeX4);
    }

    protected void handleRangeGyroY(){
        gyroDisplay.setRangeY(m_Max_RangeY4);
    }

    /**
     * Notifies user of new sampling rate after being entered.
     */
    protected void handleSampling(){
        String txt = "This is the new sampling rate: "+m_Sample_Rate;
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }

    /**
     * Calls AppManager method to set maximum data points displayed at a time for position graph.
     */
    protected void handleMaxPoints(){
        appManager.setMaxDatapoints(m_Max_Points);
    }

    /**
     * Prompts user to pick an image from their gallery to set as the graph's background.
     */
    protected void handleBackground(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

    }

    /**
     * Stops the graphing and connection then calls DataSaver SaveToFile method.
     * Notifies user file has been saved with the filepath after process is completed.
     */
    protected void handleDownload(){
        stop = true;
        String temp =  appManager.dataSaver.SaveToFile(m_File_Download);
        String text = "Saved file to: "+temp;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * Calls readFile method to load  a csv file to display position data.
     */
    protected void handleUpload(){
        stop = true;
        readFile();
        positionGraph.resetGraph();
        appManager.tracePosition();
        X_Coord.setText("X Position: ");
        Y_Coord.setText("Y Position: ");
        Z_Coord.setText("Z Position: ");
        Run_Time.setText("Time: ");
    }

    /**
     * Calls DataReceiver method UploadVariables to toggle ZUPT.
     */
    protected void handleToggleZUPT(){

        dataReceiver.UploadVariables();
    }

    /**
     * Calls DataReceiver method UploadVariables to toggle Altitude.
     */
    protected void handleToggleAltitude(){

        dataReceiver.UploadVariables();
    }


    /**
     * Handles request codes.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            background = null;
            try {

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positionLayout.getLayoutParams();

                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                positionLayout.setGravity(Gravity.CENTER_VERTICAL);
                positionLayout.setLayoutParams(params);
                positionLayout.invalidate();
                positionLayout.requestLayout();
                positionLayout.setGravity(Gravity.CENTER_VERTICAL);


                background = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //appManager.setBackground(background);
                backgroundView.setImageBitmap(background);

                RectF bounds = getImageBounds(backgroundView);
                int bw = (int)bounds.width();
                int bh = (int)bounds.height();

                System.out.println("This is the rect width: "+ bounds.width());
                System.out.println("This is the rect height: "+ bounds.height());

                int ih=backgroundView.getMeasuredHeight();//height of imageView
                int iw=backgroundView.getMeasuredWidth();//width of imageView

                System.out.println("This is the width of imageview: "+iw+" And this is height of imageview: "+ih);

                int iH=backgroundView.getDrawable().getIntrinsicHeight();//original height of underlying image
                int iW=backgroundView.getDrawable().getIntrinsicWidth();//original width of underlying image

                if (ih/iH<=iw/iW) iw=iW*ih/iH;//rescaled width of image within ImageView
                else ih= iH*iw/iW;//rescaled height of image within ImageView

                System.out.println("This is the width of image scaled: "+iw+" And this is height of image scaled: "+ih);



                System.out.println("This is the width of image inside: "+iW+" And this is height of image inside: "+iH);



                params.height = bh;
                params.width = bw;
                positionLayout.setGravity(Gravity.CENTER_VERTICAL);
                positionLayout.setLayoutParams(params);
                positionLayout.invalidate();
                positionLayout.requestLayout();
                positionLayout.setGravity(Gravity.CENTER_VERTICAL);



                /*
                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, 4000);
                rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                positionLayout.setLayoutParams(rel_btn);
*/

                //positionLayout.getLayoutParams().height = 4000;
                //positionLayout.getLayoutParams().width = 600;

                /*
                ViewGroup.LayoutParams lp = positionGraph.getLayoutParams();
                lp.width=200;
                lp.height=6000;

                scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10000));
                scrollView.invalidate();
                scrollView.requestLayout();

                positionGraph.setLayoutParams(lp);
                scrollLayout.invalidate();
                scrollLayout.requestLayout();
                resize = true;
               */

                //scrollLayout.invalidate();
                //scrollLayout.requestLayout();

                //layoutScale();
                //adjustScale();
                /*
                int h = backgroundView.getHeight();
                int w = backgroundView.getWidth();
                Toast.makeText(this, "This is new height: "+backgroundView.getMeasuredHeight()+" And this is new width: "+backgroundView.getMeasuredWidth(), Toast.LENGTH_LONG).show();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positionLayout.getLayoutParams();
                params.height = h;
                params.width=w;
                positionLayout.setLayoutParams(params);
                int h2= positionLayout.getHeight();
                int w2= positionLayout.getWidth();
                Toast.makeText(this, "This is new height: "+h2+" And this is new width: "+w2, Toast.LENGTH_LONG).show();
                positionGraph.setLayoutParams(new RelativeLayout.LayoutParams(w2, h2));
                int h3 = positionGraph.getHeight();
                int w3 = positionGraph.getWidth();
                Toast.makeText(this, "This is new height: "+h3+" And this is new width: "+w3, Toast.LENGTH_LONG).show();
                */


                //positionGraph.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void layoutScale(){
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 300);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        positionLayout.setLayoutParams(rel_btn);
    }

    public void adjustScale(){
        final ImageView iv = (ImageView)findViewById(R.id.BackgroundImage);
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = iv.getMeasuredHeight();
                finalWidth = iv.getMeasuredWidth();
                System.out.println("this is finalheight "+finalHeight);
                System.out.println("this is finalwidth "+finalWidth);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) positionLayout.getLayoutParams();
                params.height = finalHeight;
                params.width=finalWidth;
                positionLayout.setLayoutParams(params);
                positionGraph.setLayoutParams(new RelativeLayout.LayoutParams(finalWidth, finalHeight));
                return true;
            }
        });
    }

    /**
     * Calls DataSaver method readFile.
     */
    public void readFile(){
        try {
            appManager.dataSaver.readFile(m_File_Upload);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Starts the App by calling methods to create the plots, establish connection, text visuals, and buttons.
     */
    protected void StartApp(){

        resizer = new LayoutWrapContentUpdater();
        scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollLayout = (LinearLayout)findViewById(R.id.ScrollLayout);
        positionLayout = (RelativeLayout)findViewById(R.id.PositionLayoutParent);
        backgroundView = (ImageView)findViewById(R.id.BackgroundImage);
        backgroundView.setImageResource(R.drawable.ic_adjust_green_24dp);
        CreatePlots();
        CreateConnection();
        CreateText();
        setupButtons();
        Toast.makeText(this, "This is old height: "+backgroundView.getMeasuredHeight()+" And this is old width: "+backgroundView.getMeasuredWidth(), Toast.LENGTH_LONG).show();
    }

    /**
     * Creates all the TextViews.
     */
    protected void CreateText(){
        X_Coord = (TextView)findViewById(R.id.x_coordinate);
        Y_Coord = (TextView)findViewById(R.id.y_coordinate);
        Z_Coord = (TextView)findViewById(R.id.z_coordinate);
        Run_Time = (TextView)findViewById(R.id.time);
        Accelerometer = (TextView)findViewById(R.id.accelerometer);
        Altimeter = (TextView)findViewById(R.id.altimeter);
        Gyro = (TextView)findViewById(R.id.gyro);
        ZUPT_Status = (TextView)findViewById(R.id.zupt_status);

    }

    /**
     * Creates all the graphs and initializes AppManager with these plots.
     */
    protected void CreatePlots(){
        CreatePlotXY();
        CreatePlotZ();
        CreatePlotAcc();
        CreatePlotAlt();
        CreatePlotGyro();

        appManager = new AppManager(positionGraph, altitudeBar, accelerometerDisplay, altimeterDisplay, gyroDisplay);
        appManager.initGraphs();
    }

    /**
     * Creates DataReceiver object.
     */
    protected void CreateConnection(){
        dataReceiver = new DataReceiver(m_IP, appManager);
    }

    /**
     * Creates Position graph.
     */
    protected void CreatePlotXY(){

        positionGraph = (PositionDisplay) findViewById(R.id.PositionDisplay);

    }

    /**
     * Creates Altitude bar.
     */
    protected void CreatePlotZ(){

        altitudeBar = (AltitudeBar) findViewById(R.id.AltitudeBar);

    }

    /**
     * Creates Accelerometer plot.
     */
    protected void CreatePlotAcc(){

        accelerometerDisplay = (AccelerometerDisplay) findViewById(R.id.AccelerometerDisplay);

    }

    /**
     * Creates Altimeter plot.
     */
    protected void CreatePlotAlt(){

        altimeterDisplay = (AltimeterDisplay) findViewById(R.id.AltimeterDisplay);

    }

    /**
     * Creates Gyro plot.
     */
    protected void CreatePlotGyro(){

        gyroDisplay = (GyroDisplay) findViewById(R.id.GyroDisplay);

    }


    /* Main Control Flow Segment */

    /**
     * Main control flow segment.
     * Creates a thread that runs based on sample rate.
     * The thread calls Tasks method that runs every task in sequence.
     */
    protected void ScreenThread(){
        //startTime();

        final Thread tGame=new Thread(){
            @Override
            public void run(){
                while(!stop && !isInterrupted()){
                    try {
                        Thread.sleep(m_Sample_Rate);  //75% of 1sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(resize){
                                    resizer.wrapContentAgain(scrollLayout);
                                }
                                Tasks();

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tGame.start();

    }

    /**
     * Tasks method that checks if connection to device works.
     * If the connection works, update each data structure then graph each.
     * Set the text displays to the current value for each.
     * Display error message for connection if user is not connected to device.
     */
    public void Tasks(){

        if(dataReceiver.connectToDevice()!=-1) {
            appManager.updatePosition(dataReceiver.currentPosition);
            appManager.updateAcc(dataReceiver.currentAcc);
            appManager.updateAlt(dataReceiver.currentAlt);
            appManager.updateGyro(dataReceiver.currentGyro);
            appManager.updateZUPTStatus(dataReceiver.ZUPT_status);
            appManager.tracePosition();

            xPos = "X Position (in meters): " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentX()));
            yPos = "Y Position (in meters): " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentY()));
            zPos = "Z Position (in meters): " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentZ()));
            runtime = "Elapsed Time: " + String.valueOf(appManager.dataSaver.runtime) + " s";
            acctxt = "Accelerometer: " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentAccValue()));
            alttxt = "Altimeter: " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentAltValue()));
            gyrotxt = "Gyroscope: " + String.valueOf(decimalFormat.format(appManager.dataSaver.getCurrentGyroValue()));
            zupttxt = "ZUPT Status: "+appManager.dataSaver.ZUPT_Status;

            X_Coord.setText(xPos);
            Y_Coord.setText(yPos);
            Z_Coord.setText(zPos);
            Run_Time.setText(runtime);
            Accelerometer.setText(acctxt);
            Altimeter.setText(alttxt);
            Gyro.setText(gyrotxt);
            ZUPT_Status.setText(zupttxt);
        }
        else{
            Toast.makeText(this, "Error Connecting to Device, please check IP or Wi-Fi connection then press START when ready.", Toast.LENGTH_LONG).show();
            stop = true;
        }
    }


    /**
     * Initialize buttons and their functionality.
     */
    public void setupButtons(){
        startButton();
        stopButton();
        resetButton();
    }

    /**
     * Start button enables the thread to work.
     */
    public void startButton(){
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    stop = true;
                    TimeUnit.SECONDS.sleep(1);
                    stop = false;
                    appManager.reset();
                    ScreenThread();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Stop button stops the thread by setting stop boolean true.
     */
    public void stopButton(){
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stop = true;
            }
        });
    }

    /**
     * Reset button sets reset flag to true then uploads it to device.
     * User has to hold button because it can have some delay for the LabView program on the device to reset.
     * After the user lets go of the button, data structures and graph are reset.
     */
    public void resetButton(){
        findViewById(R.id.btn_reset).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dataReceiver.reset_flag=1;
                    dataReceiver.UploadVariables();
                    dataReceiver.UploadVariables();
                    dataReceiver.UploadVariables();
                    //System.out.println("Button pressed Down");
                    // Do what you want

                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    dataReceiver.reset_flag=0;
                    dataReceiver.UploadVariables();
                    appManager.reset();
                    appManager.dataSaver.resetExtras();
                    appManager.resetExtras();
                }
                //System.out.println("NO BUTTON");
                return MainActivity.super.onTouchEvent(event);
            }
        });
    }

    public static RectF getImageBounds(ImageView imageView) {
        RectF bounds = new RectF();
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            imageView.getImageMatrix().mapRect(bounds, new RectF(drawable.getBounds()));
        }
        return bounds;
    }

}
