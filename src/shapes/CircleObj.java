/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author Mateusz
 */
public class CircleObj extends ShapeObj{
    private double radius;

    public CircleObj(double x, double y){
        this.xCoord=x;
        this.yCoord=y;
    }
    
    public CircleObj(double x, double y, double radius){
        this.xCoord=x;
        this.yCoord=y;
        this.radius=radius;
    }
    
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public ShapeObj returnShape(double x, double y) {
        return new CircleObj(x,y);
    }
    
}
