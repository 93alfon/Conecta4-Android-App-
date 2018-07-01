package conecta4.eps.uam.es.conecta4_alfonso.firedatabase;

/**
 * FBDataBase.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.support.annotation.NonNull;
import android.util.Log;

import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;
import conecta4.eps.uam.es.conecta4_alfonso.model.TableroC4;
import es.uam.eps.multij.ExcepcionJuego;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Implementaci&oacute;n de la base de datos del Juego de FireBase
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class FBDataBase implements RoundRepository
{
    private DatabaseReference db;


    /**
     * Abre la conexion con la base de datos de FireBase
     * @throws Exception
     */
    @Override
    public void open() throws Exception
    {
        this.db = FirebaseDatabase.getInstance().getReference("rounds");
    }


    @Override
    public void close()
    {

    }


    /**
     * Logea a un usuario en la aplicacion, para ello comprueba que el email recibido y la password
     * estan asociadas en la base de usuarios de FireBase
     * @param playername    e-mail del usuario que hace login
     * @param password      Password del usuario que hace login
     * @param callback      Callbacks de la actividad login para informar del resultado
     */
    @Override
    public void login(String playername, String password, final LoginRegisterCallback callback)
    {
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(playername, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d("DEBUG", "signInWithEmail:success");
                            String uuid = firebaseAuth.getCurrentUser().getUid();
                            if (callback != null)
                                callback.onLogin(uuid);

                        }
                        else
                        {
                            Log.w("DEBUG", "signInWithEmail:failure", task.getException());
                            if (callback != null)
                                callback.onError(task.getException().getMessage());
                        }
                    }
                });

    }


    /**
     * Registra un usuario nuevo en la Base de Datos de Firebase, por tanto se necesita un e-mail
     * que este formado correctamente y una password de minimo 6 caracteres, en caso de que uno de
     * estos dos campos no este correctamente se informara a la actividad mediante el metodo onError
     * de la variable callback
     * @param playername    e-mail del usuario a registrar
     * @param password      Password del usuario a registar
     * @param callback      Callback de la actividad login para informar del resultado del registro
     */
    @Override
    public void register(String playername, String password, final LoginRegisterCallback callback)
    {
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(playername, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d("DEBUG", "createUserWithEmail:success");
                            String uuid = firebaseAuth.getCurrentUser().getUid();
                            if(callback != null)
                                callback.onLogin(uuid);

                        }
                        else
                        {
                            Log.w("DEBUG", "createUserWithEmail:failure", task.getException());
                            if(callback != null)
                                callback.onError(task.getException().getMessage());
                        }
                    }
                });

    }

    /**
     * Obtiene las partidas asociadas al usuario y mediante la variable callback se las manda a la actividad
     * que las muestra al usuario
     * @param playeruuid    ID del usuario que se van a mostrar las partidas
     * @param orderByField  Tipo de orden para obtener las rondas
     * @param group         El tipo de agrupamiento a utilizar para las rondas
     * @param callback      Varible callback para mandar la informacion a la actividad
     */
    @Override
    public void getRounds(final String playeruuid, String orderByField, String group, final RoundsCallback callback)
    {
        this.db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        List<Round> rounds = new ArrayList<>();
                        for (DataSnapshot roundsnapshot: dataSnapshot.getChildren())
                        {
                            FBPartida partida = roundsnapshot.getValue(FBPartida.class);

                            if(partida == null)
                                break;

                            if(!partida.getSecondplayerUUID().equals(playeruuid) &&
                                    !partida.getFirstplayerUUID().equals(playeruuid))
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
                            round.setWinnerUUID(partida.getWinnerUUID());

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
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                }
        );

    }


    /**
     * Anade a la Base de Datos una ronda, convierte la ronda recibida en una FBPartida, que es como
     * se almacenan las rondas en la Base de Datos
     * @param round     Ronda a guardar en la Base de Datos
     * @param callback  Variable callback para informar a la actividad si se ha almacenado bien la ronda
     */
    @Override
    public void addRound(final Round round, final BooleanCallback callback)
    {
        final FBPartida partida = new FBPartida(round.getId(), round.getFirstplayerUUID(), round.getSecondPlayerUUID(),
                round.getFirstplayerName(), round.getSecondPlayerName(), round.getTitle(), round.getDate(),
                round.getBoard().tableroToString(), round.getWinnerUUID());


        this.db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        List<Round> rounds = new ArrayList<>();
                        for (DataSnapshot roundsnapshot: dataSnapshot.getChildren())
                        {
                            FBPartida partida = roundsnapshot.getValue(FBPartida.class);

                            if(partida == null)
                                break;

                            /* Partidas que no esten vacias */
                            if(!partida.getSecondplayerUUID().equals("-1") &&
                                    !partida.getFirstplayerUUID().equals("-1"))
                                continue;

                            /* En las que no sea yo quien espera */
                            if(partida.getFirstplayerUUID().equals(round.getSecondPlayerUUID()) ||
                                    partida.getSecondplayerUUID().equals(round.getSecondPlayerUUID()))
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
                            round.setWinnerUUID(partida.getWinnerUUID());

                            rounds.add(round);
                        }

                        /* Para ordenar las rondas por fecha */
                        Collections.sort(rounds, new Comparator<Round>()
                        {
                            @Override
                            public int compare(Round round2, Round round1)
                            {
                                return  round2.getDate().compareTo(round1.getDate());
                            }
                        });

                        if(rounds.isEmpty())
                            db.child(round.getId()).setValue(partida);
                        else{
                            db.child(rounds.get(0).getId()).child("firstplayername").setValue(round.getSecondPlayerName());
                            db.child(rounds.get(0).getId()).child("firstplayerUUID").setValue(round.getSecondPlayerUUID());
                        }

                        if (callback != null)
                            callback.onResponse(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                }
        );


    }


    /**
     * Actualiza la base de datos con la ronda recibida, es decir, busca esa ronda en la base de datos
     * y actualiza sus datos para que refleje siempre todos los cambios.
     * @param round     Ronda que se va a actualizar
     * @param callback  Callbakcs para informar del estado de la actualizacion a la actividad
     */
    @Override
    public void updateRound(final Round round, final BooleanCallback callback)
    {
        this.db.child(round.getId()).child("boardstring").setValue(round.getBoard().tableroToString());

        if (callback != null)
            callback.onResponse(true);

    }


    /**
     * Establece un listener encargado de escuchar indefinidamente los cambios en una
     * determinada partida para mantener el tablero actualizado mientras se juega.
     *
     * AVERTENCIA: Cuando ya no sea necesario estar esuchando usar deleteListeningRound
     * para eliminar el listener.
     * @param roundID       Id de la partida de la que se quiere estar escuchando cambios
     * @param roundListener Listener que se va a establecer
     */
    @Override
    public void keepListeningRound(final String roundID, ValueEventListener roundListener)
    {
        this.db.child(roundID).addValueEventListener(roundListener);
    }


    /**
     * Elimina el listener recibido que se encargaba de escuchar cambios en una determinada partida.
     * @param roundID        Partida sobre la que el listener escuchaba
     * @param roundListener  Listener a eliminar
     */
    @Override
    public void deleteListeningRound(String roundID, ValueEventListener roundListener)
    {
        if(roundListener != null)
            this.db.child(roundID).removeEventListener(roundListener);
    }


    /**
     * Establece un listener indefinido para escuchar los cambios en todas las partidas y mantener
     * la lista de partidas del usuario actualizada.
     *
     * ADVERTENCIA: Cuando ya no sea necesario estar esuchando los cambios en todas las partidas
     * usar deletingListeningRounds para eliminar el listener.
     * @param roundsListener    Listener que se va a establecer
     */
    @Override
    public void keepListeningPlayerRounds(ValueEventListener roundsListener)
    {
        this.db.addValueEventListener(roundsListener);
    }


    /**
     * Elimina el listener encargado de escuchar los cambios en todas las rondas para la lista de partidas.
     * @param roundsListener    Listener a eliminar
     */
    @Override
    public void deletingListeningRounds(ValueEventListener roundsListener)
    {
        if (roundsListener != null)
            this.db.removeEventListener(roundsListener);
    }


    /**
     * Establece el ID del jugador ganador de la partida
     * @param roundID     Identificador de la partida
     * @param playerUUID  Identificador del usuario ganador
     */
    @Override
    public void setWinnerRound(String roundID, String playerUUID)
    {
        this.db.child(roundID).child("winnerUUID").setValue(playerUUID);
    }


}
