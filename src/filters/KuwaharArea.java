/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

/**
 *
 * @author Mateusz
 */
public class KuwaharArea {

    public int startX;
    public int startY;
    public int avgRed;
    public int avgGreen;
    public int avgBlue;
    public double varRed;
    public double varGreen;
    public double varBlue;

    public KuwaharArea(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getAvgRed() {
        return avgRed;
    }

    public void setAvgRed(int avgRed) {
        this.avgRed = avgRed;
    }

    public int getAvgGreen() {
        return avgGreen;
    }

    public void setAvgGreen(int avgGreen) {
        this.avgGreen = avgGreen;
    }

    public int getAvgBlue() {
        return avgBlue;
    }

    public void setAvgBlue(int avgBlue) {
        this.avgBlue = avgBlue;
    }

    public double getVarRed() {
        return varRed;
    }

    public void setVarRed(double varRed) {
        this.varRed = varRed;
    }

    public double getVarGreen() {
        return varGreen;
    }

    public void setVarGreen(double varGreen) {
        this.varGreen = varGreen;
    }

    public double getVarBlue() {
        return varBlue;
    }

    public void setVarBlue(double varBlue) {
        this.varBlue = varBlue;
    }

}
