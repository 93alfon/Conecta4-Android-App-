package conecta4.eps.uam.es.conecta4_alfonso.views;

/**
 * ViewC4.java
 *
 * @author Alfonso Bonilla Trueba
 *
 * 2018
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import conecta4.eps.uam.es.conecta4_alfonso.R;
import conecta4.eps.uam.es.conecta4_alfonso.activities.PreferenceActivityC4;
import conecta4.eps.uam.es.conecta4_alfonso.model.TableroC4;

/**
 * Implementaci&oacute;n visual del tablero para Conecta 4, extensi&oacute;n de View
 *
 * @author Alfonso Bonilla Trueba
 *
 */
public class ViewC4 extends View
{
    private final String DEBUG = "ViewC4";
    private int numero;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int columnas;
    private int filas;
    private TableroC4 board;
    private OnPlayListener onPlayListener;
    private int Color1;
    private int Color2;

    /**
     *
     */
    public interface OnPlayListener
    {
        void onPlay(int column);
    }

    /**
     *
     * @param listener
     */
    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    /**
     * Constructor de la vista, encargado de establecer el fondo del tablero
     * @param context
     * @param attrs
     */
    public ViewC4(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        backgroundPaint.setColor(getResources().getColor(R.color.colorBoardBackground));
        linePaint.setStrokeWidth(2);
    }


    /**
     * Responsable de llamar al movimiento si el estado del tablero esta aun en curso
     * @param event
     * @return True en caso correcto, false en caso contrario
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(onPlayListener == null)
            return true;

        if (board.getEstado() != TableroC4.EN_CURSO)
            return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToJ(event));
        }
        return true;
    }


    /**
     * Mide el trozo disponible para calcular el espacio que va a ocupar nuestro tablero
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int desiredWidth = 500;
        String wMode, hMode;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        setMeasuredDimension(width, height);
    }


    /**
     * M&eacute; encargado de calcular el tamaño de las celdas dado un nuevo tamaño de pantalla
     * @param w Nueva anchura
     * @param h Nueva altura
     * @param oldw Antigua anchura
     * @param oldh Antigua altura
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        widthOfTile = w / columnas;
        heightOfTile = h / filas;
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;
        super.onSizeChanged(w, h, oldw, oldh);

    }

    /**
     * Encargado de dibujar el tablero de la partida C4 en el canvas
     * @param canvas Canvas sobre el que dibujar
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        onSizeChanged(getWidth(), getHeight(),0,0);
        drawCircles(canvas, linePaint);
    }


    /**
     * Método encargado de dibujar todas las fichas en el tablero
     * @param canvas Objeto canvas
     * @param paint Grosor
     */
    private void drawCircles(Canvas canvas, Paint paint)
    {
        float centerRaw, centerColumn;
        for (int i = 0, k=filas-1; i < filas; i++, k--) {
            int pos = filas - k - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < columnas; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, radio, paint);
            }
        }
    }


    /**
     * Decide el color en función del estado de la celda
     * @param paint Objeto sobre el que establecer el color
     * @param i Coordenada X de la celda
     * @param j Coordenada Y de la celda
     */
    private void setPaintColor(Paint paint, int i, int j)
    {
        int color = Integer.parseInt(PreferenceActivityC4.getColorPlayer(getContext()));

        switch (color) {
            case 1:
                Color1 = getResources().getColor(R.color.colorPlayer1_1);
                Color2 = getResources().getColor(R.color.colorPlayer2_1);
                break;
            case 2:
                Color1 = getResources().getColor(R.color.colorPlayer1_2);
                Color2 = getResources().getColor(R.color.colorPlayer2_2);
                break;
            case 3:
                Color1 = getResources().getColor(R.color.colorPlayer1_3);
                Color2 = getResources().getColor(R.color.colorPlayer2_3);
                break;
        }

        if (board.getTablero(i, j) == TableroC4.JUGADOR1)
            paint.setColor(Color1);
        else if (board.getTablero(i, j) == TableroC4.VACIO)
            paint.setColor(getResources().getColor(R.color.colorEmptyPlayer));
        else
            paint.setColor(Color2);
    }

    /**
     * Encargado de calculor la columna sobre la que se ha pulsado para realizar el movimiento
     * @param event
     * @return Columna sobre la que se ha pulsado
     */
    private int fromEventToJ(MotionEvent event)
    {
        return (int) (event.getX() / widthOfTile);
    }


    /**
     * Establece el tablero que va a representar la vista
     * @param columnas Número de columnas del tablero
     * @param filas Numero de filas del tablero
     * @param board Tablero a representar
     */
    public void setBoard(int filas, int columnas , TableroC4 board)
    {
        this.refreshDrawableState();
        this.filas = filas;
        this.columnas = columnas;
        this.board = board;
    }


}
