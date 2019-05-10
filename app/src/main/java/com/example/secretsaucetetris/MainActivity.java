package com.example.secretsaucetetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int count=0;
    MyCanvas Screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Screen = (MyCanvas) findViewById(R.id.MyCanvas);

        //Buttons
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Screen.row<20) {
                    Screen.updateTestArray(1);
                }
            }
        });

        findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Score Thread
        final TextView scoreText = (TextView)findViewById(R.id.score);

        Thread tScore=new Thread(){
            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                count++;
                                String tmp = "Score: "+String.valueOf(count);
                                scoreText.setText(tmp);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tScore.start();

        //Next Piece Thread
        final TextView nextPieceText = (TextView)findViewById(R.id.nextpiece);

        Thread tNextPiece=new Thread(){
            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                count++;
                                String tmp = "Next Piece: "+String.valueOf(count);
                                nextPieceText.setText(tmp);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tNextPiece.start();

    }
}
