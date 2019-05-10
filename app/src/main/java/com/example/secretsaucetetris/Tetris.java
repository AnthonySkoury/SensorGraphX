package com.example.secretsaucetetris;

import java.lang.*;
import android.util.AttributeSet;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.support.annotation.Nullable;

public class Tetris extends Thread{

    MyCanvas sv;
    public Tetris(MyCanvas sv) {
        this.sv=sv;
    }

    public void run() {
        SurfaceHolder sh = sv.getHolder();
// Main game loop.
        while( !Thread.interrupted() ) {
//You might want to do game specific processing in a method you call here
            Canvas c = sh.lockCanvas(null);
            try {
                synchronized(sh) {
                    sv.onDraw(c);
                }
            } catch (Exception e) {
            } finally {
                if ( c != null ) {
                    sh.unlockCanvasAndPost(c);
                }
            }
// Set the frame rate by setting this delay
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
// Thread was interrupted while sleeping.
                return;
            }
        }
    }


}
