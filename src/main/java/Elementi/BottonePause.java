package Elementi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.GamePanel;

public class BottonePause {
   public int x,y;
   public int size ;  //latto del quadrato
   public boolean ispaused;
   public GamePanel panel;  //riferimento al pannello principale

   public BottonePause(int x, int y, int size, GamePanel panel){
        this.x = x;
        this.y = y;
        this.size = size;
        this.panel = panel;
        this.ispaused = false;
        panel.addMouseListener(new MouseAdapter(){
            @Override 
            public void mouseClicked(MouseEvent e){
                if(getBounds().contains(e.getX(),e.getY())){
                    togglePause();
                }
            }
        });

    }
    public void togglePause(){
        ispaused = !ispaused;
        if(ispaused){
            panel.timer.stop();
        }else{
            panel.timer.start();
        }
        panel.repaint();
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,size,size);
    }

    //disegna il quadrato di sfondo 
    public void disegna(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(x, y, size, size);

        g.setColor(Color.WHITE);
        if(ispaused){
            //triangolo
            int[] tx ={x + size/4, x + size/4, x + 3*size/4};
            int[] ty = {y + size/4,y + 3*size/4,y + size/4};
       
            g.fillPolygon(tx,ty,3);
        }else{
            //barre(pause)
            int barraWidth = size/5;
            int barraHeight = size/2;

            int spazio = barraWidth;
            g.fillRect(x + spazio, y + size/4, barraWidth, barraHeight);
            g.fillRect(x + 3*spazio, y + size/4, barraWidth, barraHeight);
        }
    }
}
    
