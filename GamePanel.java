package Main;

import Piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel  implements Runnable{
    public static final int WIDTH = 1100 ;
    public static final int HEIGHT = 800 ;
    public static Piece castlingPiece;

    final int fps = 60 ;
    Thread gameThread ;
    ChessBoard board = new ChessBoard();
    Mouse mouse = new Mouse();

    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static  ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece activePiece, checkingPiece;


    //COLOR
    public static final  int WHITE = 0 ;
    public static final  int BLACK = 1 ;
    int currentColor = WHITE ;

    boolean canMove ;
    boolean validSquare ;
    boolean promotion ;
    boolean gameOver ;
    boolean stalemate ;

    public GamePanel() {


        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);



        setPieces() ;
        copyPieces(pieces , simPieces );
    }
    public void launchGame(){
        gameThread = new Thread(this) ;
        gameThread.start();
    }

    public void setPieces() {
        // White (South)
//        pieces.add(new Pawn(WHITE, 6, 0));
//        pieces.add(new Pawn(WHITE, 6, 1));
//        pieces.add(new Pawn(WHITE, 6, 2));
//        pieces.add(new Pawn(WHITE, 6, 3));
//        pieces.add(new Pawn(WHITE, 6, 4));
//        pieces.add(new Pawn(WHITE, 6, 5));
//        pieces.add(new Pawn(WHITE, 6, 6));
//        pieces.add(new Pawn(WHITE, 6, 7));
       pieces.add(new Rook(WHITE, 7, 0));
        pieces.add(new Rook(WHITE, 7, 7));
//       pieces.add(new Knight(WHITE, 7, 1));
//        pieces.add(new Knight(WHITE, 7, 6));
//        pieces.add(new Bishop(WHITE, 7, 2));
//        pieces.add(new Bishop(WHITE, 7, 5));
//        pieces.add(new Queen(WHITE, 7, 3));
        pieces.add(new King(WHITE, 7, 4));

        // Black (North)
//        pieces.add(new Pawn(BLACK, 1, 1));
//        pieces.add(new Pawn(BLACK, 1, 2));
//        pieces.add(new Pawn(BLACK, 1, 3));
//        pieces.add(new Pawn(BLACK, 1, 4));
//        pieces.add(new Pawn(BLACK, 1, 5));
//        pieces.add(new Pawn(BLACK, 1, 6));
//        pieces.add(new Pawn(BLACK, 1, 7));
//        pieces.add(new Pawn(BLACK, 1, 0));
//        pieces.add(new Rook(BLACK, 0, 0));
//        pieces.add(new Rook(BLACK, 0, 7));
//        pieces.add(new Knight(BLACK, 0, 1));
//        pieces.add(new Knight(BLACK, 0, 6));
//        pieces.add(new Bishop(BLACK, 0, 2));
//        pieces.add(new Bishop(BLACK, 0, 5));
//        pieces.add(new Queen(BLACK, 0, 3));
//        pieces.add(new King(BLACK, 0, 4));
    }


    private void copyPieces (ArrayList<Piece> source , ArrayList<Piece> target){
        target.clear() ;
        for(int i = 0 ; i < source.size() ; i++){
            target.add(source.get(i));
        }
    }

    //void testIllegal() {

       // pieces.add(new King(WHITE, 4, 2));
        //pieces.add(new King(BLACK, 3, 0));
        //pieces.add(new Queen(WHITE ,1,2)) ;
    //}
    @Override
    public void run() {
        //Game Loop

        double  drawInterval = 1000000000 / fps ;
        double delta = 0;
        long lastTime = System.nanoTime() ;
        long currentTime ;

        while (gameThread !=null ){
            currentTime = System.nanoTime() ;

            delta += (currentTime - lastTime) / drawInterval ;
            lastTime = currentTime ;

            if (delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){

        if(promotion){
            promoting() ;
        }
        else if(!gameOver && !stalemate){
            if(mouse.pressed){
                if(activePiece == null){
                    for (Piece piece : pieces){
                        if(piece.color == currentColor
                                && piece.col==mouse.x/ChessBoard.SQUARE_SIZE &&
                                piece.row==mouse.y/ChessBoard.SQUARE_SIZE){

                            activePiece = piece;
                        }
                    }
                }
                else {
                    simulate() ;
                }
            }
            if(!mouse.pressed){
                if(activePiece != null){
                    if(validSquare) {

                        //move confirmed

                        copyPieces(simPieces, pieces);
                        activePiece.updatePosition();
                        if (castlingPiece != null) {
                            castlingPiece.updatePosition();
                        }

                        if (isKingInCheck() && isCheckMate()) {
                            gameOver = true ;
                        } else if (isStalemate()) {
                            stalemate = true ;
                        }
                        else {
                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changePlayer();
                            }
                        }

                    }
                    else {
                        copyPieces(pieces,simPieces);
                        activePiece.resetPosition();
                        activePiece = null ;
                    }
                }
            }
        }

    }

    private void simulate(){

        canMove = false;
        validSquare = false ;

        copyPieces(pieces , simPieces );

        if(castlingPiece!=null){
            castlingPiece.col = castlingPiece.preCol ;
            castlingPiece.x = castlingPiece.getX(castlingPiece.col) ;
            castlingPiece = null ;
        }
        activePiece.x =mouse.x - ChessBoard.HALF_SIZE;
        activePiece.y = mouse.y - ChessBoard.HALF_SIZE;
        activePiece.col = activePiece.getCol(activePiece.x) ;
        activePiece.row = activePiece.getRow(activePiece.y) ;

        if(activePiece.canMove(activePiece.col , activePiece.row)){
            canMove = true ;
            if(activePiece.hittingPiece != null ){
                simPieces.remove(activePiece.hittingPiece.getIndex());
            }
            checkCastling();
            if (!isIllegal(activePiece)&& !opponentCanCaptureKing()){
                validSquare = true ;
            }

        }
    }

    private boolean isIllegal(Piece king ){

        if(king.type == Type.KING){
            for(Piece piece : simPieces){
                if(piece != king  && piece.color != king.color && piece.canMove(king.col , king.row)) {
                    return true;
                }
            }
        }
         return false ;
    }
    private boolean opponentCanCaptureKing(){
        Piece king = getKing(false) ;
        for (Piece piece : simPieces){
            if(piece.color!=king.color && piece.canMove(king.col , king.row )){
                return true;
            }
        }

        return false ;
    }

    private boolean isKingInCheck(){
        Piece king = getKing (true) ;

        if(activePiece.canMove(king.col , king.row)){
            checkingPiece = activePiece ;
            return true ;
        }

        else {
            checkingPiece = null ;
        }

        return false ;
    }
    private Piece getKing (boolean opponent ){
        Piece king = null ;

        for (Piece piece : simPieces){
            if(opponent){
                if(piece.type == Type.KING && piece.color != currentColor){
                    king = piece ;
                }
            }
            else if (piece.type == Type.KING && piece.color == currentColor){
                king = piece ;
            }
        }
        return king ;
    }

    private boolean isCheckMate( ) {
        Piece king = getKing (true) ;
        if(kingcanMove(king)){
            return false ;
        }
        else{
            //Check if attack can be blocked by piece
            //Check kings and attack pieces position
            int colDiff = Math.abs(checkingPiece.col - king.col );
            int rowDiff = Math.abs(checkingPiece.row - king.row );

            if(colDiff == 0 ){//vertical attack
                if(checkingPiece.row < king.row){ //above king
                    for(int row = checkingPiece.row ; row < king.row ; row++){
                        for(Piece piece : simPieces){
                            if(piece !=king && piece.color != currentColor && piece.canMove(checkingPiece.col , row)){
                                return false ;
                            }
                        }
                    }
                }
                if(checkingPiece.row > king.row){ //below king
                    for(int row = checkingPiece.row ; row > king.row ; row--){
                        for(Piece piece : simPieces){
                            if(piece !=king && piece.color != currentColor && piece.canMove(checkingPiece.col , row)){
                                return false ;
                            }
                        }
                    }
                }
            }
            else if(rowDiff== 0 ){//horizontal attack
                if(checkingPiece.col < king.col){ //left
                    for(int col = checkingPiece.row ; col < king.row ; col++){
                        for(Piece piece : simPieces){
                            if(piece !=king && piece.color != currentColor && piece.canMove( col , checkingPiece.row)){
                                return false ;
                            }
                        }
                    }
                }
                if(checkingPiece.col > king.col){//right
                    for(int col = checkingPiece.row ; col > king.row ; col--){
                        for(Piece piece : simPieces){
                            if(piece !=king && piece.color != currentColor && piece.canMove( col , checkingPiece.row)){
                                return false ;
                            }
                        }
                    }
                }
            }

            else if (rowDiff==colDiff) {//diagonal attack

                if(checkingPiece.row < king.row){           //Above the king
                    if(checkingPiece.col < king.col){       // upper Left
                        for(int col = checkingPiece.col , row = checkingPiece.row ; col < king.row ; col++, row++){
                            for(Piece piece : simPieces){
                                if(piece!=king && piece.color != currentColor && piece.canMove(col , row)){
                                    return false ;
                                }
                            }
                        }
                    }
                    if(checkingPiece.row > king.row){       //upper right
                        for(int col = checkingPiece.col , row = checkingPiece.row ; col > king.row ; col--, row++){
                            for(Piece piece : simPieces){
                                if(piece!=king && piece.color != currentColor && piece.canMove(col , row)){
                                    return false ;
                                }
                            }
                        }
                    }
                }
                if(checkingPiece.row > king.row){           //Below the king
                    if (checkingPiece.col < king.col){         //lower left
                        for(int col = checkingPiece.col , row = checkingPiece.row ; col < king.row ; col++, row--){
                            for(Piece piece : simPieces){
                                if(piece!=king && piece.color != currentColor && piece.canMove(col , row)){
                                    return false ;
                                }
                            }
                        }
                    }
                    if(checkingPiece.row > king.row){       //lower right
                        for(int col = checkingPiece.col , row = checkingPiece.row ; col > king.row ; col--, row--){
                            for(Piece piece : simPieces){
                                if(piece!=king && piece.color != currentColor && piece.canMove(col , row)){
                                    return false ;
                                }
                            }
                        }
                    }
                }

            }
            else{//attacking piece is knight

            }
        }
        return true ;
    }
    private boolean kingcanMove(Piece king ) {

        if(isValidMove(king , -1 , -1 )) {return true ;}
        if(isValidMove(king , 0 , -1)){return true ;}
        if(isValidMove(king , 1 , -1)){return true ;}
        if(isValidMove(king , -1 , 0)){return true ;}
        if(isValidMove(king , 1, 0)){return true ;}
        if(isValidMove(king , -1 , 1)){return true ;}
        if(isValidMove(king , 0 , 1)){return true ;}
        if(isValidMove(king , 1 , 1)){return true ;}

        return false ;
    }
    private boolean isValidMove(Piece king , int colPlus , int rowPlus) {
        boolean isValidMove = false;

        king.col += colPlus ;
        king.row += rowPlus ;

        if(king.canMove(king.col , king.row)){
            if(king.hittingPiece != null){
                simPieces.remove(king.hittingPiece.getIndex());
            }
            if (!isIllegal(king)){
                isValidMove = true ;
            }
        }

        king.resetPosition();
        copyPieces(pieces , simPieces);

        return isValidMove ;
    }

    private boolean isStalemate () {
        int count = 0 ;

        for(Piece piece : simPieces){
            if(piece.color != currentColor){
                count++ ;
            }
        }
        if(count ==1 ){
            if(!kingcanMove(getKing(true))) {
                return true ;
            }
        }

        return false ;
    }

    public void checkCastling(){
        if(castlingPiece!=null){
            if(castlingPiece.col == 0 ){
                castlingPiece.col +=3 ;
            }
            else if(castlingPiece.col == 7 ){
                castlingPiece.col -=2 ;
            }
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
        }
    }
    public void changePlayer(){
        if(currentColor == BLACK){
            currentColor = WHITE ;
            for(Piece piece : pieces){
                if(piece.color == WHITE){
                    piece.twoStepped = false ;
                }
            }

        }
        else{
            currentColor = BLACK ;
            for(Piece piece : pieces){
                if(piece.color == BLACK){
                    piece.twoStepped = false ;
                }
            }
        }
        activePiece = null;
    }

    public boolean canPromote (){
        if(activePiece.type== Type.PAWN){
            if((currentColor == WHITE && activePiece.row == 0) || (currentColor == BLACK && activePiece.row == 7)){
                promotionPieces.clear();
                promotionPieces.add(new Rook(currentColor, 2, 9));
                promotionPieces.add(new Knight(currentColor, 3, 9));
                promotionPieces.add(new Bishop(currentColor, 4, 9));
                promotionPieces.add(new Queen(currentColor, 5, 9));
                return true ;
            }
        }
        return false ;
    }

    public void promoting(){
        if(mouse.pressed){
            for(Piece piece : promotionPieces){
                if(piece.col == mouse.x/ChessBoard.SQUARE_SIZE && piece.row == mouse.y/ChessBoard.SQUARE_SIZE){
                    switch(piece.type){
                        case ROOK:simPieces.add(new Rook(currentColor, activePiece.row, activePiece.col));
                            break;
                        case KNIGHT: simPieces.add(new Knight(currentColor, activePiece.row, activePiece.col));
                                break;
                        case QUEEN:
                            simPieces.add(new Queen(currentColor, activePiece.row, activePiece.col));
                                break;
                        case BISHOP:
                            simPieces.add(new Bishop(currentColor, activePiece.row, activePiece.col));
                            break ;
                        default:
                            break;
                    }

                    simPieces.remove(activePiece.getIndex());
                    copyPieces( simPieces , pieces);
                    activePiece = null ;
                    promotion = false ;
                    changePlayer();
                }
            }
        }
    }
    //paint component is a method in JComponent that JPanel inherits and is used to draw objects to the panel .
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        board.draw(g2d);

        for (Piece p : simPieces){
            p.draw(g2d) ;
        }

        if (activePiece != null){

            if(canMove) {
                if (isIllegal(activePiece)&& opponentCanCaptureKing()){
                    g2d.setColor(Color.red);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
                    g2d.fillRect(activePiece.col * ChessBoard.SQUARE_SIZE, activePiece.row * ChessBoard.SQUARE_SIZE,
                            ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                }
                else {
                    g2d.setColor(Color.white);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
                    g2d.fillRect(activePiece.col * ChessBoard.SQUARE_SIZE, activePiece.row * ChessBoard.SQUARE_SIZE,
                            ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                }

            }
            activePiece.draw(g2d);
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING ,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Book Antiqua",Font.BOLD,40));
        g2d.setColor(Color.WHITE);

        if(promotion){
            g2d.drawString("Promote to " , 840  , 150);

            for(Piece piece : promotionPieces){
                g2d.drawImage(piece.image , piece.getX(piece.col) ,piece.getY(piece.row) ,
                        ChessBoard.SQUARE_SIZE , ChessBoard.SQUARE_SIZE ,null);
            }
        }
        else {
            if(currentColor == WHITE){
                g2d.drawString("White's Turn" , 840 ,550) ;
                if(checkingPiece!=null && checkingPiece.color == BLACK){
                    g2d.setColor(Color.red);
                    g2d.drawString("The King " , 840 , 650 );
                    g2d.drawString("is in Check ", 840 , 700 );
                }
            }
            else{
                g2d.drawString("Black's Turn " , 840 ,250) ;
                if(checkingPiece!=null && checkingPiece.color == WHITE){
                    g2d.setColor(Color.red);
                    g2d.drawString("The King " , 840 , 100 );
                    g2d.drawString("is in Check ", 840 , 150 );
                }
            }
        }
    if(gameOver){
        String s = " " ;
        if(currentColor == WHITE){
            s= "White wins " ;
        }
        else{
            s= "Black wins " ;
        }
        g2d.setFont(new Font("Arial" , Font.PLAIN , 90 ));
        g2d.setColor(Color.GREEN);
        g2d.drawString(s, 200  , 420);
    }
    if(stalemate){
        g2d.setFont(new Font("Arial" , Font.PLAIN , 90 ));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Stalemate", 200  , 420);
    }
    }

}
