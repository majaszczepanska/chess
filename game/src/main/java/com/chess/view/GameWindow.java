package com.chess.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chess.model.Board;
import com.chess.model.Piece;

public class GameWindow extends JFrame {

    private final JButton[][] squares = new JButton[8][8];
    private JPanel capturedWhitePanel;
    private JPanel capturedBlackPanel;
    private Board board;
    private JPanel sidePanel;
    private JButton restartButton;
    private JLabel label;
    private Piece selectedPiece = null;
    private boolean isWhiteTurn = true;
    private int selectedX = -1;
    private int selectedY = -1;
    private Color currentColor = null;


    //LAST MOVE
    private int lastMoveStartX = -1;
    private int lastMoveStartY = -1;
    private int lastMoveEndX = -1;
    private int lastMoveEndY = -1;


    //COLORS
    //board
    private final Color lightSquare = new Color(209, 156, 155);
    private final Color darkSquare = new Color(145, 95, 109);
    //piece
    private final Color clickedPieceBackground = new Color(153, 201, 94);
    private final Color background = new Color(102, 68, 89);
    
    //move
    private final Color possibleMove = new Color(148, 255, 177);
    //private final Color possibleMoveBorder = new Color(61, 105, 67);
    private final Color possibleCapture = new Color(191, 61, 61);
    //private final Color possibleCaptureBorder = new Color(122, 20, 15);
    //private final Color lastMove = new Color(220, 185, 60);
    //private final Color lastMoveBorder = new Color(158, 84, 13);

    public GameWindow() {
        setTitle("Chess Game - White starts");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());

        board = new Board();
        add(createBoardPanel(), java.awt.BorderLayout.CENTER);
        
        JPanel sidePanel = new JPanel();
        
        //SIDE PANEL
        sidePanel.setBackground(background);
        sidePanel.setPreferredSize(new java.awt.Dimension(300, 0));
        sidePanel.setLayout(null);

        //BUTTON RESTART
        JButton restartButton = new JButton("New game");
        restartButton.setBounds(50,400,200,100);
        restartButton.setFont(new Font("Sans serif", Font.BOLD, 30));
        restartButton.setBackground(lightSquare);
        restartButton.addActionListener(e -> restartGame());
        sidePanel.add(restartButton);

        //CAPTURED WHITE PANEL
        capturedWhitePanel = new JPanel();
        capturedWhitePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        capturedWhitePanel.setBackground(lightSquare);
        capturedWhitePanel.setBounds(25, 25, 250, 250);
        sidePanel.add(capturedWhitePanel);

        //CAPTURED BLACK PANEL
        capturedBlackPanel = new JPanel();
        capturedBlackPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        capturedBlackPanel.setBackground(lightSquare);
        capturedBlackPanel.setBounds(25, 600, 250, 250);
        sidePanel.add(capturedBlackPanel);


