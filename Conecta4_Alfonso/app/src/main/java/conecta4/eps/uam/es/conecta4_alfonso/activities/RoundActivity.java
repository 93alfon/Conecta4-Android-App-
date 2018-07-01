package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * RoundActivity.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.Round;

/**
 * Implementaci&oacute;n de la actividad encargada del tablero
 * Esta actividad implementa las funciones callback de su correspondiente fragmento
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks
{
    /** Valor llave para guardar y obtener el id de la partida */
    public static final String EXTRA_ROUND_ID = "conecta4.eps.uam.es.conecta4_alfonso.round_id";

    /** Valor llave para guardar y obtener el nombre del jugador */
    public static final String EXTRA_FIRST_PLAYER_NAME = "conecta4.eps.uam.es.conecta4_alfonso.first_player_name";

    /** Valor llave para guardar y obtener el t&iacute;tulo de la partida */
    public static final String EXTRA_ROUND_TITLE = "conecta4.eps.uam.es.conecta4_alfonso.round_title";

    /** Valor llave para guardar y obtener el n&uacute;mero de columnas de la partida*/
    public static final String EXTRA_ROUND_COLS = "conecta4.eps.uam.es.conecta4_alfonso.round_cols";

    /** Valor llave para guardar y obtener el n&uacute;mero de filas de la partida */
    public static final String EXTRA_ROUND_FILAS = "conecta4.eps.uam.es.conecta4_alfonso.round_filas";

    /** Valor llave para guardar y obtener la fecha en la que se cre&oacute; la partida */
    public static final String EXTRA_ROUND_DATE = "conecta4.eps.uam.es.conecta4_alfonso.round_date";

    /** Valor llave para guardar y obtener el tablero como una cadena de caracteres */
    public static final String EXTRA_ROUND_BOARD = "conecta4.eps.uam.es.conecta4_alfonso.round_board";

    /** */
    public static final String EXTRA_ROUND_TURN = "conecta4.eps.uam.es.conecta4_alfonso.turn";

    /** */
    public static final String EXTRA_ROUND_WINNER = "conecta4.eps.uam.es.conecta4_alfonso.winner";


    /**
     * Creacion de la actividad de la ronda que se va a jugar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Configuration newConfig = new Configuration();
        switch (Integer.parseInt(PreferenceActivityC4.getLanguage(RoundActivity.this))){
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

        super.onCreate(savedInstanceState);

        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            String firstplayername = getIntent().getStringExtra(EXTRA_FIRST_PLAYER_NAME);
            String roundTitle = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            int roundcols = getIntent().getIntExtra(EXTRA_ROUND_COLS, 0);
            int roundfilas = getIntent().getIntExtra(EXTRA_ROUND_FILAS, 0);
            String roundDate = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String roundBoard = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            int turno = getIntent().getIntExtra(EXTRA_ROUND_TURN, 0);
            String winnerUUID = getIntent().getStringExtra(EXTRA_ROUND_WINNER);

            /*MUY RARO*/
            RoundFragment roundFragment = RoundFragment.newInstance(roundId, firstplayername,roundTitle,
                    roundcols, roundfilas, roundDate, roundBoard, turno, winnerUUID);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /*MAS ARGUMENTOS BY ME*/
    public static Intent newIntent(Context packageContext, String roundId, String firstPlayerName,
                            String roundTitle, int roundcols, int roundfilas, String roundDate,
                            String roundBoard, int turno, String winnerUUID)
    {
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        intent.putExtra(EXTRA_FIRST_PLAYER_NAME, firstPlayerName);
        intent.putExtra(EXTRA_ROUND_TITLE, roundTitle);
        intent.putExtra(EXTRA_ROUND_COLS, roundcols);
        intent.putExtra(EXTRA_ROUND_FILAS, roundfilas);
        intent.putExtra(EXTRA_ROUND_DATE, roundDate);
        intent.putExtra(EXTRA_ROUND_BOARD, roundBoard);
        intent.putExtra(EXTRA_ROUND_TURN, turno);
        intent.putExtra(EXTRA_ROUND_WINNER, winnerUUID);



        return intent;
    }

    @Override
    public void onRoundUpdated(Round round)
    {
        /* Vacio porque cuando se ejecuta esta actividad la lista de partidas no puede estar visible */
    }

    /**
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        getApplicationContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
    }

}