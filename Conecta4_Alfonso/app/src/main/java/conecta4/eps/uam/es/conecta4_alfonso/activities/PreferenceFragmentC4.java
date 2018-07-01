package conecta4.eps.uam.es.conecta4_alfonso.activities;

/**
 * PreferenceFragmentC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import conecta4.eps.uam.es.conecta4_alfonso.R;

/**
 * Implementaci&oacute;n del fragmento encargado de mostrar las preferencias de la app
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class PreferenceFragmentC4 extends PreferenceFragmentCompat
{
    private Callbacks callbacks;

    public interface Callbacks
    {
        void setLanguage(int option);
    }

    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.settings, rootKey);
        Preference myPref = (Preference) this.findPreference("language_option");

        myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                callbacks.setLanguage(Integer.parseInt(newValue.toString()));

                return true;
            }
        });
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
}
