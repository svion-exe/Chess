package Piece;

import Main.GamePanel;
import Main.Type;

public class Pawn extends Piece {
    public Pawn(int color, int row, int col) {
        super(color, row, col);

        type = Type.PAWN ;

        if (color == GamePanel.WHITE){
            image = getImage("/piece/w-pawn");
        }
        else {
            image = getImage("/piece/b-pawn");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow) ){

            int moveValue ;

            if(color == GamePanel.WHITE){
                moveValue = -1;
            }
            else{
                moveValue = 1;
            }

            hittingPiece = getHittingPiece(targetCol, targetRow);

            if(targetCol == preCol && targetRow == preRow+moveValue && hittingPiece == null ){
                return true ;
            }

            if(targetCol == preCol && targetRow == preRow+moveValue*2 && hittingPiece == null && !moved &&
                    !pieceIsOnStraightLine(targetCol,targetRow)){
                return true ;
            }

            if (Math.abs(targetCol-preCol)==1 && targetRow == preRow+moveValue && hittingPiece != null &&
                  hittingPiece.color != color){
                return true ;
            }
            if(Math.abs(targetCol-preCol) == 1 && targetRow == preRow+ moveValue ){
                for(Piece piece : GamePanel.pieces){
                    if(piece.col == targetCol && piece.row == preRow && piece.twoStepped == true ){
                        hittingPiece = piece ;
                        return true ;
                    }
                }
            }
        }

        return false ;
    }
}
