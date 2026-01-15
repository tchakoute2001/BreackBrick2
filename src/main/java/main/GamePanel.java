package main;

//import Element.PanelConstants;
import java.awt.Color;//nello stesso libreria :Font,Graphics,Rectangle,
import java.awt.Font;//event.actionEvent\.Keylistener \.ArrayList..
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Elementi.BottonePause;
import Elementi.GameButton;
import Elementi.GameConstants;
import Elementi.ImageLoader;
import Elementi.LifeDisplay;
import Elementi.Livelli;
import Elementi.Mattone;
import Elementi.PallaEx;
import resources.AudioPlayer;

public class GamePanel extends JPanel implements ActionListener,KeyListener{
    
    public Timer timer;
    public Image sfondo;

    public int pallaDX = GameConstants.BALL_SPEED/2;
    public int pallaDY = - GameConstants.BALL_SPEED; //PalleDX e PalleDY : Velocità della palla
    public BottonePause pauseBotton;
    /*public double pallaDX ;
    public double pallaDY ;*/

    public int Spazio  = 10;
    public int vite = 3;
    public LifeDisplay life = new LifeDisplay(3, 20, 70); // 3 vite iniziali, posizione in alto a sinistra;
    public boolean pallaHaToccatoMattone = false;
    public int racchettaX = GameConstants.WINDOW_WIDTH/2 - GameConstants.PADDLE_WIDTH/2; //Posizione iniziale della racchetta
    public int racchettaY = GameConstants.WINDOW_HEIGHT - 100; // monte de 40 pixels
  
    //posizionamento iniziale della palla
    public  int pallaX = racchettaX + GameConstants.PADDLE_WIDTH/2 - GameConstants.BALL_SIZE/2;
    public int pallaY = racchettaY - GameConstants.BALL_SIZE; //la palla si trova inizialmente sopra la racchetta

    public List<Mattone> mattoni;//Lista di mattoni
    public  int conteggio = 0;

    public boolean GameOver = false;
    public boolean GameWin = false;
    public boolean pallaInMovimento = false;
     
    public GameButton ReplayBottone;
    public GameButton ExitBottone;
    public GameButton NextBottone; 

    public List<PallaEx> Palle = new ArrayList<>(); 

    public Livelli level;
    public Color[] coloriLivelli = {
    Color.RED,
    Color.BLUE,
    Color.GREEN,
    Color.ORANGE,
    Color.MAGENTA,
    Color.CYAN
    };

    public  AudioPlayer Collisione;
    public  AudioPlayer SuonogameOver;
    public  AudioPlayer suonoWin; 
    public  AudioPlayer musica; 

    public GamePanel() {

        setFocusable(true);     
        addKeyListener(this);    // per dire al panello di ascoltare la tastiera

        
        pauseBotton = new BottonePause(20,20,30,this);
        setBackground(Color.BLACK);//colore del  fonto della finestra
        //addMouseMotionListener(this);
        timer = new Timer(10,this);
        timer.start();
        
        //Inizializza il livello
        level = new Livelli();
        //Crea mattoni
        mattoni = level.creaMattoni();
        applicaColoreMattoni();

        ReplayBottone = new GameButton(GameConstants.WINDOW_WIDTH/2 - 150,350,100 , 30, "Replay", this, () -> restartGame());
        ExitBottone = new GameButton(GameConstants.WINDOW_WIDTH/2 + 55,350,100 , 30, "Exit", this, () -> System.exit(0));
        NextBottone = new GameButton(GameConstants.WINDOW_WIDTH/2 + 130,350,100 , 30, "Next", this, () -> NextLevel());

        //parte dei suoni
        musica = new AudioPlayer("resources/audio/allGame.wav");
        if (musica != null) musica.loop();
        
        Collisione = new AudioPlayer("resources/audio/collision.wav");
        SuonogameOver = new AudioPlayer("resources/audio/game-over.wav");
        suonoWin = new AudioPlayer("resources/audio/orchestral-win.wav");
        //sfondo = ImageLoader.Load("R.jpg");
        sfondo = ImageLoader.Load("/resources/immagini" +level.levelCorrente + ".jpg");
        
    }
    

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        //1. disegna dello sfondo e gli elementi di gioco
        g.drawImage(sfondo, 0, 0, GameConstants.WINDOW_WIDTH,GameConstants.WINDOW_HEIGHT,this);
       