        add(sidePanel, java.awt.BorderLayout.EAST);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //PAIN THE BOARD
    private javax.swing.JPanel createBoardPanel() { 
        javax.swing.JPanel boardPanel = new javax.swing.JPanel(new GridLayout(8,8));  
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
                boardPanel.add(squares[y][x]);
            }
        }
        refreshBoard();
        return boardPanel;
    }

    //UPDATE THE BOARD - ADD PIECES
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


    //BUTTON CLICKED
    private void handleClick(int x, int y){
        //1. FIRST CLICK
        if (selectedPiece == null) {
            Piece piece = board.getPiece(x, y);

            //if there is a piece and it's the correct turn
            if(piece != null && piece.isWhite() == isWhiteTurn) {
                //selected parameters
                selectedPiece = piece;
                selectedX = x;
                selectedY = y;
                currentColor = squares[selectedY][selectedX].getBackground();
                squares[selectedY][selectedX].setBackground(clickedPieceBackground);
                showMoves();
            }
        }



        //2. SECOND CLICK - selectedPiece is not null 
        else {
            //1. THE SAME SQUARE - deselect 
            if (x == selectedX && y == selectedY) {
                resetSelection();
                return;
            }
            
            //move selected piece to selected target square
            int targetX = x;
            int targetY = y;
            //get target square
            Piece target = board.getPiece(targetX, targetY);

            //2. SECOND CLICK IS PIECE IN THE SAME COLOR AS FIRST
            if (target != null && target.isWhite() == isWhiteTurn){
                //reset color of the square from first click, do the same thing as in the first click  
                resetSelection();  
                selectedPiece = target;
                selectedX = targetX;
                selectedY = targetY;
                currentColor = squares[targetY][targetX].getBackground();
                squares[targetY][targetX].setBackground(clickedPieceBackground);
                showMoves();
                return;
            }
            
            //3. RESET SELECTION IF USER WANT TO CAPTURE KING
            if (target != null) {
                String pieceType = target.getClass().getSimpleName();
                if (pieceType.equals("King")){
                    resetSelection();
                }
  
            }

            //4. MOVE IS VALID
            if (selectedPiece.isValidMove(selectedX, selectedY, targetX, targetY, board)) {
                //for show last move
                lastMoveStartX = selectedX;
                lastMoveStartY = selectedY;
                lastMoveEndX = targetX;
                lastMoveEndY = targetY;

                //side panel
                if (target != null){
                    JLabel label = new JLabel(target.getSymbol());
                    label.setFont(new Font("Serif", Font.PLAIN, 40));
                    if (target.isWhite()){
                        label.setForeground(Color.white);
                        capturedWhitePanel.add(label);
                    } else {
                        label.setForeground(Color.black);
                        capturedBlackPanel.add(label);
                    }
                }
                
                
                //put selected piece to target square and remove from original square
                board.setPiece(targetX, targetY, selectedPiece);
                board.setPiece(selectedX, selectedY, null);
                //if isCheck - undo move
                if (board.isCheck(isWhiteTurn)) {
                    board.setPiece(selectedX, selectedY, selectedPiece);
                    board.setPiece(targetX, targetY, target);
                    return;
                }

                //get name of selected piece
                String pieceType = selectedPiece.getClass().getSimpleName();
            
                
                //1. CASTLING - king moved 2 squares
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

                //2. PAWN EXCHANGE
                boolean currentColor = selectedPiece.isWhite();
                refreshBoard();
                resetSelection();
                if (pieceType.equals("Pawn") && (targetY == 0 || targetY == 7)){
                    //option in exchange
                    String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                    //show an option box
                    int choice = javax.swing.JOptionPane.showOptionDialog(this, "Select piece to receive for pawn: ", "Promotion",
                        javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                    );
                    //create new Piece based on choice
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

    //ACTION AFTER MOVE ENDS
    private void nextTurn() {
        //change turn
        isWhiteTurn = !isWhiteTurn;
        boolean inCheck = board.isCheck(isWhiteTurn);
        boolean canMove = board.hasAnyMove(isWhiteTurn);
        //KING CHECKED
        if (inCheck){
            //CHECKMATE
            if (!canMove) {
                setTitle("Checkmate, " +(!isWhiteTurn ? "White" : "Black")+" won!");
                javax.swing.JOptionPane.showMessageDialog(this, "Checkmate\nEnd of the game, " +(!isWhiteTurn ? "White" : "Black")+" won!");
                refreshBoard();
            } else {
                setTitle("Chess - Turn: " +(isWhiteTurn ? "White" : "Black"));
            }
        } else {
            //STALEMATE
            if (!canMove){
                setTitle("Stalemate, nobody won!");
                javax.swing.JOptionPane.showMessageDialog(this, "Stalemate\nEnd of the game, withdraw");
                refreshBoard();
            } else {
                setTitle("Chess - Turn: " +(isWhiteTurn ? "White" : "Black"));
            }
        }
    }


    //SHOW POSSIBLE MOVES
    private void showMoves(){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (selectedPiece.isValidMove(selectedX, selectedY, x, y, board)){
                    Piece target = board.getPiece(x, y);
                    if (x == selectedX && y == selectedY){
                        continue;
                    }
                    if (target != null && target.isWhite() == selectedPiece.isWhite()){
                        continue;
                    }

                    //LOOK FOR MOVES - PREVENT CHECK
                    board.setPiece(x, y, selectedPiece);
                    board.setPiece(selectedX, selectedY, null);
                    boolean moveSafe = !board.isCheck(isWhiteTurn);
                    board.setPiece(selectedX, selectedY, selectedPiece);
                    board.setPiece(x, y, target);
                    //MOVE IS SAFE
                    if (moveSafe){
                        if (target == null){
                            squares[y][x].setForeground(possibleMove);
                            squares[y][x].setText("•");
                        } else {
                            squares[y][x].setBackground(possibleCapture);
                            //paintBorder(y, x, possibleCaptureBorder);
                        }
                    }
                }
            }
        }
                
    }
    


    //RESET SELECTION - clear selected piece and restore square color
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
            refreshBoard();
        }
        //show last move in different color
        /*
        if (lastMoveStartX != -1) {
            squares[lastMoveStartY][lastMoveStartX].setBackground(lastMove);
            squares[lastMoveEndY][lastMoveEndX].setBackground(lastMove);

        }
        */      
    }

    private void restartGame(){
        board = new Board();
        isWhiteTurn = true;
        selectedPiece = null;
        selectedX = -1;
        selectedY = -1;
        capturedWhitePanel.removeAll();
        capturedWhitePanel.repaint();
        capturedBlackPanel.removeAll();
        capturedBlackPanel.repaint();
        setTitle("Chess Game - White starts");
        refreshBoard();
        resetSelection();
    }
    
    /* 
    private void paintBorder(int y, int x, Color color){
        squares[y][x].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(1,1,1,1),
            BorderFactory.createLineBorder(color, 3)
        ));
        squares[y][x].setBorderPainted(true);
    }

    */
    
}
