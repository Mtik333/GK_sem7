/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import data.DataAccessor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;

/**
 *
 * @author Mateusz
 */
public class BezierCurve {
    
    public void calculateBezierCurve(){
        double i=0;
        List<Circle> bezierPoints = new ArrayList<>();
        List<Circle> controlPoints = DataAccessor.getControlPoints();
        if (controlPoints.size()>2){
            int pointsNumber = controlPoints.size();
            int n=pointsNumber-1;
            for (i=0; i<=1; i=i+0.001){
                double valueX=0;
                double valueY=0;
                for (int j=0; j<pointsNumber; j++){
                    valueX+=controlPoints.get(j).getCenterX()*bernsteinCoeff(i, n, j);
                    valueY+=controlPoints.get(j).getCenterY()*bernsteinCoeff(i, n, j);
                }
                Circle circle = new Circle(valueX, valueY, 2);
                bezierPoints.add(circle);
            }
        }
        DataAccessor.setBezierPoints(bezierPoints);
    }
    
    private long binomialCoeff(long n, long k){
        long result = 1;
        if (k==0){
            return result;
        }
        else {
            for (int i=1; i<=k; i++){
                result=result*(n-i+1)/i;
            }
            return result;
        }
    }
    
    private double bernsteinCoeff(double step, int n, int k){
        double newton=binomialCoeff(n, k);
        double value=newton*Math.pow(step,n-k)*Math.pow(1-step,k);
        return value;
    }
}
