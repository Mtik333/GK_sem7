/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transformations;

import data.DataAccessor;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Mateusz
 */
public class Transformations {
    
    public void translatePointsByVector(double x, double y){
        for (int i=0; i<DataAccessor.getPolygon().getPoints().size(); i++){
            Double point=DataAccessor.getPolygon().getPoints().get(i);
            if (i%2==0){
                point=point+x;
            } else{
                point=point+y;
            }
            DataAccessor.getPolygon().getPoints().set(i, point);
        }
        for (int i=0; i<DataAccessor.getPolygon().getPoints().size(); i++){
            System.out.println(DataAccessor.getPolygon().getPoints().get(i));
        }
    }
    
    public void rotateByPointAndAngle(double x, double y, double angle){
        double radian=angle*Math.PI/180.0;
        System.out.println(radian);
        Rotate rotate = new Rotate(angle, x, y);
        double[] points = DataAccessor.getPolygon().getPoints().stream().mapToDouble(Number::doubleValue).toArray();
        rotate.transform2DPoints(points, 0, points, 0, points.length/2);
        DataAccessor.getPolygon().getPoints().clear();
        DataAccessor.getPolygon().getPoints().addAll(DoubleStream.of(points).boxed().collect(Collectors.toList()));
        for (int i=0; i<DataAccessor.getPolygon().getPoints().size(); i++){
            System.out.println(DataAccessor.getPolygon().getPoints().get(i));
        }
    }
    
    public void scaleByPointAndFactor(double x, double y, double coeff){
        for (int i=0; i<DataAccessor.getPolygon().getPoints().size(); i++){
            Double point=DataAccessor.getPolygon().getPoints().get(i);
            if (i%2==0){
                point=point*coeff+(1-coeff)*x;
            } else{
                point=point*coeff+(1-coeff)*y;
            }
            DataAccessor.getPolygon().getPoints().set(i, point);
        }
        for (int i=0; i<DataAccessor.getPolygon().getPoints().size(); i++){
            System.out.println(DataAccessor.getPolygon().getPoints().get(i));
        }
    }
}
