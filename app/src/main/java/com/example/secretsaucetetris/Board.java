package com.example.secretsaucetetris;

public class Board {
    private int score=0;
    private int pieceType;
    private int rotateState;
    private int M = 20;
    private int N = 10;
    private int board[][];
    private int board_with_projection[][];
    private int a[][];//0 corresponds to x; 1 corresponds to y
    private int b[][];
    private int nextPiece;
    private boolean gameOver;
    private int figures[][] = {
            {1,3,5,7}, // I
            {2,4,5,7}, // Z
            {3,5,4,6}, // S
            {3,5,4,7}, // T
            {2,3,5,7}, // L
            {3,5,7,6}, // J
            {2,3,4,5}, // O
    };
    public Board(){
        gameOver = false; //intialize game over to false
        board = new int[M][N];
        board_with_projection = new int[M][N];
        a = new int[4][2];//0 corresponds to x; 1 corresponds to y
        b = new int[4][2];
        pieceType = generateRandomNumber(0,6);
        nextPiece = generateRandomNumber(0,6);
        rotateState =0;
        this.set_projection_figure(pieceType);
        this.unconditionalMoveRight();
        this.unconditionalMoveRight();
        this.unconditionalMoveRight();
        this.unconditionalMoveRight();
        //this.unconditionalMoveUp();
        this.updateBoardWithProjection();
    }
    public void printBoard(){
        for (int i=0; i < board.length; i++){
            for (int j=0; j < board[0].length; j++){
                System.out.print(board[i][j]);
            }
            System.out.print("\t(Line " + i + ")");
            System.out.println();
        }
        System.out.println("----------------------");
    }
    public void updateBoardWithProjection(){
        //update according to board
        for(int i=0; i<board.length; i++){//i is y value
            for(int j=0; j<board[0].length; j++){//j is x value
                board_with_projection[i][j] = board[i][j];
            }
        }
        //update according to projection
        for(int i=0; i<a.length; i++){
            board_with_projection[a[i][1]][a[i][0]] = pieceType+1;//set to 9 as test value
        }
    }
    public void printBoardWithProjection(){
        for (int i=0; i<board_with_projection.length; i++){
            for(int j=0; j<board_with_projection[0].length; j++){
                System.out.print(board_with_projection[i][j]);
            }
            System.out.print("\t(Line " + i + ")");
            System.out.println();
        }
        System.out.println("----------------------");
    }
    public void updateBoard(int array[][], int color){ //must use array a or b for this
        for(int i=0; i<array.length; i++){
            board[array[i][1]][array[i][0]] = color;
        }
    }
    public void set_projection_figure(int figureNum){//set the shape we want projection to have
        for(int i=0; i<a.length; i++){
            a[i][0] = figures[figureNum][i] % 2;
            a[i][1] = figures[figureNum][i] / 2;
        }
    }
    public boolean check()//determine whether a collision will occur
    {
        for (int i=0;i<4;i++)
            if (a[i][0]<0 || a[i][0]>=N || a[i][1]>=M) return false;//collision with edge
            else if (board[a[i][1]][a[i][0]] != 0) return false;//collision with filled square

        return true;
    };
    public void moveLeft(){ //direction must either be 1 or -1
        int direction = -1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][0] += direction;
        }
        if(!check()){//if the check fails, revert back to original state
            for(int i=0; i<a.length; i++){
                a[i][0] = b[i][0];
                a[i][1] = b[i][1];
            }
        }
    }
    public void moveRight(){ //direction must either be 1 or -1
        int direction = 1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][0] += direction;
        }
        if(!check()){//if the check fails, revert back to original state
            for(int i=0; i<a.length; i++){
                a[i][0] = b[i][0];
                a[i][1] = b[i][1];
            }
        }
    }
    public void unconditionalMoveRight(){ //direction must either be 1 or -1
        int direction = 1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][0] += direction;
        }
    }
    public void moveDown(){
        int direction = 1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][1] += direction; //shift y value down
        }
        if(!check()){//if the check fails, revert back to original state
            for(int i=0; i<a.length; i++){
                a[i][0] = b[i][0];
                a[i][1] = b[i][1];
            }
        }
    }
    public void unconditionalMoveUp(){
        int direction = -1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][1] += direction; //shift y value up
        }
    }
    public void rotate(){ //rotate 90*
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
        }
        /*if(pieceType == 0){
            if(rotateState == 0){
            }
            else if(rotateState == 1){
            }
            else if(rotateState == 2){
            }
            else{
            }
        }*/
        int center_x = a[1][0];
        int center_y = a[1][1];
        for (int i=0; i <a.length; i++){
            int diff_x = a[i][1] - center_y;
            int diff_y = a[i][0] - center_x;
            a[i][0] = center_x - diff_x;
            a[i][1] = center_y + diff_y;
        }
        if(!check()){//if check fails, revert back to original state
            for(int i=0; i<a.length; i++){
                a[i][0] = b[i][0];
                a[i][1] = b[i][1];
            }
        }
        if(pieceType == 6){//if the piece is a square, revert back to original state
            for(int i=0; i<a.length; i++){
                a[i][0] = b[i][0];
                a[i][1] = b[i][1];
            }
        }
        else{
            rotateState = (rotateState + 1)%4;
        }
    }
    public void tick(){
        int direction = 1;
        for(int i=0; i < a.length; i++){
            b[i][0]=a[i][0];//store b to hold old a values
            b[i][1]=a[i][1];
            a[i][1] += direction; //shift y value down
        }
        if(!check()){//if the check fails, revert back to original state
            updateBoard(b,pieceType+1);//need to update with color-------------------piecetype + 1 determines color
            int count = 0;
            for(int i = 0; i < board.length; i++){
                if(checkRow(i)){
                    clearRowAndShiftAllDown(i);
                    count++;
                    score+=100;
                }
            }
            if(count!=0) {
                score = score * count;
            }
            score+=10;
            pieceType = nextPiece;//set new piece to next piece
            nextPiece = generateRandomNumber(0, 6);//new next piece is generated
            rotateState = 0;
            set_projection_figure(pieceType);
            unconditionalMoveRight();
            unconditionalMoveRight();
            unconditionalMoveRight();
            unconditionalMoveRight();
            if(pieceType != 0)
                unconditionalMoveUp();
            if(!check()){//if there is a collision on attempted spawn
                gameOver=true;
            }
        }
    }

    public int[][] getBoard_with_projection() {
        return board_with_projection;
    }
    public boolean checkRow(int rowToCheck){
        boolean full = true;
        for(int i = 0; i < board[0].length; i++) {//check if board.length or board[0].length
            if (board[rowToCheck][i] == 0) {
                full = false;
                break;
            }
        }
        return full;
    }
    public void clearRowAndShiftAllDown(int rowToCheck){
        for(int i = rowToCheck; i > 0; i--){
            for(int j = 0; j < board[0].length; j++){
                board[i][j] = board[i-1][j];
            }
        }
        for (int j = 0; j < board[0].length; j++){//push in row of zeros for top row
            board[0][j] = 0;
        }
    }
    public int generateRandomNumber(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    public int getScore(){
        return score;
    }

    public int setScore(int new_score){
        this.score = new_score;
        return score;
    }

    public int getNextPiece(){
        return nextPiece;
    }
    public int getPieceType(){
        return pieceType;
    }
    public boolean getGameOver(){//if returns true if game is over
        return gameOver;
    }
}
