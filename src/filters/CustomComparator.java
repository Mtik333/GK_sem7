/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.util.Comparator;

/**
 *
 * @author Mateusz
 */
public class CustomComparator implements Comparator<KuwaharArea> {
        private String color;
        public CustomComparator(String color){
            this.color=color;
        }
        @Override
        public int compare(KuwaharArea t, KuwaharArea t1) {
            int comparison=0;
            switch(this.color){
                case "RED":
                    if (t.getVarRed() < t1.getVarRed()) {
                        comparison=-1;
                    }
                    if (t.getVarRed() == t1.getVarRed()) {
                        comparison=0;
                    }
                    if (t.getVarRed() > t1.getVarRed()) {
                        comparison=1;
                    }
                    break;
                case "GREEN":
                    if (t.getVarGreen() < t1.getVarGreen()) {
                        comparison=-1;
                    }
                    if (t.getVarGreen() == t1.getVarGreen()) {
                        comparison=0;
                    }
                    if (t.getVarGreen() > t1.getVarGreen()) {
                        comparison=1;
                    }
                    break;
                case "BLUE":
                    if (t.getVarBlue() < t1.getVarBlue()) {
                        comparison=-1;
                    }
                    if (t.getVarBlue() == t1.getVarBlue()) {
                        comparison=0;
                    }
                    if (t.getVarBlue() > t1.getVarBlue()) {
                        comparison=1;
                    }
                    break;
            }
            /*
            if (t.getVarRed() < t1.getVarRed()) {
                return -1;
            }
            if (t.getVarRed() == t1.getVarRed()) {
                return 0;
            }
            if (t.getVarRed() > t1.getVarRed()) {
                return 1;
            }
            */
            return comparison;
        }
        
    }
