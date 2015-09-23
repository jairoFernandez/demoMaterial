package net.tucompualdia.ajuste.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.tucompualdia.ajuste.fragments.CalculatorFragment;
import net.tucompualdia.ajuste.fragments.CatalogFragment;

/**
 * Created by jairo.fernandez on 23/09/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CalculatorFragment tab1 = new CalculatorFragment();
                return tab1;
            case 1:
                CatalogFragment tab2 = new CatalogFragment();
                return tab2;
            case 2:
                CatalogFragment tab3 = new CatalogFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
