package ello.com.sudokubreak.alg;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 4/7/2020.
 */
public class Sudoku {




    //Miembros
    MatrizCeldas mMatrizCeldas;

    public boolean getActivePencil() {
        return mActivePencil;
    }

    public void setActivePencil(boolean mActivePencil) {
        this.mActivePencil = mActivePencil;
    }

    boolean mActivePencil;



    static final List<Integer> listaNumeros = construirListaNumeros();
    static final List<String> listaNumerosStr = construirListaNumerosStr();
    public static List<String> getListaNumerosStr() {
        return listaNumerosStr;
    }


    public MatrizCeldas getMatrizCelda() {
        return mMatrizCeldas;
    }

    private static List<String> construirListaNumerosStr() {
        List<String> listaNumeros = new ArrayList<>();
        for (int i=1;i<10;i++){listaNumeros.add(String.valueOf(i));}
        return listaNumeros;
    }

    //Constructores
   public Sudoku() {
        mActivePencil=false;
        mMatrizCeldas = new MatrizCeldas();

    }

    public Sudoku(Sudoku sudoku) {

        sudoku.getActivePencil();
        mMatrizCeldas = new MatrizCeldas();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mMatrizCeldas._9Filas[i]._9Celdas[j].setValor(sudoku.getCeldaValor(i, j));
                mMatrizCeldas._9Filas[i]._9Celdas[j].setPencil(sudoku.getCeldaPencil(i,j));
            }
        }

    }


    /**
     * @return la lista de los 9 numeros con los que se completa el sudoku
     */
    private static List<Integer> construirListaNumeros() {
        List<Integer> listaNumeros = new ArrayList<>();
        for (int i=1;i<10;i++){listaNumeros.add(i);}
        return listaNumeros;
    }

    /**
     * Retorna la lista: 1 2 3 4 5 6 7 8 9
     */
    public static List<Integer> getListaNumeros() {
        return listaNumeros;
    }

    Sudoku_plano getMatrizPlana() {
        return new Sudoku_plano(this);
    }


    public void LlenarCelda(int fila, int columna, int valor) {
        getCelda(fila, columna).setValor(valor);
    }

    public void MostrarDebiugString(String name, int mIteracion) {

        Log.e("sudoku", name + " Iteracion:" + String.valueOf(mIteracion));
        Log.e("sudoku", "-------------------------------------");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    stringBuilder.append(" .");
                }
                stringBuilder.append(" ").append(getCeldaValor(i, j));

            }
            if (i % 3 == 0 && i != 0) {
                Log.e("sudoku", "-");
            }
            Log.e("sudoku", stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        Log.e("sudoku", "-------------------------------------");
    }

    MatrizCeldas.Celda getCelda(int fila, int columna) {
        return mMatrizCeldas._9Filas[fila]._9Celdas[columna];
    }

    int getCeldaValor(int fila, int columna) {
        return mMatrizCeldas._9Filas[fila]._9Celdas[columna].getValor();
    }
    boolean getCeldaPencil(int fila, int columna) {
        return mMatrizCeldas._9Filas[fila]._9Celdas[columna].getPencil();
    }

    boolean TieneValor(int fila, int columna) {
        return mMatrizCeldas._9Filas[fila]._9Celdas[columna].TieneValor();
    }

    public boolean EstaResuelto() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!mMatrizCeldas._9Filas[i]._9Celdas[j].TieneValor()) {
                    return false;
                }
            }

        }
        return true;
    }

    public static List<MatrizCeldas.Cuadro> getCuadrosFromSudoku(Sudoku s) {

        List<MatrizCeldas.Cuadro> los9cuadros = new ArrayList<>();


        for (int cuadro = 1; cuadro < 10; cuadro++) {//Se recoge el cuadro y se marcan las celdas

            List<MatrizCeldas.Celda> listado = new ArrayList<>();
            int fil = MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
            int col = MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);

            for (int f = 0; f < 3; f++) {// 0 1 2
                for (int c = 0; c < 3; c++) {// 0 1 2

                        listado.add(s.getCelda(fil + f, col + c));

                }
            }

            MatrizCeldas.Cuadro temp = new MatrizCeldas.Cuadro();
            temp.setCeldas_del_Cuadro(listado);
            los9cuadros.add(temp);
        }
