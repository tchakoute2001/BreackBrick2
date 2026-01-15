package Elementi;

import java.awt.Color;
import  java.util.ArrayList;
import java.util.List;

import Elementi.Mattone.TipoMattone;

public class Livelli {
   public int levelCorrente;
   public final int MAX_LEVEL = 5;
   public int Spazio;


   public Livelli(){
    this.levelCorrente=1;
    this.Spazio = 10;
   }

    public int getLevelCorrente(){
        return levelCorrente;
   }

   public void AumentaLevel(){
    if(levelCorrente < MAX_LEVEL){
        levelCorrente++;
    }
   }

   //generare i mattoni rispetto il livello corrente
   public List<Mattone> creaMattoni(){
        List<Mattone> mattoni = new ArrayList<>(); 

        int NumColonna = 6;
        int NumRiga=(levelCorrente <= 2)? 3 : (levelCorrente <=4) ? 4:5;

        //prima calcola colore del livello
        //Color[] coloreLivelli = {Color.RED,Color.CYAN,Color.BLACK,Color.GREEN,Color.ORANGE};
        // Definiamo i colori base per ogni livello (come avevi nel tuo GamePanel)
        Color[] coloriLivelli = {
            Color.RED,     // Livello 1
            Color.BLUE,    // Livello 2
            Color.GREEN,   // Livello 3
            Color.ORANGE,  // Livello 4
            Color.MAGENTA  // Livello 5
        };
        // Selezioniamo il colore base in base al livello corrente
        Color coloreBaseLivello =  coloriLivelli[(levelCorrente-1) % coloriLivelli.length];

        //calcolo della larghezza totale del blocco di mattoni
        int larghezzaTotale = NumColonna * GameConstants.BRICK_WIDTH + (NumColonna-1)*Spazio;

        //centralizzare il blocco
        int StartX = (GameConstants.WINDOW_WIDTH - larghezzaTotale)/2;
        int StartY = 70;

        for (int riga = 0; riga < NumRiga; riga++){
            for (int colonna = 0; colonna < NumColonna; colonna++) {
                TipoMattone tipo = TipoMattone.NORMALE;
                //LOGICA LIVELLI
                switch(levelCorrente) {
                    case 2 -> {
                        // Bianco Indistruttibile: Estremità e Centro
                        // Centro: riga 1, colonne centrali (2 o 3)
                        // Estremità: Destra (colonna 5), Sinistra (colonna 0)
                        if ((riga == 1 && (colonna == 0 || colonna == 2 || colonna == 5)) || 
                            (colonna == 5 && (riga == 0 || riga == 2))) {
                            tipo = TipoMattone.INDISTRUTTIBILE;
                        }
                    }
                    case 3 -> {
                        // Livello 3: Mattoni grigi sparsi
                        if ((riga == 1 && (colonna == 1 || colonna == 4)) || 
                            (riga == 2 && colonna == 2) || (riga == 0 && colonna == 3)) {
                            tipo = TipoMattone.INDISTRUTTIBILE;
                        }
                    }
                    case 4 -> {
                        // Livello 4: Stelle speciali per palle extra
                        if ((riga == 1 && colonna == 3) || (riga == 3 && colonna == 5)) {
                            tipo = TipoMattone.SPECIAL_STAR;
                        }
                    }
                    case 5 -> {
                        // Livello 5: Il caos finale
                        if (colonna % 3 == 0) tipo = TipoMattone.INDISTRUTTIBILE;
                        else if (riga % 2 == 0) tipo = TipoMattone.SPECIAL_STAR;
                        else if (riga == 2 && colonna == 3) tipo = TipoMattone.BONUS;
                    }
                }
                int X = StartX + colonna * (GameConstants.BRICK_WIDTH + Spazio) + 50;
                int Y =StartY + riga * (GameConstants.BRICK_HEIGHT + Spazio) + 50;
                //mattoni.add(new Mattone(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT));
                
                Color mattoneColor = switch(tipo){
                    case INDISTRUTTIBILE -> Color.WHITE;
                    case SPECIAL_STAR -> Color.CYAN;
                    default -> coloreBaseLivello;
                };
                Mattone m = new Mattone(X,Y,GameConstants.BRICK_WIDTH,GameConstants.BRICK_HEIGHT,tipo, mattoneColor);
                
                //Se è SPECIAL_STAR,aggiungiamo le palle extra dentro
                if(tipo == TipoMattone.SPECIAL_STAR){
                    m.palleDentro.add(new PallaEx(X,Y,2,-3));
                    m.palleDentro.add(new PallaEx(X,Y,-2,-3));
                    m.palleDentro.add(new PallaEx(X,Y,0,-4));
                }
                mattoni.add(m);
            }
        }
        return mattoni;
    }
    

   //veruficare se si è all'ultimo livello
   public boolean isUltimoLevel(){
    return levelCorrente>=MAX_LEVEL;
   }
}