        //disegna della palla principale
        g.setColor(Color.WHITE);
        g.fillOval(pallaX,pallaY,GameConstants.BALL_SIZE,GameConstants.BALL_SIZE);
        
        //disegna della Racchetta
        g.fillRect(racchettaX,racchettaY,GameConstants.PADDLE_WIDTH,GameConstants.PADDLE_HEIGHT);
        
        //Disegna dei Mattoni 
        for(Mattone mattone : mattoni){
            mattone.disegna(g);
        }
       
        // ✅ Disegna bottone pausa
        pauseBotton.disegna(g);

        //--disegna delle palle Extra
        for (PallaEx p : Palle) {
            p.disegnaP(g);
        }

        //stampare i guardagni
        g.setColor(Color.ORANGE);
        String StampaGuadagni = "Guadagni :" + conteggio;
        g.setFont(new Font("verdana", Font.BOLD, 24)); // 24 = taille du texte
        int SpazioDelTesto = g.getFontMetrics().stringWidth(StampaGuadagni);
        g.drawString(StampaGuadagni, GameConstants.WINDOW_WIDTH - SpazioDelTesto - 20,30);
        
        //stampa il livello in esercuzione 
        g.setFont(new Font("verdana",Font.BOLD,18));
        g.setColor(Color.MAGENTA);
        g.drawString("Level :" + level.levelCorrente,GameConstants.WINDOW_WIDTH -100,50);
    
