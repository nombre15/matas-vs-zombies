package matas_contra_zombis;

import java.awt.*;
import javax.swing.*;

class Graficos extends JPanel {
         
        Juego juego;
        private final Image backgroundImage;
        private final int radio = 50;

        public Graficos(String imagePath, Juego juego) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            this.juego = juego;
        }
        
        //Pintar los diferentes elementos
        @Override
        protected void paintComponent(Graphics g) {
            
            super.paintComponent(g);
            
            //Tabla de posiciones
            int posicionesX[] = new int[9];
            int posicionesY[] = new int[5];
            int plantaAncho = 120;
            int plantaAlto = 120;
            int zombieAncho = 140;
            int zombieAlto = 140;
            
            posicionesX[0] = 527; posicionesX[1] = 645; posicionesX[2] = 758; 
            posicionesX[3] = 874; posicionesX[4] = 987; posicionesX[5] = 1097; 
            posicionesX[6] = 1208; posicionesX[7] = 1331; posicionesX[8] = 1439;
            
            posicionesY[0] = 312; posicionesY[1] = 442; posicionesY[2] = 562;
            posicionesY[3] = 702; posicionesY[4] = 832;
            //Fin tabla de posiciones
            
            //Declaracion de imagenes
            ImageIcon planta = new ImageIcon("Imagenes/lanzaguisantes.gif");
            Image lanzaguisantes = planta.getImage();
            
            ImageIcon carta = new ImageIcon("Imagenes/carta_lanzaguisantes.png");
            Image carta_lanzaguisantes = carta.getImage();
            
            ImageIcon preview = new ImageIcon("Imagenes/preview_lanzaguisantes.png");
            Image preview_lanzaguisantes = preview.getImage();
            
            ImageIcon zombiesImg = new ImageIcon("Imagenes/zombie_caminando.gif");
            Image zombie_caminando = zombiesImg.getImage();
            
            ImageIcon solImg = new ImageIcon("Imagenes/sol.gif");
            Image imgSol = solImg.getImage();
            
            ImageIcon solGlow = new ImageIcon("Imagenes/sol_glow.png");
            Image glowSol = solGlow.getImage();
            
            ImageIcon sombra = new ImageIcon("Imagenes/sombra.png");
            Image sombraImg = sombra.getImage();
            
            ImageIcon fondoSoles = new ImageIcon("Imagenes/fondo_soles.png");
            Image fondoSolesImg = fondoSoles.getImage();
            
            ImageIcon placaSoles = new ImageIcon("Imagenes/placa_soles.png");
            Image placaSolesImg = placaSoles.getImage();
            
            ImageIcon soles = new ImageIcon("Imagenes/soles.gif");
            Image solesGif = soles.getImage();
            //Fin declaracion de imagenes
                    
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            
            //Soles
            if(juego.getIsSolVisible()) {
                
                g.drawImage(glowSol, juego.getSolX() - radio*2, juego.getSolY() - radio*2, radio*4, radio*4, null);
                g.drawImage(imgSol, juego.getSolX() - radio, juego.getSolY() - radio, radio*2, radio*2, null);
            }
            //Fin soles
            
            //Preview planta
            if (juego.getIsPreviewVisible()) {
                
                g.drawImage(preview_lanzaguisantes, juego.getPreviewX()-100/2, juego.getPreviewY()-100/2, 100, 100, null);
            }
            //Fin preview planta
            
            //Colocar planta
            for (Point point : juego.getPlantas()) {
                
                int x = (int) point.getX();
                int y = (int) point.getY();
                
                for(int i=0; i < 9; i++){
                    for(int j = 0; j < 5; j++){
                        
                        if(x >= posicionesX[i] - 60 && x <= posicionesX[i] + 40 && y >= posicionesY[j] - 50 && y <= posicionesY[j] + 70){
                            
                            g.drawImage(sombraImg, (posicionesX[i] - plantaAncho/2) - 30, (posicionesY[j] - plantaAlto/2) + 100, 160, 30, null);
                            g.drawImage(lanzaguisantes, posicionesX[i] - plantaAncho/2, posicionesY[j] - plantaAlto/2, plantaAncho, plantaAlto, null);                          
                            point.setLocation(posicionesX[i], posicionesY[j]);
                        }
                    }
                }
            }
            //Fin colocar planta

            //Spawn zombies
            for (Point point : juego.getZombies()) {
                
                int x = (int) point.getX();
                int y = (int) point.getY();
                
                g.drawImage(zombie_caminando, x - zombieAncho/2, y - zombieAlto/2, zombieAncho, zombieAlto, null);
                g.drawImage(sombraImg, x - zombieAncho/2, (y - zombieAlto/2) + 127, 160, 30, null);
            }
            //Fin spawn zombies
            
            g.drawImage(carta_lanzaguisantes, juego.getCartaX(), juego.getCartaY(), juego.getCartaAncho(), juego.getCartaAlto(), null);    
            g.setColor(Color.darkGray);
            g.fillRoundRect(-460, -40, 705, 203, 50, 50);
            g.drawImage(fondoSolesImg, -460, -40, 700, 200, null);
            g.drawImage(placaSolesImg, 17, 60, 200, 100, null);
            g.drawImage(solesGif, 17, -10, 200, 100, null);
            
            if (juego.isGameOver()) {
                
                // Dibujar la pantalla de "Game Over"
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
                // Otros elementos de la pantalla de "Game Over"
            }
        }
        
        //Funcion para hacer que los soles sean clickeables
        public boolean isPointInCircle(int x, int y) {
            
            int distanceSquared = (x - juego.getSolX()) * (x - juego.getSolX()) + (y - juego.getSolY()) * (y - juego.getSolY());
            return distanceSquared <= radio * radio;
        }
        
        //Funcion para hacer que las cartas de plantas sean clickeables
        public boolean isPointInRectangle(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight) {
            
            return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight;
        }
    }

