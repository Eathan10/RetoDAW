package ModeloController;

import Modelo.Enfrentamiento;
import Modelo.Equipo;
import Modelo.Jornada;
import ModeloDAO.EnfrentamientoDAO;
import ModeloDAO.EquipoDAO;
import ModeloDAO.JornadaDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author Equipo tres
 * @version (2.0)
 * @see Enfrentamiento
 */
public class EnfrentamientoController {
    private EnfrentamientoDAO enfrentamientoDAO;
    private ModeloController modeloController;
    private ArrayList<Enfrentamiento> enfrentamientos = new ArrayList<>();
    private ArrayList<Enfrentamiento> enfrentamientosMitad1 = new ArrayList<>();
    private List<Jornada> jornadas;
    private List<Equipo> equipos;

    /**
     * Constructor 
     * @param enfrentamientoDAO DAO 
     * @param modeloController  
     */
    public EnfrentamientoController(EnfrentamientoDAO enfrentamientoDAO, ModeloController modeloController) {
        this.enfrentamientoDAO = enfrentamientoDAO;
        this.modeloController = modeloController;
    }

    
    /**
     * Crea los enfrentamientos para todas las jornadas
     * @throws Exception si ocurre un error al obtener los datos o crear enfrentamientos
     */
    public void crearEnfrentamientos() throws Exception{
            jornadas = modeloController.getJornadas();
            primeraMitad();
            segundaMitad();
    }

    /**
     * Genera los enfrentamientos pero para la primera mitad 
     * @throws Exception si ocurre un error al obtener los equipos o crear enfrentamientos
     */
    private void primeraMitad() throws Exception {
        for (int p = 0; p < jornadas.size()/2; p++) {
            equipos = modeloController.getEquipos();
            hacerEnfrentamiento(p);
            for(Enfrentamiento enfrentamiento : enfrentamientosMitad1){
                equipos.add(enfrentamiento.getEquipo1());
                equipos.add(enfrentamiento.getEquipo2());
            }
        }
    }

    /**
     * Genera los enfrentamientos para la otra mitad 
     * @throws SQLException si ocurre un error con los enfrentamientos
     */
    private void segundaMitad() throws SQLException {
        Random rand = new Random();
        for (int p = jornadas.size()/2; p < jornadas.size(); p++) {
            Enfrentamiento enfrentamiento = new Enfrentamiento();
            enfrentamiento.setHora(elegirHora());
            enfrentamiento.setJornada(jornadas.get(p));
            int enfre = rand.nextInt(enfrentamientosMitad1.size());
            enfrentamiento.setEquipo1(enfrentamientosMitad1.get(enfre).getEquipo2());
            enfrentamiento.setEquipo2(enfrentamientosMitad1.get(enfre).getEquipo1());
            enfrentamientosMitad1.remove(enfre);
            enfrentamientoDAO.anadirEnfrentamientos(enfrentamiento);
        }
    }
    
    /**
     * Genera enfrentamientos para una jornada pero de la primera mitad.
     * @param p inicio de la jornada.
     * @throws Exception si ocurre un error al generar o insertar enfrentamientos
     */
    private void hacerEnfrentamiento(int p) throws Exception{
        for (int i = 0; i <= equipos.size()/2; i++) {
            Enfrentamiento enfrentamiento = new Enfrentamiento();
            enfrentamiento.setHora(elegirHora());
            enfrentamiento.setEquipo1(elegirEquipo(equipos));
            equipos.remove(enfrentamiento.getEquipo1());

            noSeHanEnfrentado(enfrentamiento);

            equipos.remove(enfrentamiento.getEquipo2());
            enfrentamiento.setJornada(jornadas.get(p));
            enfrentamientoDAO.anadirEnfrentamientos(enfrentamiento);
            enfrentamientosMitad1.add(enfrentamiento);
            enfrentamientos.add(enfrentamiento);
        }
    }

    /**
     * Que los equipos seleccionados no se hayan enfrentado anteriormente
     * @param enfrentamiento enfrentamientos que se estan creando
     */
    private void noSeHanEnfrentado(Enfrentamiento enfrentamiento){
        boolean yes = false;
        do {
            enfrentamiento.setEquipo2(elegirEquipo(equipos));
            try{
                for (int j = 1; j <= enfrentamientos.size(); j++) {
                    for (int o = 0; o <= enfrentamientos.size(); o++)
                        if (o != j) {
                            if (enfrentamientos.get(j).getEquipo1() == enfrentamientos.get(o).getEquipo1()
                                    && enfrentamientos.get(j).getEquipo2() == enfrentamientos.get(o).getEquipo2()) {
                                yes = true;
                            } else {
                                o = enfrentamientos.size() + 1;
                                j = enfrentamientos.size() + 1;
                                yes = false;
                            }
                        }
                }
            }catch (Exception e){
                System.out.println();
            }
        }while (yes);
    }

     /**
     * Selecciona una hora
     * @return hora seleccionada
     */
    private LocalTime elegirHora() {
        Random rand = new Random();
        int hora = rand.nextInt(15);
        return LocalTime.of(7, 0, 0).plusHours(hora);
    }

    /**
     * Selecciona aleatoriamente un equipo
     * @param equipos lista de equipos disponibles para elegir
     * @return equipo seleccionado eq1
     */
    private Equipo elegirEquipo(List<Equipo> equipos) {
        Random rand = new Random();
        int eq1 = rand.nextInt(equipos.size());
        return equipos.get(eq1);
    }

