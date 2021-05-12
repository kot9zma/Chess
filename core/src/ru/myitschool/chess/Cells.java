package ru.myitschool.chess;


public class Cells {
    int x, y;
    int color; // 0 -белый, 1 - чёрный
    Figure figure;

    public Cells(int x, int y, int color, Figure figure) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.figure = figure;
    }
}
