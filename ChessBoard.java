package Main;

import java.awt.*;

public class ChessBoard {
    final int  MAX_COLUMNS = 8;
    final int  MAX_ROWS = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SIZE =SQUARE_SIZE/2;

    public void draw(Graphics2D g){

        int c = 0 ;
        for(int row = 0; row < MAX_ROWS; row++){
            for(int col = 0; col < MAX_COLUMNS; col++){
                if (c==0) {
                    g.setColor(new Color(175,115,70));
                    c=1 ;
                }
                else {
                    g.setColor(new Color(210,165,125) );
                    c=0 ;
                }
                //g.fillRect(int x , int y ,int  width , int height);
                g.fillRect(col*SQUARE_SIZE , row*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            if(c==0){
                c=1;
            }
            else {
                c=0 ;
            }
        }
    }

}
