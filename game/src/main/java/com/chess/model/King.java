package com.chess.model;

public class King extends Piece {
    public King(boolean w) {
        super(w);
    }

    @Override
    public String getSymbol(){
        return isWhite() ? "\u2654" : "\u265A";
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board) {
        int dx = Math.abs(ex - sx);
        int dy = Math.abs(ey - sy);
        return (dx == 1 && dy == 0 || dx == 0 && dy == 1 || dx == 1 && dy == 1);   //that's function if (if (dx == 1 && dy == 0 || dx == 0 && dy == 1 || dx == 1 && dy == 1) return true; else return false;

    }
}
