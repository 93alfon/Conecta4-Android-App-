package conecta4.eps.uam.es.conecta4_alfonso.model;

/**
 * RoundRepository.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Implementaci&oacute;n
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public interface RoundRepository
{

    interface LoginRegisterCallback
    {
        void onLogin(String playerUuid);
        void onError(String error);
    }

    interface RoundsCallback
    {
        void onResponse(List<Round> rounds);
        void onError(String error);
    }

    interface BooleanCallback
    {
        void onResponse(boolean ok);
    }

    interface UpdateUIRoundCallback
    {
        void onResponse(String boardstringUpdated, String winnerUUID);
    }


    void open() throws Exception;

    void close();


    /**
     *
     * @param playername
     * @param password
     * @param callback
     */
    void login(String playername, String password, LoginRegisterCallback callback);


    /**
     *
     * @param playername
     * @param password
     * @param callback
     */
    void register(String playername, String password, LoginRegisterCallback callback);


    /**
     *
     * @param playeruuid
     * @param orderByField
     * @param group
     * @param callback
     */
    void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback);

    /**
     *
     * @param round
     * @param callback
     */
    void addRound(Round round, BooleanCallback callback);


    /**
     *
     * @param round
     * @param callback
     */
    void updateRound(Round round, BooleanCallback callback);

    /**
     *
     * @param roundID
     * @param roundListener
     */
    void keepListeningRound(final String roundID,  ValueEventListener roundListener);


    /**
     *
     * @param roundID
     * @param roundListener
     */
    void deleteListeningRound(String roundID, ValueEventListener roundListener);


    /**
     *
     * @param roundsListener
     */
    void keepListeningPlayerRounds(ValueEventListener roundsListener);


    /**
     *
     * @param roundsListener
     */
    void deletingListeningRounds(ValueEventListener roundsListener);

    void setWinnerRound(String roundID, String playerUUID);
}