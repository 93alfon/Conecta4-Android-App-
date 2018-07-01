package conecta4.eps.uam.es.conecta4_alfonso.model;

/**
 * MovimientoC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import es.uam.eps.multij.*;

/**
 * Implementacion del movimiento para el Juego Conecta 4
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class MovimientoC4 extends Movimiento
{
    private int col;

    /**
     * Contructor de un movimiento
     * @param columna sobre la que se realiza el movimiento
     */
    public MovimientoC4(int columna)
    {
        this.col =  columna;
    }

    /**
     *
     */
    @Override
    public String toString()
    {
        return "Movimiento:" + this.col;
    }

    /**
     * Compara si dos movimientos son iguales
     */
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(this.toString().compareTo(o.toString()) == 0)
            return true;

        return false;
    }

    /**
     * Obtiene la columna sobre la que se mueve la ficha
     *
     * @return número de columna
     */
    public int getColumna()
    {
        return this.col;
    }

}