package com.chess.model;

public class Pawn extends Piece {
    public Pawn (boolean w) {
        super(w);
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "\u2659" : "\u265F";
    }

    @Override
    public boolean isValidMove(int sx, int sy, int ex, int ey, Board board) {
        int color = isWhite() ? -1 : 1; //white = -1, black = 1
        int dx = Math.abs(sx - ex);
        int dy = ey - sy;

        //forward move
        if (dx == 0) {
            // one square forward
            if (dy == color) {
                if (board.getPiece(ex, ey) == null) {
                    return true;
                }

            } 
            // two squares forward from starting position 
            else if (dy == 2 * color){
                if (isWhite() && sy == 6 || !isWhite() && sy == 1) {
                    if (board.getPiece(ex, ey) == null){
                        if (BoardUtils.isPathClear(sx, sy, ex, ey, board)) {
                            return true;
                        }
                    }
                }
            }
        } 

        //killing move
        if (dx == 1 && dy == color) {
            Piece target = board.getPiece(ex, ey);
            if (target != null && target.isWhite() != this.isWhite()) {
                return true;
            }
        }

        return false;
    }

}
