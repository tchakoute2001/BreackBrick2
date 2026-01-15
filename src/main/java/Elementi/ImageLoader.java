package Elementi;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
    
    public static Image Load(String filename) {
       try {
           return ImageIO.read(ImageLoader.class.getResourceAsStream(filename));       
       } catch (IOException e) {
           System.out.println("Errore di carica:" +filename);
           return null;
       }
        
    }
    

}
