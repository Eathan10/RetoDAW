package ModeloDAO;

import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipoDAO {

    protected Connection con;
    protected String sql;
    public EquipoDAO(Connection c) {
        this.con = c;
    }

    public boolean crearEquipo(String nombre,String fechaFund) throws SQLException {
        sql = "INSERT INTO equipos(nombre,fecha_fundacion) VALUES(?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
        ps.setDate(2, validarFecha(fechaFund));
        //antes de llegar a ps.executeUpdate si salta un error entonces devolerá false, en otro caso True
        return ps.executeUpdate() != 0;
    }

    public java.sql.Date validarFecha(String fechaFund) {
        return java.sql.Date.valueOf(fechaFund);
    }


    public Equipo validarEquipo(String nombreEquipo) throws SQLException {
        sql = "SELECT * FROM equipos WHERE nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombreEquipo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            throw new SQLException("El Equipo ya existe");
        } else {
            return null; //lo que interesa por que es boolean, para que se pase a false
        }
    }
}