package com.example.secretsaucetetris;

import android.util.AttributeSet;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.support.annotation.Nullable;
public class MyCanvas extends View{


    private int canvasWidth;
    private int canvasHeight;

    private Paint defPaint = new Paint();
    private Paint linePaint = new Paint();

    private static final int numColumns=10;
    private static final int numRows=20;

    Square screenField[][];

    public MyCanvas(Context context) {
        super(context);
        //getHolder().addCallback(this);
        init(null);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        defPaint.setColor(Color.WHITE);
        linePaint.setColor(Color.DKGRAY);

        if (set == null)
            return;

    }

    @Override
    protected void onSizeChanged(int canvasWidth, int canvasHeight, int oldWidth, int oldHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        super.onSizeChanged(canvasWidth, canvasHeight, oldWidth, oldHeight);
    }

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

    public void swapColor() {

    }

    public void updatescreenField(int [][] array, int num){
        screenField = new Square[20][10];

        for(int i=0; i<getNumRows(); i++){
            for(int j=0; j<getNumColumns(); j++){
                if(array[i][j]!=0){
                    screenField[i][j] = new Square(i, j, getCanvasWidth(), getCanvasHeight(), getNumColumns(), getNumRows());
                }
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {

        drawGrid(canvas);
        drawBoard(canvas);

    }

    private void drawGrid(Canvas canvas){

        canvas.drawColor(Color.WHITE);

//        canvas.drawLine(0, 0, 0, getCanvasHeight(), linePaint);

  //      canvas.drawLine(getCanvasWidth()/numColumns, 0, getCanvasWidth()/numColumns, getCanvasHeight(), linePaint);

        int width = getCanvasWidth();
        int height = getCanvasHeight();
        // Vertical lines
        for (int i = 0; i < numColumns; i++) {
            canvas.drawLine(width * i / numColumns, 0, width * i / numColumns, height, linePaint);
        }

        canvas.drawLine(width-1, 0, width-1, height, linePaint);

        // Horizontal lines
        for (int i = 0; i < numRows; i++) {
            canvas.drawLine(0, height * i / numRows, width, height * i / numRows, linePaint);
        }

    }

    private void drawBoard(Canvas canvas){

        Paint p = new Paint();
        p.setColor(Color.GREEN);

        Rect rect = new Rect();
        rect.set(0,0,canvasWidth/10,canvasHeight/20);
        canvas.drawRect(rect, p);

        p.setColor(Color.BLUE);

        rect.set(0,canvasHeight/20,canvasWidth/10,2*canvasHeight/20);
        canvas.drawRect(rect, p);

        p.setColor(Color.RED);

        rect.set(canvasWidth/10,0,2*canvasWidth/10,canvasHeight/20);
        canvas.drawRect(rect, p);

        p.setColor(Color.YELLOW);

        rect.set(canvasWidth/10,canvasHeight/20,2*canvasWidth/10,2*canvasHeight/20);
        canvas.drawRect(rect, p);

    }

}
