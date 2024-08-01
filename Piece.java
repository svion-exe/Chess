package Piece;

import Main.ChessBoard;
import Main.GamePanel;
import Main.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Piece {

    public Type type ;
    public BufferedImage image ;
    public int x,y ;
    public int col , row , preRow , preCol ;
    public int color ;
    public Piece hittingPiece ;
    public boolean moved , twoStepped ;

    public Piece(int color , int row , int col )
    {
        this.row = row ;
        this.col = col ;
        this.color = color ;
        x = getX(col) ;
        y = getY(row) ;
        preCol = col ;
        preRow = row ;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png"))); // Add ".png" extension
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public int getX(int col){
        return col * ChessBoard.SQUARE_SIZE  ;
    }
    public int getY(int row){
        return row * ChessBoard.SQUARE_SIZE ;
    }
    public int getCol(int x){
        return (x+ ChessBoard.HALF_SIZE) / ChessBoard.SQUARE_SIZE ;
    }
    public int getRow(int y){
        return (y + ChessBoard.HALF_SIZE) / ChessBoard.SQUARE_SIZE ;
    }

    public int getIndex(){
        for (int index =0  ; index < GamePanel.simPieces.size() ; index++){
            if(GamePanel.simPieces.get(index)==this){
                return index ;
            }
        }
        return 0 ;
    }
    public void updatePosition(){

        // for en-passant
        if(type == Type.PAWN){
            if(Math.abs(row - preRow)==2){
                twoStepped = true ;
            }
        }
        x=getX(col) ;
        y=getY(row) ;
        preCol = getCol(x) ;
        preRow = getRow(y) ;
        moved = true ;
  }

  public void resetPosition(){
      col = preCol ;
      row = preRow ;
      x = getX(col) ;
      y = getY(row) ;
  }
  public boolean canMove(int targetCol, int targetRow){
      return false;
  }// this method needs to be overloaded as per piece requirement

  public boolean isWithinBoard(int targetCol, int targetRow){
      return targetCol >= 0 && targetCol < 8 && targetRow >= 0 && targetRow < 8;
  }

  public boolean isSameSquare(int targetCol, int targetRow){
        if(targetCol == preCol && targetRow == preRow ){
            return true;
        }
        return false;
  }

  public Piece getHittingPiece(int targetCol ,int  targetRow){

        for(Piece piece : GamePanel.simPieces){
            if (piece.col == targetCol && piece.row == targetRow && piece != this){
                return piece;
            }
        }
        return null;
  }

  public boolean isValidSquare(int targetCol, int targetRow){
        hittingPiece = getHittingPiece(targetCol,targetRow);
        if (hittingPiece == null){   // Not pre-Occupied
            return true ;
        }
        else{ // pre -Occupied
            if(hittingPiece.color != this.color){  // Pieces "Not"  of same color
                return true ;
            }
            else {
                return false ;
            }
        }
  }
  public boolean pieceIsOnStraightLine(int targetCol, int targetRow){

        // moves to left
        for(int c = preCol-1 ; c > targetCol ; c--){
            for(Piece piece : GamePanel.simPieces){
                if(piece.col == c && piece.row == targetRow){
                    hittingPiece = piece;
                    return true ;
                }
            }
        }
        //moves to right
      for(int c = preCol+1 ; c < targetCol ; c++){
          for(Piece piece : GamePanel.simPieces){
              if(piece.col == c && piece.row == targetRow){
                  hittingPiece = piece;
                  return true ;
              }
          }
      }

        //moves up
      for(int r = preRow-1 ; r >targetRow ; r--){
          for(Piece piece : GamePanel.simPieces){
              if(piece.col == targetCol && piece.row == r ){
                  hittingPiece = piece;
                  return true ;
              }
          }
      }

      //moves down
      for(int r = preRow+1 ; r < targetRow ; r++){
          for(Piece piece : GamePanel.simPieces){
              if(piece.col == targetCol && piece.row == r ){
                  hittingPiece = piece;
                  return true ;
              }
          }
      }

      return false;
  }

  public boolean pieceIsOnDiagonalPath(int targetCol, int targetRow){
        if(targetRow < preRow){

            //upLeft
            for (int c = preCol-1 ; c > targetCol ; c--){
                int diff = Math.abs(c-preCol);
                 for(Piece piece : GamePanel.simPieces){
                     if(piece.col == c && piece.row == preRow-diff){
                         hittingPiece = piece;
                         return true ;
                     }
                 }
            }
            //upright
            for (int c = preCol+1 ; c < targetCol ; c++){
                int diff = Math.abs(c-preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow-diff){
                        hittingPiece = piece;
                        return true ;
                    }
                }
            }

        }

        if(targetRow > preRow){

            //Down left
            for (int c = preCol-1 ; c > targetCol ; c--){
                int diff = Math.abs(c-preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow+diff){
                        hittingPiece = piece;
                        return true ;
                    }
                }
            }
            //Down Right
            for (int c = preCol+1 ; c < targetCol ; c++){
                int diff = Math.abs(c-preCol);
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow+diff){
                        hittingPiece = piece;
                        return true ;
                    }
                }
            }

        }
        return false;
  }
    public void draw(Graphics2D g2d){
        g2d.drawImage(image,x,y,ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE , null);
    }
}