return los9cuadros;

    }


    public static List<MatrizCeldas.Cuadro> getCuadrosFromSudoku(Sudoku_plano s) {

        List<MatrizCeldas.Cuadro> los9cuadros = new ArrayList<>();


        for (int cuadro = 1; cuadro < 10; cuadro++) {//Se recoge el cuadro y se marcan las celdas

            List<MatrizCeldas.Celda> listado = new ArrayList<>();
            int fil = MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
            int col = MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);

            for (int f = 0; f < 3; f++) {// 0 1 2
                for (int c = 0; c < 3; c++) {// 0 1 2


                    listado.add(s.getCelda(fil + f, col + c));

                }
            }

            MatrizCeldas.Cuadro temp = new MatrizCeldas.Cuadro();
            temp.setCeldas_del_Cuadro(listado);
            los9cuadros.add(temp);
        }
        return los9cuadros;

    }













    /**
     * LLena un valor segun la posicion especificada
     *Recordar pasar el parametro cuadro del 1 al 9
     * @param cuadro
     * @param pos
     * @param valor
     */
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

        mMatrizCeldas._9Filas[fil]._9Celdas[col].setValorPencil(valor);

        //Log.e("-----","Fila"+String.valueOf(fil)+" Columna"+String.valueOf(col)+" Valor"+String.valueOf(valor));
    }

    /**
     * Dado el numero del cuadro 1-9 y la posicion del numero
     * @return retorna una celda con los valores de fila y columna y cuadro correspondientes a su ubicacion
     * Recordar pasar el parametro cuadro del 1 al 9
     */
    public static MatrizCeldas.Celda ObtenerCelda_By_Cuadro(int cuadro, int pos)
    {
        MatrizCeldas.Celda celda=new MatrizCeldas.Celda();

        int fil = MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
        int col = MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);
        //  Log.e("-----","Cuadro"+String.valueOf(cuadro)+" pos"+String.valueOf(pos)+" Valor"+String.valueOf(valor));
        if (pos<3){}
        else if (pos<6){fil++;}
        else {fil++;fil++;}

        if (pos%3==0){}
        else  if (pos%3==1){col++;}
        else               {col++;col++;}

        celda.setFil(fil);
        celda.setCol(col);
        celda.setPos(Sudoku.ObtenerPosicionEnCuadro_ConFilColu(fil,col));
        celda.setCua(cuadro);
        return celda;

    }








    public void MostrarDebiugString2(int mIteracion) {


        Log.e("sudoku",  " Iteracion:" + String.valueOf(mIteracion));
        Log.e("sudoku", "-------------------------------------");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    stringBuilder.append(" .");
                }
                stringBuilder.append(" ").append(getCeldaValor(i, j));

            }
            if (i % 3 == 0 && i != 0) {
                Log.e("sudoku", "-");
            }
            Log.e("sudoku", stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        Log.e("sudoku", "-------------------------------------");
    }

    public static Sudoku createFrom_Sudoku(Sudoku_plano sudoku_plano) {
    Sudoku s=new Sudoku();
        for (int i = 0; i <9 ; i++) {
            for (int j=0;j<9;j++)
            {
                s.LlenarCelda(i,j,sudoku_plano.getValue(i,j));
            }
        }
        return s;
    }

    /**
     * Funcion Legendaria
     * Retorna el numero de cuadro correspondiente a esta columna
     * @param fila
     * @param columna
     * @return
     */
    public static int ObtenerCuadroFromFilCol(int fila, int columna) {


            if(fila>=6 && columna>=6){return 9;}
            if(fila>=6 && columna<6 && columna>=3){return 8;}
            if(fila>=6 && columna<3 ){return 7;}
            if(fila<6 && fila>=3 &&columna>=6){return 6;}
            if(fila<6 && fila>=3 && columna<6 && columna>=3){return 5;}
            if(fila<6 && fila>=3 &&columna<3 ){return 4;}
            if(fila<3 && columna>=6 ){return 3;}
            if(fila<3 && columna<6 && columna>=3){return 2;}
            return 1;




    }

    public static int ObtenerPosicionEnCuadro_ConFilColu(int fila,int columna) {

        int valor1=fila%3;
        //if (valor1==0){valor1=0}
         if (valor1==1){valor1=3;}
        else if(valor1==2){valor1=6;}

        int valor2=columna%3;

        return valor1+valor2;



    }



}