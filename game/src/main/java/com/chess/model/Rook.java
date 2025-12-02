package com.chess.model;


public class Rook extends Piece {
    public Rook (boolean w){
        super(w);
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "\u2656" : "\u265C";
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board){
        if ((sx == ex && sy != ey) || (sx != ex && sy == ey)) { //rook move (one line)
            return BoardUtils.isPathClear(sx, sy, ex, ey, board);
        } else {
            return false;
        }
    }
}
