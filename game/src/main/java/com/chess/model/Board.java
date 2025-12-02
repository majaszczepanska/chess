package com.chess.model;

public class Board {
    private final Piece[][] grid;
    
    public Board() {
        grid = new Piece[8][8];
        initializeBoard();
    }

    //GETTER for getting pieces from the board
    public Piece getPiece(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) { //out of borders
            return null;    
        }
        return grid[y][x];  //row y, column x
    }

    //SETTER for placing pieces on the board
    public void setPiece(int x, int y, Piece piece) {   
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            grid[y][x] = piece; 
        }
    }
    
    //FINDER FOR KING returns int array with x and y coordinates of the king (white or black)
    public int[] findKing(boolean isWhite){
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                Piece pieceKing = getPiece(x, y);
                if (pieceKing != null && pieceKing instanceof King && pieceKing.isWhite() == isWhite){
                    //System.out.println("King found at: " + x + ", " + y);
                    return new int[]{x, y};
                    
                }   
            }
        }
        return null;
    }


    //CHECK IF KING IS IN CHECK create array with king position, return result of isKingAttacked (king(x,y, color))
    public boolean isCheck(boolean isWhite){
        int[] kingPosition = findKing(isWhite);
        if (kingPosition != null){
            return(isKingAttacked(kingPosition[0], kingPosition[1], isWhite));
        }
        return false;
    }

    //CHECK IF KING IS ATTACKED declare every piece, check if it is opponent's piece, check if it can move to king's position
    public boolean isKingAttacked(int targetX, int targetY, boolean isWhite){
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                Piece pieceAttacker = getPiece(x, y);
                if (pieceAttacker != null && pieceAttacker.isWhite() != isWhite){
                    if (pieceAttacker.isValidMove(x, y, targetX, targetY, this)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasAnyMove(boolean isWhiteTurn) {
        for (int sy = 0; sy < 8; sy++){
            for (int sx = 0; sx < 8; sx++){
                Piece piece = getPiece(sx, sy);
                if(piece != null && piece.isWhite() == isWhiteTurn){
                    for (int targetY = 0; targetY < 8; targetY++){
                        for (int targetX = 0; targetX < 8; targetX++){
                            Piece target = getPiece(targetX, targetY);
                            if (target != null && target.isWhite() == isWhiteTurn){
                                continue;
                            }

                            if (piece.isValidMove(sx, sy, targetX, targetY, this)){
                                setPiece(targetX, targetY, piece);
                                setPiece(sx, sy, null);
                                boolean kingSafe = !isCheck(isWhiteTurn);
                                setPiece(sx, sy, piece);
                                setPiece(targetX, targetY, null);
                                if (kingSafe){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    
    //create board with pieces in starting positions
    private void initializeBoard() {
        // Black pieces
        grid[0][0] = new Rook(false); //create new object with parameter false (black) (STEP 1)
        grid[0][1] = new Knight(false);
        grid[0][2] = new Bishop(false);
        grid[0][3] = new Queen(false);
        grid[0][4] = new King(false);
        grid[0][5] = new Bishop(false);
        grid[0][6] = new Knight(false);
        grid[0][7] = new Rook(false);
        for (int i=0; i<8; i++){
            grid[1][i] = new Pawn(false);
        }

        // White pieces
        grid[7][0] = new Rook(true);
        grid[7][1] = new Knight(true);
        grid[7][2] = new Bishop(true);
        grid[7][3] = new Queen(true);
        grid[7][4] = new King(true);
        grid[7][5] = new Bishop(true);
        grid[7][6] = new Knight(true);
        grid[7][7] = new Rook(true);
        for (int i=0; i<8; i++){
            grid[6][i] = new Pawn(true);
        }

    }
}
