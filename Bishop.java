package Piece;

import Main.GamePanel;
import Main.Type;

public class Bishop extends Piece{
    public Bishop(int color , int row , int col){
        super(color, row, col);

        type = Type.BISHOP ;

        if (color == GamePanel.WHITE){
            image = getImage("/piece/w-bishop");
        }
        else{
            image = getImage("/piece/b-bishop");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            if(Math.abs(targetCol-preCol) == Math.abs(targetRow-preRow)){
                if(isValidSquare(targetCol, targetRow)&& !pieceIsOnDiagonalPath(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
