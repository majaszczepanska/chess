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
- pawn hop
- last move (colors)
- restart game
- prettier message (win)
- view on killed pieces
- delate click on null squares
*/