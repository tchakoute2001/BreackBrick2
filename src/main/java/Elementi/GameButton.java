package Elementi;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.GamePanel;

public class GameButton{
    public int x,y,width,height;
    public String text ;
    public GamePanel panel;
    public Runnable azione;

    public GameButton(int x,int y,int width, int height,String text, GamePanel panel, Runnable azione){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.panel = panel;
        this.azione = azione;
        
        //Aggiungere l'opzione per seguire il toppolino
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(getBounds().contains(e.getX(), e.getY())){
                   if(azione != null){
                         azione.run(); //esercutare l'azione del bottone
                   }
                }
            }
        });
    }

     public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public void disegna(Graphics g){
        //fondi dei bottoni
        g.setColor(Color.darkGray);
        g.fillRect(x, y, width, height);

        //bordi dei bottoni
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        //centralizazzione dei testi (next,...)
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD,15));
        FontMetrics fm = g.getFontMetrics();
        int  text_Width = fm.stringWidth(text);
        int  text_heigth = fm.getAscent();
        g.drawString(text, x + (width - text_Width)/2, y + (height + text_heigth)/2 - 4);
    } 
}
