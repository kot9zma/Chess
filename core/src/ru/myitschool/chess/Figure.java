package ru.myitschool.chess;

public class Figure {
    int boardX, boardY;
    float scrX, scrY;
    static float scrXUnderBoard[] = new float[2];
    int side;
    int color;
    int type;
    boolean isAlive;
    boolean isMove;

    public Figure(int boardX, int boardY, int color, int type, int side) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.color = color;
        this.type = type;
        this.side = side;
        isAlive = true;
        isMove = false;
        boardToScreen();
    }

    void boardToScreen(){
        scrX = boardX *ChessMain.size;
        scrY = boardY *ChessMain.size + ChessMain.paddingBottom;
    }

    void screenToBoard(){
        boardX = (int)((scrX + ChessMain.size/2)/ChessMain.size);
        boardY = (int)((scrY + ChessMain.size/2 - ChessMain.paddingBottom)/ChessMain.size);
    }

    boolean isHit(float touchX, float touchY){
        return touchX > scrX && touchX < scrX + ChessMain.size && touchY > scrY && touchY < scrY + ChessMain.size;
    }

    void move(float touchX, float touchY){
        scrX = touchX-ChessMain.size/2;
        scrY = touchY-ChessMain.size/2;
    }

    boolean isDropCorrect(float scrX, float scrY, Cells[][] board){
        int x = (int)(scrX/ChessMain.size);
        int y = (int)((scrY- ChessMain.paddingBottom)/ChessMain.size);
        System.out.println("x = "+x+" y = "+y);
        if(x<0 || x>7 || y<0 || y>7) return false;

        if(board[x][y].figure != null)
            if(board[x][y].figure.side == side) return false;
            else {
                board[x][y].figure.isAlive = false;
                board[x][y].figure.scrX = scrXUnderBoard[board[x][y].figure.side];
                if(side == ChessMain.OUR_SIDE) board[x][y].figure.scrY = ChessMain.paddingBottom - ChessMain.size*2;
                else board[x][y].figure.scrY = ChessMain.paddingBottom + ChessMain.SCR_WIDTH + ChessMain.size*1.5f;
                scrXUnderBoard[board[x][y].figure.side] += ChessMain.size/2;
            }

        return true;
    }
}
