package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * RoundListFragment.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.firedatabase.FBPartida;
import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepositoryFactory;
import conecta4.eps.uam.es.conecta4_alfonso.model.TableroC4;
import conecta4.eps.uam.es.conecta4_alfonso.views.ViewC4;
import es.uam.eps.multij.ExcepcionJuego;


/**
 * Implementaci&oacute;n del fragmento encargado de mostrar la lista de partidas
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundListFragment extends Fragment
{
    private RecyclerView roundRecyclerView;
    private ProgressBar progressBar;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;

    private ValueEventListener listenerRounds = null;

    /**
     *
     * @param bundle
     */
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setHasOptionsMenu(true);

        /* Establezco el escuchador */
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

        final RoundRepository.RoundsCallback callback = new RoundRepository.RoundsCallback()
        {
            @Override
            public void onResponse(List<Round> rounds) {
                if (roundAdapter == null) {
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.setRounds(rounds);
                    roundAdapter.notifyDataSetChanged();
                }

                /*Cuando acaba se oculta la barra de cargar */
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(String error) {
                Log.d("DEBUG", "No rounds in database");
            }
        };


        /*Creo el escuchador*/
        this.listenerRounds = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                List<Round> rounds = new ArrayList<>();
                for (DataSnapshot roundsnapshot: dataSnapshot.getChildren())
                {
                    FBPartida partida = roundsnapshot.getValue(FBPartida.class);

                    if(partida == null)
                        break;

                    if(!partida.getSecondplayerUUID().equals(PreferenceActivityC4.getPlayerUUID(getContext())) &&
                            !partida.getFirstplayerUUID().equals(PreferenceActivityC4.getPlayerUUID(getContext())))
                        continue;

                    TableroC4 board = new TableroC4();

                    try {
                        board.stringToTablero(partida.getBoardstring());
                    } catch (ExcepcionJuego excepcionJuego) { }

                    Round round = new Round(board.getColumnas(), board.getFilas());
                    round.setBoard(board);
                    round.setDate(partida.getDate());
                    round.setTitle(partida.getTitle());
                    round.setFirstPlayerName(partida.getFirstplayername());
                    round.setSecondPlayerName(partida.getSecondplayername());
                    round.setId(partida.getId());
                    round.setSecondPlayerUUID(partida.getSecondplayerUUID());
                    round.setFirstplayerUUID(partida.getFirstplayerUUID());

                    rounds.add(round);
                }

                /* Para ordenar las rondas por fecha */
                Collections.sort(rounds, new Comparator<Round>()
                {
                    @Override
                    public int compare(Round round2, Round round1)
                    {
                        return  round1.getDate().compareTo(round2.getDate());
                    }
                });

                if (callback != null)
                    callback.onResponse(rounds);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        repository.keepListeningPlayerRounds(this.listenerRounds);

    }


    /**
     * Inicializa el contenido del Fragmento contenedor con las opciones del menu.
     * Debes meter tus elementos en <var>menu</var>.
     * @param menu Barra de Menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        MenuItem item = menu.getItem(2);
        SpannableString s = new SpannableString(getResources().getString(R.string.settings));
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);

        item = menu.getItem(1);
        s = new SpannableString(getResources().getString(R.string.logout));
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);
    }


    /**
     * Se llama a este metodo cada vez que se selecciona un elemento en su menú de opciones.
     * Para cada opción del menú se ejecuta una acción distinta:
     * - Para crear una partida nueva se llama a la Base de Datos para crearla y se muestra
     * en la lista de partidass
     *
     * - Para las preferencias se carga la actividad encargada de mostrarlas
     * - Para cerrar sesion se llama al metodo de la actividad RoundList encargado de hacerlo
     *
     * @param item El elemento del men&uacute; seleccionado
     *
     * @return true si no hubo ningún fallo, false en caso contrario
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int size = Integer.parseInt(PreferenceActivityC4.getBoardSize(getContext()));
        int cols = size;
        int filas = size-1;

        switch (item.getItemId()) {
            case R.id.menu_item_new_round:

                final Round round = new Round(cols, filas);
                round.setSecondPlayerUUID(PreferenceActivityC4.getPlayerUUID(getActivity()));

                if(PreferenceActivityC4.getOnline(getContext())){
                    round.setFirstPlayerName("Vacio");
                    round.setFirstplayerUUID("-1");
                }
                else{
                    round.setFirstplayerUUID("0");
                    round.setFirstPlayerName("random");
                }


                round.setSecondPlayerName(PreferenceActivityC4.getPlayerName(getActivity()));
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

                RoundRepository.BooleanCallback callback = new
                        RoundRepository.BooleanCallback() {
                            @Override
                            public void onResponse(boolean response) {
                                if (response == false)
                                    Snackbar.make(getView(), R.string.error_adding_round,
                                            Snackbar.LENGTH_LONG).show();
                                else
                                    callbacks.onNewRoundAdded(round);
                            }
                        };

                repository.addRound(round, callback);
                updateUI();

                return true;
            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;

            case R.id.menu_item_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_question)
                        .setPositiveButton(R.string.yes_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        callbacks.onLogoutSelected();
                                    }
                                })
                        .setNegativeButton(R.string.no_dialog,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Interfaz para las actualizaciones
     */
    public interface Callbacks
    {
        /**
         * Cuando creas una ronda nueva
         * @param round
         */
        void onNewRoundAdded(Round round);

        /**
         * Cuando seleccionas una ronda para poder jugar
         * @param round Ronda Seleccionada
         */
        void onRoundSelected(Round round);

        /**
         * Abrir la actividad de preferencias de la app
         */
        void onPreferencesSelected();

        /**
         * Metodo para cerrar la sesion del usuario
         */
        void onLogoutSelected();
    }


    /**
     * Se llama cuando el fragmento se asocia por primera vez a su contexto y establece
     * la variable de callback para porder usarla en llamadas
     * @param context
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }


    /**
     * Se invoca cuando el fragmento ya no está vinculado a su actividad
     */
    @Override
    public void onDetach()
    {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Creacion de la vista de la lista de partidas
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Vista creada con la lista de partidas
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setCardListener(view);
        updateUI();

        return view;
    }


    private void setCardListener(View view)
    {
        roundRecyclerView = (RecyclerView)view.findViewById(R.id.round_recycler_view);

        RecyclerItemClickListener.OnItemClickListener onItemClickListener =
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
                        {
                            @Override
                            public void onResponse(List<Round> rounds) {
                                //callbacks.onRoundSelected(rounds.get(rounds.size() - position - 1));
                                if(callbacks != null)
                                    callbacks.onRoundSelected(rounds.get(position));
                            }
                            @Override
                            public void onError(String error) {
                                Snackbar.make(getView(), R.string.error_reading_rounds,
                                        Snackbar.LENGTH_LONG).show();
                            }
                        };
                        String playeruuid = PreferenceActivityC4.getPlayerUUID(getActivity());
                        repository.getRounds(playeruuid, null, null, roundsCallback);
                    }
                };
        RecyclerItemClickListener listener =
                new RecyclerItemClickListener(getActivity(), onItemClickListener);
        roundRecyclerView.addOnItemTouchListener(listener);
    }


    /**
     * Se invoca cuando el fragmento es visible para el usuario y se está ejecutando activamente.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }


    @Override
    public void onDestroy()
    {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        repository.deletingListeningRounds(this.listenerRounds);
        super.onDestroy();
    }


    public void updateUI()
    {
        progressBar.setVisibility(View.VISIBLE);
        List<Round> rounds = new ArrayList<>();
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
        {
            @Override
            public void onResponse(List<Round> rounds) {
                if (roundAdapter == null) {
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.setRounds(rounds);
                    roundAdapter.notifyDataSetChanged();
                }

                /*Cuando acaba se oculta la barra de cargar */
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(String error) {
                Log.d("DEBUG", "No rounds in database");
            }
        };
        String playeruuid = PreferenceActivityC4.getPlayerUUID(getActivity());

        repository.getRounds(playeruuid, null, null, roundsCallback);

    }



    /* CLASE INTERNA NIVEL 1*/
    public class RoundAdapter extends RecyclerView.Adapter<RoundListFragment.RoundAdapter.RoundHolder>
    {
        private List<Round> rounds;

        /* Clase RoundHolder interna 2*/
        public class RoundHolder extends RecyclerView.ViewHolder
        {
            private TextView idTextView;
            private TextView playersTextView;
            private TextView dateTextView;
            private ViewC4 boardView;

            private Round round;


            public RoundHolder(View itemView)
            {
                super(itemView);
                //itemView.setOnClickListener(this);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                boardView = (ViewC4) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
                playersTextView = (TextView) itemView.findViewById(R.id.list_item_players);
            }


            public void bindRound(Round round)
            {
                this.round = round;
                idTextView.setText(round.getTitle());

                TableroC4 board = round.getBoard();



                boardView.setBoard(board.getFilas(), board.getColumnas(), board);
                boardView.invalidate();
                dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));

                String versus = "";

                if (round.getFirstplayerName().length() > 8)
                    versus += round.getFirstplayerName().substring(0,8) + "...";
                else
                    versus += round.getFirstplayerName();

                versus += " vs ";
                if (round.getSecondPlayerName().length() > 8)
                    versus += round.getSecondPlayerName().substring(0,8) + "...";
                else
                    versus += round.getSecondPlayerName();


                if (round.getFirstplayerUUID().equals("-1") || round.getSecondPlayerUUID().equals("-1"))
                    versus = getResources().getString(R.string.waiting_opponent);


                playersTextView.setText(versus);
            }


        }
        /* Fin de la clase interna 2 */


        public RoundAdapter(List<Round> rounds)
        {
            this.rounds = rounds;
        }

        @Override
        public RoundListFragment.RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new RoundListFragment.RoundAdapter.RoundHolder(view);
        }


        /**
         * Llamado por RecyclerView para mostrar los datos especificos de una posicion. Este metodo
         * actualiza el contenido para mostrar el del elemento recibido
         *
         * @param holder El ViewHolder que se va a actualizar para representar los nuevos datos.
         * @param position La posición del elemento dentro del conjunto de datos del adaptador.
         */
        @Override
        public void onBindViewHolder(RoundListFragment.RoundAdapter.RoundHolder holder, int position)
        {

            Round round = rounds.get(position);
            //Round round = rounds.get(this.rounds.size() - position - 1);

            holder.bindRound(round);
            holder.itemView.invalidate();
        }

        @Override
        public int getItemCount()
        {
            return rounds.size();
        }

        /**
         * Establece el conjunto de datos del adaptador
         * @param rounds Lista de rondas que forman el conjunto de datos para el adaptador
         */
        public void setRounds(List<Round> rounds){ this.rounds = rounds; }

    }

}
