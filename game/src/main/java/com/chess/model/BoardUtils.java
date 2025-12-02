package com.chess.model;

public class BoardUtils {

    public static boolean isPathClear(int sx, int sy, int ex, int ey, Board board) {
        //difference from start to end (direction of movement)
        int dx = Integer.compare(ex, sx);
        int dy = Integer.compare(ey, sy);

        //new x and y to check
        int x = sx + dx;
        int y = sy + dy;

        //check each square along the path (is null)
        while (x != ex || y != ey) {
            if (board.getPiece(x, y) != null){
                return false;
            } 
            x += dx;
            y += dy;
        }
        return true;
    }
}
