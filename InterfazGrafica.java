import javax.swing.*;
import java.awt.*;

public class InterfazGrafica extends JFrame {
    private JLabel[] cartasJugador1, cartasJugador2;
    private JLabel cartaTriunfoLabel, barajaLabel;
    private JLabel puntajeJugador1Label, puntajeJugador2Label;

    private static final int CARTA_ANCHO = 60;
    private static final int CARTA_ALTO = 90;
    private static final String BACK_IMAGE = "src/CartasBaraja/BACK.png";
    private static final String EMPTY_IMAGE = "src/CartasBaraja/EMPTY.png";

    public InterfazGrafica() {
        setTitle("Tute en Java");
        setLayout(new BorderLayout());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para cartas del jugador 2 (izquierda)
        JPanel panelJugador2 = new JPanel(new GridLayout(6, 1, 5, 5));
        cartasJugador2 = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            cartasJugador2[i] = new JLabel(escalarImagen(BACK_IMAGE));
            panelJugador2.add(cartasJugador2[i]);
        }

        // Panel central para carta de triunfo y baraja
        JPanel panelCentro = new JPanel(new GridLayout(3, 1, 5, 5));
        cartaTriunfoLabel = new JLabel(escalarImagen(BACK_IMAGE));
        barajaLabel = new JLabel(escalarImagen(BACK_IMAGE));
        panelCentro.add(cartaTriunfoLabel);
        panelCentro.add(barajaLabel);

        // Panel para cartas del jugador 1 (derecha)
        JPanel panelJugador1 = new JPanel(new GridLayout(6, 1, 5, 5));
        cartasJugador1 = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            cartasJugador1[i] = new JLabel(escalarImagen(BACK_IMAGE));
            panelJugador1.add(cartasJugador1[i]);
        }

        // Panel de puntajes
        JPanel panelPuntajes = new JPanel(new GridLayout(1, 2, 5, 5));
        puntajeJugador1Label = new JLabel("Jugador 1: 0 puntos");
        puntajeJugador2Label = new JLabel("Jugador 2: 0 puntos");
        panelPuntajes.add(puntajeJugador1Label);
        panelPuntajes.add(puntajeJugador2Label);

        add(panelJugador2, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
        add(panelJugador1, BorderLayout.EAST);
        add(panelPuntajes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private ImageIcon escalarImagen(String ruta) {
        ImageIcon icon = new ImageIcon(ruta);
        Image imagen = icon.getImage().getScaledInstance(CARTA_ANCHO, CARTA_ALTO, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    public void mostrarCartaSeleccionada(int jugador, int posicion, Carta carta) {
        JLabel[] cartasJugador = (jugador == 1) ? cartasJugador1 : cartasJugador2;
        cartasJugador[posicion].setIcon(escalarImagen(carta.getImagen()));
    }

    public void restaurarCartasBack(int jugador) {
        JLabel[] cartasJugador = (jugador == 1) ? cartasJugador1 : cartasJugador2;
        for (int i = 0; i < cartasJugador.length; i++) {
            cartasJugador[i].setIcon(escalarImagen(BACK_IMAGE));
        }
    }

    public void actualizarPuntajes(String puntajeJugador1, String puntajeJugador2) {
        puntajeJugador1Label.setText(puntajeJugador1);
        puntajeJugador2Label.setText(puntajeJugador2);
    }

    public void mostrarCartaTriunfo(Carta carta) {
        cartaTriunfoLabel.setIcon(escalarImagen(carta.getImagen()));
    }

    public void reemplazarBarajaConEmpty() {
        barajaLabel.setIcon(escalarImagen(EMPTY_IMAGE));
    }
}