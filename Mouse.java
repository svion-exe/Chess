package Main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    public int x  , y ;//x , y are coordinates of mouse movements on the surface ...
    public boolean pressed ; // indiactes if the mouse button is pressed or not ...

    @Override
    public void mousePressed(MouseEvent e) {
        // sets pressed to true if mouse is pressed
        pressed=true;
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // sets pressed to false if mouse is pressed
        pressed=false;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        // updates mouse position when dragged
        // this works when button is pressed and mouse is moved ..
        x=e.getX();
        y=e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //updates mouse position when mouse is moved
        // this works when button isnt pressed and mouse is moved ..
        x=e.getX();
        y=e.getY();
    }
}