        //2.disegna dei messaggi di fine del gioco se necessario
        life.setVite(vite); // aggiorna il numero di vite 
        life.disegna(g);
        if(GameOver || GameWin){
            g.setFont(new Font("Arial",Font.BOLD,48));
            String Messaggio = GameOver? "Game Over" :"You Win";
            g.setColor(GameOver? Color.RED :Color.GREEN);
        
            int text_Width = g.getFontMetrics().stringWidth(Messaggio);
            g.drawString(Messaggio,(GameConstants.WINDOW_WIDTH - text_Width)/2 ,GameConstants.WINDOW_HEIGHT/2); 
            
           //System.out.println(Messaggio);*/
            disegnaBottoniCentrati(g);

        }
    }
    public void disegnaBottoniCentrati(Graphics g) {
    List<GameButton> bottoni = new ArrayList<>();

    bottoni.add(ReplayBottone);
    bottoni.add(ExitBottone);

    if (GameWin && !level.isUltimoLevel()) {
        bottoni.add(NextBottone);
    }

    int larghezzaTotale = bottoni.size() * 120 + (bottoni.size() - 1) * 20;
    int startX = (GameConstants.WINDOW_WIDTH - larghezzaTotale) / 2;
    int posY = GameConstants.WINDOW_HEIGHT / 2 + 70;

    int x = startX;

    for (GameButton b : bottoni) {
        b.x = x;
        b.y = posY;
        b.width = 120;
        b.height = 40;
        b.disegna(g);
        x += 140; // 120 largeur + 20 espace
    }
}

    @Override
    public void actionPerformed(ActionEvent e){
        //muovi palla
        pallaHaToccatoMattone = false;
        if(pallaInMovimento){
            pallaX += pallaDX;
            pallaY += pallaDY;
        }

        //collisione con bordi
        if(pallaX <= 0 || pallaX>= GameConstants.WINDOW_WIDTH -GameConstants.BALL_SIZE) {
            pallaDX = -pallaDX;
        }
        if(pallaY <= 0){
            pallaDY = -pallaDY ;
        } 

        //Game over se la palla cade sotto
        if(pallaY >= GameConstants.WINDOW_HEIGHT - GameConstants.BALL_SIZE){
           life.decrementaVita();
           vite = life.getVite();
           
            if(vite <= 0){
                GameOver = true; //attivazione dellao stampa della parola GameOver 
                timer.stop();
                if (SuonogameOver != null) SuonogameOver.play();
                if (musica != null) musica.stop();
            }else {
                // resetta la posizione della palla sopra la racchetta
                pallaX = racchettaX + GameConstants.PADDLE_WIDTH/2 - GameConstants.BALL_SIZE/2;
                pallaY = racchettaY - GameConstants.BALL_SIZE;
                pallaDX = GameConstants.BALL_SPEED/2;
                pallaDY = -GameConstants.BALL_SPEED;
                pallaInMovimento = false;
            }
           
            //repaint();
            //return;
            //JOptionPane.showMessageDialog(this,"Game Over");
        }

        for(PallaEx p : Palle){
            p.x += p.px;
            p.y +=p.py;

            //Rimbalza sui bordi 
            if(p.x <= 0 || p.x >= GameConstants.WINDOW_WIDTH - GameConstants.BALL_SIZE)p.px += -p.px;
            //Rimbalza in alto 
            if(p.y <= 0)p.py =-p.py;

            // Rimbalzo sulla racchetta per le palle EXTRA
            Rectangle rBounds = new Rectangle(racchettaX, racchettaY, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
            if (new Rectangle(p.x, p.y, 10, 10).intersects(rBounds)) {
                p.py = -Math.abs(p.py); // Fa risalire la palla
            }

        }
        Palle.removeIf(p -> p.y >= GameConstants.WINDOW_HEIGHT - GameConstants.BALL_SIZE);


        //disegnazione dei rettangoli di collisione
        Rectangle pallaBounds = new  Rectangle(pallaX,pallaY,GameConstants.BALL_SIZE,GameConstants.BALL_SIZE);
        Rectangle racchettaBounds = new  Rectangle(racchettaX,racchettaY/*getHeight() - 50*/,GameConstants.PADDLE_WIDTH,GameConstants.PADDLE_HEIGHT);
  
        // collisione entrambi palla e racchetta
        if(pallaBounds.intersects(racchettaBounds)){//se fosse una intersezione tra le due rettangoli 
            pallaDY = -pallaDY; //assicura che la palla vada verso l'alto  
        }
       
        //collisione con mattoni
       for (Mattone mattone : mattoni) {
            if(!mattone.isDistrutto() && pallaBounds.intersects(mattone.getBounds())){
                
                switch(mattone.getTipo()){
                    case INDISTRUTTIBILE->{
                        // la palla rimbalza senza distruggere il mattone
                        // controllo collisione verticale o orizzontale
                        Rectangle mattoneBounds = mattone.getBounds();

                        boolean collisioneOrizzontale = (pallaX + GameConstants.BALL_SIZE > mattoneBounds.x &&
                                                        pallaX < mattoneBounds.x + mattoneBounds.width);

                        boolean collisioneVerticale = (pallaY + GameConstants.BALL_SIZE > mattoneBounds.y &&
                                                    pallaY < mattoneBounds.y + mattoneBounds.height);

                        if (collisioneOrizzontale) pallaDY = -pallaDY;
                        if (collisioneVerticale) pallaDX = -pallaDX;

                    }
                    case BONUS ->{
                        conteggio += 15;
                        mattone.setDistrutto(true);
                        pallaDY = -pallaDY;
                        Collisione.play();
                        //suonoWinOBonus.play();
                    }
                       
                    case SPECIAL_STAR->{
                        //creare nuove palle
                        // libérer les balles stockées dans la brique
                        for (PallaEx p : mattone.palleDentro){
                              
                            // commencer au centre de la brique
                            p.x = mattone.x + GameConstants.BRICK_WIDTH / 2 - GameConstants.BALL_SIZE / 2;
                            p.y = mattone.y + GameConstants.BRICK_HEIGHT / 2 - GameConstants.BALL_SIZE / 2;

                            Palle.add(p);  // ajouter au jeu
                        }
                        mattone.setDistrutto(true);
                        pallaDY = -pallaDY;
                        Collisione.play();
                    }
                    case NORMALE ->{
                        mattone.setDistrutto(true);
                        conteggio += GameConstants.GUADAGNA;
                        pallaDY = -pallaDY;
                        Collisione.play();
                    } //NORMALE
                }

                break;
            }
        }
        
        //verificare se tuti i mattoni sono distrutti eccetto quelli non distrutti 
        boolean tuttiDistrutti = true;
        for(Mattone m: mattoni){
            if(m.getTipo() != Mattone.TipoMattone.INDISTRUTTIBILE && !m.isDistrutto()){
                tuttiDistrutti = false;
                break;
            }
        }
        if(tuttiDistrutti){
            timer.stop();
            GameWin = true;
            if (suonoWin != null) suonoWin.play();
            if (musica != null) musica.stop();
        }
        repaint();

    }
    /*public void PallaInPiù(){
        Palle.add(new PallaEx(pallaX, pallaY, +3, -4));
        Palle.add(new PallaEx(pallaX, pallaY, -3, -4));
    }*/
    @Override
    public void keyPressed(KeyEvent e){
        if(pauseBotton.ispaused) return; //ferma la tastiera e per consequenza niente tocca della tastiera non funziona
        if (GameOver || GameWin) return;  // blocca i tasti quando il gioco è finito

        pallaInMovimento = true; //Al movimento della palla dal'utente
       int key = e.getKeyCode();
       if(key == KeyEvent.VK_LEFT){
        racchettaX -= GameConstants.PADDLE_SPEED; 
       }else if(key == KeyEvent.VK_RIGHT){
        racchettaX += GameConstants.PADDLE_SPEED; 
       }
       
       //per impedisce che esce dallo schermo a sinistra e a destra
        if (racchettaX < 0) {
            racchettaX = 0;    //blocca a sinistra 
        } else if (racchettaX > GameConstants.WINDOW_WIDTH - GameConstants.PADDLE_WIDTH) {
            racchettaX = GameConstants.WINDOW_WIDTH - GameConstants.PADDLE_WIDTH;//blocca a destra 
        }

        repaint();//ridisegna il pannello
    }


    public void restartGame() {
        // Réinitialiser la raquette
        racchettaX = GameConstants.WINDOW_WIDTH / 2 - GameConstants.PADDLE_WIDTH / 2;
        racchettaY = GameConstants.WINDOW_HEIGHT - 150;

        // Réinitialiser la balle juste au-dessus
        pallaX = racchettaX + GameConstants.PADDLE_WIDTH / 2 - GameConstants.BALL_SIZE / 2;
        pallaY = racchettaY - GameConstants.BALL_SIZE;

        // Réinitialiser la vitesse de la balle
        pallaDX = GameConstants.BALL_SPEED / 2;
        pallaDY = -GameConstants.BALL_SPEED;

        // La balle ne bouge pas encore (attente d’action du joueur)
        pallaInMovimento = false;

        // Créer de nouveaux mattoni
        mattoni.clear();
        mattoni = level.creaMattoni();
        applicaColoreMattoni();
        // Réinitialiser états
        GameOver = false;
        GameWin = false;
        conteggio = 0;

         if (musica != null) musica.loop();

        //rilanciare il timer se necessario
        if(!timer.isRunning()) timer.start();
        repaint();
    }
    public void NextLevel() {
        //1.andare al prossimo livello 
        level.AumentaLevel();

        //2.ripristina le vite al max dopo il vincolo di ogni livello
        this.vite = 3; 
        life.setVite(3); // Aggiorna l'oggetto grafico LifeDisplay
        
        // 3) charger le nouveau plateau (briques + fond)
        mattoni = level.creaMattoni();

        //aumento oppure gestione della velocità base
        if (level.levelCorrente >= 2) {
            pallaDX = (GameConstants.BALL_SPEED / 2) + 2; 
            pallaDY = -(GameConstants.BALL_SPEED + 2);
        } else {
            pallaDX = GameConstants.BALL_SPEED / 2;
            pallaDY = -GameConstants.BALL_SPEED;
        }

        //charger le fond (vérifier le chemin)
        Image newSfondo = ImageLoader.Load("/resources/immagini" + level.levelCorrente + ".jpg");
        if (newSfondo != null) {
            sfondo = newSfondo;
        } else {
            System.err.println("Image du niveau introuvable: immagini" + level.levelCorrente + ".jpg");
        }

        if (musica != null) musica.stop();
        
        // 4) Reset posizioni palla e racchetta (ne recrée pas les mattoni)
        restartGame();
    
    }
    private void applicaColoreMattoni() {
        int indice = (level.levelCorrente - 1) % coloriLivelli.length;
        Color coloreBase = coloriLivelli[indice];
        for (Mattone m : mattoni) {
            if(m.getTipo() == Mattone.TipoMattone.NORMALE){
                m.colore = coloreBase;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
