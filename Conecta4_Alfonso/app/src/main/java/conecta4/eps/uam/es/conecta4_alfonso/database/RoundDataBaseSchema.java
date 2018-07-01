package conecta4.eps.uam.es.conecta4_alfonso.database;

/**
 * RoundDataBaseSchema.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

public class RoundDataBaseSchema
{
    /** TABLA PARA GUARDAR LOS USUARIOS*/
    public static final class UserTable
    {
        public static final String NAME = "users";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid1";
            public static final String PLAYERNAME = "playername";
            public static final String PLAYERPASSWORD = "playerpassword";
        }
    }

    /** TABLA PARA GUARDAR LAS RONDAS */
    public static final class RoundTable
    {
        public static final String NAME = "rounds";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid2";
            public static final String ROUNDUUID = "rounduuid";
            public static final String DATE = "date";
            public static final String TITLE = "title";
            public static final String COLUMNAS = "columnas";
            public static final String FILAS = "filas";
            public static final String BOARD = "board";
        }
    }
}
