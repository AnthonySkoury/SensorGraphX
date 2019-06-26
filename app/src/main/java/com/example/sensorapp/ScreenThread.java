package com.example.sensorapp;

import java.lang.*;
import android.view.*;
import android.graphics.*;

public class ScreenThread extends Thread{

    AltitudeDisplay sv;
    public ScreenThread(AltitudeDisplay sv) {
        this.sv=sv;
    }

    public void run() {
        SurfaceHolder sh = sv.getHolder();

    //Loop for updating screen
        while( !Thread.interrupted() ) {

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
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }
    }


}
