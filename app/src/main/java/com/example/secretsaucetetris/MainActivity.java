package com.example.secretsaucetetris;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyCanvas Screen;
    Board board = new Board();
    MediaPlayer song;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartGame();

    }

    //starts song, initializes buttons and gamethread
    protected void StartGame(){
        song = MediaPlayer.create(getApplicationContext(), R.raw.tetris);
        song.start();
        song.setLooping(true);

        Screen = (MyCanvas) findViewById(R.id.MyCanvas);

        Buttons();

        GameThread();
    }

    //All 5 buttons
    protected void Buttons(){
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

        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Restarting Game, your score was: "+String.valueOf(board.getScore()), Toast.LENGTH_LONG).show();
                board = new Board();
            }
        });
    }

    //game thread has thread inner class with Ui threads
    protected void GameThread(){

        final TextView GameState = (TextView)findViewById(R.id.score);
        final ImageView nextPiece = (ImageView)findViewById(R.id.nextpiece);

        Thread tGame=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(750);  //75% of 1sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //checking if game is over before making piece come down
                                if(board.getGameOver()){
                                    Toast.makeText(MainActivity.this,"Game Over, your score was: "+String.valueOf(board.getScore())+" Restarting Game...", Toast.LENGTH_LONG).show();
                                    board = new Board();
                                }
                                board.tick();
                                board.updateBoardWithProjection();
                                int score = board.getScore();
                                String tmp = "Score: "+String.valueOf(score);
                                Screen.updateGrid(board.getBoard_with_projection());
                                GameState.setText(tmp);
                                setNextPiece(nextPiece, board.getNextPiece());
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

    public void setNextPiece(ImageView nextPiece, int flag){
        switch(flag){
            case 0:
                nextPiece.setImageResource(R.drawable.i_block);
                break;
            case 1:
                nextPiece.setImageResource(R.drawable.z_block);
                break;
            case 2:
                nextPiece.setImageResource(R.drawable.s_block);
                break;
            case 3:
                nextPiece.setImageResource(R.drawable.t_block);
                break;
            case 4:
                nextPiece.setImageResource(R.drawable.l_block);
                break;
            case 5:
                nextPiece.setImageResource(R.drawable.j_block);
                break;
            case 6:
                nextPiece.setImageResource(R.drawable.o_block);
                break;
            default:
                nextPiece.setImageResource(R.drawable.i_block);
                break;
        }
    }

}
