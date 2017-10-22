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
public class RectangleObj extends ShapeObj {

    private double height;
    private double width;

    public RectangleObj(double x, double y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    public RectangleObj(double x, double y, double height, double width) {
        this.xCoord = x;
        this.yCoord = y;
        this.height = height;
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public ShapeObj returnShape(double x, double y) {
        return new RectangleObj(x, y);
    }
}