    /*
    public void anadirResultado(){
        ArrayList<Enfrentamiento> en = conseguirEq1();
        Enfrentamiento enfrentamiento = conseguirEq2(en);
        ponerResultados(enfrentamiento);
    }
    private ArrayList<Enfrentamiento> conseguirEq1() {
        String[] nombres = equipos.stream().map(Equipo::getNombre).toArray(String[]::new);
        ArrayList<Enfrentamiento> en = new ArrayList<>();
        boolean fallo;
        do {
            String equipoElegido = (String) JOptionPane.showInputDialog(null,
                    "¿Cual es el primer equipo?",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );
            if (equipoElegido == null || equipoElegido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,"El numero no puede ser nulo");
                fallo = true;
            }else {
                enfrentamientos.stream().filter(enfrentamiento -> enfrentamiento.getEquipo1().getNombre().equals(equipoElegido)).forEach(en::add);
                fallo = false;
            }
        }while (fallo);
        return en;
    }
    private Enfrentamiento conseguirEq2(ArrayList<Enfrentamiento> en) {
        ArrayList<String> nombresList = new ArrayList<>();
        for(int i = 0; i < en.size(); i++) {
            equipos.stream().map(Equipo::getNombre).forEach(nombresList::add);
        }
        //es necesario este for? .add añade en la siguiente posicion libre
        String[] nombres = new String[nombresList.size()];
        for (int i = 0; i < nombresList.size(); i++) {
            nombres[i] = nombresList.get(i);
        }
        //este for igual, podemos usar LAMBDA

        Enfrentamiento enfrentamientoReturn = new Enfrentamiento();
        boolean fallo;
        do {
            String equipoElegido = (String) JOptionPane.showInputDialog(null,
                    "¿Cual es el segundo equipo?",
                    "Opciones",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );
            if (equipoElegido == null || equipoElegido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,"El numero no puede ser nulo");
                fallo = true;
            }else {
                enfrentamientoReturn = en.stream().filter(enfrentamiento -> enfrentamiento.getEquipo2().getNombre().equals(equipoElegido)).findFirst().orElse(null);
                fallo = false;
            }
        }while (fallo);
        return enfrentamientoReturn;
    }
    private void ponerResultados(Enfrentamiento enfrentamiento){
        boolean isValid = false;
        Pattern pattern = Pattern.compile("^[0-9]{2}-[0-9]{2}$");
        String var;
        int resultadoEq1 = 0;
        int resultadoEq2 = 0;
        do {
            try {
                var = JOptionPane.showInputDialog(null,"Dime el resultado del partido en el siguiente formato: " + pattern);
                Matcher matcher = pattern.matcher(var);
                if (matcher.matches()) {
                    resultadoEq1 = Integer.parseInt(var.substring(0,2));
                    resultadoEq2 = Integer.parseInt(var.substring(3,5));
                    if (resultadoEq1 == resultadoEq2) {
                        JOptionPane.showMessageDialog(null,"Los equipos no pueden empatar");
                    }else if (resultadoEq1 > 13 || resultadoEq2 > 13) {
                        if (resultadoEq1-resultadoEq2 != 2 && resultadoEq2-resultadoEq1 != 2) {
                            JOptionPane.showMessageDialog(null, "Si los equipos han hecho mas de 12 rondas, tiene que haber diferencia de 2 rondas ganadas entre ellos.");
                        }else
                            isValid = true;
                    }else
                        isValid = true;
                }else {
                    System.out.println("El resultado no utiliza un formato valido");
                }

            }catch (NullPointerException e){
                System.out.println("No se puede ingresar el resultado vacio");
            }
        }while (!isValid);
        enfrentamiento.setResultadosEq1(resultadoEq1);
        enfrentamiento.setResultadosEq2(resultadoEq2);
        if (enfrentamiento.getResultadosEq1() > enfrentamiento.getResultadosEq2())
            enfrentamiento.getEquipo1().setPuntuacion(enfrentamiento.getEquipo1().getPuntuacion()+1);
        else
            enfrentamiento.getEquipo2().setPuntuacion(enfrentamiento.getEquipo2().getPuntuacion()+1);
    }
     */


    /**
     * Devuelve la lista de enfrentamientos generados
     * @return enedrentamientos
     */
    public List<Enfrentamiento> getEnfrentamientos(){
        return enfrentamientos;
    }

    /**
     * Devuelve una jornada teniendo en cuenta su ID
     * @param id identificador de la jornada
     * @return jornada 
     * @throws SQLException si ocurre un error 
     */
    public Jornada getJornadaPorId(int id) throws SQLException{
        return modeloController.getJornadaPorId(id);
    }

    /**
     * Devuelve un equipo teniendo en cuenta su ID
     * @param id identificador del equipo
     * @return equipo 
     * @throws Exception si ocurre un error 
     */
    public Equipo getEquipoPorId(int id) throws Exception{
        return modeloController.getEquipoPorId(id);
    }

    /**
     * Devuelve los enfrentamientos de una jornada 
     * @param j jornada
     * @return  enfrentamientos.
     * @throws Exception si ocurre un error
     */
    public List<Enfrentamiento> enfrentamientos(int j) throws Exception {
        return enfrentamientoDAO.enfrentamientos(j);
    }

    
    /**
     * Devuelve los enfrentamientos de una jornada 
     * @param j  jornada.
     * @return enfrentamientos
     * @throws Exception si ocurre un error al ejecutar 
     */
    public List<String> enfrentamientosProcedimiento(int j) throws Exception {
        return enfrentamientoDAO.enfrentamientosProcedimiento(j);
    }

    /**
     * Actualizar  resultado de enfrentamiento
     * @param enfrentamiento enfrentamientoque hay que actualizar
     * @throws SQLException si ocurre un error al actualizar 
     */
    public void actualizarResultado(Enfrentamiento enfrentamiento) throws SQLException {
        enfrentamientoDAO.actualizarEnfrentamiento(enfrentamiento);
    }
}
