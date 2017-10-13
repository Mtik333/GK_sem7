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
    public static int width;
    public static int height;
    public static double scale;
    public static int numberOfBytes;
    public static int newLines=4;

    public static Image fetchHeader(File file) throws FileNotFoundException, IOException {
        myFile = file;
        newLines=4;
        Image image = null;
        BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
        String headerType = br.readLine();
        if (!headerType.equals("P3") && !headerType.equals("P6")){
            return null;
        }
        String comment = br.readLine();
        boolean stillComment = true;
        String resolution = br.readLine();
        while (stillComment) {
            if (resolution.contains("#")) {
                newLines++;
                resolution = br.readLine();
            } else {
                stillComment=false;
                break;
            }
        }
        String max = br.readLine().trim();
        scale = (Integer.parseInt(max)) / 255;
        String[] values = resolution.split(" ");
        width = Integer.parseInt(values[0]);
        height = Integer.parseInt(values[1]);
        numberOfBytes = Integer.parseInt(values[0]) * Integer.parseInt(values[1]);
        if (headerType.equals("P3")) {
            image = loadPPMP3FileNewLine(br);
        }
        if (headerType.equals("P6")) {
            image = loadPPMP6File(br);
        }
        return image;
    }

    public static Image loadPPMP3FileNewLine(BufferedReader br) throws FileNotFoundException, IOException {
        WritableImage image = new WritableImage((int) width, (int) height);
        PixelWriter pixelWriter = image.getPixelWriter();
        br.mark(100);
        boolean test = true;
        int channel = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int y = 0;
        int x = 0;
        while (test) {
            Color color;
            String bla = br.readLine();
            if (bla == null) {
                test = false;
                break;
            }
            if (bla.length() > String.valueOf(scale).length()) {
                image = (WritableImage) loadPPMP3SpaceFile(br, pixelWriter, image);
                return image;
            }
            int next = Integer.parseInt(bla);
            switch (channel) {
                case 0:
                    r = next;
                    break;
                case 1:
                    g = next;
                    break;
                case 2:
                    b = next;
                    break;
            }
            channel = (channel + 1) % 3;
            if (channel == 0) {
                color = Color.rgb(r, g, b);
                r = 0;
                g = 0;
                b = 0;
                pixelWriter.setColor(x++, y, color);
                if (x == height) {
                    x = 0;
                    y++;
                }
            }
        }
        return image;
    }

    public static Image loadPPMP3SpaceFile(BufferedReader br, PixelWriter pw, WritableImage wi) throws IOException {
        br.reset();
        int x = 0;
        int y = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int channel = 0;
        boolean exists = true;
        while (exists) {
            Color color;
            String test = br.readLine();
            if (test == null) {
                exists = false;
                break;
            }
            String[] values = test.split(" ");
            int[] intValues = Arrays.asList(values).stream().mapToInt(Integer::parseInt).toArray();
            if (intValues.length == (width * 3)) {
                for (int i = 0; i < intValues.length; i++) {
                    int next = intValues[i];
                    switch (channel) {
                        case 0:
                            r = next;
                            break;
                        case 1:
                            g = next;
                            break;
                        case 2:
                            b = next;
                            break;
                    }
                    channel = (channel + 1) % 3;
                    if (channel == 0) {
                        color = Color.rgb(r, g, b);
                        r = 0;
                        g = 0;
                        b = 0;
                        pw.setColor(x++, y, color);
                        if (x == width) {
                            x = 0;
                            y++;
                        }
                    }
                }
            }
        }
        return wi;
    }

    public static Image loadPPMP6File(BufferedReader br) throws IOException {
        byte[] bytes = Files.readAllBytes(myFile.toPath());
        int countLines=0;
        boolean test = true;
        int channel = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int y = 0;
        int x = 0;
        for (int i=0; i<bytes.length; i++){
            if (bytes[i]==13){
                if (countLines>=newLines){
                    continue;
                }
                countLines++;
            }
            else{
                if (countLines==newLines){
                     
                    
                    
                }
            }
        }
        return null;
    }
}
