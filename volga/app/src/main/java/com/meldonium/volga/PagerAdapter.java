package com.meldonium.volga;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by androidwarriors on 10/16/2015.
 */
//public class PagerAdapter extends FragmentStatePagerAdapter {
public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new StatFragment();
                break;
            case 1:
                frag=new BoxFragment();
                break;
            case 2:
                frag=new HistoryFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Info";
                break;
            case 1:
                title="Box";
                break;
            case 2:
                title="History";
                break;
        }

        return title;
    }
}
