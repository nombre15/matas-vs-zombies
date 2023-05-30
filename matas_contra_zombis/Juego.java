package matas_contra_zombis;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.util.Random;

public class Juego extends JFrame {

    //Inicializo coordenadas para los elementos, las plantas en forma de vector, y el contador de soles
    private int solX = (int) ((Math.random() * (1920 - 0)) + 0);
    private int solY = -100;
    private int cartaX = 750;
    private int cartaY = 0;
    private int cartaAncho = 150;
    private int cartaAlto = 150;
    private int previewX = -100;
    private int previewY = -100;
    private int contadorSoles = 0;
    private int zombieQuietoIndex = -1;
    private List<Point> plantas = new ArrayList<>();
    private List<Point> zombies = new ArrayList<>();
    String fondoPath = "Imagenes/fondo.png"; // Ruta de la imagen de fondo
    
    //Inicializo todo lo relacionado a cronometros y tiempos de espera
    private long startTime = System.currentTimeMillis();
    private long resetTime = 0;
    private boolean resetTimerStarted = false;
    private Random random = new Random();
    private int delay = getRandomDelay();
    
    //Inicializo los estados de los elementos
    private boolean isSolVisible = true; 
    private boolean isPreviewVisible = false;
    private boolean clicked = false;
    private boolean canPlacePlant = false;
    private boolean gameOver = false;
    
    //Inicializo los JLabel    
    JLabel contadorLbl = new JLabel(Integer.toString(contadorSoles));
    JLabel sombraContadorLbl = new JLabel (Integer.toString(contadorSoles));
    //Termino de declarar variables
        
    Graficos imagenFondo = new Graficos(fondoPath, this);
    
    public Juego(){
        
        initVentana();
        initLabels();
        initMouseEvents();
        initTimers();
    }
    
    /**
     * Inicializa la ventana
     */
    private void initVentana(){
    
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  
        setTitle("Matas contra zombis");
        imagenFondo.setLayout(null);    
        getContentPane().add(imagenFondo);
    }
    
    /**
     * Inicializa los labels
     * <br><br>
     * <b>contadorLbl:</b> La cantidad de soles que tiene el jugador a su disponibilidad
     */
    private void initLabels(){
        
        contadorLbl.setForeground(Color.lightGray);
        contadorLbl.setBounds(40, 86, 200, 50);
        contadorLbl.setFont(new Font("Arial", Font.PLAIN, 50));
        contadorLbl.setVisible(true);
        imagenFondo.add(contadorLbl);
        sombraContadorLbl.setForeground(Color.black);
        sombraContadorLbl.setBounds(39, 88, 200, 50);
        sombraContadorLbl.setFont(new Font("Arial", Font.PLAIN, 48));
        sombraContadorLbl.setVisible(true);
        imagenFondo.add(sombraContadorLbl);
    }
    
