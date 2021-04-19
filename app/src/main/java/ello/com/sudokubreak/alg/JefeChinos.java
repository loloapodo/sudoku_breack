package ello.com.sudokubreak.alg;

import ello.com.sudokubreak.interfaces.Respuestas_I;

/**
 * Created by Laptop on 4/7/2020.
 * Esta es la clase donde tod o comienza.
 * Creando el primer hilo
 */
public class JefeChinos {

    public JefeChinos(Respuestas_I respuestas_i){
        this.respuestas_i = respuestas_i;
    }



    public Sudoku sEjemplo;

    public Solucion mSoluciones;
    public Chino empleado;
    public Respuestas_I respuestas_i;




    public void Crear_Situciacion1()
    {
        sEjemplo=getSudokuEjmplo();
        mSoluciones=new Solucion(10000);
        empleado= new Chino(sEjemplo,0,0,mSoluciones, respuestas_i);
        empleado.execute();

    }





    public static Sudoku getSudokuEjmplo()
    {
    Sudoku eje=new Sudoku();
        eje.LlenarCelda(0, 0, 5);

        eje.LlenarCelda(0,1,3);eje.LlenarCelda(0,4,7);
        eje.LlenarCelda(1,0,6);eje.LlenarCelda(1,3,1);eje.LlenarCelda(1,4,9);eje.LlenarCelda(1,5,5);
        eje.LlenarCelda(2,1,9);eje.LlenarCelda(2,2,8);eje.LlenarCelda(2,7,6);

        eje.LlenarCelda(3,0,8);eje.LlenarCelda(3,4,6);eje.LlenarCelda(3,8,3);
        eje.LlenarCelda(4,0,4);eje.LlenarCelda(4,3,8);eje.LlenarCelda(4,5,3);eje.LlenarCelda(4,8,1);
       eje.LlenarCelda(5,0,7);eje.LlenarCelda(5,4,2);eje.LlenarCelda(5,8,6);

        eje.LlenarCelda(6,1,6);eje.LlenarCelda(6,6,2);eje.LlenarCelda(6,7,8);
        eje.LlenarCelda(7,3,4);eje.LlenarCelda(7,4,1);eje.LlenarCelda(7,5,9);eje.LlenarCelda(7,8,5);
       eje.LlenarCelda(8,4,8);eje.LlenarCelda(8,7,7);eje.LlenarCelda(8,8,9);
        return eje;
    }

    public static Sudoku getSudokuEjmplo2()
    {
        Sudoku eje=new Sudoku();
        eje.LlenarCelda(0, 0, 5);eje.LlenarCelda(0,1,3);eje.LlenarCelda(0,4,7);
        return eje;
    }

    public static Sudoku getSudokuVacio()
    {
        return new Sudoku();
    }



}
