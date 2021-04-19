package ello.com.sudokubreak.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ello.com.sudokubreak.R;
import ello.com.sudokubreak.alg.MatrizCeldas;
import ello.com.sudokubreak.interfaces.click_i;

/**
 * Created by Laptop on 4/12/2020.
 */
public class RecyclerAdapterSudoku extends RecyclerView.Adapter<RecyclerAdapterSudoku.CuadroHold> {
    private  List<MatrizCeldas.Cuadro> mCuadros;
    RecyclerAdapterCuadro mRecyclerAdapterCuadro;
    click_i listener;

    public RecyclerAdapterSudoku(List<MatrizCeldas.Cuadro> cuadros,click_i listener) {
        mCuadros=cuadros;
        this.listener =listener;
    }

    @Override
    public CuadroHold onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_and_recycler_cuadro,parent,false);
        return new CuadroHold(view);

    }

    @Override
    public void onBindViewHolder(CuadroHold holder, int position) {


        mRecyclerAdapterCuadro=new RecyclerAdapterCuadro(mCuadros.get(position),position+1, listener);
        holder.mRecyclerViewCuadro.setHasFixedSize(true);
        holder.mRecyclerViewCuadro.setLayoutManager(new GridLayoutManager(holder.mRecyclerViewCuadro.getContext(),3));
        holder.mRecyclerViewCuadro.setAdapter(mRecyclerAdapterCuadro);

    }

    @Override
    public int getItemCount() {
        return 9;
    }


    public void ActualizarData(List<MatrizCeldas.Cuadro> cuadrosFromSudoku) {
    mCuadros=cuadrosFromSudoku;
    }




    public class CuadroHold extends RecyclerView.ViewHolder{
        RecyclerView mRecyclerViewCuadro;
        public CuadroHold(View itemView){
            super(itemView);
            mRecyclerViewCuadro=(RecyclerView) itemView.findViewById(R.id.recycler_cuadro);

        }
    }




}
