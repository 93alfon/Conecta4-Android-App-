package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * PreferenceActivityC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

import conecta4.eps.uam.es.conecta4_alfonso.R;

/**
 * Implementaci&oacute;n de la actividad encargada de las preferencias de la app
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class PreferenceActivityC4 extends AppCompatActivity implements PreferenceFragmentC4.Callbacks
{
    /** Llave usada para almacenar el tamaño del tablero */
    public final static String BOARDSIZE_KEY = "boardsize";

    /** Tamaño por defecto del tablero */
    public final static String BOARDSIZE_DEFAULT = "7";

    /** Llave usada para almacenar el color de las fichas */
    public final static String PLAYERCOLOR_KEY = "colorplayer";

    /** Color por defecto para las fichas del juego */
    public final static String PLAYERCOLOR_DEFAULT = "2";

    /** Llave usada para almacenar el id del Jugador */
    public final static String PLAYERID_KEY = "playerid";

    /** Identificador por defecto de Jugador*/
    public final static String PLAYERID_DEFAULT = "-1";

    /** Llave usada para almacenar el nombre del Jugador */
    public final static String PLAYERNAME_KEY = "playername";

    /** Nombre por defecto del Jugador */
    public final static String PLAYERNAME_DEFAULT = "defaultnameconecta4";

    /** Llave usada para almacenar la contraseña del jugador */
    public final static String PLAYERPASSWORD_KEY = "playerpassword";

    /** Contraseña por defecto */
    public final static String PLAYERPASSWORD_DEFAULT = "psswc4";

    /** Llave para almacenar el lenguaje de la aplicación */
    public final static String LANGUAGE_KEY = "language_option";

    /** Indicador del lenguaje por defecto de la aplicación */
    public final static String LANGUAGE_DEFAULT = "1";

    /** Llave para almacenar si se dispone de conexion */
    public final static String ONLINE_KEY = "online_status";

    /** Por defecto se considera que no hay coneion */
    public final static Boolean ONLINE_DEFAULT = false;

    /** Llave para almacenar si la musica esta activada o no */
    public final static String PLAYMUSIC_KEY = "play_music";

    /** Valor por defecto sobre la musica, se considera desactivada por defecto */
    public final static Boolean PLAYMUSIC_DEFAULT = false;

    private boolean fromCrate = false;

    /**
     * Metodo ejecutado al crear la actividad
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState)
    {
        this.fromCrate = true;
        setLanguage(Integer.parseInt(PreferenceActivityC4.getLanguage(PreferenceActivityC4.this)));
        this.fromCrate = false;

        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        PreferenceFragmentC4 fragment = new PreferenceFragmentC4();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.settings);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Obtiene el n&uacute;mero de columnas del tablero para el juego, este n&uacute;mero
     * est&aacute; almacenado en el fichero de preferencias
     * @param context Contexto de la actividad
     * @return Cadena con el n&uacute;mero de columnas
     */
    public static String getBoardSize(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }


    /**
     * Obtiene el id perteneciente al conjunto de colores almacenado en el fichero de
     * preferencias
     * @param context Contexto de la actividad
     * @return Cadena de texto con un numero entero del modo de colores almacenado
     */
    public static String getColorPlayer(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYERCOLOR_KEY, PLAYERCOLOR_DEFAULT);
    }

    /**
     * Obtiene el nombre perteneciente al jugador almacenado en el fichero de
     * preferencias
     * @param context Contexto de la actividad
     * @return Cadena con el nombre del jugador
     */
    public static String getPlayerName(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYERNAME_KEY, PLAYERNAME_DEFAULT );
    }

    /**
     * Obtiene el identificador perteneciente al jugador almacenado en el fichero de
     * preferencias
     * @param context Contexto de la actividad
     * @return Cadena con el nombre del jugador
     */
    public static String getPlayerUUID(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYERID_KEY, PLAYERID_DEFAULT );
    }

    public static String getLanguage(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LANGUAGE_KEY, LANGUAGE_DEFAULT );
    }

    /**
     *
     * @param context
     * @return
     */
    @NonNull
    public static Boolean getPlaymusic(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PLAYMUSIC_KEY, PLAYMUSIC_DEFAULT);
    }

    /**
     * Obtiene un booleano para saber si esta usando conexi&oacute;n a internet.
     * @param context   Contexto de la actividad
     * @return True si esta usando conexi&oacute;n a internet, false en caso contrario.
     */
    public static Boolean getOnline(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(ONLINE_KEY, ONLINE_DEFAULT);
    }




    /**
     * Almacena el tamaño del tablero del juego
     * @param context   Contexto de la actividad
     * @param size      Opci&oacute;n del tamaño del tablero
     */
    public static void setBoardsize(Context context, String size)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivityC4.BOARDSIZE_KEY, size);
        editor.commit();
    }


    /**
     * Almacena el id del jugador en el fichero de preferencias
     * @param context   Contexto de la actividad
     * @param id        Identificador a almacenar
     */
    public static void setPlayerUUID(Context context, String id)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivityC4.PLAYERID_KEY, id);
        editor.commit();
    }

    /**
     * Almacena el nombre del jugador en el fichero de preferencias
     * @param context Contexto de la actividad
     * @param name Nombre a almacenar
     */
    public static void setPlayerName(Context context, String name)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivityC4.PLAYERNAME_KEY, name);
        editor.commit();
    }

    /**
     * Almacena la contraseña del juegador en el fichero de preferencias
     * @param context   Contexto de la actividad
     * @param psswd     Contraseña a almacenar
     */
    public static void setPlayerPassword(Context context, String psswd)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivityC4.PLAYERPASSWORD_KEY, psswd);
        editor.commit();
    }


    /**
     * Almacena el estado de la conexion a internet
     * @param context   Conexto de la actividad
     * @param status    Estado de la conexion a internet
     */
    public static void setOnline(Context context, boolean status)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PreferenceActivityC4.ONLINE_KEY, status);
        editor.commit();
    }


    /**
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getApplicationContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());

        if (!this.fromCrate)
            recreate();
    }


    /**
     *
     * @param option
     */
    @Override
    public void setLanguage(int option) {

        Configuration newConfig = new Configuration();
        switch (option){
            case 1:
                newConfig.locale = new Locale("es", "ES");
                break;
            case 2:
                newConfig.locale = Locale.ENGLISH;
                break;
            case 3:
                newConfig.locale = Locale.FRENCH;
                break;
        }

        onConfigurationChanged(newConfig);

    }

    /**
     * Da la misma funcionalidad al boton back de android que el boton back de la toogle bar
     */
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }

}
