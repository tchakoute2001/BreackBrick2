package Elementi;

import java.awt.Color;
import java.awt.Graphics;

public class LifeDisplay {

    public  int vite;          // nombre de vies
    public int x, y;          // position d'affichage
    public int taglia = 11;   // taille des cercles

    public LifeDisplay(int vite, int x, int y) {
        this.vite = vite;
        this.x = x;
        this.y = y;
    }

    public void setVite(int vite) {
        this.vite = vite;
    }

    public int getVite() {
        return vite;
    }


    public void decrementaVita() {
        if (vite > 0) vite--;
    }

    // dessiner les vies en forme de cœur
    public void disegna(Graphics g) {
        //disegnare un solo cuore come icona rappresentativa
        int vitaIniziali = 3;
        for (int i = 0; i < vitaIniziali; i++) {
            int posizioneX = x + i * (taglia * 2 + 8);
            
            if (i < vite) {
                // Se l'indice è minore delle vite attuali, disegna cuore PIENO
                drawHeart(g, posizioneX, y, taglia, true);
            } else {
                // Se la vita è stata persa, disegna cuore SVUOTATO
                drawHeart(g, posizioneX, y, taglia, false);
            }
        }
    }

    // metodo per disegnare il cuore
    public void drawHeart(Graphics g, int x, int y, int size,boolean pieno) {
        if(pieno){
        g.setColor(Color.RED);
        
        // Parte superiore (i due cerchi)   
        g.fillOval(x, y, size, size);
        g.fillOval(x + size, y, size, size);
        
        // triangolo per il basso
        int[] xs = {x , x + size, x + size * 2};
        int[] ys = {y + size/2 + 5, y + size * 2, y + size/2 + 5};
        g.fillPolygon(xs, ys,3);
        }else{
            //cuore svuotato
            g.setColor(Color.DARK_GRAY);

            //drawOval per l'effetto svuotare 
            g.setColor(Color.RED);
        
        // Parte superiore (i due cerchi)   
        g.drawOval(x, y, size, size);
        g.drawOval(x + size, y, size, size);
        
        // triangolo per il basso
        int[] xs = {x , x + size, x + size * 2};
        int[] ys = {y + size/2 + 5, y + size * 2, y + size/2 + 5};
        g.fillPolygon(xs, ys,3);


        }
    }
}
