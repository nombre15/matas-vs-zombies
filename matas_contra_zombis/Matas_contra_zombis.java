package matas_contra_zombis;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Matas_contra_zombis extends JFrame{

    public static void main(String[] args) {
        
        //invokeLater usado para el fondo con imagen
        SwingUtilities.invokeLater(() -> {
            Juego juego = new Juego();
            juego.setVisible(true);
        });
    }
    
}
