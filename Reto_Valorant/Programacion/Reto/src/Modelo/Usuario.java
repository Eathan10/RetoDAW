package Modelo;

public class Usuario {

    private String nombreUsuario;
    private String paswd;
    private String tipoUsuario;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String paswd, String tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.paswd = paswd;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPaswd() {
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
