package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * LoginActivity.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepository;
import conecta4.eps.uam.es.conecta4_alfonso.model.RoundRepositoryFactory;

/**
 * Implementaci&oacute;n de la actividad encargada del login de usuarios
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;


    public void onCreate(Bundle savedInstanceState)
    {
        Configuration newConfig = new Configuration();
        switch (Integer.parseInt(PreferenceActivityC4.getLanguage(LoginActivity.this))){
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
        setContentView(R.layout.activity_login);
        if (!PreferenceActivityC4.getPlayerName(this).
                equals(PreferenceActivityC4.PLAYERNAME_DEFAULT)){
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }

        PreferenceActivityC4.setOnline(this, false);
        if (isOnline())
            PreferenceActivityC4.setOnline(this, true);
        else
            Toast.makeText(LoginActivity.this, R.string.no_internet,
                    Toast.LENGTH_SHORT).show();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        usernameEditText = (EditText) findViewById(R.id.login_username);
        //if(PreferenceActivityC4.getOnline(this))
        usernameEditText.setHint(R.string.login_email_hint);

        passwordEditText = (EditText) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        Button newUserButton = (Button) findViewById(R.id.new_user_button);
        newUserButton.setOnClickListener(this);
        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();

        /* Mismo color para el fondo por si el layout no ocupa toda la pantalla */
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.colorBackgroundLogin));

        /* Color Blanco a la barra de progreso para contrastar con el fondo azul */
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN );
    }


    /**
     *
     * @param v
     */
    public void onClick(View v)
    {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        /* Esto es la implementacion de los metodos de la interfaz que hay dentro de la factoria */
        RoundRepository.LoginRegisterCallback loginRegisterCallback = new
                RoundRepository.LoginRegisterCallback()
                {
                    @Override
                    public void onLogin(String playerId)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        PreferenceActivityC4.setPlayerUUID(LoginActivity.this, playerId);
                        PreferenceActivityC4.setPlayerName(LoginActivity.this, playername);
                        PreferenceActivityC4.setPlayerPassword(LoginActivity.this, password);
                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error)
                    {
                        String errorMessage = null;
                        switch (error){
                            case "The email address is badly formatted.":
                                errorMessage = LoginActivity.this.getResources().getString(R.string.bad_address);
                                break;
                            case "The given password is invalid. [ Password should be at least 6 characters ]":
                                errorMessage = LoginActivity.this.getResources().getString(R.string.invalid_password);
                                break;
                            case "The password is invalid or the user does not have a password.":
                                errorMessage = LoginActivity.this.getResources().getString(R.string.bad_password);
                                break;
                            case "The email address is already in use by another account.":
                                errorMessage = LoginActivity.this.getResources().getString(R.string.email_in_use);
                                break;
                            default:
                                errorMessage = error;
                        }

                        progressBar.setVisibility(View.INVISIBLE);

                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle(R.string.login_alert_dialog_title)
                                .setMessage(errorMessage)
                                .setNeutralButton(R.string.ok_dialog,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                    }
                };

        switch (v.getId())
        {
            case R.id.login_button:
                if(playername.equals("") || password.equals(""))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.login_alert_dialog_title)
                            .setMessage(R.string.login_alert_dialog_message)
                            .setNeutralButton(R.string.ok_dialog,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                }
                else
                {
                    repository.login(playername, password, loginRegisterCallback);
                }
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.new_user_button:
                if(playername.equals("") || password.equals(""))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.login_alert_dialog_title)
                            .setMessage(R.string.register_alert_dialog_message)
                            .setNeutralButton(R.string.ok_dialog,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                }
                else
                {
                    repository.register(playername, password, loginRegisterCallback);
                }

                break;
        }

    }

    /**
     * Metodo para comprobar si se dispone de conexion a internet
     * @return True si hay internet, false en caso contrario
     */
    public boolean isOnline()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
