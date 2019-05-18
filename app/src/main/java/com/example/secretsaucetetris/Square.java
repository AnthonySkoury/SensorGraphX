package com.example.secretsaucetetris;

//this class is for converting 2D coordinates into areas for squares on the screen
public class Square {

    int x1;
    int x2;
    int y1;
    int y2;
    int colorCode;

    public Square(int x, int y, int canvasWidth, int canvasHeight, int columns, int rows, int colorCode){
        this.y1=(y*canvasHeight)/rows;
        this.y2=((y+1)*canvasHeight)/rows;
        this.x1=(x*canvasWidth)/columns;
        this.x2=((x+1)*canvasWidth)/columns;
        this.colorCode = colorCode;
    }

}
