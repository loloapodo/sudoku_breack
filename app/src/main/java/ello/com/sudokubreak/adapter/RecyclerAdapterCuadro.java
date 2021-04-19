package ello.com.sudokubreak.adapter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ello.com.sudokubreak.R;
import ello.com.sudokubreak.alg.MatrizCeldas;
import ello.com.sudokubreak.interfaces.click_i;

/**
 * Created by Laptop on 4/17/2020.
 */
public class RecyclerAdapterCuadro extends RecyclerView.Adapter<RecyclerAdapterCuadro.NumeroHold> {


    click_i upper_listener;
    MatrizCeldas.Cuadro cuadro;

    private int NoCuadro;






    public RecyclerAdapterCuadro(MatrizCeldas.Cuadro cuadro, int No_Cuadro_de_este_adapter, click_i listener) {
        this.cuadro =cuadro;
        this.NoCuadro=No_Cuadro_de_este_adapter;
        this.upper_listener =listener;
    }



    @Override
    public NumeroHold onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_numero,parent,false);
        return new NumeroHold(view);
    }

    @Override
    public void onBindViewHolder(final NumeroHold holder, int position) {
        if (cuadro.getCelda(position).getValor()!=0)
        {
            holder.textView_numero.setText(String.valueOf(cuadro.getCelda(position).getValor()));
            if (cuadro.getCelda(position).getPencil()){holder.textView_numero.setTextColor(ContextCompat.getColor(holder.textView_numero.getContext(),R.color.lapiz_color));}



//Se colorea con rojo debido en caso de errores
            if (upper_listener.HayNumerosMarcados()) {

            MatrizCeldas.Celda celda[]= upper_listener.GetCeldasMarcadas();

            for (int i = 0; i < celda.length; i++) {// chequear si las celdas van a coincidir

                if (celda[i].getCuadro()==NoCuadro)//si coincide en el cuadro
                {
                    if (celda[i].getPos()==position)
                    {
                        holder.textView_numero.setTextColor(ContextCompat.getColor(holder.textView_numero.getContext(),R.color.quantum_white_100));
                        holder.textView_numero.setBackgroundColor(ContextCompat.getColor(holder.textView_numero.getContext(),R.color.notif_red));

                    }
                }
            }
            }


















        }
            holder.textView_numero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Valor "+holder.textView_numero.getText(),"Cuadro-"+String.valueOf(NoCuadro)+" Numero: "+String.valueOf(holder.getAdapterPosition()));
                upper_listener.click_numero(NoCuadro,holder.getAdapterPosition(),0);

            }
        });
    }




    @Override
    public int getItemCount() {
        return 9;
    }



    public class NumeroHold extends RecyclerView.ViewHolder {
        TextView textView_numero;
        public NumeroHold(View itemView) {
            super(itemView);
            textView_numero=(TextView) itemView.findViewById(R.id.item_numero_textview);
        }
    }


}
