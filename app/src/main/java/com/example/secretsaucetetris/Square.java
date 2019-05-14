package com.example.secretsaucetetris;

public class Square {

    float x1;
    float x2;
    float y1;
    float y2;
    int colorCode;

    public Square(int x, int y, float canvasWidth, float canvasHeight, int columns, int rows, int colorCode){

        this.x1=x*(canvasHeight/rows);
        this.x2=(x+1)*(canvasHeight/rows);
        this.y1=y*(canvasWidth/columns);
        this.y2=(y+1)*(canvasWidth/columns);
        this.colorCode = colorCode;
    }

}
