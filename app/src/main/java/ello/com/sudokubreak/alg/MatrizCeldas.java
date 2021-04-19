package ello.com.sudokubreak.alg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 4/8/2020.
 */
public class MatrizCeldas
{
    Fila _9Filas[];
    public MatrizCeldas(){
        _9Filas =new Fila[9];
        for (int i = 0; i < 9; i++) {
            _9Filas[i]=new Fila();
        }
    }

    public class Fila
    {
        public Celda _9Celdas[];
        public Fila(){
            _9Celdas=new Celda[9];
            for (int i = 0; i < 9; i++) {
                _9Celdas[i]=new Celda();
            }
        }
    }

         /*
    public class Columna
    {
        Celda _9Celdas[];
        public Columna(){_9Celdas=new Celda[9];}
    }
    */


   public static class Celda
    {

        private int mValor =0;
        private boolean marcas[]={false,false,false,false,false,false,false,false,false,false};

        public Celda() {
        }

        private int mFil;
        private int mCuadro;
        private int mCol;
        private int mPos;//posicion q indida en un cuadro del 0 al 9
        private boolean mPencil;

        public boolean getPencil() {
            return mPencil;
        }

        public void setPencil(boolean mPencil) {
            this.mPencil = mPencil;
        }



        public Celda(int fila, int columna, int valor) {
            mFil =fila;
            mCol =columna;
            mValor=valor;
            mPencil=false;

        }

        /**
         *posicion q indica en un cuadro del 0 al 9
         */

        public int getPos() {
            return mPos;
        }

        public void setPos(int pos) {
            this.mPos = pos;
        }



        public int getCuadro() {
            return mCuadro;
        }

        public int getCol() {
            return mCol;
        }

        public void setCol(int col) {
            this.mCol = col;
        }

        public int getFil() {
            return mFil;
        }

        public void setFil(int fil) {
            this.mFil = fil;
        }



        /**
         * OJO mPos: del 1-9
         * al retornar:
         * al comienzo todas las marcas son false
         * (la mPos cero se ignora)
         */
        public boolean getMarca(int pos) {
            return marcas[pos];
        }
        /**
         * OJO mPos: del 1-9
         * al retornar:
         * marca true-- Se puede colocar el numero
         * ajusta la marca a true y significa q el numero puede ser colocado
         */
        public void setMarca(int pos) {
            marcas[pos]=true;
        }



        public boolean[] getMarcas() {
            return marcas;
        }
        public int getValor()
        {
            return mValor;
        }

        public void setValor(int valor) {
            marcas[0]=true;
            this.mValor = valor;
        }

        public void setValorPencil(int valorPencil) {
            mPencil=true;
            mValor=valorPencil;
        }



        public boolean TieneValor()
        {
            return mValor !=0;
        }

        /**
         *
         * @return la cantidad de numeros posibles que pueden ser colocados en la celda
         */
        public int getCantidadNumerosPosibles() {
            int Contador_de_Banderas=0;
            for (int i = 1; i < 10; i++) {
                if (!marcas[i]){Contador_de_Banderas++;}
            }
            return Contador_de_Banderas;
        }


        /**
         *
         * @return retorna un arreglo de integers con los posibles valores de la celda
         */
        public ArrayList<Integer> getvaloresPosibles() {

            ArrayList<Integer> valoresPosibles=new ArrayList<>();
            for (int i = 1; i < 10; i++) {
            if (!marcas[i]){valoresPosibles.add(i);}
            }
            return valoresPosibles;
        }

        public void setCua(int cua) {
            this.mCuadro = cua;
        }



    }

    public static class Cuadro
    {
        List<Celda> Celdas_del_Cuadro;

        public void setCeldas_del_Cuadro(List<Celda> celdas_del_Cuadro) {
            Celdas_del_Cuadro=new ArrayList<>();
            Celdas_del_Cuadro = celdas_del_Cuadro;
        }
        public void setCelda(Celda celda, int pos) {
            Celdas_del_Cuadro.set(pos,celda);
        }
        public Celda getCelda(int position) {
            return Celdas_del_Cuadro.get(position);
        }

        static int ObtenerFilaInicial(int cuadro) {
            if (cuadro < 4) {
                return 0;
            } else if (cuadro < 7) {
                return 3;
            }
            return 6;
        }
        static int ObtenerColumnaInicial(int cuadro) {
            if (cuadro==1||cuadro==4||cuadro==7) {
                return 0;
            } else if (cuadro==2||cuadro==5||cuadro==8) {
                return 3;
            }
            return 6;
        }




    }


}