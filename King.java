package Piece;

import Main.GamePanel;
import Main.Type;

public class King extends Piece {
    public King(int color ,int row, int col) {
        super(color, row, col);

        type = Type.KING ;
        if (color == GamePanel.WHITE){
            image = getImage("/piece/w-king");
        }
        else{
            image = getImage("/piece/b-king");
        }
    }
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)){
            if (Math.abs(targetCol-preCol)+Math.abs(targetRow-preRow)==1||
                    Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow)==1){
                if(isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }

            //CASTLING
            if(moved == false ){

                //right Castling
                if(targetCol == preCol+2 && targetRow == preRow && !pieceIsOnStraightLine(targetCol, targetRow)){
                    for(Piece piece : GamePanel.simPieces){
                        if(piece.col == preCol+3 && piece.row == preRow && !piece.moved){
                            GamePanel.castlingPiece = piece ;
                            return true ;

                        }
                    }
                }

                //left Castling
                if(targetCol == preCol-2 && targetRow == preRow && !pieceIsOnStraightLine(targetCol, targetRow)){
                    Piece p[] = new Piece[2] ;

                    for(Piece piece : GamePanel.simPieces){
                        if(piece.col == preCol -3 && piece.row == targetRow ){
                            p[0] = piece ;
                        }
                        if (piece.col == preCol - 4  && piece.row == targetRow){
                            p[1] = piece ;
                        }

                        if(p[0] == null && p[1]!=null && !p[1].moved){
                            GamePanel.castlingPiece = p[1] ;
                            return true ;
                        }
                    }
                }
            }
        }
        return false ;
    }
}
