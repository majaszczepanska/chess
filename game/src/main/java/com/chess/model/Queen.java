package com.chess.model;

public class Queen extends Piece {
    public Queen (boolean w){
        super(w);
    }

    @Override
    public String getSymbol () {
        return isWhite() ? "\u2655" : "\u265B";
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board){
        int dx = Math.abs(sx - ex);
        int dy = Math.abs(sy - ey);
        if ((sx == ex && sy != ey) || (sx != ex && sy == ey) || (dx == dy)) { //rook (one line) or bishop move (diagonal)
            return BoardUtils.isPathClear(sx, sy, ex, ey, board);
        } else {
            return false;
        }
        
    }
}
