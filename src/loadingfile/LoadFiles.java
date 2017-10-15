/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loadingfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Mateusz
 */
public class LoadFiles {

    public static File myFile;
    public static int[] myResolution;
    public static int width;
    public static int height;
    public static double scale;
    public static int numberOfBytes;
    public static int newLines=4;
    public static int x=0;
    public static int y=0;
    public static int i=0;
    public static String mess;
    public static PixelWriter pwr;
    public static WritableImage wri;
    public static BufferedReader br;

    public static Image fetchHeader(File file) throws FileNotFoundException, IOException {
        x=0;
        y=0;
        i=0;
        myResolution = new int[2];
        myFile = file;
        newLines=4;
        Image image = null;
        br = new BufferedReader(new FileReader(file.getPath()));
        String headerType = br.readLine();
        if (!headerType.equals("P3") && !headerType.equals("P6")){
            return null;
        }
        //br.mark(100);
        String comment = br.readLine();
        String resolution;
        if (comment.contains("#")){
            boolean stillComment = true;
            resolution = br.readLine();
            while (stillComment) {
                if (resolution.isEmpty()){
                    resolution = br.readLine();
                    continue;
                }
                if (resolution.contains("#") || !resolution.isEmpty()) {
                    int substr = resolution.indexOf("#");
                    if (substr==-1 && myResolution[0]!=0 && myResolution[1]!=0){
                        stillComment=false;
                        break;
                    }
                    else if (substr==-1 && resolution.matches(".*\\d+.*")){
                        String[] values = resolution.split("\\s+");
                        int value=0;
                        for (int j=0; j<values.length; j++){
                            try {
                                value=Integer.parseInt(values[j]);
                                myResolution[i++]=value;
                              } catch (NumberFormatException e) {
                                //
                              }
                        }
                        if (value!=0){
                            //myResolution[i++]=value;
                            stillComment=false;
                            resolution=myResolution[0]+" "+myResolution[1];
                            break;
                        }
                    }
                    String chkchk = resolution.substring(0, substr);
                    String[] values = chkchk.split("\\s+");
                    try {
                        myResolution[i++]=Integer.parseInt(values[0]);
                      } catch (NumberFormatException e) {
                        i--;
                      }
                    newLines++;
                    resolution = br.readLine();
                } else {
                    stillComment=false;
                    break;
                }
            }
        }
        else{
            newLines--;
            resolution=comment;
        }
        String max="";
        String max2;
        boolean isMax=false;
        while (!isMax){
            max2=br.readLine();
            if (max2.isEmpty()){

            }
            else if (max2.matches(".*\\d+.*")){
                isMax=true;
                if (max2.contains("#")){
                    max2=max2.substring(0,max2.indexOf("#"));
                    max2=max2.replace("\t", "");
                    max2=max2.replace("\\s","");
                }
                scale = 255.0 / ((double)Integer.parseInt(max2.trim()));
                break;
            }
        }        
        String[] values = resolution.split(" ");
        width = Integer.parseInt(values[0]);
        height = Integer.parseInt(values[1]);
        numberOfBytes = Integer.parseInt(values[0]) * Integer.parseInt(values[1]);
        wri = new WritableImage((int) width, (int) height);
        pwr = wri.getPixelWriter();
        if (headerType.equals("P3")) {
            loadPPMP3FileNewLine();
        }
        if (headerType.equals("P6")) {
            loadPPMP6File();
        }
        return wri;
    }

    public static void loadPPMP3FileNewLine() throws FileNotFoundException, IOException {
        boolean test = true;
        int channel = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        while (test) {
            Color color;
            String bla = br.readLine();
            if (x==width && y==height){
                break;
            }
            if (bla == null) {
                test = false;
                break;
            }
            else if (bla.isEmpty() || bla.startsWith("#")){
                continue;
            }
            if (bla.length() > String.valueOf(scale).length()) {
                mess = bla;
                loadPPMP3SpaceFile();
                if (x==height && y==width)
                    break;
                else {
                    continue;
                }
            }
            int next = Integer.parseInt(bla);
            switch (channel) {
                case 0:
                    r = (int)(next*scale);
                    break;
                case 1:
                    g = (int)(next*scale);
                    break;
                case 2:
                    b = (int)(next*scale);
                    break;
            }
            channel = (channel + 1) % 3;
            if (channel == 0) {
                color = Color.rgb(r, g, b);
                r = 0;
                g = 0;
                b = 0;
                pwr.setColor(x++, y, color);
                if (x == width) {
                    x = 0;
                    y++;
                }
            }
        }
        return;
    }

    public static void loadPPMP3SpaceFile() throws IOException {
        int r = 0;
        int g = 0;
        int b = 0;
        int channel = 0;
        boolean exists = true;
        while (exists) {
            //br.reset();
            Color color;
            String test;
            if (mess!=null){
                test=mess;
            }
            else return;
            if (test == null) {
                exists = false;
                break;
            }
            else if (test.isEmpty() || test.startsWith("#")){
                continue;
            }
            else if (test.contains("#")){
                test = test.substring(0, test.indexOf("#"));
                if (test.contains("\t")){
                    test=test.replace("\t", " ");
                }
            }
            test = test.replaceFirst("^\\s*", "");
            String[] values = test.split("\\s+");
            int[] intValues = Arrays.asList(values).stream().mapToInt(Integer::parseInt).toArray();
            if (intValues.length == (width * 3) || intValues.length==3) {
                for (int i = 0; i < intValues.length; i++) {
                    int next = intValues[i];
                    switch (channel) {
                        case 0:
                            r = (int)(next*scale);
                            break;
                        case 1:
                            g = (int)(next*scale);
                            break;
                        case 2:
                            b = (int)(next*scale);
                            break;
                    }
                    channel = (channel + 1) % 3;
                    if (channel == 0) {
                        color = Color.rgb(r, g, b);
                        r = 0;
                        g = 0;
                        b = 0;
                        pwr.setColor(x++, y, color);
                        if (x == width) {
                            x = 0;
                            y++;
                        }
                    }
                }
            }
            mess=null;
        }
        return;
    }

    public static void loadPPMP6File() throws IOException {
        byte[] bytes = Files.readAllBytes(myFile.toPath());
        int countLines=0;
        boolean test = true;
        int channel = 0;
        int value = -1;
        int r = 0;
        int g = 0;
        int b = 0;
        int step = 0;
        boolean afterHeader=false;
        Color color;
        for (int z=0; z<bytes.length; z++){
            if (bytes[z]==0x0a && !afterHeader){
                if (countLines>=newLines){
                    continue;
                }
                countLines++;
            }
            else{
                if (countLines==newLines){
                    if (step==0){
                        if (height * width * 6 == bytes.length-z && scale > 1) {
                            step = 6;
                        }
                        else step=3;
                    }
                    afterHeader=true;
                    if (step==3){
                        value = bytes[z] & 0xff;
                    }
                    else {
                        value = bytes[z++] & 0xff;
                        value += bytes[z] & 0xff;
                    }
                    switch (channel) {
                        case 0:
                            r = (int)(value*scale);
                            break;
                        case 1:
                            g = (int)(value*scale);
                            break;
                        case 2:
                            b = (int)(value*scale);
                            break;
                    }
                    value=0;
                    channel = (channel + 1) % 3;
                    if (channel == 0) {
                        color = Color.rgb(r, g, b);
                        r = 0;
                        g = 0;
                        b = 0;
                        pwr.setColor(x++, y, color);
                        if (x == width) {
                            x = 0;
                            y++;
                        }
                    }
                }
            }
        }
        return;
    }
}
