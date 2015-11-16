/* 
 * Creation : 13 avr. 2015
 */

import java.awt.Color;
import java.awt.Point;



/**
 * @date    13 avr. 2015
 * @author  Anthony CHAFFOT
 */
public class Line {
    private Color       color = Color.red;
    private int         size = 1;
    private Point       a;
    private Point       b;
    
    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public Line(Point a, Point b, int size, Color c){
        this.a      = a;
        this.b      = b;
        this.size   = size;
        this.color  = c;
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************

    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public Point getPointA(){
        return a;
    }
    
    public void setPointA(Point a){
        this.a = a;
    }
    
    public Point getPointB(){
        return b;
    }
    
    public void setPointB(Point b){
        this.b = b;
    }
}
