/**
 * FBPartida.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

package conecta4.eps.uam.es.conecta4_alfonso.firedatabase;


/**
 * Implementaci&oacute;n de la clase que se va a subir a FB
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class FBPartida
{
    private String firstplayerUUID;
    private String secondplayerUUID;
    private String firstplayername;
    private String secondplayername;
    private String id;
    private String title;
    private String date;
    private String boardstring;
    private String winnerUUID = "-1";

    public FBPartida(){}

    public FBPartida(String id,
                     String firstplayerUUID,
                     String secondplayerUUID,
                     String firstplayername,
                     String secondplayername,
                     String title,
                     String date,
                     String boardstring,
                     String winnerUUID)
    {
        this.firstplayerUUID = firstplayerUUID;
        this.secondplayerUUID = secondplayerUUID;
        this.firstplayername = firstplayername;
        this.secondplayername = secondplayername;
        this.id = id;
        this.title = title;
        this.date = date;
        this.boardstring = boardstring;
        this.winnerUUID = winnerUUID;
    }

    /**
     * Obtiene el ID del primer jugador
     * @return Cadena con el Identificador del primer jugador
     */
    public String getFirstplayerUUID() { return firstplayerUUID; }

    /**
     * Obtiene el ID del segundo jugador
     * @return Cadena con el Identificador del segundo jugador
     */
    public String getSecondplayerUUID()
    {
        return secondplayerUUID;
    }

    /**
     * Obtiene el nombre del primer jugador
     * @return Cadena con el nombre del primer jugador
     */
    public String getFirstplayername()
    {
        return firstplayername;
    }

    /**
     * Obtiene el nombre del segundo jugador
     * @return Cadena con el nombre del segundo jugador
     */
    public String getSecondplayername()
    {
        return secondplayername;
    }

    /**
     * Obtiene el ID de la partida
     * @return Cadena con el ID de la partida
     */
    public String getId()
    {
        return id;
    }

    /**
     * Obtiene el titulo de la partida
     * @return Cadena con el titulo de la partida
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Obtiene la fecha de creación de la partida
     * @return Cadena con la fecha de creacion de la partida
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Obtiene la cadena que representa el tablero de la partida
     * @return Cadena con el tablero
     */
    public String getBoardstring()
    {
        return boardstring;
    }

    /**
     * Obtiene el ID del jugador ganador
     * @return Cadena con el identificador del jugador ganador de la partida
     */
    public String getWinnerUUID() { return winnerUUID; }


}
