/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loadingfile;

import java.util.Map;

/**
 *
 * @author Mateusz
 */
public class Convertion {
    public static Map<String,Integer> rgbValues;
    public static Map<String,Integer> cmykValues;
    
    public static void convertToCMYK(){
//        double r=rgbValues.get("r")/255.0f;
//        double g=rgbValues.get("g")/255.0f;
//        double b=rgbValues.get("b")/255.0f;
        
        double r=21/255.0f;
        double g=40/255.0f;
        double b=23/255.0f;
        
        double k = 1-Math.max(r,Math.max(g,b));
        double c = (1-r-k)/(1-k);
        double m = (1-g-k)/(1-k);
        double y = (1-b-k)/(1-k);
        System.out.println("xd");
    }
    
    public static void convertToRGB(){
//        double c=rgbValues.get("c")/255.0f;
//        double m=rgbValues.get("m")/255.0f;
//        double y=rgbValues.get("y")/255.0f;
//        double k=rgbValues.get("k")/255.0f;

        double c=19/40.0f;
        double m=0/40.0f;
        double y=18/40.0f;
        double k=215/255.0f;
        
        double r=(1-Math.min(1,c*(1-k)+k))*255;
        double g=(1-Math.min(1,m*(1-k)+k))*255;
        double b=(1-Math.min(1,y*(1-k)+k))*255;
        
        System.out.println("xd");
    }
}
