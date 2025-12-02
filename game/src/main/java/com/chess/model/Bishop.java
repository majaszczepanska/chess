package com.chess.model;

public class Bishop extends Piece {
    public Bishop(boolean w) {
        super(w);   //in Board new Bishop(w: false) -> in Piece this.isWhite = false;; (STEP 2)
    }
    
    @Override
    public String getSymbol() {
        return isWhite() ? "\u2657" : "\u265D"; // Unicode symbols for white and black bishop
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board) {
        int dx = Math.abs(sx - ex);
        int dy = Math.abs(sy - ey);
        if (dx == dy) { //bishop move (diagonal)
            return BoardUtils.isPathClear(sx, sy, ex, ey, board);
        } else {    
            return false;
        }
    }
}
