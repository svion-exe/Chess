package Piece;

import Main.GamePanel;
import Main.Type;

public class Rook extends Piece {
    public Rook(int color , int row ,int col){
        super(color ,row ,col) ;

        type = Type.ROOK ;

        if (color == GamePanel.WHITE){
            image = getImage("/piece/w-rook");
        }
        else{
            image = getImage("/piece/b-rook");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)&& !isSameSquare(targetCol, targetRow)){
            if(targetCol == preCol|| targetRow == preRow){
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
