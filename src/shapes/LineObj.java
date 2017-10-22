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
public class LineObj extends ShapeObj {

    private double length;

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public LineObj(double x, double y, double length) {
        this.xCoord = x;
        this.yCoord = y;
        this.length = length;
    }

    public LineObj(double x, double y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    @Override
    public ShapeObj returnShape(double x, double y) {
        return new LineObj(x, y);
    }

}