    /**
     * Metodo que inicializa todos los eventos del mouse
     * <br><br>
     * <b>mouseClicked: </b>Eventos que ocurren si se le da click al mouse
     * <br><br>
     * <b>mouseMoved: </b>Eventos que ocurren si se mueve el mouse
     */
    private void initMouseEvents(){
        
        //Funcion para aumentar soles y comprar plantas
        imagenFondo.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                
                int x = e.getX();
                int y = e.getY();
                
                //Si se da click al sol, aumenta el contador y el sol desaparece
                if (imagenFondo.isPointInCircle(x, y) && isSolVisible) {
                    
                    contadorSoles += 50;
                    contadorLbl.setText(Integer.toString(contadorSoles));
                    sombraContadorLbl.setText(Integer.toString(contadorSoles));
                    solY = -110;
                    solX = (int) ((Math.random() * (1920 - 0)) + 0);
                    clicked = true;
                    delay = getRandomDelay();
                    resetTime = System.currentTimeMillis() + delay;
                    resetTimerStarted = true;
                    isSolVisible = false;
                }
                
                //Si se da click a la carta, aparece la preview
                if (imagenFondo.isPointInRectangle(x, y, cartaX, cartaY, cartaAncho, cartaAlto) && contadorSoles >= 100){
                    
                    isPreviewVisible = true;
                    canPlacePlant = true;
                    previewX = x;
                    previewY = y;                                     
                }  
                
                //Si la preview existe, se puede colocar
                else if (isPreviewVisible) {
                    
                    if (!imagenFondo.isPointInRectangle(x, y, cartaX, cartaY, cartaAncho, cartaAlto) && canPlacePlant) {
                        
                        boolean canPlace = true;
                        
                        for (Point point : plantas) {
                            
                            //bx y by se usan para saber si ya existe una planta en la posicion
                            int bx = (int) point.getX();
                            int by = (int) point.getY();
                            
                            if (bx == x && by == y) {
                                
                                canPlace = false;
                                break;
                            }
                        }
                        
                        if (canPlace) {
                            
                            plantas.add(new Point(x, y));
                            imagenFondo.repaint();
                            canPlacePlant = false;
                            isPreviewVisible = false;
                            contadorSoles -= 100;
                            contadorLbl.setText(Integer.toString(contadorSoles));
                            sombraContadorLbl.setText(Integer.toString(contadorSoles));
                        }
                    }
                }
            }
        });
        
        //La previsualizacion de la planta se mueve junto al mouse
        imagenFondo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                
                int x = e.getX();
                int y = e.getY();
                
                if (isPreviewVisible) {
                    
                    previewX = x;
                    previewY = y;
                    imagenFondo.repaint();
                }
            }
        });
    }
    
    /**
     * Inicializador de todos los diferentes cronometros usados dentro del juego
     * <br> <br>
     * <b>timerSoles:</b> Cronometro para mover los soles hacia abajo en funcion de los segundos
     * <br> <br>
     * <b>resetTimer:</b> Cronometro de espera para reaparecer otro sol
     * <br> <br>
     * <b>timerZombies:</b> Cronometro para spawnear zombies
     */
    private void initTimers(){
        
        //Cronometro para mover los soles hacia abajo en funcion de los segundos
        Timer timerSoles = new Timer(50, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSol();
                moveZombies();
                
                imagenFondo.repaint();
            }
        });
        timerSoles.start();
        //Fin cronometro soles
        
        //Cronometro de espera para reaparecer otro sol
        Timer resetTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (resetTimerStarted && System.currentTimeMillis() >= resetTime) {
                    
                    resetTimerStarted = false;
                    startTime = System.currentTimeMillis();
                    isSolVisible = true;
                }
            }
        });
        resetTimer.start();
        //Fin cronometro de espera soles
        
        //Cronometro para spawnear zombies
        Timer timerZombies = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int[] posicionesY = {312, 442, 562, 702, 832};
                int y = posicionesY[random.nextInt(posicionesY.length)];
                zombies.add(new Point(1507, y));
                imagenFondo.repaint();
            }
        });
        timerZombies.start();
        //Fin cronometro spawn zombies
    }
    
    /**
     * Metodo para que los soles se muevan hacia abajo
     */
    private void moveSol() {
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        //Calcula el desplazamiento basado en los segundos actuales
        int displacement = (int) (elapsedTime / 100) * 5; //Ajusta la velocidad de movimiento

        if(solY <= 800){          
            solY = -110 + displacement;
        } 
        else if (clicked) {
            
            clicked = false;
            resetTime = System.currentTimeMillis() + delay;
            resetTimerStarted = true;
        }
    }
    
    /**
     * Metodo que hace que los zombies se muevan hacia la izquierda
     */        
    private void moveZombies() {
        
        int comerplantaindex = -1;
        
        for (int i = 0; i < zombies.size(); i++) {
            
            Point point = zombies.get(i);           
            
            int x = (int) point.getX();
            int y = (int) point.getY();
            x -= 1; // Ajusta la velocidad del movimiento cambiando el valor del desplazamiento

            if (x < 0) {
                
                zombies.remove(i);
                i--;
                gameOver = true;
                contadorLbl.setVisible(false);
                sombraContadorLbl.setVisible(false);
            } 
            else {
                
                
                // Comparar las posiciones en ambos ejes de las plantas y los zombies
                for (int j = 0; j < plantas.size(); j++) {
                    
                    Point plantPoint = plantas.get(j);
                    
                    if (point.getX() == plantPoint.getX() && point.getY() == plantPoint.getY()) {
                        
                        zombieQuietoIndex = i;
                        comerplantaindex = j;
                        break;
                    }
                }

                if (i != zombieQuietoIndex) {
                    
                    zombies.set(i, new Point(x, y));                   
                }
                
                /*
                if(comerplantaindex != -1){
                    
                    Point planta = plantas.get(i);
                    final int index = comerplantaindex;
                        
                        Timer comerPlanta = new Timer(4000, new ActionListener() {
                        
                            @Override
                            public void actionPerformed(ActionEvent e) {

                            }
                        });
                    comerPlanta.start();
                }
                */
            }         
        }
        zombieQuietoIndex = -1;
        comerplantaindex = -1;
    }
    
    /**
     * Método para generar un numero al azar entre el 5 y el 10, que sera la cantidad de segundos de espera entre la generacion de soles
     * @return Número random entre 5 y 10
     */
    private int getRandomDelay() {
        return random.nextInt(6000) + 5000;
    }
    
    public void verificarGameOver() {
    // ...

        if (gameOver) {
            imagenFondo.repaint();
       }
    }
    
    public boolean getIsSolVisible(){
        
        return isSolVisible;
    }
    
    public boolean getIsPreviewVisible(){
        
        return isPreviewVisible;
    }
    
    public boolean getIsGameOver(){
        
        return gameOver;
    }
    
    public boolean isGameOver(){
        
        return gameOver;
    }
    
    public int getSolX(){
        
        return solX;
    }
    
    public int getSolY(){
        
        return solY;
    }
    
    public int getPreviewX(){
        
        return previewX;
    }
    
    public int getPreviewY(){
        
        return previewY;
    }
    
    public List<Point> getPlantas(){
        
        return plantas;
    }
    
    public List<Point> getZombies(){
        
        return zombies;
    }
    
    public int getZombieQuietoIndex(){
        
        return zombieQuietoIndex;
    }
    
    public int getCartaX(){
        
        return cartaX;
    }
    
    public int getCartaY(){
        
        return cartaY;
    }
    
    public int getCartaAncho(){
        
        return cartaAncho;
    }  
    
    public int getCartaAlto(){
        
        return cartaAlto;
    }
}

