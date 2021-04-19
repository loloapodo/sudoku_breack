package ello.com.sudokubreak.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import java.util.List;

import ello.com.sudokubreak.R;
import ello.com.sudokubreak.alg.Sudoku;
import ello.com.sudokubreak.interfaces.click_i;


/**
 * Created by Laptop on 4/19/2020.
 * La variable numeros trae en orden 1 2 3 4 5 6 7 8 9 X
 * Las clases agenas a esta Tratan la X como el valor cero
 * Debido a esto la clase tiene ajustes necesarios.
 */
public class RecyclerAdapterPicker extends RecyclerView.Adapter<RecyclerAdapterPicker.NumberPick> {


    List<String> Numeros;
    click_i listener;
    int mNumero_Seleccionado;

    public void setmNumero_Seleccionado(int numero) {
        this.mNumero_Seleccionado = AjustarNumeroSeleccionado(numero);
    }

    private int AjustarNumeroSeleccionado(int n) {
        if (n==0){return 9;}
        return n-1;
    }

    public RecyclerAdapterPicker(click_i frag1_click_listener, int numero)
    {
        Numeros=Sudoku.getListaNumerosStr();

        Numeros.add("X");
        listener=frag1_click_listener;
        this.mNumero_Seleccionado=AjustarNumeroSeleccionado(numero)-1;
    }


    @Override
    public NumberPick onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker, parent, false);
        return new NumberPick(view);
    }

    @Override
    public void onBindViewHolder(final NumberPick holder, final int position) {


        String n=Numeros.get(position);
        int color_numero=R.color.grey_60;
        if (position==mNumero_Seleccionado)
        {
            color_numero=R.color.colorPrimaryDark;
            holder.fab.startAnimation(AnimationUtils.loadAnimation(holder.fab.getContext(),R.anim.shake));
        }//Numeros

        holder.fab.setImageBitmap(textAsBitmap(n,800, ContextCompat.getColor(holder.fab.getContext(),color_numero)));

        holder.fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (position==9){listener.click_pick(0);}//para borrar--> X
                    else {listener.click_pick(position+1);}//Enviando los numeros del 1 al 9
                }
                    return false;
            };
        });







    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class NumberPick extends RecyclerView.ViewHolder {
        FloatingActionButton fab;

        public NumberPick(View itemView) {
            super(itemView);
            fab = (FloatingActionButton) itemView.findViewById(R.id.flotante_number);
        }
    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}