package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * AlertDialogFragment.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.Round;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepositoryFactory;

/**
 * Implementaci&oacute;n del cuadro de dialogo a mostrar cuando se gana una partida
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class AlertDialogFragment extends DialogFragment
{
    /**
     * Sobreescribe el metodo para poner el mensaje, las opciones y las acciones que conlleva
     * el cuadro de dialogo de ganar partida
     * @param savedInstanceState Contenido guardado
     * @return Dialogo creado
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.game_over);
        alertDialogBuilder.setMessage(R.string.game_over_message);

        alertDialogBuilder.setPositiveButton(R.string.yes_dialog,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int size = Integer.parseInt(PreferenceActivityC4.getBoardSize(getContext()));
                        int cols = size;
                        int filas = size-1;

                        Round round = new Round(cols, filas);

                        round.setFirstPlayerName("random");
                        round.setFirstplayerUUID("-1");

                        round.setSecondPlayerUUID(PreferenceActivityC4.getPlayerUUID(getActivity()));
                        round.setSecondPlayerName(PreferenceActivityC4.getPlayerName(getActivity()));

                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

                        RoundRepository.BooleanCallback callback = new
                                RoundRepository.BooleanCallback() {
                                    @Override
                                    public void onResponse(boolean response) {
                                        if (response == false)
                                            Snackbar.make(getView(), R.string.error_adding_round,
                                                    Snackbar.LENGTH_LONG).show();
                                        /*else
                                            callbacks.onNewRoundAdded(round);*/
                                    }
                                };

                        repository.addRound(round, callback);
                        if (activity instanceof RoundListActivity)
                            ((RoundListActivity) activity).onRoundUpdated(round);
                        else
                            ((RoundActivity) activity).finish();
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.no_dialog,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    if (activity instanceof RoundActivity)
                        activity.finish();
                    dialog.dismiss();
                    }
        });

        return alertDialogBuilder.create();
    }

}