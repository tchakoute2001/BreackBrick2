package Elementi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PallaEx {
    public int x,y,px,py;

    public PallaEx(int a,int b,int w,int h){
        x=a;
        y=b;
        px=w;
        py=h;
    }

    public void disegnaP(Graphics g){
        g.setColor(Color.PINK);
        g.fillOval(x, y, GameConstants.BALL_SIZE,GameConstants.BALL_SIZE);
    }

    public Rectangle getbounds(){
        return new Rectangle(x,y,GameConstants.BALL_SIZE,GameConstants.BALL_SIZE);
    }
    
}
