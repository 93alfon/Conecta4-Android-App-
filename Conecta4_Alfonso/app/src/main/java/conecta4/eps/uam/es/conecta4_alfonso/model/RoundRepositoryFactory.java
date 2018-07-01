package conecta4.eps.uam.es.conecta4_alfonso.model;

/**
 * RoundRepositoryFactory.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;

import conecta4.eps.uam.es.conecta4_alfonso.activities.PreferenceActivityC4;
import conecta4.eps.uam.es.conecta4_alfonso.database.DataBaseC4;
import conecta4.eps.uam.es.conecta4_alfonso.firedatabase.FBDataBase;


/**
 * Implementaci&oacute;n de la clase que crea la base de datos para la aplicacion en funcion de
 * si se dispone de conexion a internet o no.
 *
 * En caso de no disponer de conexi&oacute;n se crea una base de datos SQLite en local, que tambien
 * implementa la interfaz de RoundRepository, de manera que para el resto del codigo no hay diferencia
 * de la base de datos que se haya creado, con ambas se interacciona de la misma manera puesto que
 * amabas implementan la misma interfaz
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class RoundRepositoryFactory {

    /**
     * Contructor de la factoria del repositorio, crea según lo que este indicado en las preferencias
     * una base de datos local (DataBaseC4) o una en linea de FireBase (FBDataBase)
     * Despues de crear la BD la abre llamando al metodo de la interfaz RoundRepository
     * @param context Contexto de la actividad desde la que se ha llamado a este constructor
     * @return El repositorio creado, es decir, la base de datos que hemos creado
     */
    public static RoundRepository createRepository(Context context)
    {
        RoundRepository repository;

        boolean ONLINE = PreferenceActivityC4.getOnline(context);
        //repository = ONLINE ? new FBDataBase() : new DataBaseC4(context);
        repository = ONLINE ? new FBDataBase() : new FBDataBase();

        try {
            repository.open();
        }
        catch (Exception e) {
            return null;
        }
        return repository;
    }

}
