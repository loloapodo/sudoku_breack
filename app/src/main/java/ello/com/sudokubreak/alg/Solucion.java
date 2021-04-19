package ello.com.sudokubreak.alg;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 4/9/2020.
 * Esta clase es para guardar las soluciones y tambien los pasos con los q se realiza
 *
 *
 */
public class Solucion {
    List<Sudoku_plano> mSoluciones;
    int mCapacidad;//Capacidad
    List<MatrizCeldas.Celda> mHistorial;
    List<Integer> mRegistroSupociciones;//Contiene las posiciones de los numeros que fueron puesto por supocicion



    public void setFullForzado(boolean mFullForzado) {
        this.mFullForzado = mFullForzado;
    }

    boolean mFullForzado;

    /**
     * Constructor regular de la clase
     * @param capacidad Capaciadad de almacenamiento de las soluciones
     */
    public Solucion(int capacidad)
    {
        mSoluciones=new ArrayList<>();
        mHistorial=new ArrayList<>();
        mRegistroSupociciones=new ArrayList<>();
        mCapacidad=capacidad;

    }
    /**
     *OJO Este constructor solo inicializa el historial
     */
    public Solucion()
    {
        mHistorial=new ArrayList<>();
        mRegistroSupociciones=new ArrayList<>();
    }
    public List<MatrizCeldas.Celda> getHistorial() {
        return mHistorial;
    }
    public void AnadirPaso(MatrizCeldas.Celda celda)
    {
        mHistorial.add(celda);
    }
    public void AnadirPaso(int fila,int columna,int valor)
    {
        mHistorial.add(new MatrizCeldas.Celda(fila,columna,valor));
    }








    public void AnadirSuposicion(MatrizCeldas.Celda celda)
    {
        mRegistroSupociciones.add(mHistorial.size());
        mHistorial.add(celda);

    }


    public void setHistorial(List<MatrizCeldas.Celda> mHistorial) {
        this.mHistorial = mHistorial;
    }

    public int getCapacidadTotal() {
        return mCapacidad;
    }

    public int getCantidadSoluciones()
    {
        return mSoluciones.size();
    }

    public void addSolucion(Sudoku s)
    {
        mSoluciones.add(s.getMatrizPlana());
    }
    public void add_and_Debug(Sudoku s, String str, int iter)
    {
        mSoluciones.add(s.getMatrizPlana());
        Log.e("SOLUCION AÑADIDA!!! ",str+" Iteracion"+ String.valueOf(iter) + " SOLUCION #"+String.valueOf(mSoluciones.size()) );
    }


    public boolean IsFull() {
    return mSoluciones.size()>=mCapacidad||mFullForzado;
    }

    public List<Sudoku_plano> getSoluciones() {
        return mSoluciones;
    }

    public void ShowAllDebug()
    {
        for (int i = 0; i < mSoluciones.size(); i++) {
            mSoluciones.get(i).ShowDebug();
        }
    }

    public boolean IsEmpty() {
    return mSoluciones.size()==0;
    }

    public void AnadirSuposicion(int fil, int col, Integer integer) {
    AnadirSuposicion(new MatrizCeldas.Celda(fil,col,integer));
    }

    public void setHistorial(Solucion historial) {
    mHistorial=historial.getHistorial();
    }


}
