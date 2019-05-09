package com.example.secretsaucetetris;

import android.util.AttributeSet;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.support.annotation.Nullable;
public class MyCanvas extends View{


    private static final int SQUARE_SIZE_DEF = 200;



    public MyCanvas(Context context) {
        super(context);

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


        if (set == null)
            return;

    }

    public void swapColor() {

    }
//grid view
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.RED);
        Paint p = new Paint();
        p.setColor(Color.GREEN);

        Rect rect = new Rect();
        rect.set(10,10,100,100);
        canvas.drawRect(rect, p);

    }
}
