package Piece;

import Main.GamePanel;
import Main.Type;

public class Queen extends Piece {
    public Queen(int color ,int row ,int col) {
        super(color, row, col);

        type = Type.QUEEN ;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-queen");
        }
        else{
            image = getImage("/piece/b-queen");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {

            //vertical Movement
            if(targetCol == preCol|| targetRow == preRow){
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }

            //Diagonal Movement
            if(Math.abs(targetCol-preCol) == Math.abs(targetRow-preRow)){
                if(isValidSquare(targetCol, targetRow)&& !pieceIsOnDiagonalPath(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false ;
    }
}
