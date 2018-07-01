package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * RoundListActivity.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Locale;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.Round;


/**
 * Implementaci&oacute;n de la actividad encargada de manejar la lista de partidas
 * Implementa las funciones callback de su correspondiente fragmento y del framgento encargado
 * de mostrar la partida y poder jugar
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks
{
    /**
     * Creacion de la actividad de la lista de rondas
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Configuration newConfig = new Configuration();
        switch (Integer.parseInt(PreferenceActivityC4.getLanguage(RoundListActivity.this))){
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

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        setContentView(R.layout.activity_masterdetail);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        PreferenceManager.setDefaultValues(this,R.xml.settings,false);
    }


    @Override
    public void onNewRoundAdded(Round round)
    {
        /*Toast.makeText(RoundListActivity.this, "el new round added",
                Toast.LENGTH_SHORT).show();*/
    }

    /**
     * Metodo que define la accion cuando se pulsa sobre un ronda de la lista
     * @param round sobre la que se ha pulsado
     */
    @Override
    public void onRoundSelected(Round round)
    {
        int turno = round.getTurnPlayer(PreferenceActivityC4.getPlayerUUID(RoundListActivity.this));

        if (findViewById(R.id.detail_fragment_container) == null)
        {
            Intent intent = RoundActivity.newIntent(this, round.getId(), round.getFirstplayerName(),
                    round.getTitle(), round.getBoard().getColumnas(), round.getBoard().getFilas(),
                    round.getDate(), round.getBoard().tableroToString(), turno, round.getWinnerUUID());
            startActivity(intent);
        }
        else
        {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(), round.getFirstplayerName(),
                    round.getTitle(), round.getBoard().getColumnas(), round.getBoard().getFilas(),
                    round.getDate(), round.getBoard().tableroToString(), turno, round.getWinnerUUID());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Actualiza el elemento cuando se actualiza la partida
     * @param round que se ha actualizado
     */
    @Override
    public void onRoundUpdated(Round round)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }


    /**
     * Se encarga de crear una intencion y cargar la actividad de las preferencias de la aplicación
     */
    @Override
    public void onPreferencesSelected()
    {
        Intent intent = new Intent(this, PreferenceActivityC4.class);
        startActivity(intent);
    }


    /**
     * Se encarga de poner los atributos del usuario que mantienen la sesion iniciada a valores
     * por defecto. Finaliza la actividad y carga la actividad encargada del login y registro
     * de usuarios
     */
    @Override
    public void onLogoutSelected()
    {
        PreferenceActivityC4.setPlayerName(this, PreferenceActivityC4.PLAYERNAME_DEFAULT);
        PreferenceActivityC4.setPlayerUUID(this, PreferenceActivityC4.PLAYERID_DEFAULT);
        PreferenceActivityC4.setPlayerPassword(this, PreferenceActivityC4.PLAYERPASSWORD_DEFAULT);
        PreferenceActivityC4.setBoardsize(this, PreferenceActivityC4.BOARDSIZE_DEFAULT);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getApplicationContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
    }

}
