package ello.com.sudokubreak.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ello.com.sudokubreak.R;
import ello.com.sudokubreak.adapter.RecyclerAdapterPicker;
import ello.com.sudokubreak.adapter.RecyclerAdapterSudoku;
import ello.com.sudokubreak.alg.Chino;
import ello.com.sudokubreak.alg.MatrizCeldas;
import ello.com.sudokubreak.alg.Sudoku_plano;
import ello.com.sudokubreak.alg.Sudoku;
import ello.com.sudokubreak.interfaces.Frag1Comunication_I;
import ello.com.sudokubreak.interfaces.click_i;
import ello.com.sudokubreak.snippets.expand;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1Comunication_I} interface
 * to handle interaction events.
 * Use the {@link Frag_Sudoku#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_Sudoku extends Fragment implements Frag1Comunication_I {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SCREEN_NUMBER = "screen_number";
    private RecyclerAdapterSudoku mRecyclerAdapterSudoku;
    private  RecyclerView mRecyclerView;
    private  RecyclerView mRecyclerViewPicker;
    private RecyclerAdapterPicker mRecyclerAdapterPicker;
    private int mNumero_Seleccionado;//Numero seleccionado por el picker
    private int mColorBoton;// 1 azul 2 verde 3 rojo
    //CREAR DE UNA VEZ EL ENUM

    private LinearLayout mNotifLinear;
    //private ImageView mNotifImage;
    private TextView mNotifText;
    private Button mBotonSoluc;

    private TextView mNivelDif;

    private TextView mCantidadSoluciones;
    private LinearLayout mLayMasSoluciones;boolean mLayMasSolucionesCollapsed;



    // TODO: Rename and change types of parameters
    private int mScreenNumber;//1 y 2 ya q el fragmento se utiliza en las dos primeras pantallas
    private String mName;
    private View ThisView;
    private Sudoku_plano sudoku;
    private Frag1Comunication_I mListener;
    private click_i frag1_click_listener;

    private CardView mCardView;
    private Context mAct;
    private Animation animationes;
    private boolean mHayNumerosMarcados;
    private MatrizCeldas.Celda[] mCeldasMarcadas;

    public Frag_Sudoku() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Frag_Sudoku newInstance(int param1) {
        Frag_Sudoku fragment = new Frag_Sudoku();
        Bundle args = new Bundle();
        args.putInt(SCREEN_NUMBER, param1);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        mNumero_Seleccionado=-1;
        mColorBoton=1;//azul

        //Definir interfase para comunicar con el adapter mas bajo
        frag1_click_listener=new click_i() {
            @Override
            public void click_numero(int cuadro, int pos, int valor) {

                if (mNumero_Seleccionado!=-1){
                    sudoku.LlenarCelda_By_Cuadro(cuadro,pos,mNumero_Seleccionado);
                    Refrescar_Sudoku();
                }




            }

            @Override
            public void click_pick(int valor) {
                mNumero_Seleccionado=valor;
                Refrescar_Picker();
            }

            @Override
            public boolean HayNumerosMarcados() {
                return mHayNumerosMarcados;
            }

            @Override
            public MatrizCeldas.Celda[] GetCeldasMarcadas() {
                return mCeldasMarcadas;
            }
        };
        super.onCreate(savedInstanceState);

            mScreenNumber = getArguments().getInt(SCREEN_NUMBER);



    }

    private void InicializarAnimaciones() {
        animationes= AnimationUtils.loadAnimation(ThisView.getContext(),R.anim.blinking);
        animationes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBotonSoluc.setVisibility(View.VISIBLE);
                mListener.ActualizarTodosBotones();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



    private void Refrescar_Picker() {
        mRecyclerAdapterPicker.setmNumero_Seleccionado(mNumero_Seleccionado);
        mRecyclerAdapterPicker.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         if (mScreenNumber==1){ThisView=inflater.inflate(R.layout.fragmento1, container, false);}
        else {ThisView=inflater.inflate(R.layout.fragmento2, container, false);}
        mBotonSoluc=(Button) ThisView.findViewById(R.id.boton_solucionar);
        mNotifLinear=(LinearLayout)ThisView.findViewById(R.id.notificLay);
        //mNotifImage=(ImageView)ThisView.findViewById(R.id.notificImageview);
        mNotifText=(TextView)ThisView.findViewById(R.id.notificaciontextview);
        mNotifLinear.setVisibility(View.GONE);
        mCardView =(CardView)ThisView.findViewById(R.id.cardview_sudoku);
        InicializarAnimaciones();
        ConformarInterfazSudoku();
        if (mScreenNumber==1){ConformarInterfazPicker();}
        else {InicializarViewsDetalles();mBotonSoluc.setVisibility(View.INVISIBLE);}






        return ThisView;
    }

    private void InicializarViewsDetalles() {
         mNivelDif=(TextView)ThisView.findViewById(R.id.textview_dificulty);

         mCantidadSoluciones=(TextView)ThisView.findViewById(R.id.textview_cantidadsoluciones);
         mLayMasSoluciones=(LinearLayout)ThisView.findViewById(R.id.lay_otra_solucion);
         expand.collapse(mLayMasSoluciones);mLayMasSolucionesCollapsed=true;
    }


    private void ConformarInterfazSudoku() {
        mRecyclerView= (RecyclerView) ThisView.findViewById(R.id.recycler_sudoku);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mAct,3));
        mRecyclerAdapterSudoku=new RecyclerAdapterSudoku(Sudoku.getCuadrosFromSudoku(sudoku),frag1_click_listener);
        mRecyclerView.setAdapter(mRecyclerAdapterSudoku);
    }






    private void ConformarInterfazPicker() {

        mRecyclerViewPicker= (RecyclerView) ThisView.findViewById(R.id.recycler_picker);
        mRecyclerViewPicker.setHasFixedSize(true);
        GridLayoutManager llm=new GridLayoutManager(mAct,5);
        mRecyclerViewPicker.setLayoutManager(llm);
        mRecyclerAdapterPicker=new RecyclerAdapterPicker(frag1_click_listener,mNumero_Seleccionado);
        mRecyclerViewPicker.setAdapter(mRecyclerAdapterPicker);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Frag1Comunication_I) {
            mListener = (Frag1Comunication_I) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ello.com.sudokubreak.interfaces.Frag1Comunication_I");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }






    public Sudoku_plano getSudoku()
    {
        return sudoku;
    }




    public void Refrescar_Sudoku() {
        if (mRecyclerAdapterSudoku!=null)
        {
            mRecyclerAdapterSudoku.ActualizarData(Sudoku.getCuadrosFromSudoku(sudoku));
            mRecyclerAdapterSudoku.notifyDataSetChanged();
        }

    }


    public void SetBlue()
    {
        if (mColorBoton==1){return;}
        mColorBoton=1;
        //mNotifLinear.setVisibility(View.GONE);
        mBotonSoluc.setVisibility(View.VISIBLE);

    }
    public void SetGreen()
    {

        mNotifLinear.setBackgroundResource(R.color.notif_green);
        mColorBoton=2;

        mNotifLinear.setVisibility(View.VISIBLE);
        mNotifText.setVisibility(View.VISIBLE);
        mNotifText.setText(getString(R.string.notif_green));

        AnimarBoton();

    }

    public void SetRed(MatrizCeldas.Celda[] celda)
    {
        mHayNumerosMarcados=true;
        mCeldasMarcadas=celda;


        mNotifLinear.setBackgroundResource(R.color.notif_red);
        mColorBoton=3;


                mNotifLinear.setVisibility(View.VISIBLE);
                mNotifText.setVisibility(View.VISIBLE);
                mNotifText.setText(getString(R.string.notif_red));


        AnimarBoton();
        mCardView.startAnimation(AnimationUtils.loadAnimation(ThisView.getContext(),R.anim.shake));
        Refrescar_Sudoku();// Se refresca para q muestre las casillas en rojo

    }


    public void SetYellow()
    {

        mNotifLinear.setBackgroundResource(R.color.notif_yellow);
        mColorBoton=4;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                mNotifLinear.setVisibility(View.VISIBLE);
                mNotifText.setVisibility(View.VISIBLE);
                mNotifText.setText(getString(R.string.notif_yellow));

            }
        });
        AnimarBoton();

    }




    /**
     * Esconde el boton
     * Muestra notificacion durante unos segundos
     * Luego muestra el boton (tiene asociado un anim listener)
     */
    private void AnimarBoton() {
        mBotonSoluc.setVisibility(View.INVISIBLE);
        mNotifLinear.startAnimation(animationes);
    }

    public void SetSudoku(Sudoku_plano s) {
        this.sudoku=s;
        Refrescar_Sudoku();
    }


    public void ActualizarDetalles(long cantidad_soluciones, Chino.Dificuldad dificultad,int numeros_completados) {

        mCantidadSoluciones.setText("1");

        if (dificultad== Chino.Dificuldad.Extremo){mNivelDif.setText(getString(R.string.dif_extremo));}
        if (dificultad== Chino.Dificuldad.Dificil){mNivelDif.setText(getString(R.string.dif_dificil));}
        if (dificultad== Chino.Dificuldad.Medio){mNivelDif.setText(getString(R.string.dif_medio));}
        if (dificultad== Chino.Dificuldad.Facil){mNivelDif.setText(getString(R.string.dif_facil));}

    }


    public void ActualizarDetalles(final long cantidad_soluciones, final boolean isFinished) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (cantidad_soluciones!=1)
                {

                    if (isFinished){mCantidadSoluciones.setText(String.valueOf(cantidad_soluciones));}//Muestra el valor real
                    else {mCantidadSoluciones.setText(String.valueOf(cantidad_soluciones-1));}//Se muestra un valor antes. Para poder de 100 en 100. Adecuado al user


                }


            }
        });


    }

    @Override
    public boolean EstaEjecutandose() {
        //pertenece a principal
        return false;
    }

    @Override
    public void ActualizarTodosBotones() {
        //pertenece a principal
    }


    /**
     * una funcion utiliza la otra por efectividad
     */
    public void ActualizarBoton() {
        ActualizarBoton(mListener.EstaEjecutandose());

    }

    /**
     * una funcion utiliza la otra por efectividad
     */
    public void ActualizarBoton(boolean estaEjecutandose) {

        if(estaEjecutandose){mBotonSoluc.setText(R.string.boton_parar);}
        else
        {
          if (mScreenNumber==1){ mBotonSoluc.setText(R.string.boton_solucionar);}
            else if (mScreenNumber==2){mBotonSoluc.setVisibility(View.INVISIBLE);}
        }

    }

    public void Refrescar_Marcadas_en_rojo() {
        if (mHayNumerosMarcados){mHayNumerosMarcados=false;Refrescar_Sudoku();}
    }


    public void ExpandDetalis() {
        if (mLayMasSolucionesCollapsed){expand.expand(mLayMasSoluciones);mLayMasSolucionesCollapsed=false;}
    }
}
