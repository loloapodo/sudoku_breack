package ello.com.sudokubreak.interfaces;

import ello.com.sudokubreak.alg.Chino;
import ello.com.sudokubreak.alg.MatrizCeldas;
import ello.com.sudokubreak.alg.Solucion;
import ello.com.sudokubreak.alg.Sudoku;

/**
 * Created by Laptop on 4/17/2020.
 */
public interface Respuestas_I {



    //void AgregarRespuesta(Sudoku sudoku,String name,int iteration);
    //void AgregarRespuesta(Sudoku sudoku);
    void NotificarErrordeEntrada(MatrizCeldas.Celda celda[]);
    void NotificarSudokuSinSolucion();
    void NotificarFinalAlgoritmo(Solucion soluciones);
    void Nueva_Solucion(Solucion soluciones, Chino.Dificuldad dificuldad);
}
