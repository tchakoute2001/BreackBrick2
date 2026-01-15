package Elementi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Mattone {
    public int  x,y; //posizione del mattone
    public  boolean visible;
    public  boolean distrutto;
    public int larghezza;
    public int altezza;
    public Color colore;

    public TipoMattone tipo;
    public List<PallaEx> palleDentro = new ArrayList<>();
    
    public Mattone(int x,int y,int larghezza,int altezza,TipoMattone tipo,Color colore){
        this.x = x;
        this.y = y;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.tipo = tipo;
        this.colore=(colore != null)? colore :Color.RED ;//colore predefinito 
        this.visible =true;
        if (tipo == TipoMattone.SPECIAL_STAR) {
            // Les balles sont créées et cachées dans la brique
            palleDentro.add(new PallaEx(x, y, +3, -4));
            palleDentro.add(new PallaEx(x, y, -3, -4));
        }
    }

    public enum TipoMattone{
    NORMALE,
    INDISTRUTTIBILE,
    BONUS,
    SPECIAL_STAR
   }
public TipoMattone getTipo(){return tipo;} 
    //disegno il mattone se è visibile
    public void disegna(Graphics g){
        if(!distrutto){
            g.setColor(colore);
            g.fillRect(x,y,larghezza,altezza);
        }
    }

    //Ritorna i rettangolo del mattone per i colliisioni
    public Rectangle getBounds(){
        return new Rectangle(x,y,larghezza,altezza);
    }
    

    public boolean isDistrutto(){
        return distrutto;
    }

    public void setDistrutto(boolean distrutto){
        this.distrutto = distrutto;
    }


}
