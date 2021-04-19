package ello.com.sudokubreak.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 4/12/2020.
 */
public class SectionPageAdaper extends FragmentPagerAdapter {

    private List<Fragment> lista_fragmentos;

    public SectionPageAdaper(FragmentManager fm) {
        super(fm);
        lista_fragmentos=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return lista_fragmentos.get(position);
    }

    @Override
    public int getCount() {
        return lista_fragmentos.size();
    }

    public void AgregarFragmento(Fragment f) {
        lista_fragmentos.add(f);
    }



}
