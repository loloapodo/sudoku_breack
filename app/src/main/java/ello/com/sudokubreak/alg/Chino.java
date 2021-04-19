package ello.com.sudokubreak.alg;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ello.com.sudokubreak.interfaces.Respuestas_I;

/**
     * Created by Laptop on 4/7/2020.
     * Esta es la clase mas importante
     * Es la que resuelve el sudoku
     * Comienza con un hilo principal y se va derivando en muchos hilos en dependencia del sudoku y tomando cada sudoku UN CAMIMNO DISTINTO
     * OJO LOS HIJOS ESTAN TRABAJANDO ACTUALMENTE ASINCRONICAMENTE (cunando termina uno, le da paso a otro, y luego otro...)
     */
    public class Chino extends AsyncTask{


        private Dificuldad mDificultad;
        private static final String NOTIFICAR_SOLUCION = "UPDATE_UI";
    private final Respuestas_I mRespuestas_i;
    protected int mParentCount;//Cantidad de generaciones por encima de la actual
        protected  int mIteracion;// Conteo de iteraciones realizadas por una generacion determinada
        protected int mIdChino;//identidad del chino dentro de una generacion determinada
        protected String mNombreChino;
        protected Solucion mSoluciones;//Variable donde se guardan las soluciones y se comunica con la UI
        //protected Solucion mHistorial;


        protected Sudoku sudoku_del_chino;//Sudoku a resolver
        protected ThreadGroup mThreadGroup;//ESTA VARIABLE NO PINTA NADA TODAVIA. QUIZAS ALGUN DIA



        private final int LIMITE_VALORES_A_SUPONER_EN_CELDA=9;//Valor maximo de posibles numeros cuando se va suponer el valor de una celda
        private final int GENERACIONES_PERMITIDAS = -1;
        private boolean Ramndon_Flag=true;// A la hora de suponer supone ramndon de los numeros posibles

        private Sudoku_plano mSudokuplanoAntes;




        //Constructores

        /**
         * @param sudoku dado para resolver
         * @param parentCount cantidad de padres o generaciones arriba de el
         * @param IdChino identidad del chino dentro de una generacion dada
         * @param soluciones  variable donde se almacenan  las soluciones
         * @param respuestas_i

         */
       public Chino(Sudoku sudoku, int parentCount, int IdChino, Solucion soluciones, Respuestas_I respuestas_i) {

            sudoku_del_chino=sudoku;
            mParentCount=parentCount;
            mIdChino=IdChino;
            mIteracion=0;
            mSoluciones=soluciones;
            this.mRespuestas_i = respuestas_i;
            mNombreChino=NombrarChino();
           mDificultad=Dificuldad.Facil;
           //mHistorial =new Solucion();
        }

        /**
         *Constructor copia. No utilizar para crear nuevas generaciones. Ya que los valores serian los mismos
         */
        Chino(Chino chino)
        {

            sudoku_del_chino=chino.sudoku_del_chino;
            mIdChino=chino.mIdChino;
            mIteracion=chino.mIteracion;
            mSoluciones=chino.mSoluciones;
            mRespuestas_i =chino.mRespuestas_i;
            mNombreChino=NombrarChino();
            mDificultad=Dificuldad.Facil;
            //mHistorial =new Solucion();
        }

    public Chino(Sudoku_plano sudoku, int parentCount, int IdChino, Solucion soluciones, Respuestas_I respuestas_i) {

        sudoku_del_chino=Sudoku.createFrom_Sudoku(sudoku);
        mParentCount=parentCount;
        mIdChino=IdChino;
        mIteracion=0;
        mSoluciones=soluciones;
        this.mRespuestas_i = respuestas_i;
        mNombreChino=NombrarChino();
        mDificultad=Dificuldad.Facil;
       // mHistorial =new Solucion();
    }

    public Chino(Sudoku sudoku, int parentCount, int IdChino, Solucion soluciones, Respuestas_I respuestas_i,Dificuldad dif) {


        sudoku_del_chino=sudoku;
        mParentCount=parentCount;
        mIdChino=IdChino;
        mIteracion=0;
        mSoluciones=soluciones;
        this.mRespuestas_i = respuestas_i;
        mNombreChino=NombrarChino();
        mDificultad=dif;
     //   mHistorial =new Solucion();
    }


    @Override
    protected Object doInBackground(Object[] params) {

        if (TieneErrores())
        {
            mRespuestas_i.NotificarErrordeEntrada(ObtenerCeldasConError());


        }
        else
        {
            sudoku_del_chino.setActivePencil(true);
            Resolver();
            if(mSoluciones.IsEmpty()){mRespuestas_i.NotificarSudokuSinSolucion();}
            else {mRespuestas_i.NotificarFinalAlgoritmo(mSoluciones);}
        }



        return null;
    }

    /**
     * Obtiene las celdas que incumplen con las reglas del sudoku. Los dos primeros que encuentre
     * @return un arreglo con dos Celdas
     */
    private MatrizCeldas.Celda[] ObtenerCeldasConError() {
        MatrizCeldas.Celda dosCeldas[]=new MatrizCeldas.Celda[2];
        dosCeldas[0]=new MatrizCeldas.Celda();dosCeldas[1]=new MatrizCeldas.Celda();
        int[] pos=null;



        List<Integer> Numeros_En_La_Fila;
        List<Integer> Numeros_En_La_Columna;
        List<Integer> Numeros_En_El_Cuadro;

        for (int i = 0; i < 9; i++) {

            Numeros_En_La_Fila= RecogerNumerosFila_con_0(i);
            pos=ObtenerPosicionesDeCeldasDuplicadas(Numeros_En_La_Fila);
            if (pos!=null)
            {
                dosCeldas[0].setFil(i);
                dosCeldas[0].setCol(pos[0]);
                dosCeldas[0].setCua(Sudoku.ObtenerCuadroFromFilCol(i,pos[0]));
                dosCeldas[0].setPos(Sudoku.ObtenerPosicionEnCuadro_ConFilColu(i,pos[0]));

                dosCeldas[1].setFil(i);
                dosCeldas[1].setCol(pos[1]);
                dosCeldas[1].setCua(Sudoku.ObtenerCuadroFromFilCol(i,pos[1]));
                dosCeldas[1].setPos(Sudoku.ObtenerPosicionEnCuadro_ConFilColu(i,pos[1]));
                return dosCeldas;
            }


            Numeros_En_La_Columna= RecogerNumerosColumna_con_0(i);
            pos=ObtenerPosicionesDeCeldasDuplicadas(Numeros_En_La_Columna);
            if (pos!=null)
            {


                dosCeldas[0].setCol(i);
                dosCeldas[0].setFil(pos[0]);
                dosCeldas[0].setCua(Sudoku.ObtenerCuadroFromFilCol(pos[0],i));
                dosCeldas[0].setPos(Sudoku.ObtenerPosicionEnCuadro_ConFilColu(pos[0],i));

                dosCeldas[1].setCol(i);
                dosCeldas[1].setFil(pos[1]);
                dosCeldas[1].setCua(Sudoku.ObtenerCuadroFromFilCol(pos[1],i));
                dosCeldas[1].setPos(Sudoku.ObtenerPosicionEnCuadro_ConFilColu(pos[1],i));
                return dosCeldas;
            }


            Numeros_En_El_Cuadro= RecogerNumerosCuadro_con_0(i+1);//Cuadro empieza en 1
            pos=ObtenerPosicionesDeCeldasDuplicadas(Numeros_En_El_Cuadro);
            if (pos!=null)
            {
                dosCeldas[0]=Sudoku.ObtenerCelda_By_Cuadro(i+1,pos[0]);
                dosCeldas[1]=Sudoku.ObtenerCelda_By_Cuadro(i+1,pos[1]);
                return dosCeldas;

            }


        }
        return null;
    }

    /**
     * Esta funcion se utiliza solo para apoyar a la funcion q busca las celdas con errores. Deberia ser una clase anonima
     * @param lista_de_numeros una lista de 9 valores enteros
     * @return Retorna null o las posiciones de valores q se repiten en la lista.
     *
     */
    private int[] ObtenerPosicionesDeCeldasDuplicadas(List<Integer> lista_de_numeros) {
    int posiciones[]=new int[2];
        for (int i = 0; i < 9; i++) {
            if(lista_de_numeros.get(i)==0) {continue;}//si es cero buscar el proximo numero
                for (int j = 0; j < 9; j++) {
                if(j!=i && lista_de_numeros.get(i).compareTo(lista_de_numeros.get(j))==0)//si las posiciones son distintas pero los valores iguales
                {
                    posiciones[0]=i;
                    posiciones[1]=j;
                    return posiciones;
                }
                }
        }
    return null;
    }

    private List<Integer> RecogerNumerosCuadro_con_0(int cuadro) {
        List<Integer> listado=new ArrayList<>();


        int fil= MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
        int col= MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);


        for (int f = 0; f < 3; f++) {// 0 1 2
            for (int c = 0; c < 3; c++) {// 0 1 2
                listado.add(sudoku_del_chino.getCeldaValor(fil+f,col+c));
            }
        }
        return listado;
    }

    private List<Integer> RecogerNumerosColumna_con_0(int columna) {
        List<Integer> listado=new ArrayList<>();

        for (int fil = 0; fil < 9; fil++) {
                listado.add(sudoku_del_chino.getCeldaValor(fil,columna));

        }

        return listado;
    }

    private List<Integer> RecogerNumerosFila_con_0(int fila) {
        List<Integer> listado=new ArrayList<>();

        for (int col = 0; col < 9; col++) {
                listado.add(sudoku_del_chino.getCeldaValor(fila,col));
        }

        return listado;


    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        ProcesarSudokuResuleto();
        super.onProgressUpdate(values);
    }



        /**
         * Esta funcion resuleve el sudoku_del_chino. Y si es necesario busca mas chinos para darles hasta donde pudo resolverlo
         *
         */
        private void Resolver()
        {

            if (mSoluciones.IsFull()){cancel(true);Log.e("Cancelada","Cancelada esta llena"); return;}

            mIteracion++;
            mSudokuplanoAntes = sudoku_del_chino.getMatrizPlana();
            EtapaRecogeryMarcarSudoku();
            EtapaEscribirSudoku();
            //sudoku_del_chino.MostrarDebiugString(mHiloResolver,mIteracion);

            if(SudokuFueEscrito())
            {
                if (TieneErrores()) {
                    Log.e("    Error-->", mNombreChino + " Iteracion " + String.valueOf(mIteracion) + " VALOR REPETIDO");
                } else {
                    if (sudoku_del_chino.EstaResuelto()) {

                        mSoluciones.add_and_Debug(sudoku_del_chino,mNombreChino,mIteracion);
                        if ((mSoluciones.getCantidadSoluciones()%100)==1){publishProgress();}
                    } else {
                        Resolver();
                    } //Se mantiene parentCount debido a q el mismo chino va a volver intentarlo
                }
            }
            else if (PuedoCrearMasHilos())
            {
                CrearHilosHijos();//Crea mas hilos (o chinos) para resolver el sudoku
            }


        }


        private void ProcesarSudokuResuleto() {
            //mSoluciones.setHistorial(mHistorial);
            DefinirDifucultad();
           mRespuestas_i.Nueva_Solucion(mSoluciones,mDificultad);
        }

        /**
         * La funcion va fila por fila recogiendo los numeros y marcando las posiblidades que tiene la celda
         * Repite lo mismo para las columnas
         * Repite lo mismo para los cuadros
         */
        private void EtapaRecogeryMarcarSudoku() {

            for (int fila = 0; fila < 9; fila++) {//Se recoge la fila y se marcan las celdas
                List<Integer> Numeros_En_La_Fila= RecogerNumerosFila(fila);
                MarcarCeldasFila(Numeros_En_La_Fila,fila);
            }

            for (int columna = 0; columna < 9; columna++) {//Se recoge la columna y se marcan las celdas
                List<Integer> Numeros_En_La_Columna= RecogerNumerosColumna(columna);
                MarcarCeldasColumna(Numeros_En_La_Columna,columna);
            }

            for (int cuadro=1;cuadro<10;cuadro++) {//Se recoge el cuadro y se marcan las celdas
                List<Integer> Numeros_En_El_Cuadro= RecogerNumerosCuadro(cuadro);
                MarcarCeldasCuadro(Numeros_En_El_Cuadro,cuadro);
            }

        }




        /**
         * Se llenan los valores del sudoku que cumplen la regla de llenado
         */
        private void EtapaEscribirSudoku() {

            for (int fila = 0; fila < 9; fila++) {//Empezar a escibir en el sudoku
                for (int columna = 0; columna < 9; columna++) {
                    if (!sudoku_del_chino.getCelda(fila,columna).TieneValor())
                    {
                        int numero= PuedoEscribirCelda(sudoku_del_chino.getCelda(fila,columna));
                        if (numero!=0){
                            EscribirCelda(sudoku_del_chino.getCelda(fila,columna),numero);
                          //  mHistorial.AnadirPaso(fila,columna,numero);
                        }

                    }
                }
            }

        }

        /**
         *
         * @return Retorna Ramas del sudoku_del_chino. Estas ramas difieren en el valor de una celda especifica
         */
        private List<Sudoku> CrearSudokusRamas() {

            List<Sudoku>SudokusRamas=new ArrayList<>();
            int cant_valores_posibles=2;

            while (cant_valores_posibles<=LIMITE_VALORES_A_SUPONER_EN_CELDA)
            {


                for (int fil = 0; fil < 9; fil++) {//Recorriendo el sudoku en busca de una casilla con @cant_valores_posibles
                    for (int col = 0; col < 9; col++) {

                        if(!sudoku_del_chino.getCelda(fil,col).TieneValor())
                        {
                            if (sudoku_del_chino.getCelda(fil,col).getCantidadNumerosPosibles()==cant_valores_posibles)
                            {
                                //Una vez identificada la posicion de una celda para suponer valores:
                                return CrearSudokusRamas(fil,col);
                            }
                        }
                    }
                }
                cant_valores_posibles++;
            }
            return SudokusRamas;

        }

        /**
         * Crea ramificaciones del sudoku_del_chino a partir de la posicion de la celda en la que se quiere suponer
         * @param fil fila de la celda donde se quiere suponer
         * @param col columna de la celda donde se quiere suponer
         * @return ramificaciones del sudoku_del_chino(sudoku padre)
         */
        private List<Sudoku> CrearSudokusRamas(int fil, int col) {

            ArrayList<Sudoku> sudokusRamas=new ArrayList<>();
            ArrayList<Integer> valoresPosibles=sudoku_del_chino.getCelda(fil, col).getvaloresPosibles();


            Log.e("A partir de", mNombreChino+" Iteracion "+String.valueOf(mIteracion));
            for (int i = valoresPosibles.size()-1; i>-1 ; i--) {//Una vez los valores a colocar fueron identificados:
        //ALEATORIO A LA HORA DE SUPONER VALORES... REALIZADA POR LA SIGUIENTE LINEA DE CODIGO
                int iterador_aleatorio= getIteradorAleatorio(i);
                //Son añadidos tantos sudokus como numeros posibles a poner en la celda
                Sudoku s=new Sudoku(sudoku_del_chino);
                //mHistorial.AnadirSuposicion(fil,col,valoresPosibles.get(i));
                Log.e("Se supuso "+String.valueOf(i+1),String.valueOf(fil)+" "+String.valueOf(col)+"-->"+String.valueOf(valoresPosibles.get(i)));
                s.getCelda(fil,col).setValorPencil(valoresPosibles.get(iterador_aleatorio));
                valoresPosibles.remove(iterador_aleatorio);
                sudokusRamas.add(s);
                //Son añadidos tantos sudokus como numeros posibles a poner en la celda
            }
            Log.e("---","                   ");
            return sudokusRamas;
        }

    private void DefinirDifucultad() {

                 if (mParentCount==0){mDificultad=Dificuldad.Facil;}
            else if (mParentCount==1){mDificultad=Dificuldad.Medio;}
            else if (mParentCount==2){mDificultad=Dificuldad.Dificil;}
            else if (mParentCount>3){mDificultad=Dificuldad.Extremo;}

    }

    private int getIteradorAleatorio(int i) {
        return new Random().nextInt(i+1);
    }


    private boolean PuedoCrearMasHilos() {
            //return mParentCount<GENERACIONES_PERMITIDAS;
            return true;
        }


        private void CrearHilosHijos()
        {
            List<Sudoku> SudokusRamas=CrearSudokusRamas();
            if (SudokusRamas.size()!=0) {CrearHilosHijos(SudokusRamas);}
            else {Log.e("Sudoku Rama",mNombreChino+" No se pudieron formar sudokus ramas" );}
        }


        /**
         * Crea hilos hijos para resolver el sudoku.
         * (Crea mas chinos dandole a cada uno una rama del sudoku padre.)
         *
         * Pone a los chinos a resolver el sudoku
         * @param sudokusRamas lista de sudokus a dar a los nuevos chinos
         */
        private void CrearHilosHijos(List<Sudoku> sudokusRamas) {
            for (int i = 0; i < sudokusRamas.size(); i++) {
                Chino chino = new Chino(sudokusRamas.get(i), mParentCount + 1, i,mSoluciones, mRespuestas_i,mDificultad);//No se crea con contructor copia pq es una nueva generacion!//
                chino.Resolver();
            }

        }


        /**
         *
         * @param sudoku_del_chino Sudoku padre del cual se van a suponer valores. El sudoku padre ...
         *                         Va generar varias ramas.
         *                         Va generar varias vias.
         *                         Va generar varias valores.
         *
         * @param cant_valores_posibles La cantidad de valores que pueden ser escritos en una celda
         * Nota:No confundir cantidad_valores_posibles con valores posibles
         * @return retorna un arreglo de sudokus que solo se diferencian en el valor de una celda. La celda puede tener
         */
        private List<Sudoku> SuponerValores(Sudoku sudoku_del_chino, int cant_valores_posibles) {

            List<Sudoku> listaSudokus=new ArrayList<>();

            return listaSudokus;//Si retorna null significa q ninguna casilla puede aceptar @cant_valores_posibles







        }


        private List<Integer> RecogerNumerosFaltantesColumna(int columna) {
            List<Integer> Numeros=Sudoku.getListaNumeros();

            for (int fil = 0; fil < 9; fil++) {
                if (sudoku_del_chino.getCelda(columna,fil).TieneValor())
                {
                    Numeros.remove(Integer.valueOf(sudoku_del_chino.getCeldaValor(fil,columna)));//Aqui el valor fue convertido a Integer pq de lo contrario la funcion cambiaria(funcion sobrecargada)
                }
            }

            return Numeros;


        }

        private List<Integer> RecogerNumerosFaltantesFila(int fila) {

            List<Integer> Numeros=Sudoku.getListaNumeros();

            for (int col = 0; col < 9; col++) {
                if (sudoku_del_chino.getCelda(fila,col).TieneValor())
                {
                    Numeros.remove(Integer.valueOf(sudoku_del_chino.getCeldaValor(fila,col)));//Aqui el valor fue convertido a Integer pq de lo contrario la funcion cambiaria(funcion sobrecargada)
                }
            }

            return Numeros;


        }

        /**
         *Retorna true si el sudoku fue modificado con respecto al anterior
         *
         */
        private boolean SudokuFueEscrito() {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if(                     mSudokuplanoAntes.getValue(i,j)!=
                            sudoku_del_chino.getMatrizPlana().getValue(i,j)){return true;}//Si algun valor es distinto entonces la fue modificada
                }
            }

            return false;
        }

        private boolean TieneErrores()
        {
            List<Integer> Numeros_En_La_Fila;
            List<Integer> Numeros_En_La_Columna;
            List<Integer> Numeros_En_El_Cuadro;

            for (int i = 0; i < 9; i++) {
                Numeros_En_La_Fila= RecogerNumerosFila(i);
                if (TieneValoresDuplicados(Numeros_En_La_Fila)) {return true;}

                Numeros_En_La_Columna= RecogerNumerosColumna(i);
                if (TieneValoresDuplicados(Numeros_En_La_Columna)) {return true;}

                Numeros_En_El_Cuadro= RecogerNumerosCuadro(i+1);//Cuadro empieza en 1
                if (TieneValoresDuplicados(Numeros_En_El_Cuadro)) {return true;}
            }
            return false;


        }


        /**
         * dada una lista de integer retorna true si algun numero se repite
         * @param lista_de_numeros
         * @return
         */
        private boolean TieneValoresDuplicados(List<Integer> lista_de_numeros)
        {
            for (int i = 0; i < lista_de_numeros.size()-1; i++) {//size-1--> El ultimo valor no se va a repetir pq no tiene valores delante

                for (int j = i+1; j < lista_de_numeros.size(); j++) {//empieza en i+1 pq no se va contar el primer valor
                    if (lista_de_numeros.get(i).equals(lista_de_numeros.get(j))){return true;}//si un valor coinside con alguno de los consecutivos retornar true
                }
            }
            return false;
        }

        private List<Integer> RecogerNumerosFila(int fila) {
            List<Integer> listado=new ArrayList<>();

            for (int col = 0; col < 9; col++) {
                if (sudoku_del_chino.getCelda(fila,col).TieneValor())
                {
                    listado.add(sudoku_del_chino.getCeldaValor(fila,col));
                }
            }

            return listado;
        }

        private List<Integer> RecogerNumerosColumna(int columna) {
            List<Integer> listado=new ArrayList<>();

            for (int fil = 0; fil < 9; fil++) {
                if (sudoku_del_chino.getCelda(fil,columna).TieneValor())
                {
                    listado.add(sudoku_del_chino.getCeldaValor(fil,columna));
                }
            }

            return listado;
        }



        private  List<Integer> RecogerNumerosCuadro(int cuadro) {
            List<Integer> listado=new ArrayList<>();


            int fil= MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
            int col= MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);


            for (int f = 0; f < 3; f++) {// 0 1 2
                for (int c = 0; c < 3; c++) {// 0 1 2
                    if (sudoku_del_chino.getCelda(fil+f,col+c).TieneValor()) {listado.add(sudoku_del_chino.getCeldaValor(fil+f,col+c));}
                }
            }
            return listado;
        }

        private void MarcarCeldasFila(List<Integer> numeros_en_la_fila, int fila) {

            for (int col = 0; col < 9; col++) {
                if (!sudoku_del_chino.getCelda(fila,col).TieneValor())

                {
                    for (int valores = 0; valores < numeros_en_la_fila.size(); valores++) {

                        //Log.e("Marca_de_Fila","Celda "+String.valueOf(fila)+" "+String.valueOf(col)+"--> "+String.valueOf(numeros_en_la_fila.getCelda(valores)));
                        MarcarCelda(fila,col,numeros_en_la_fila.get(valores));

                    }

                }
            }

        }

        private void MarcarCeldasColumna(List<Integer> numeros_en_la_columna, int columna) {

            for (int fil = 0; fil < 9; fil++) {
                if (!sudoku_del_chino.getCelda(fil,columna).TieneValor())

                {
                    for (int valores = 0; valores < numeros_en_la_columna.size(); valores++) {

                        //Log.e("Marca_de_Colu","Celda "+String.valueOf(fil)+" "+String.valueOf(columna)+"--> "+String.valueOf(numeros_en_la_columna.getCelda(valores)));
                        MarcarCelda(fil,columna,numeros_en_la_columna.get(valores));

                    }

                }
            }

        }

        private void MarcarCeldasCuadro(List<Integer> numeros_en_el_cuadro, int cuadro) {

            int fil = MatrizCeldas.Cuadro.ObtenerFilaInicial(cuadro);
            int col = MatrizCeldas.Cuadro.ObtenerColumnaInicial(cuadro);

            for (int f = 0; f < 3; f++) {// 0 1 2
                for (int c = 0; c < 3; c++) {// 0 1 2
                    if (!sudoku_del_chino.getCelda(fil + f, col + c).TieneValor()) {
                        for (int valores = 0; valores < numeros_en_el_cuadro.size(); valores++) {
                            //Log.e("Marca_de_Cuad","Celda "+String.valueOf(fil+ f)+" "+String.valueOf(col+ c)+"--> "+String.valueOf(numeros_en_el_cuadro.getCelda(valores)));
                            MarcarCelda(fil+ f, col+c, numeros_en_el_cuadro.get(valores));
                        }
                    }
                }

            }
        }

        /**
         * @return 0 si no se puede escribir. De poderse escribir, retorna el valor que se puede
         *
         */
        private int PuedoEscribirCelda(MatrizCeldas.Celda celda) {

            int contador_de_Falses=0;//False significa q el numero puede ser escrito!
            int valor_celda=0;

            for (int pos = 1; pos <10 ; pos++) {//Las marcas se trabajan del 1 al 9
                if (!celda.getMarca(pos)){
                    contador_de_Falses++;
                    if (contador_de_Falses>1){return 0;}//Si hay varias numeros en false ya no se puede escribir
                    valor_celda=pos;
                }
            }
            return valor_celda;

        }

        private void EscribirCelda(MatrizCeldas.Celda celda, int valor) {
            celda.setValorPencil(valor);
        }

        private void MarcarCelda(int fila, int col, int valor) {


            sudoku_del_chino.getCelda(fila,col).setMarca(valor);

        }

        private String NombrarChino() {

            StringBuilder stringBuilder=new StringBuilder();
            if (mParentCount==0){stringBuilder.append("Progenitor");}
            else
            {
                stringBuilder.append("Generacion: ").append(mParentCount).append(" --> ");
                stringBuilder.append("Chino: ").append(mIdChino+1);
            }
            return stringBuilder.toString();
        }






    public enum Dificuldad
    {
        Facil, Medio, Dificil, Extremo
    }

    }

