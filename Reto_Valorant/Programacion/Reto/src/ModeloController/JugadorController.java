package ModeloController;

import Modelo.Jugador;
import ModeloDAO.JugadorDAO;

import java.sql.SQLException;
import java.util.*;


public class JugadorController {

    private final JugadorDAO jugadorDAO;

    public JugadorController(JugadorDAO jugadorDAO) {
        this.jugadorDAO = jugadorDAO;
    }

    public ArrayList<Jugador> obtenerJugadores(int codEquipo) throws SQLException {
        return jugadorDAO.obtenerPorEquipo(codEquipo);
    }

}
