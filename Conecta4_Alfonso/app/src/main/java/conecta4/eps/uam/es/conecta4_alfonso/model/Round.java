package conecta4.eps.uam.es.conecta4_alfonso.model;
/**
 * Round.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import java.util.Date;
import java.util.UUID;

/**
 * Implementaci&oacute;n de la ronda, es decir, el tablero con datos como fecha, y titulo para mantener guardada la partida
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class Round {

    private String firstplayername;
    private String firstplayerUUID;
    private String secondplayername;
    private String secondplayerUUID;
    private String id;
    private String title;
    private String date;
    private String winnerUUID;
    private TableroC4 board;


    /**
     * Constructor del elemento Ronda que crea un tablero nuevo
     */
    public Round(int columnas, int filas)
    {
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new TableroC4(filas, columnas);
        winnerUUID = "-1";
    }


    /**
     * Devuelve el ID de la ronda
     * @return Cadena con el ID de la ronda
     */
    public String getId()
    {
        return id;
    }

    /**
     * Establece el ID de la ronda
     * @param id que se va a asignar a la ronda
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Devuelve el titulo de la ronda
     * @return Cadena con el titulo de la ronda
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Establece el titulo de la ronda
     * @param title que se va a asignar a la ronda
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Devuelve la fecha de cracion de la ronda
     * @return Cadena con la fecha de creacion
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Establece la fecha de creacion de la ronda
     * @param date que se va a asignar a la partida
     */
    public void setDate(String date)
    {
        this.date = date;
    }

    /**
     * Devuelve el tablero de la ronda
     * @return instancia al tablero de la ronda
     */
    public TableroC4 getBoard()
    {
        return board;
    }

    /**
     * Establece el tablero de la ronda
     * @param board Tablero que se va a asignar a la ronda
     */
    public void setBoard(TableroC4 board)
    {
        this.board = board;
    }

    /**
     * Obtiene el nombre del primer jugador
     * @return Cadena con el nombre del primer jugador
     */
    public String getFirstplayerName(){ return this.firstplayername; }

    /**
     * Establece el nombre del primer jugador
     * @param name Nombre del primer jugddor
     */
    public void setFirstPlayerName(String name){ this.firstplayername = name; }

    /**
     * Obtiene el nombre del segundo jugador
     * @return Cadena con el nombre del segundo jugador
     */
    public String getSecondPlayerName(){ return this.secondplayername; }

    /**
     * Establece el nombre del segundo jugador de la partida
     * @param name Nombre del Segundo Jugador
     */
    public void  setSecondPlayerName(String name){ this.secondplayername = name; }

    /**
     * Obtiene el ID del primer jugador
     * @return Cadena con el identificador del primer jugador
     */
    public String getFirstplayerUUID() { return firstplayerUUID; }

    /**
     * Establece el ID del primer jugador
     * @param playerUUID Cadena con el identificador del primer usuario
     */
    public void setFirstplayerUUID(String playerUUID) { this.firstplayerUUID = playerUUID; }

    /**
     * Obtiene el ID del segundo Jugador
     * @return Cadena de texto con el identificador de usuario
     */
    public String getSecondPlayerUUID(){ return this.secondplayerUUID; }

    /**
     * Establece el identificador del segundo usuario de la ronda
     * @param playerUUID Cadena con el identificador del segundo usuario
     */
    public void setSecondPlayerUUID(String playerUUID){ this.secondplayerUUID = playerUUID; }

    public Integer getTurnPlayer(String playerUUID){

        if (playerUUID.equals(this.firstplayerUUID))
            return 1;

        if(playerUUID.equals(this.secondplayerUUID))
            return 0;

        return -1;
    }

    public String getWinnerUUID() {
        return winnerUUID;
    }

    public void setWinnerUUID(String playerUUID){
        this.winnerUUID = playerUUID;
    }
}