/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loadingfile;

import data.DataAccessor;
import java.util.Map;

/**
 *
 * @author Mateusz
 */
public class Convertion {
    public static void convertToCMYK(){
        double r=DataAccessor.getRgbValues().get("r")/255.0f;
        double g=DataAccessor.getRgbValues().get("g")/255.0f;
        double b=DataAccessor.getRgbValues().get("b")/255.0f;
        double k = 1-Math.max(r,Math.max(g,b));
        double c = (1-r-k)/(1-k);
        double m = (1-g-k)/(1-k);
        double y = (1-b-k)/(1-k);
        DataAccessor.getCmykValues().replace("c", (int) (c*100));
        DataAccessor.getCmykValues().replace("m", (int) (m*100));
        DataAccessor.getCmykValues().replace("y", (int) (y*100));
        DataAccessor.getCmykValues().replace("k", (int) (k*100));
        System.out.println("xd");
    }
    
    public static void convertToRGB(){
        double c=DataAccessor.getCmykValues().get("c")/100.0f;
        double m=DataAccessor.getCmykValues().get("m")/100.0f;
        double y=DataAccessor.getCmykValues().get("y")/100.0f;
        double k=DataAccessor.getCmykValues().get("k")/100.0f;
        double r=(1-Math.min(1,c*(1-k)+k))*255;
        double g=(1-Math.min(1,m*(1-k)+k))*255;
        double b=(1-Math.min(1,y*(1-k)+k))*255;
        DataAccessor.getRgbValues().replace("r", (int) r);
        DataAccessor.getRgbValues().replace("g", (int) g);
        DataAccessor.getRgbValues().replace("b", (int) b);
        System.out.println("xd");
    }
}
