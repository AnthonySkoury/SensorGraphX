package com.example.secretsaucetetris;

public class Square {

    int x1;
    int x2;
    int y1;
    int y2;
    int colorCode;

    public Square(int x, int y, int canvasWidth, int canvasHeight, int columns, int rows, int colorCode){
        //canvasHeight+=1;
        this.y1=(y*canvasHeight)/rows;
        this.y2=((y+1)*canvasHeight)/rows;
        this.x1=(x*canvasWidth)/columns;
        this.x2=((x+1)*canvasWidth)/columns;
        this.colorCode = colorCode;
    }

}
