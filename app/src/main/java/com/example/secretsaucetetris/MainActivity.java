package com.example.secretsaucetetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int count=0;
    MyCanvas Screen;
    TestGame arr = new TestGame(20,10);
    Board board = new Board();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Screen = (MyCanvas) findViewById(R.id.MyCanvas);

        //Buttons
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.moveLeft();
                board.updateBoardWithProjection();
               Screen.updateGrid(board.getBoard_with_projection());
            }
        });

        findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.rotate();
                board.updateBoardWithProjection();
                Screen.updateGrid(board.getBoard_with_projection());
            }
        });

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.moveDown();
                board.updateBoardWithProjection();
                Screen.updateGrid(board.getBoard_with_projection());
            }
        });

        findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.moveRight();
                board.updateBoardWithProjection();
                Screen.updateGrid(board.getBoard_with_projection());
            }
        });


        //Score Thread
        final TextView GameState = (TextView)findViewById(R.id.score);

        Thread tGame=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                board.tick();
                                board.updateBoardWithProjection();
                                count++;
                                String tmp = "Score: "+String.valueOf(count);
                                Screen.updateGrid(board.getBoard_with_projection());
                                GameState.setText(tmp);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tGame.start();

        final ImageView nextPiece = (ImageView)findViewById(R.id.nextpiece);
        Thread tnextPiece=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(count%2==0) {
                                    nextPiece.setImageResource(R.drawable.j_block);
                                }
                                else{
                                    nextPiece.setImageResource(R.drawable.t_block);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tnextPiece.start();


    }
}
