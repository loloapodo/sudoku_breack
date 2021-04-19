package ello.com.sudokubreak.interfaces;

/**
 * Created by Laptop on 4/13/2020.
 */

import ello.com.sudokubreak.alg.MatrizCeldas;

/**
 * Interface para desencadenar la accion cuando se interactua con una casilla especifica
 */
public interface click_i {
    /**
     *
     * @param cuadro al que pertence el numero (1- 10)
     * @param pos posicion del 0 al 9
     * @param valor valor del numero
     */
    public void click_numero(int cuadro, int pos, int valor);
    public void click_pick(int valor);
    boolean HayNumerosMarcados();
    MatrizCeldas.Celda[] GetCeldasMarcadas();
}
