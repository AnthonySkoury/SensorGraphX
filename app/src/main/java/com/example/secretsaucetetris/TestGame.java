package com.example.secretsaucetetris;

public class TestGame {
    public int [][] boardArray;
    int row;
    int col;

    public TestGame(int row, int col){
        this.row = row;
        this.col = col;
        boardArray = new int[row][col];
    }

    public void defaultArray(){
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                boardArray[i][j]=0;
            }
        }
    }

    public void modifyArray(int select){
        if(select==1) {
            boardArray[0][4] = 1;
            boardArray[1][3] = 1;
            boardArray[1][4] = 1;
            boardArray[1][5] = 1;
            for (int i = 0; i < 10; i++) {
                boardArray[19][i] = 2;
            }
        }
        else if(select == 2){
            defaultArray();
            boardArray[3][4]=4;
            boardArray[3][5]=4;
            boardArray[3][6]=4;
            boardArray[3][7]=4;

        }
        else{
            defaultArray();
            boardArray[10][4]=6;
            boardArray[11][4]=6;
            boardArray[12][4]=6;
            boardArray[13][4]=6;

        }
    }

}
