package com.chess;

import javax.swing.SwingUtilities;

import com.chess.view.GameWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            new GameWindow();
        });
    }
}



/*lack of function:
- castling (king and rook)
- exchange pawn for a different piece
- pawn hop
- moving options
- killing options
- last move (colors)
- unclick own piece 
- restart game
- prettier message (win)
- checkmate (first move then message)
- view on killed pieces
*/