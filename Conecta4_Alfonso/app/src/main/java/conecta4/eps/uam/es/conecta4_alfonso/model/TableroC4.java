package conecta4.eps.uam.es.conecta4_alfonso.model;

/**
 * TableroC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import java.util.ArrayList;
import es.uam.eps.multij.*;

/**
 * Implementaci&oacute;n del tablero para Conecta 4, extensi&oacute;n de la clase tablero que implenta uno gen&eacute;rico.
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class TableroC4 extends Tablero
{
    /** Valor que toman las celdas cuando el Jugador 1 coloca su ficha */
    public static final int JUGADOR1 = 1;

    /** Valor que toman las celdas cuando el Jugador 2 coloca su ficha */
    public static final int JUGADOR2 = 2;

    /** Valor que toman las celdas cuando estan vacios*/
    public static final int VACIO = 0;

    /** Cadena que identifica cuando gana el Jugador 1 */
    public static final String JUGADOR1_WIN = "1111";

    /** Cadena que identifica cuando gana el Jugador 2 */
    public static final String JUGADOR2_WIN = "2222";


    /** Numero de filas del tablero */
    private int filas;

    /** Numero de columnas del tablero */
    private int columnas;

    /**  Matriz contenedora de los valores de las celdas del tablero */
    private int tablero[][];


    /**
     * Constructor de tamaño personalizado para el tablero
     * @param filas
     * @param celdas
     */
    public TableroC4(int filas, int celdas)
    {
        this.estado = EN_CURSO;
        this.turno = 0;

        if (filas < 4)
            this.filas = 4;
        else
            this.filas = filas;


        if (celdas < 4)
            this.columnas = 4;
        else
            this.columnas = celdas;

        this.tablero = new int[this.filas][this.columnas];
    }


    /**
     * Constructor de un tablero de tamaño estandar para Conecta 4 (6x7)
     */
    public TableroC4()
    {
        this.estado = EN_CURSO;
        this.turno = 0;
        this.filas = 6;
        this.columnas = 7;

        this.tablero = new int[this.filas][this.columnas];
    }


    /**
     *
     */
    @Override
    protected void mueve(Movimiento m) throws ExcepcionJuego
    {
        int col = ((MovimientoC4) m).getColumna();

        if(col > this.columnas-1 || col < 0 )
            throw new ExcepcionJuego("Movimiento no válido");

        if (esValido(m))
        {
            int columna = m.toString().charAt(11) - '0';

            for(int fila = this.filas-1; fila >= 0; fila--)
            {
                if(tablero[fila][columna] == 0)
                {
                    tablero[fila][columna] = turno == 0 ? JUGADOR1 : JUGADOR2;
                    this.ultimoMovimiento = m;
                    break;
                }

            }

            actualizarEstado();

            if (estado == EN_CURSO)
            {
                cambiaTurno();
                movimientosValidos();
            }

        }

    }


    /**
     * Actualiza el estado de la partida en funci&oacute;n del estado del tablero.
     */
    private void actualizarEstado()
    {
        if(comprobarFilas() || comporbarColumnas() || comprobarDiagonalDerecha() || comprobarDiagonalIzquierda())
            this.estado = FINALIZADA;
        else if (tableroLleno())
            this.estado = TABLAS;
        else
            this.estado = EN_CURSO;
    }


    /**
     * Comprueba si alguno de los jugadores ha conectado cuatro fichas en horizontal
     *
     * @return true si ha conectado 4 fichas, false en caso contrario
     */
    private boolean comprobarFilas()
    {
        for (int i=0; i<this.filas; i++)
        {
            String cadena = "";
            for(int j=0; j<this.columnas; j++)
            {
                cadena += this.tablero[i][j];
            }

            if (this.turno == 0)
            {
                if(cadena.contains(JUGADOR1_WIN))
                    return true;
            }
            else
            {
                if(cadena.contains(JUGADOR2_WIN))
                    return true;
            }
        }
        return false;
    }


    /**
     * Comprueba si alguno de los jugadores ha conectado cuatro fichas en vertical
     *
     * @return true si ha conectado 4 fichas, false en caso contrario
     */
    private boolean comporbarColumnas()
    {
        for(int j=0; j<this.columnas; j++)
        {
            String cadena = "";
            for(int i=0; i<this.filas; i++)
            {
                cadena += this.tablero[i][j];
            }

            if(compruebaCadenaGanadora(cadena))
                return true;

        }

        return false;
    }


    /**
     * Comprueba si alguno de los jugadores ha conectado cuatro fichas en diagonal hacia la derecha inferior
     *
     * @return true si ha conectado 4 fichas, false en caso contrario
     */
    private boolean comprobarDiagonalDerecha()
    {
        String cadena = "";

		/* Diagonal Principal */
        for (int i=0; i<Math.min(this.filas, this.columnas); i++)
        {
            cadena+=this.tablero[i][i];
        }

        cadena += "|";

        for(int h=1; h<this.filas-3 ;h++)
        {
			/* Diagonales inferiores a la principal */
            for(int i=h, j=0; i<Math.min(this.filas, this.columnas); i++, j++)
            {
				 /*if (j == this.columnas-i)
					 break;*/

                cadena += this.tablero[i][j];
            }

            cadena+="|";
        }

        for(int h=1; h < this.columnas-3;h++)
        {
			/* Diagonales superiores a la principal */
            for(int i=0, j=h; j<=this.filas-h; i++, j++)
            {
                cadena += this.tablero[i][j];
            }

            cadena+="|";
        }

        return compruebaCadenaGanadora(cadena);
    }


    /**
     * Comprueba si alguno de los jugadores ha conectado cuatro fichas en diagonal hacia la izquierda inferior
     *
     * @return true si ha conectado 4 fichas, false en caso contrario
     */
    private boolean comprobarDiagonalIzquierda()
    {
        String cadena = "";


		/* Diagonal Principal */
        for (int i=0, j=this.columnas-1; i<Math.min(this.filas, this.columnas); i++, j--)
        {
            cadena+=this.tablero[i][j];
        }

        cadena+="|";


        for(int h=1; h<this.filas-3 ;h++)
        {
			/* Diagonales inferiores a la principal */
            for(int i=h, j=this.columnas-1; i<Math.min(this.filas, this.columnas); i++, j--)
            {
                cadena += this.tablero[i][j];
            }

            cadena+="|";
        }

        for(int h=1; h<this.filas-3 ;h++)
        {
			/* Diagonales superiores a la principal */
            for(int i=0, j=this.columnas-(h+1); j>=0; i++, j--)
            {
                cadena += this.tablero[i][j];
            }

            cadena+="|";
        }

        return compruebaCadenaGanadora(cadena);
    }


    /**
     * Comprueba si la cadena tiene 4 fichas consecutivas segun el turno determina si se ha ganado.
     * Esta función esta pensada para llamarse desde las comprobaciones verticual, horizontar y diagonales
     *
     * @param cadena
     * @return true si un jugador ha ganado, false en caso contrario.
     */
    private boolean compruebaCadenaGanadora(String cadena)
    {
        if (this.turno == 0 && cadena.contains(JUGADOR1_WIN))
        {
            return true;
        }
        else
        {
            if(cadena.contains(JUGADOR2_WIN))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Comprueba si el tablero está lleno, o aun quedas celdas libres
     *
     * @return true si el tablero está lleno de fichas, false en caso contrario
     */
    private boolean tableroLleno()
    {
        for (int i=0; i<this.filas; i++)
        {
            for (int j=0; j<this.columnas; j++)
            {
                if(this.tablero[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Comprueba si el movimiento es valido, es decir, hay espacio en la columna
     */
    @Override
    public boolean esValido(Movimiento m)
    {
        if(this.tablero[0][((MovimientoC4) m).getColumna()] != 0)
            return false;
        return true;
    }

    /**
     * Lista con los movimientos validos en el estado actual del tablero
     */
    @Override
    public ArrayList<Movimiento> movimientosValidos()
    {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        for(int i=0; i<this.columnas;i++)
        {
            MovimientoC4 move = new MovimientoC4(i);
            if(this.esValido(move))
                movimientos.add(move);
        }
        return movimientos;
    }


    /**
     * Resetea el tablero establencion todas las casillas como vacias y poniendo el turno a 0
     * @return True
     */
    @Override
    public boolean reset()
    {
        super.reset();

        for (int i=0; i<this.filas; i++)
        {
            for (int j=0; j<this.columnas; j++)
            {
                this.tablero[i][j] = 0;
            }
        }

        this.turno = 0;
        return true;
    }

    /**
     * Genera una cadena para poder reanudar el tablero mas tarde
     */
    @Override
    public String tableroToString()
    {
        String save;

        save = this.filas +""+ this.columnas;

        for (int i = 0; i < this.filas; i++)
        {
            for (int j = 0; j < this.columnas; j++)
            {
                save += this.tablero[i][j];
            }
        }

        save += this.turno;
        save += this.estado;

        return save;
    }

    /**
     * Devuelve el valor de la celda indicada por las coordenadas
     * @param i coordenada X
     * @param j coordenada Y
     * @return valor entero de la celda
     */
    public int getTablero(int i, int j)
    {
        return this.tablero[i][j];
    }

    /**
     * Consulta el numero de columnas que tiene el tablero
     * @return Numero de columnas
     */
    public int getColumnas()
    {
        return this.columnas;
    }

    /**
     * Consulta el numero de filas que tiene el tablero
     * @return Numero de filas
     */
    public int getFilas()
    {
        return this.filas;
    }

    /**
     * Recupera el estado de un tablero dada la cadena de entrada
     *
     */
    @Override
    public void stringToTablero(String cadena) throws ExcepcionJuego
    {
        if (cadena == null || cadena == "" || cadena.length() == 0)
            throw new ExcepcionJuego("Intentando cargar una partida desde una cadena vacia");

        this.filas = cadena.charAt(0) - '0'; /* Valor ASCII de fila, menos ASCII del 0 me da el entero */
        this.columnas = cadena.charAt(1) - '0';

        if(cadena.length() != ((this.filas * this.columnas) + 4))
            throw new ExcepcionJuego("Cadena no coherente para crear un tablero, revise el tamaño");

        this.tablero = new int[this.filas][this.columnas];


        int element = 2;

        for (int i = 0; i < this.filas; i++)
        {
            for (int j = 0; j < this.columnas; j++)
            {
                this.tablero[i][j] = cadena.charAt(element) - '0';
                element++;
            }
        }

        element = (this.filas * this.columnas)+2;
        this.turno = cadena.charAt(element) - '0';
        this.estado = cadena.charAt(element+1) - '0';
    }

    /**
     * Genera una String de manera visual el estado en el que se encuentra el tablero
     */
    @Override
    public String toString()
    {
        String salida;
        salida = "Turno: " + this.turno + "\n";
        salida += "Tablero: \n\n";

        for(int i=0; i < this.filas; i++)
        {
            salida += " | ";
            for(int j=0; j<this.columnas; j++)
            {
                salida += this.tablero[i][j] + " | ";
            }

            salida += "\n  ";

            for(int j=0; j<this.columnas; j++)
            {
                salida += "----";
            }

            salida += "\n";
        }

        return salida;
    }

    /**
     * Representacion sencilla (solo valores de las celdas) en modo tablero
     *
     * @return String con la representacion del tablero
     */
    public String toSimpleString()
    {
        String salida = "";

        for(int i=0; i < this.filas; i++)
        {
            for(int j=0; j<this.columnas; j++)
            {
                salida += this.tablero[i][j] + " ";
            }
            salida += "\n";
        }
        //System.out.println(salida);
        return salida;
    }

}