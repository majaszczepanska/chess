package com.chess.model;


public class Knight extends Piece {
    public Knight(boolean w) {
        super(w);

    }

    @Override
    public String getSymbol() {
        return isWhite() ? "\u2658" : "\u265E";
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board) {
        int dx = Math.abs(sx - ex);
        int dy = Math.abs(sy - ey);
        return ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)); // that's function if (if((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) return true; else return false;
    }
}
