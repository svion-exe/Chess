package Piece;

import Main.GamePanel;

public class Knight extends Piece{
    public Knight(int color , int row , int col){
        super(color, row, col);

        if (color == GamePanel.WHITE){
            image = getImage("/piece/w-knight");
        }
        else{
            image = getImage("/piece/b-knight");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)){
            if(Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow) ==  2 ){
                if(isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
