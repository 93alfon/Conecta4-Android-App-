package conecta4.eps.uam.es.conecta4_alfonso.database;

/**
 * RoundCursorWrapper.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import es.uam.eps.multij.ExcepcionJuego;

/**
 * Implementaci&oacute;n
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundCursorWrapper extends CursorWrapper
{
    private String DEBUG = "DEBUG";

    public RoundCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    /**
     * Obtiene una ronda en funcion de los argumentos obtenidos internamente
     * @return Objeto Round perteneciente a los argumentos obtenidos
     */
    public Round getRound()
    {
        String playername = getString(getColumnIndex(RoundDataBaseSchema.UserTable.Cols.PLAYERNAME));
        String playeruuid = getString(getColumnIndex(RoundDataBaseSchema.UserTable.Cols.PLAYERUUID));
        String rounduuid = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.ROUNDUUID));
        String date = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.DATE));
        String title = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.TITLE));
        String columnas = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.COLUMNAS));
        String filas = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.FILAS));
        String board = getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.BOARD));

        Round round = new Round(Integer.parseInt(columnas), Integer.parseInt(filas));
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(playername);
        round.setSecondPlayerUUID(playeruuid);
        round.setId(rounduuid);
        round.setDate(date);
        round.setTitle(title);

        try {
            round.getBoard().stringToTablero(board);
        } catch (ExcepcionJuego e) {
            Log.d(DEBUG, "Error turning string into tablero");
        }

        return round;
    }
}
