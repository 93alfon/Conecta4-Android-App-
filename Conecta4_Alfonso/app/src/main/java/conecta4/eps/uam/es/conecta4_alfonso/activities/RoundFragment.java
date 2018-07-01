package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * RoundFragment.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepositoryFactory;
import conecta4.eps.uam.es.conecta4_alfonso.model.TableroC4;
import conecta4.eps.uam.es.conecta4_alfonso.views.ViewC4;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * Implementaci&oacute;n del fragmento que muestra el tablero
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundFragment extends Fragment implements PartidaListener
{
    public static final String DEBUG = "DEBUG";

    /** Llave con la cual se guarda en el Bundle el id de la partida */
    public static final String ARG_ROUND_ID = "conecta4.eps.uam.es.conecta4_alfonso.round_id";

    /** Llave con la cual se guarda en el Bundle el nombre del Jugardor 1 */
    public static final String ARG_FIRST_PLAYER_NAME = "conecta4.eps.uam.es.conecta4_alfonso.first_player_name";

    /** Llave con la cual se guarda en el Bundle el titulo de la partida */
    public static final String ARG_ROUND_TITLE = "conecta4.eps.uam.es.conecta4_alfonso.round_title";

    /** Llave con la cual se guarda en el Bundle el numero de columnas de la partida */
    public static final String ARG_ROUND_COLS = "conecta4.eps.uam.es.conecta4_alfonso.round_cols";

    /** Llave con la cual se guarda en el Bundle el numero de filas de la partida */
    public static final String ARG_ROUND_FILAS = "conecta4.eps.uam.es.conecta4_alfonso.round_filas";

    /** Llave con la cual se guarda en el Bundle la fecha en la que se creo la partida */
    public static final String ARG_ROUND_DATE = "conecta4.eps.uam.es.conecta4_alfonso.round_date";

    /** Llave con la cual se guarda el estado del tablero en una string dentro del Bundle */
    public static final String ARG_ROUND_BOARD = "conecta4.eps.uam.es.conecta4_alfonso.round_board";

    /** Llave con la cual se guarda el turno de la partida en una string dentro del Bundle */
    public static final String ARG_ROUND_TURN = "conecta4.eps.uam.es.conecta4_alfonso.turn";

    /** Llave con la cual se guarda el ID del ganador de la partida dentro del Bundle */
    public static final String ARG_ROUND_WINNERUUID = "conecta4.eps.uam.es.conecta4_alfonso.winner";

    private int columnas;
    private int filas;

    private Round round;
    private Partida game;
    private TableroC4 board;
    private Callbacks callbacks;
    private ViewC4 boardView;
    private MediaPlayer mediaPlayer;
    private String roundId, firstPlayerName, roundTitle, roundDate, boardString;
    private Integer turno;
    private String winnerUUID;
    private ValueEventListener listenerRound = null;

    private View viewFragment;


    public interface Callbacks
    {
        void onRoundUpdated(Round round);
    }

    public RoundFragment() { }


    public static RoundFragment newInstance(String roundId, String firstPlayerName, String roundTitle,
                                int roundCols, int roundFilas, String roundDate, String roundBoard, int turno,
                                String winnerUUID)
    {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putInt(ARG_ROUND_COLS, roundCols);
        args.putInt(ARG_ROUND_FILAS, roundFilas);
        args.putString(ARG_ROUND_DATE, roundDate);
        args.putString(ARG_ROUND_BOARD, roundBoard);
        args.putInt(ARG_ROUND_TURN, turno);
        args.putString(ARG_ROUND_WINNERUUID, winnerUUID);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    /**
     * Llamada para hacer la creación inicial de un fragmento
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ROUND_ID)) {
            roundId = getArguments().getString(ARG_ROUND_ID);
        }
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }
        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }
        if (getArguments().containsKey(ARG_ROUND_COLS)) {
            columnas = getArguments().getInt(ARG_ROUND_COLS);
        }
        if (getArguments().containsKey(ARG_ROUND_FILAS)) {
            filas = getArguments().getInt(ARG_ROUND_FILAS);
        }
        if (getArguments().containsKey(ARG_ROUND_DATE)) {
            roundDate = getArguments().getString(ARG_ROUND_DATE);
        }
        if (getArguments().containsKey(ARG_ROUND_BOARD)) {
            boardString = getArguments().getString(ARG_ROUND_BOARD);
        }
        if (getArguments().containsKey(ARG_ROUND_TURN)) {
            turno = getArguments().getInt(ARG_ROUND_TURN);
        }
        if (getArguments().containsKey(ARG_ROUND_WINNERUUID)){
            winnerUUID = getArguments().getString(ARG_ROUND_WINNERUUID);
        }

        if (savedInstanceState != null){
            boardString = savedInstanceState.getString(ARG_ROUND_BOARD);
        }

        if (PreferenceActivityC4.getPlaymusic(getContext()))
        {
            this.mediaPlayer = MediaPlayer.create(getContext(), R.raw.loop_music);
            this.mediaPlayer.setLooping(true);
            this.mediaPlayer.start();
            //this.mediaPlayer.seekTo();
            //this.mediaPlayer.getCurrentPosition();
        }

        /**/

        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        final RoundRepository.UpdateUIRoundCallback callback = new RoundRepository.UpdateUIRoundCallback() {

            @Override
            public void onResponse(String boardstringUpdated, String winnerUUID) {
                boardString = boardstringUpdated;
                startRound();
                boardView.invalidate();

                if(!winnerUUID.equals("-1") && !winnerUUID.equals(PreferenceActivityC4.getPlayerUUID(getContext()))){
                    viewFragment.setBackgroundColor(getContext().getResources().getColor(R.color.looserbackground));
                    new AlertDialogFragment().show(getActivity().getSupportFragmentManager(), "ALERT DIALOG");
                }
            }
        };

        this.listenerRound = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String newboard = dataSnapshot.child("boardstring").getValue(String.class);
                String winnerUUID = dataSnapshot.child("winnerUUID").getValue(String.class);
                //Log.w("DEBUG", "EL WINNER ES : "+ winnerUUID);
                if(callback != null)
                    callback.onResponse(newboard, winnerUUID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        /* Para evitar que siga escuchando si la partida ya esta acabada */
        if(this.winnerUUID.equals("-1"))
            repository.keepListeningRound(roundId, this.listenerRound);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_ROUND_BOARD, board.tableroToString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView;

        rootView = inflater.inflate(R.layout.fragment_round, container, false);
        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(this.roundTitle);

        if(this.winnerUUID.equals(PreferenceActivityC4.getPlayerUUID(getContext()))){
            rootView.setBackgroundColor(getContext().getResources().getColor(R.color.winnerbackground));
        }

        if(!this.winnerUUID.equals("-1") && !this.winnerUUID.equals(PreferenceActivityC4.getPlayerUUID(getContext()))){
            rootView.setBackgroundColor(getContext().getResources().getColor(R.color.looserbackground));
        }

        viewFragment = rootView;

        return rootView;
    }

    /**
     * Llamado cuando el fragmento es visible al usuario
     * Lifecycle de la actividad.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        startRound();
    }

    private void registerListeners()
    {
        FloatingActionButton resetButton = (FloatingActionButton) getView().findViewById(R.id.reset_round_fab);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (board.getEstado() != Tablero.EN_CURSO) {
                    Snackbar.make(getView(), R.string.round_already_finished,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                board.reset();
                boardString = board.tableroToString();
                startRound();
                updateRound();
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.round_restarted,
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();
        boardView.invalidate();
    }

    /**
     * Inicializa la partida
     */
    void startRound()
    {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        LocalPlayerC4 localPlayer = new LocalPlayerC4(getContext(), this.turno);

        //ServerPlayerC4 onlinePlayer = new ServerPlayerC4();

        //players.add(randomPlayer);
        players.add(localPlayer);
        players.add(new Jugador() {
            @Override
            public String getNombre() {
                return null;
            }

            @Override
            public boolean puedeJugar(Tablero tablero) {
                return false;
            }

            @Override
            public void onCambioEnPartida(Evento evento) {

            }
        });

        this.board = new TableroC4();
        try {
            board.stringToTablero(this.boardString);
        } catch (ExcepcionJuego excepcionJuego) {  }

        this.round = createRound();

        game = new Partida(board, players);

        game.addObservador(this);
        localPlayer.setPartida(game);

        this.columnas = board.getColumnas();
        this.filas = board.getFilas();


        boardView = (ViewC4) getView().findViewById(R.id.board_viewC4);
        boardView.setBoard(this.filas, this.columnas, board);
        boardView.setOnPlayListener(localPlayer);

        registerListeners();

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    /**
     * Listener cuando se produce algun cambio causado por el movimiento del jugador local
     * @param evento    Evento que se ha producido
     */
    @Override
    public void onCambioEnPartida(Evento evento) {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();
                updateRound();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                updateRound();
                callbacks.onRoundUpdated(round);
                repository.setWinnerRound(roundId, PreferenceActivityC4.getPlayerUUID(getContext()));
                viewFragment.setBackgroundColor(getContext().getResources().getColor(R.color.winnerbackground));
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(), "ALERT DIALOG");
                break;
        }
    }


    /**
     * Llamado cuando un fragmento se adjunta por primera vez a su contexto
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }


    /**
     * Se invoca cuando el fragmento ya no está vinculado a su actividad.
     * Es llamado despu&eacute;s de onDestroy.
     */
    @Override
    public void onDetach()
    {
        super.onDetach();
        callbacks = null;
    }

    private void updateRound()
    {
        Round round = createRound();

        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Snackbar.make(getView(), R.string.error_updating_round,
                            Snackbar.LENGTH_LONG).show();
            }
        };
        repository.updateRound(round, callback);
    }


    /**
     * Crea una ronda a partir de las variables locales del fragmento
     * @return Ronda creada con los datos
     */
    private Round createRound()
    {
        Round round = new Round(this.columnas, this.filas);

        round.setSecondPlayerUUID(PreferenceActivityC4.getPlayerUUID(getActivity()));
        round.setId(roundId);
        round.setFirstPlayerName("vacio2");
        round.setSecondPlayerName(firstPlayerName);
        round.setDate(roundDate);
        round.setTitle(roundTitle);
        round.setBoard(board);
        return round;
    }


    /**
     * Llamado cuando el fragmento ya no está en uso
     */
    public void onDestroy()
    {
        if (PreferenceActivityC4.getPlaymusic(getContext()) && this.mediaPlayer != null)
            this.mediaPlayer.stop();

        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        repository.deleteListeningRound(this.roundId, this.listenerRound);

        super.onDestroy();
    }


}
