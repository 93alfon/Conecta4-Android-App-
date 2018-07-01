package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * LocalPlayerC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.widget.Toast;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.MovimientoC4;
import conecta4.eps.uam.es.conecta4_alfonso.views.ViewC4;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Implementaci&oacute;n del jugador del Conecta4, Jugador que juega con el dispositivo movil
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class LocalPlayerC4 implements ViewC4.OnPlayListener, Jugador
{
    private final Context context;
    private int turno;
    Partida game;


    /**
     * Constructor
     */
    public LocalPlayerC4(Context context, int turno)
    {
        this.context = context;
        this.turno = turno;
    }

    /**
     * Establece la partida a la que va a jugar
     * @param game Instancia del juego sobre la que jugar
     */
    public void setPartida(Partida game)
    {
        this.game = game;
    }

    @Override
    public String getNombre()
    {
        return "Local player";
    }

    @Override
    public boolean puedeJugar(Tablero tablero)
    {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) { }

    /**
     * Metodo que crea el movimiento en funcion de la columna recibida
     * Este se llama desde el callback de ViewC4
     * @param column Columna sobre la que se inserta la ficha
     */
    @Override
    public void onPlay(int column)
    {
        if(game.getTablero().getTurno() != this.turno){
            Toast.makeText(this.context, context.getResources().getString(R.string.not_your_turn),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) { return; }
            MovimientoC4 m;
            m = new MovimientoC4(column);
            game.realizaAccion(new AccionMover(this, m));
        }
        catch (Exception e)
        {

        }
    }
}

