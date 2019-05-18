package com.example.secretsaucetetris;

import android.util.AttributeSet;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.support.annotation.Nullable;

public class MyCanvas extends SurfaceView implements SurfaceHolder.Callback{


    private int canvasWidth;
    private int canvasHeight;

    private Paint defPaint = new Paint();
    private Paint linePaint = new Paint();

    private static final int numColumns=10;
    private static final int numRows=20;

    private int purple_T = Color.rgb(128,0,128);
    private int aqua_I = Color.rgb(0,255,255);
    private int blue_J = Color.rgb(0,0,255);
    private int orange_L = Color.rgb(255,165,0);
    private int yellow_O = Color.rgb(255,255,0);
    private int green_S = Color.rgb(0,255,0);
    private int red_Z = Color.rgb(255,0,0);

    Square Grid[][];

    ScreenThread thread;

    /* Constructors */
    public MyCanvas(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        init(null);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
        setFocusable(true);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        defPaint.setColor(Color.WHITE);
        linePaint.setColor(Color.DKGRAY);

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

    public int getNumColumns(){
        return numColumns;
    }

    public int getNumRows(){
        return numRows;
    }

    public Square[][] getGrid(){
        return Grid;
    }

    //updates the grid data structure based on the game state in the back end
    public void updateGrid(int [][] array){
        Grid = new Square[numRows][numColumns];

        for(int i=0; i<getNumRows(); i++){
            for(int j=0; j<getNumColumns(); j++){
                if(array[i][j]!=0){
                    //j is x coordinate, i is y coordinate, also passing in width, height, columns, and rows to compute coordinates (x1,y1) and (x2,y2) for square area on grid
                    Grid[i][j] = new Square(j, i, getCanvasWidth(), getCanvasHeight(), getNumColumns(), getNumRows(), array[i][j]);
                }
            }
        }
    }

    private Paint colorCode(Paint p, int code){
        switch(code){
            case 1:
                p.setColor(aqua_I);
                break;
            case 2:
                p.setColor(red_Z);
                break;
            case 3:
                p.setColor(green_S);
                break;
            case 4:
                p.setColor(purple_T);
                break;
            case 5:
                p.setColor(orange_L);
                break;
            case 6:
                p.setColor(blue_J);
                break;
            case 7:
                p.setColor(yellow_O);
                break;
            default:
                p.setColor(Color.LTGRAY);
                break;
        }
        return p;
    }

    //update method for screen
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        drawBoard(canvas);
        drawGrid(canvas);
    }

    //draws grid based on scaling of canvas
    private void drawGrid(Canvas canvas){

        int width = getCanvasWidth();
        int height = getCanvasHeight();
        //vertical lines
        for (int i = 0; i < numColumns; i++) {
            canvas.drawLine((i * width) / numColumns, 0, (i * width) / numColumns, height, linePaint);
        }

        canvas.drawLine(width-1, 0, width-1, height, linePaint);

        //horizontal lines
        for (int i = 0; i < numRows; i++) {
            canvas.drawLine(0, (i * height) / numRows, width, (i * height) / numRows, linePaint);
        }

    }

    //draws tetronimoes on screen based of Grid data structure
    private void drawBoard(Canvas canvas){

        Paint p = new Paint();
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numColumns; j++){
                try {
                    if (Grid[i][j] != null) {
                        Rect rect = new Rect();
                        rect.set(Grid[i][j].x1, Grid[i][j].y1, Grid[i][j].x2, Grid[i][j].y2);
                        p = colorCode(p, Grid[i][j].colorCode);
                        canvas.drawRect(rect, p);
                    }
                }
                catch (NullPointerException e){
                    System.out.println("Initializing Board...");
                }
            }
        }
    }

}
