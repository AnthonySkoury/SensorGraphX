package com.example.sensorapp;

import android.util.AttributeSet;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.support.annotation.Nullable;

/* Custom Display for graphics if needed for altitude */
public class AltitudeDisplay extends SurfaceView implements SurfaceHolder.Callback{


    private int canvasWidth;
    private int canvasHeight;
    int count=0;


    ScreenThread thread;

    /* Constructors */
    public AltitudeDisplay(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        init(null);
    }

    public AltitudeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    public AltitudeDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    public AltitudeDisplay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {


        if (set == null)
            return;

    }

    //Creates game screen thread
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new ScreenThread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
      thread.interrupt();
    }

    //auto scales canvas width and height based on phone model
    @Override
    protected void onSizeChanged(int canvasWidth, int canvasHeight, int oldWidth, int oldHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        super.onSizeChanged(canvasWidth, canvasHeight, oldWidth, oldHeight);
    }

    //encapsulation functions
    public int getCanvasWidth(){
        return canvasWidth;
    }

    public int getCanvasHeight(){
        return canvasHeight;
    }



    //update method for screen
    @Override
    protected void onDraw(Canvas canvas) {
        count++;
        if(count%100==0){
            canvas.drawColor(Color.LTGRAY);
        }
        else{
            canvas.drawColor(Color.LTGRAY);

        }
    }
}
