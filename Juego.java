import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Juego {
    private Baraja baraja;
    private Jugador jugador1;
    private Jugador jugador2;
    private Mesa mesa;
    private String figuraTriunfo;
    private Carta cartaTriunfo;
    private InterfazGrafica interfaz;
    private Scanner scanner;
    private HashSet<String> cantesRealizados = new HashSet<>();

    public Juego() {
        scanner = new Scanner(System.in);
        baraja = new Baraja();
        mesa = new Mesa();
        interfaz = new InterfazGrafica();

        iniciarJugadores();
        repartirCartas();
        prepararUltimaCartaTriunfo();

        System.out.println("Figura de triunfo: " + figuraTriunfo + ", Carta de triunfo: " + cartaTriunfo);

        interfaz.mostrarCartaTriunfo(cartaTriunfo);
        interfaz.restaurarCartasBack(0);
        interfaz.restaurarCartasBack(1);
    }

    private void iniciarJugadores() {
        String nombre1 = JOptionPane.showInputDialog("Introduce el nombre del Jugador 1:");
        String nombre2 = JOptionPane.showInputDialog("Introduce el nombre del Jugador 2:");
        jugador1 = new Jugador(nombre1);
        jugador2 = new Jugador(nombre2);
    }

    private void repartirCartas() {
        for (int i = 0; i < 6; i++) {
            jugador1.recibirCarta(baraja.robar());
            jugador2.recibirCarta(baraja.robar());
        }
    }

    private void prepararUltimaCartaTriunfo() {
        cartaTriunfo = baraja.robar(); // Retirar carta de triunfo de la baraja solo al final
        figuraTriunfo = cartaTriunfo.getFigura();
    }

    public void jugar() {
        Jugador jugadorActual = jugador1;  // Jugador 1 inicia
        Jugador jugadorOponente = jugador2;  // Jugador 2 es el oponente
        boolean cartasEnBaraja = baraja.tieneCartas();  // Comprobamos si hay cartas en la baraja

        // Continuamos mientras alguno de los jugadores tenga cartas
        while (jugador1.tieneCartas() || jugador2.tieneCartas()) {

            // FORZAR ALTERNANCIA cuando ya no haya cartas en la baraja
            if (!cartasEnBaraja) {
                Jugador temp = jugadorActual;
                jugadorActual = jugadorOponente;
                jugadorOponente = temp;
            }

            // Turno del jugador actual
            Carta cartaJugada = null;
            if (jugadorActual.tieneCartas()) {
                if (jugadorActual.getMazo().size() == 1) {
                    // Si solo queda una carta, la juega automáticamente
                    cartaJugada = jugadorActual.jugarCarta(0);
                } else {
                    Object[] opciones = jugadorActual.getMazo().toArray();

                    // Mostrar opciones de cartas disponibles
                    if (opciones != null && opciones.length > 0 && !contieneNull(opciones)) {
                        int index = JOptionPane.showOptionDialog(null, jugadorActual.getNombre() + ", elige una carta para jugar:",
                                "Turno de " + jugadorActual.getNombre(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                null, opciones, opciones[0]);
                        cartaJugada = jugadorActual.jugarCarta(index);
                    }
                }
            }

            if (cartaJugada != null) {
                mesa.ponerCarta(cartaJugada);  // Colocar la carta del jugador en la mesa
                interfaz.mostrarCartaSeleccionada(jugadorActual == jugador1 ? 0 : 1, 0, cartaJugada);  // Mostrar la carta jugada
            }

            // Turno del jugador oponente
            Carta cartaOponente = null;
            if (jugadorOponente.tieneCartas()) {
                if (jugadorOponente.getMazo().size() == 1) {
                    // Si solo queda una carta, la juega automáticamente
                    cartaOponente = jugadorOponente.jugarCarta(0);
                } else {
                    Object[] opciones = jugadorOponente.getMazo().toArray();

                    // Mostrar opciones de cartas disponibles
                    if (opciones != null && opciones.length > 0 && !contieneNull(opciones)) {
                        int index = JOptionPane.showOptionDialog(null, jugadorOponente.getNombre() + ", elige una carta para jugar:",
                                "Turno de " + jugadorOponente.getNombre(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                null, opciones, opciones[0]);
                        cartaOponente = jugadorOponente.jugarCarta(index);
                    }
                }
            }

            if (cartaOponente != null) {
                mesa.ponerCarta(cartaOponente);  // Colocar la carta del oponente en la mesa
                interfaz.mostrarCartaSeleccionada(jugadorOponente == jugador1 ? 0 : 1, 0, cartaOponente);  // Mostrar la carta jugada
            }

            // Comparar cartas jugadas y determinar el ganador de la baza
            if (cartaJugada != null && cartaOponente != null) {
                Jugador ganador = determinarGanador(cartaJugada, cartaOponente);
                JOptionPane.showMessageDialog(null, ganador.getNombre() + " ganó la baza!");

                // Actualizar puntaje y recoger las cartas
                ganador.recogerBaza(mesa.getCartasJugadas());
                ganador.sumarPuntos(calcularPuntos(cartaJugada, cartaOponente));
                interfaz.actualizarPuntajes(jugador1.getNombre() + ": " + jugador1.getPuntaje() + " puntos",
                        jugador2.getNombre() + ": " + jugador2.getPuntaje() + " puntos");

                // El ganador comienza solo si aún hay cartas en la baraja
                if (cartasEnBaraja) {
                    jugadorActual = ganador;
                    jugadorOponente = (jugadorActual == jugador1) ? jugador2 : jugador1;
                }
            }

            // Limpiar la mesa para la siguiente ronda
            mesa.limpiarMesa();

            // Repartir cartas mientras haya cartas en la baraja
            if (cartasEnBaraja && baraja.tieneCartas()) {
                jugadorActual.recibirCarta(baraja.robar());
                jugadorOponente.recibirCarta(baraja.robar());
            } else {
                // Si no hay más cartas en la baraja, alternar los turnos equitativamente
                cartasEnBaraja = false;
            }

            // Actualizar la interfaz para reflejar la cantidad de cartas restantes
            interfaz.restaurarCartasBack(jugadorActual == jugador1 ? 0 : 1);
            interfaz.restaurarCartasBack(jugadorOponente == jugador1 ? 0 : 1);
        }

        // Mostrar el resultado final cuando ambos jugadores se queden sin cartas
        mostrarResultadoFinal();
    }

    // Método auxiliar para validar que no haya valores nulos en el arreglo de opciones
    private boolean contieneNull(Object[] opciones) {
        for (Object opcion : opciones) {
            if (opcion == null) {
                return true;
            }
        }
        return false;
    }
    
    private Jugador determinarGanador(Carta carta1, Carta carta2) {
        if (carta1.getFigura().equals(carta2.getFigura())) {
            return (carta1.getValor() > carta2.getValor()) ? jugador1 : jugador2;
        } else if (carta2.getFigura().equals(figuraTriunfo)) {
            return jugador2;
        } else {
            return jugador1;
        }
    }

    private int calcularPuntos(Carta carta1, Carta carta2) {
        int puntos = 0;
        puntos += obtenerPuntosCarta(carta1);
        puntos += obtenerPuntosCarta(carta2);
        return puntos;
    }

    private int obtenerPuntosCarta(Carta carta) {
        switch (carta.getValor()) {
            case 1: return 11;
            case 3: return 10;
            case 11: return 2;
            case 12: return 4;
            default: return 0;
        }
    }

    private void mostrarResultadoFinal() {
        String resultado = "Resultado final:\n" +
                jugador1.getNombre() + ": " + jugador1.getPuntaje() + " puntos\n" +
                jugador2.getNombre() + ": " + jugador2.getPuntaje() + " puntos\n";

        if (jugador1.getPuntaje() > jugador2.getPuntaje()) {
            resultado += "¡" + jugador1.getNombre() + " ha ganado el juego!";
        } else if (jugador2.getPuntaje() > jugador1.getPuntaje()) {
            resultado += "¡" + jugador2.getNombre() + " ha ganado el juego!";
        } else {
            resultado += "¡El juego terminó en empate!";
        }

        // Mostrar el resultado en un JOptionPane
        JOptionPane.showMessageDialog(null, resultado, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);
    }

}
