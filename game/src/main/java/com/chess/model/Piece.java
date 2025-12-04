package com.chess.model;

public abstract class Piece {
    public boolean isWhite;
    
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;   //in Board new Bishop(w: false) -> in Piece this.isWhite = false; (STEP 3)
    }

    public boolean isWhite() {
        return isWhite;
    }

    //
    boolean hasMoved;
    public boolean hasMoved() { 
        return hasMoved; 
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract String getSymbol();

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);
}
