package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/*
 * Created by CristinaFontanet on 20/12/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter  {

    public static ParkingStatus ajudaParking;
    public static historicFragment ajudaHisto;
    public Controller bigControl;
    final int PAGE_COUNT = 2;
    private static String tabTitles[];
    android.support.v4.app.Fragment tab = null;
    private static Activity father;

    public TabPagerAdapter(FragmentManager fm, Activity fatherAct, Controller contr) {
        super(fm);
        father = fatherAct;
        bigControl = contr;
        tabTitles = new String[] {father.getString(R.string.tabParking) ,father.getString(R.string.tabHisto) };
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch(position) {
            case 0:
                Log.i("OPEN","ENCARA NOOOOO HE CREAT EL PARKINGSTATUS");
                ajudaParking = new ParkingStatus();
                Log.i("OPEN","JA HE CREAT EL PARKINGSTATUS");
                ajudaParking.sendFather(this, bigControl,father);
                tab = ajudaParking;
                break;
            case 1:
                ajudaHisto = new historicFragment();
                ajudaHisto.sendFather(this, bigControl,father);
                tab = ajudaHisto;
                break;
        }
        return tab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}

