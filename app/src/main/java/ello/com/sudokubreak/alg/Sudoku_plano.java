package ello.com.sudokubreak.alg;

import android.util.Log;

/**
 * Created by Laptop on 4/8/2020.
 * A diferencia de la clase matriz celda esta clase solo contiene un arreglo bidimencional de enteros
 */
public class Sudoku_plano {


    boolean mActivePencil;
    int mMatriz[][];
    boolean mPencil[][];


    public boolean[][] getPencil() {
        return mPencil;
    }

    public void setmPencil(boolean[][] mPencil) {
        this.mPencil = mPencil;
    }

    public Sudoku_plano() {

        mActivePencil=false;
        mPencil=new boolean[9][9];
        mMatriz=new int[9][9];
        for (int i = 0; i < 0; i++) {
            for (int j = 0; j <9 ; j++) {
                mMatriz[i][j]=0;
                mPencil[i][j]=mActivePencil;
            }
        }
    }


    public Sudoku_plano(Sudoku_plano matriz) {

        mMatriz=matriz.getMatriz();
        mPencil=matriz.getPencil();

    }

    public int[][] getMatriz() {
        return mMatriz;
    }
    public int getValue(int f,int c) {
        return mMatriz[f][c];
    }






    //Constructores
    public Sudoku_plano(Sudoku s)
    {
        mMatriz=new int[9][9];
        mPencil=new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

               mMatriz[i][j]= s.getMatrizCelda()._9Filas[i]._9Celdas[j].getValor();
                mPencil[i][j]=s.getMatrizCelda()._9Filas[i]._9Celdas[j].getPencil();
            }
        }

    }

    /**
     * Muestra el sudoku en la consola de debug
     */
    public void ShowDebug() {
        Log.e("sudoku","-------------------------------------");
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <9 ; i++) {
            for (int j = 0; j <9 ; j++) {
                if (j%3==0&&j!=0){stringBuilder.append(" .");}
                stringBuilder.append(" ").append(mMatriz[i][j]);

            }
            if (i%3==0&&i!=0){Log.e("sudoku","-");}
            Log.e("sudoku",stringBuilder.toString());
            stringBuilder=new StringBuilder();
        }
        Log.e("sudoku","-------------------------------------");

    }

    public void LlenarCelda_By_Cuadro(int cuadro, int pos, int valor) {
        int fil = MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
        int col = MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);
        //  Log.e("-----","Cuadro"+String.valueOf(cuadro)+" pos"+String.valueOf(pos)+" Valor"+String.valueOf(valor));
        if (pos<3){}
        else if (pos<6){fil++;}
        else {fil++;fil++;}

        if (pos%3==0){}
        else  if (pos%3==1){col++;}
        else               {col++;col++;}

        mMatriz[fil][col]=valor;

        //Log.e("-----","Fila"+String.valueOf(fil)+" Columna"+String.valueOf(col)+" Valor"+String.valueOf(valor));
    }

    /**
     *
     * @return Retorna la cantidad de casillas con valor cero. Es decir las que se encuentren vacias
     */
    public int getTotalCeldasVacias() {
        int total=0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (mMatriz[i][j]==0){total++;}
            }
        }
        return total;
    }

    public MatrizCeldas.Celda getCelda(int i, int j) {
        MatrizCeldas.Celda celda= new MatrizCeldas.Celda(i,j,mMatriz[i][j]);
        celda.setPencil(mPencil[i][j]);
        return celda;
    }
}
