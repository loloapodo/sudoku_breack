package ello.com.sudokubreak;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import ello.com.sudokubreak.adapter.SectionPageAdaper;

import ello.com.sudokubreak.alg.Chino;
import ello.com.sudokubreak.alg.JefeChinos;
import ello.com.sudokubreak.alg.MatrizCeldas;
import ello.com.sudokubreak.alg.Sudoku_plano;
import ello.com.sudokubreak.alg.Solucion;
import ello.com.sudokubreak.fragment.Frag_Sudoku;
import ello.com.sudokubreak.interfaces.Frag1Comunication_I;
import ello.com.sudokubreak.interfaces.Respuestas_I;


/**
 * Created by Laptop on 4/12/2020.
 */
public class Principal extends AppCompatActivity implements Frag1Comunication_I {

    private SectionPageAdaper adapter;
    private ViewPager vp;
    boolean EsPrimeraSolucion;
    boolean EstaEjecutandose=false;

    private Frag_Sudoku fragment1;
    private Frag_Sudoku fragment2;



    public Solucion mSoluciones; int iter_SolucionesMostradas;
    public Chino empleado;
    public Respuestas_I respuestas_i;

//OJO-------------------------      SUBIR EL HOLDER A UN NIVEL MAS ARRIBA

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Init_Tab();

    }

    private void Init_Tab() {

         adapter=new SectionPageAdaper(getSupportFragmentManager());
        CrearFragmento1();
        CrearFragmento2();
        Nombrar_Tabla_Superior();



    }

    private void Nombrar_Tabla_Superior() {
        vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(adapter);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);
        tabLayout.getTabAt(0).setText(getString(R.string.Tab1));
        tabLayout.getTabAt(1).setText(getString(R.string.Tab2));

    }

    private void CrearFragmento1() {

         fragment1= Frag_Sudoku.newInstance(1);
        adapter.AgregarFragmento(fragment1);
        fragment1.SetSudoku(new Sudoku_plano(JefeChinos.getSudokuVacio()));
    }
    private void CrearFragmento2() {

        fragment2= Frag_Sudoku.newInstance(2);
        adapter.AgregarFragmento(fragment2);
        fragment2.SetSudoku(new Sudoku_plano(JefeChinos.getSudokuVacio()));


    }







    @Override
    public boolean EstaEjecutandose() {
        return EstaEjecutandose;
    }

    @Override
    public void ActualizarTodosBotones() {
        fragment1.ActualizarBoton(EstaEjecutandose);
        fragment2.ActualizarBoton(EstaEjecutandose);
    }


    public void MostrarOtraSolucionClick(View view) {
        iter_SolucionesMostradas++;
        if (iter_SolucionesMostradas==mSoluciones.getCantidadSoluciones()){iter_SolucionesMostradas=0;}
        fragment2.SetSudoku(mSoluciones.getSoluciones().get(iter_SolucionesMostradas));
    }

    public void BotonClick(View view) {


        if (EstaEjecutandose)
        {
            mSoluciones.setFullForzado(true);
            empleado.cancel(true);
            empleado=null;

            EstaEjecutandose=false;
            return;
        }


        fragment1.Refrescar_Marcadas_en_rojo();


        EstaEjecutandose=true;
        EsPrimeraSolucion=true;




        fragment2.SetSudoku(new Sudoku_plano());//Limpiar el fragmento 2
        Sudoku_plano sudoku2=new Sudoku_plano(fragment1.getSudoku());

        respuestas_i=new Respuestas_I() {


            @Override
            public void NotificarErrordeEntrada(final MatrizCeldas.Celda celda[]) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        vp.setCurrentItem(0,true);
                        fragment1.SetRed(celda);
                        EstaEjecutandose=false;
                        fragment1.ActualizarBoton();
                        fragment2.ActualizarBoton();

                    }
                });



            }

            @Override
            public void NotificarSudokuSinSolucion() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        vp.setCurrentItem(0,true);
                        fragment1.SetYellow();
                        EstaEjecutandose=false;
                        fragment1.ActualizarBoton(EstaEjecutandose);
                        fragment2.ActualizarBoton(EstaEjecutandose);

                    }
                });



            }

            @Override
            public void NotificarFinalAlgoritmo(final Solucion soluciones) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        fragment2.ActualizarDetalles(soluciones.getCantidadSoluciones(),true);
                        if (soluciones.getCantidadSoluciones()>1){fragment2.ExpandDetalis();}
                        EstaEjecutandose=false;
                        fragment1.ActualizarBoton(EstaEjecutandose);
                        fragment2.ActualizarBoton(EstaEjecutandose);


                    }
                });



            }

            @Override
            public void Nueva_Solucion(final Solucion soluciones,final Chino.Dificuldad dificuldad) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {


                        if(EsPrimeraSolucion)
                        {

                            fragment2.SetSudoku(soluciones.getSoluciones().get(0));
                            fragment2.Refrescar_Sudoku();
                            fragment2.SetGreen();
                            fragment2.ActualizarDetalles(1,dificuldad,fragment1.getSudoku().getTotalCeldasVacias());
                            vp.setCurrentItem(1,true);//Enfocar la pagina 2
                            EsPrimeraSolucion=false;
                            iter_SolucionesMostradas=0;
                        }
                        else
                        {
                            fragment2.ExpandDetalis();
                            fragment2.ActualizarDetalles(soluciones.getCantidadSoluciones(),false);
                        }



                    }
                });



            }

        };


        mSoluciones=new Solucion(Integer.MAX_VALUE-1);
        empleado= new Chino(sudoku2,0,0,mSoluciones, respuestas_i);
        empleado.execute();

    }

    @Override
    protected void onDestroy() {
        if (mSoluciones!=null){mSoluciones.setFullForzado(true);}
        super.onDestroy();
    }
}
