package com.chess.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.chess.model.Board;
import com.chess.model.Piece;

public class GameWindow extends JFrame {

    private final JButton[][] squares = new JButton[8][8];
    private final Board board;
    private Piece selectedPiece = null;
    private boolean isWhiteTurn = true;
    private int selectedX = -1;
    private int selectedY = -1;
    private Color currentColor = null;

    boolean isWhiteCastlingPossible = true;
    boolean isBlackCastlingPossible = true;


    //COLORS
    //board
    private final Color lightSquare = new Color(209, 156, 155);
    private final Color darkSquare = new Color(145, 95, 109);
    private final Color background = new Color(153, 201, 94);
    private final Color border = new Color(102, 68, 89);
    //move
    private final Color possibleMove = new Color(142, 173, 134);
    private final Color possibleCapture = new Color(191, 61, 61);

    public GameWindow() {
        setTitle("Chess Game - White starts");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        //create new board
        board = new Board();
        drawBoard();
        getContentPane().setBackground(border);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //paint the board
    private void drawBoard() {   
        for (int y=0; y<8; y++){
            for (int x=0; x<8; x++){
                squares[y][x] = new JButton();
                squares[y][x].setMargin(new Insets(0,0,0,0));
                squares[y][x].setFocusPainted(false);
                squares[y][x].setBorderPainted(false);
                squares[y][x].setFont(new Font("Serif", Font.PLAIN, 100));
                //set square colors
                if (x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    squares[y][x].setBackground(lightSquare);
                } else {
                    squares[y][x].setBackground(darkSquare);
                }
                
                //add action listener to each button, finalX and finalY selected
                final int finalX = x;
                final int finalY = y;
                squares[y][x].addActionListener(e -> handleClick(finalX, finalY));

                //add button to the frame
                add(squares[y][x]);
            }
            
        }
        refreshBoard();
    }

    //update the board display add pieces
    private void refreshBoard() {
        for (int y=0; y<8; y++){
            for (int x=0; x<8; x++){
                Piece piece = board.getPiece(x, y);
                if (piece != null ){
                    squares[y][x].setText(piece.getSymbol());
                    squares[y][x].setForeground(piece.isWhite() ? Color.white : Color.black);   //white pieces are white, black pieces are black - decision based on piece color - true/false (STEP 4)
                } else {
                    squares[y][x].setText("");
                }
            }
        }
    }


    //button clicked
    private void handleClick(int x, int y){
        //first click
        if (selectedPiece == null) {
            Piece piece = board.getPiece(x, y);
            //if there is a piece and it's the correct turn
            if(piece != null && piece.isWhite() == isWhiteTurn) {
                //selected parameters
                selectedPiece = piece;
                selectedX = x;
                selectedY = y;
                currentColor = squares[selectedY][selectedX].getBackground();
                squares[selectedY][selectedX].setBackground(background);
                showMoves();
            }
        }



        //selectedPiece is not null - second click
        else {
            //deselect if clicked the same square
            if (x == selectedX && y == selectedY) {
                resetSelection();
                return;
            }
            
            //move selected piece to selected target square
            int targetX = x;
            int targetY = y;
            //get target piece/square
            Piece target = board.getPiece(targetX, targetY);

            //function to prevent extra clicking when changing one's mind when selecting which piece should move
            //if second click is click in piece in the same color
            if (target != null && target.isWhite() == isWhiteTurn){
                //reset color of the piece from first click, do the same thing as in the first click  
                resetSelection();  
                selectedPiece = target;
                selectedX = targetX;
                selectedY = targetY;
                currentColor = squares[targetY][targetX].getBackground();
                squares[targetY][targetX].setBackground(background);
                showMoves();
                return;
                
            }
            
            //can not capture the king
            if (target != null) {
                String pieceType = target.getClass().getSimpleName();
                if (pieceType.equals("King")){
                    resetSelection();
                }
  
            }

            //check if the move is valid
            if (selectedPiece.isValidMove(selectedX, selectedY, targetX, targetY, board)) {
                //simulate move to check for check condition
                //put selected piece to target square and remove from original square
                board.setPiece(targetX, targetY, selectedPiece);
                board.setPiece(selectedX, selectedY, null);
                if (board.isCheck(isWhiteTurn)) {
                    board.setPiece(selectedX, selectedY, selectedPiece);
                    board.setPiece(targetX, targetY, target);
                    return;
                }

                String pieceType = selectedPiece.getClass().getSimpleName();
            
                
                //King moved for 2 squares - castling
                if (pieceType.equals("King") && Math.abs(targetX-selectedX) == 2){
                    //white castling - short
                    if (targetX == 6 && targetY == 7){
                        Piece castlingRook = board.getPiece(7, 7);
                        board.setPiece(5, 7, castlingRook);
                        board.setPiece(7, 7, null);
                        castlingRook.setHasMoved(true);
                    }
                    //white castling - long
                    if (targetX == 2 && targetY == 7){
                        Piece castlingRook = board.getPiece(0, 7);
                        board.setPiece(3, 7, castlingRook);
                        board.setPiece(0, 7, null);
                        castlingRook.setHasMoved(true);
                    }
                    //black castling - short
                    if (targetX == 6 && targetY == 0){
                        Piece castlingRook = board.getPiece(7, 0);
                        board.setPiece(5, 0, castlingRook);
                        board.setPiece(7, 0, null);
                        castlingRook.setHasMoved(true);
                    }
                    //black castling - long
                    if (targetX == 2 && targetY == 0){
                        Piece castlingRook = board.getPiece(0, 0);
                        board.setPiece(3, 0, castlingRook);
                        board.setPiece(0, 0, null);
                        castlingRook.setHasMoved(true);
                    }
                }

                //important to set true to hasMoved in selectedPiece
                selectedPiece.setHasMoved(true);

                //pawn exchange
                boolean currentColor = selectedPiece.isWhite();
                refreshBoard();
                resetSelection();
                if (pieceType.equals("Pawn") && (targetY == 0 || targetY == 7)){
                    String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                    int choice = javax.swing.JOptionPane.showOptionDialog(this, "Select piece to receive for pawn: ", "Promotion",
                        javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                    );
                    Piece promotedPiece;
                    switch(choice){
                        case 1:
                            promotedPiece = new com.chess.model.Rook(currentColor);
                            break;
                        case 2:
                            promotedPiece = new com.chess.model.Bishop(currentColor);
                            break;
                        case 3:
                            promotedPiece = new com.chess.model.Knight(currentColor);
                            break;
                        default:
                            promotedPiece = new com.chess.model.Queen(currentColor);
                            break;
                    }
                    board.setPiece(targetX, targetY, promotedPiece);
                    refreshBoard();
                }
                nextTurn();
            }
        }
    }


    private void nextTurn() {
        //change turn
        isWhiteTurn = !isWhiteTurn;
        boolean inCheck = board.isCheck(isWhiteTurn);
        boolean canMove = board.hasAnyMove(isWhiteTurn);
        //if king in check
        if (inCheck){
            if (!canMove) {
                setTitle("Checkmate, " +(!isWhiteTurn ? "White" : "Black")+" won!");
                javax.swing.JOptionPane.showMessageDialog(this, "Checkmate\nEnd of the game, " +(!isWhiteTurn ? "White" : "Black")+" won!");
                refreshBoard();
            } else {
                setTitle("Chess - Turn: " +(isWhiteTurn ? "White" : "Black"));
            }
        } else {
            if (!canMove){
                setTitle("Stalemate, nobody won!");
                javax.swing.JOptionPane.showMessageDialog(this, "Stalemate\nEnd of the game, withdraw");
                refreshBoard();
            } else {
                setTitle("Chess - Turn: " +(isWhiteTurn ? "White" : "Black"));
            }
        }
    }



    private void showMoves(){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (selectedPiece.isValidMove(selectedX, selectedY, x, y, board)){
                    Piece target = board.getPiece(x, y);
                    if (x == selectedX && y == selectedY){
                        continue;
                    }
                    if (target != null && target.isWhite() == isWhiteTurn){
                        continue;
                    }

                    board.setPiece(x, y, selectedPiece);
                    board.setPiece(selectedX, selectedY, null);
                    boolean moveSafe = !board.isCheck(isWhiteTurn);
                    board.setPiece(selectedX, selectedY, selectedPiece);
                    board.setPiece(x, y, target);
                    if (moveSafe){
                        squares[y][x].setBackground(possibleMove);
                        squares[y][x].setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(1,1,1,1),
                            BorderFactory.createLineBorder(border, 3)
                        ));
                        squares[y][x].setBorderPainted(true);
                        if (target != null){
                             squares[y][x].setBackground(possibleCapture);
                        }
                    }
                }
            }
        }
                
    }
    


    //reset selection - clear selected piece and restore square color
    private void resetSelection(){
        squares[selectedY][selectedX].setBackground(currentColor);
        selectedPiece = null;
        selectedX = -1;
        selectedY = -1;
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                if ((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
                    squares[y][x].setBackground(lightSquare);
                } else {
                    squares[y][x].setBackground(darkSquare);
                }
                squares[y][x].setBorderPainted(false);
                squares[y][x].setBorder(null);
            }
        }
                
    }
        
 


    
}
