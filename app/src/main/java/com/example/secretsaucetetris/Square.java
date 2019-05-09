package com.example.secretsaucetetris;

public class Square {

    int x1;
    int x2;
    int y1;
    int y2;

    public Square(int x, int y, int canvasWidth, int canvasHeight, int columns, int rows){
        this.x1=x*(canvasHeight/rows);
        this.x2=(x+1)*(canvasHeight/rows);
        this.y1=y*(canvasWidth/columns);
        this.y2=(y+1)*(canvasWidth/columns);
    }

}
