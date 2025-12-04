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
        if (dx == 1 && dy == 0 || dx == 0 && dy == 1 || dx == 1 && dy == 1){
            return true;
        }

        //castling
        if (dy == 0 && dx == 2){
            //king in wrong place or already moved
            if (isWhite() && sy != 7) return false;
            if (!isWhite() && sy != 0) return false;
            if (this.hasMoved()) return false;

            //short castling
            if (ex>sx){
                //clear path
                if(board.getPiece(sx+1, sy) == null && board.getPiece(sx+2, sy) == null){
                    Piece rook = board.getPiece(sx+3, sy);
                    if (rook != null && rook instanceof Rook && rook.isWhite() == isWhite() && !rook.hasMoved()){
                        return true;
                    }
                }
            //long castling    
            } else {
                if(board.getPiece(sx-1, sy) == null && board.getPiece(sx-2, sy) == null && board.getPiece(sx-3, sy) == null){
                    Piece rook = board.getPiece(sx-4, sy);
                    if (rook != null && rook instanceof Rook && rook.isWhite() == isWhite() && !rook.hasMoved()){
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
