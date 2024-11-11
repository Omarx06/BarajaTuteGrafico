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
        int turno = 1;

        while (jugador1.tieneCartas() && jugador2.tieneCartas()) {
            Jugador jugadorActual = (turno % 2 == 1) ? jugador1 : jugador2;
            Jugador jugadorOponente = (turno % 2 == 1) ? jugador2 : jugador1;

            // Mostrar cartas ocultas para ambos jugadores al inicio de la ronda
            interfaz.restaurarCartasBack(1);
            interfaz.restaurarCartasBack(2);

            // Mostrar cartas al jugador actual
            int index = JOptionPane.showOptionDialog(null, jugadorActual.getNombre() + ", elige una carta para jugar:",
                    "Turno de " + jugadorActual.getNombre(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, jugadorActual.getMazo().toArray(), null);

            Carta cartaJugada = jugadorActual.jugarCarta(index);
            mesa.ponerCarta(cartaJugada);
            interfaz.mostrarCartaSeleccionada(turno % 2, index, cartaJugada);

            // Turno del jugador oponente
            index = JOptionPane.showOptionDialog(null, jugadorOponente.getNombre() + ", elige una carta para jugar:",
                    "Turno de " + jugadorOponente.getNombre(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, jugadorOponente.getMazo().toArray(), null);

            Carta cartaOponente = jugadorOponente.jugarCarta(index);
            mesa.ponerCarta(cartaOponente);
            interfaz.mostrarCartaSeleccionada((turno + 1) % 2, index, cartaOponente);

            // Comparar cartas y determinar ganador
            Jugador ganador = determinarGanador(cartaJugada, cartaOponente);
            JOptionPane.showMessageDialog(null, ganador.getNombre() + " ganó la baza!");

            // Actualizar puntaje y bazas
            ganador.recogerBaza(mesa.getCartasJugadas());
            ganador.sumarPuntos(calcularPuntos(cartaJugada, cartaOponente));
            interfaz.actualizarPuntajes(jugador1.getNombre() + ": " + jugador1.getPuntaje() + " puntos",
                    jugador2.getNombre() + ": " + jugador2.getPuntaje() + " puntos");

            // Limpiar mesa y repartir una nueva carta si hay cartas disponibles
            mesa.limpiarMesa();
            if (baraja.tieneCartas()) {
                // Repartir una carta adicional a cada jugador
                jugadorActual.recibirCarta(baraja.robar());
                jugadorOponente.recibirCarta(baraja.robar());
                interfaz.restaurarCartasBack(turno % 2);
                interfaz.restaurarCartasBack((turno + 1) % 2);
            }

            turno++;
        }

        mostrarResultadoFinal();
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
        System.out.println("Resultado final:");
        System.out.println(jugador1.getNombre() + ": " + jugador1.getPuntaje() + " puntos");
        System.out.println(jugador2.getNombre() + ": " + jugador2.getPuntaje() + " puntos");

        if (jugador1.getPuntaje() > jugador2.getPuntaje()) {
            System.out.println("¡" + jugador1.getNombre() + " ha ganado el juego!");
        } else if (jugador2.getPuntaje() > jugador1.getPuntaje()) {
            System.out.println("¡" + jugador2.getNombre() + " ha ganado el juego!");
        } else {
            System.out.println("¡El juego terminó en empate!");
        }
    }
}
