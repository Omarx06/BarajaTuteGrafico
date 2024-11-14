import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Mesa {
    private ArrayList<Carta> cartasJugadas;
    private JLabel etiquetaCartaTriunfo;  // JLabel para mostrar la carta de triunfo

    // Constructor sin JLabel
    public Mesa() {
        cartasJugadas = new ArrayList<>();
    }

    // Constructor con JLabel para mostrar la carta de triunfo
    public Mesa(JLabel etiquetaCartaTriunfo) {
        this.cartasJugadas = new ArrayList<>();
        this.etiquetaCartaTriunfo = etiquetaCartaTriunfo;  // Asignamos el JLabel pasado al constructor
    }

    // Método para agregar una carta a la mesa
    public void ponerCarta(Carta carta) {
        cartasJugadas.add(carta);
    }

    // Método para obtener una carta en la posición index
    public Carta obtenerCarta(int index) {
        return cartasJugadas.get(index);
    }

    // Método para limpiar la mesa
    public void limpiarMesa() {
        cartasJugadas.clear();
    }

    // Método para obtener las cartas jugadas
    public ArrayList<Carta> getCartasJugadas() {
        return cartasJugadas;
    }

    // Método para mostrar la carta de triunfo en la interfaz gráfica
    public void mostrarCartaTriunfo(Carta cartaTriunfo) {
        if (etiquetaCartaTriunfo != null) {
            // Suponemos que la clase Carta tiene un método getImagen() que devuelve la imagen de la carta
            ImageIcon iconoCarta = new ImageIcon(cartaTriunfo.getImagen());  // Creamos un icono con la imagen de la carta de triunfo
            etiquetaCartaTriunfo.setIcon(iconoCarta);  // Mostramos la imagen en el JLabel
            etiquetaCartaTriunfo.repaint();  // Repintamos el JLabel para asegurar que se actualice en la interfaz gráfica
        }
    }
}
