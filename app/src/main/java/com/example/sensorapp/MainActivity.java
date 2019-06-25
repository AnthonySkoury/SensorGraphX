package com.example.sensorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MyCanvas Screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();

    }

    //starts song, initializes buttons and gamethread
    protected void StartApp(){


        Screen = (MyCanvas) findViewById(R.id.MyCanvas);

    }


}
