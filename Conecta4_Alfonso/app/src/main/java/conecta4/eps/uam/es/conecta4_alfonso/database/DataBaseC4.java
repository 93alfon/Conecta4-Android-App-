package conecta4.eps.uam.es.conecta4_alfonso.database;

/**
 * DataBaseC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;

import static conecta4.eps.uam.es.conecta4_alfonso.database.RoundDataBaseSchema.RoundTable;
import static conecta4.eps.uam.es.conecta4_alfonso.database.RoundDataBaseSchema.UserTable;


/**
 * Implementaci&oacute;n de la base de datos del Juego
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class DataBaseC4 implements RoundRepository
{
    private final String DEBUG_TAG = "DEBUG";
    private static final String DATABASE_NAME = "c4.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper helper;
    private SQLiteDatabase db;


    /**
     * Constructor para la base de datos local de Conecta4
     * @param context
     */
    public DataBaseC4(Context context)
    {
        this.helper = new DatabaseHelper(context);
        this.open();
        this.helper.onCreate(this.db);
    }

    /**
     *
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            createTable(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.NAME);
            createTable(db);
        }

        private void createTable(SQLiteDatabase db)
        {
            String str1 = "CREATE TABLE IF NOT EXISTS " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);";

            String str2 = "CREATE TABLE IF NOT EXISTS " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES "+UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.COLUMNAS + " TEXT, "
                    + RoundTable.Cols.FILAS + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);";

            try {
                db.execSQL(str1);
                db.execSQL(str2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Apertura de la Base de datos
     * @throws SQLException
     */
    @Override
    public void open() throws SQLException
    {
        db = helper.getWritableDatabase();
    }


    /**
     * Cierra la Base de Datos de SQLite
     */
    public void close()
    {
        db.close();
    }


    /**
     * Metodo encargado del login de usuarios en la base de datos,  comprueba que existe el usuario
     * con la contrasena asociada igual a la recibida
     * @param playername        Nombre del usuario que hace login
     * @param playerpassword    Password del usuario que hace login
     * @param callback          Calbacks de login
     */
    @Override
    public void login(String playername, String playerpassword, LoginRegisterCallback callback)
    {
        Log.d(DEBUG_TAG, "Login " + playername);

        Cursor cursor = db.query(UserTable.NAME,
                new String[]{UserTable.Cols.PLAYERUUID},
                UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?",
                new String[]{playername, playerpassword}, null, null, null);

        int count = cursor.getCount();
        String uuid = count == 1 && cursor.moveToFirst() ? cursor.getString(0) : "";

        cursor.close();

        if (count == 1)
            callback.onLogin(uuid);
        else
            callback.onError("Username or password incorrect.");
    }


    /**
     * Metodo encargado de registrar usuarios nuevos en la base de datos SQL
     * @param playername    Nombre del Jugador a registrar
     * @param password      Password del Jugador a registrar
     * @param callback      Variable callback para los metodos de login
     */
    @Override
    public void register(String playername, String password, LoginRegisterCallback callback)
    {
        ContentValues values = new ContentValues();
        String uuid = UUID.randomUUID().toString();
        values.put(UserTable.Cols.PLAYERUUID, uuid);
        values.put(UserTable.Cols.PLAYERNAME, playername);
        values.put(UserTable.Cols.PLAYERPASSWORD, password);
        long id = db.insert(UserTable.NAME, null, values);
        if (id < 0)
            callback.onError("Error inserting new player named " + playername);
        else
            callback.onLogin(uuid);
    }

    private ContentValues getContentValues(Round round)
    {
        ContentValues values = new ContentValues();
        values.put(RoundTable.Cols.PLAYERUUID, round.getSecondPlayerUUID());
        values.put(RoundTable.Cols.ROUNDUUID, round.getId());
        values.put(RoundTable.Cols.DATE, round.getDate());
        values.put(RoundTable.Cols.TITLE, round.getTitle());
        values.put(RoundTable.Cols.COLUMNAS, round.getBoard().getColumnas());
        values.put(RoundTable.Cols.FILAS, round.getBoard().getFilas());
        values.put(RoundTable.Cols.BOARD, round.getBoard().tableroToString());
        return values;
    }


    /**
     * Metodo encargado de anadir una partida nueva a la base de datos
     * @param round     Partida a anadir
     * @param callback  Callbacks
     */
    @Override
    public void addRound(Round round, BooleanCallback callback)
    {
        ContentValues values = getContentValues(round);
        long id = db.insert(RoundTable.NAME, null, values);

        if (callback != null)
            callback.onResponse(id >= 0);
    }


    /**
     * Actualiza la ronda en la Base de Datos de SQLite
     * @param round     Ronda a actualizar en la Base de Datos
     * @param callback  Callback para informar a la actividad de la correcta actualizacion de la ronda
     */
    @Override
    public void updateRound(Round round, BooleanCallback callback)
    {
        ContentValues values = getContentValues(round);
        int id = db.update(RoundTable.NAME, values, RoundTable.Cols.ROUNDUUID + " = ?",
                new String[] { round.getId() });

        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void keepListeningRound(String roundID, ValueEventListener roundListener)
    {
        /* No es necesario en una base local */
    }

    @Override
    public void deleteListeningRound(String roundID,  ValueEventListener roundListener)
    {
        /* No es necesario en una base local */
    }

    @Override
    public void keepListeningPlayerRounds(ValueEventListener roundsListener)
    {
        /* No es necesario en una base local */
    }

    @Override
    public void deletingListeningRounds(ValueEventListener roundsListener)
    {
        /* No es necesario en una base local */
    }

    @Override
    public void setWinnerRound(String roundID, String playerUUID)
    {

    }


    /**
     * Consulta SQL para obtener las partidas de un usuario
     * @return Cursor SQL
     */
    private RoundCursorWrapper queryRounds()
    {
        String sql = "SELECT " + UserTable.Cols.PLAYERNAME + ", " +
                UserTable.Cols.PLAYERUUID + ", " +
                RoundTable.Cols.ROUNDUUID + ", " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.TITLE + ", " +
                RoundTable.Cols.COLUMNAS + ", " +
                RoundTable.Cols.FILAS + ", " +
                RoundTable.Cols.BOARD + " " +
                "FROM " + UserTable.NAME + " AS p, " +
                RoundTable.NAME + " AS r " +
                "WHERE " + "p." + UserTable.Cols.PLAYERUUID + "=" +
                "r." + RoundTable.Cols.PLAYERUUID + ";";
        Cursor cursor = db.rawQuery(sql, null);
        return new RoundCursorWrapper(cursor);
    }


    /**
     * Obtiene el conjunto de rondas pertenecientes al jugador
     *
     * @param playeruuid    Id del jugador del que se quiere obtener sus partidas
     * @param orderByField  Clausula de orden (generalmente por fecha de creación)
     * @param group         Agrupacion para la consulta
     * @param callback      Callbacks para poder actualizar la lista con el resultado de la consulta
     */
    @Override
    public void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback)
    {
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Round round = cursor.getRound();
            if (round.getSecondPlayerUUID().equals(playeruuid))
                rounds.add(round);
            cursor.moveToNext();
        }
        cursor.close();

        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError("No rounds found in database");
    }

}
