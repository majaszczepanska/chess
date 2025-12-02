package com.chess.model;

public abstract class Piece {
    public boolean isWhite;
    
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;   //in Board new Bishop(w: false) -> in Piece this.isWhite = false; (STEP 3)
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract String getSymbol();

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);
}
