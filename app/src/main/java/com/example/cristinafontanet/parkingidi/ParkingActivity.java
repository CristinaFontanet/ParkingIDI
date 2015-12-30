package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.sql.Time;
import java.sql.Timestamp;

/*
 * Created by CristinaFontanet on 20/12/2015.
 */
public class ParkingActivity extends AppCompatActivity implements View.OnClickListener, DialogNewCar.OnCompleteListener, DialogExitCar.OnFragmentInteractionListener, ViewPager.OnPageChangeListener, DialogHistory.OnFragmentInteractionListener, DialogDateChooser.OnFragmentInteractionListener,DialogHourChoose.OnFragmentInteractionListener {
    private Controller bigControl;
    private TabPagerAdapter pagAdapt;
    private DialogHistory dialogHistoryFragment;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private int tabPosition;
    private int historicType;
    private Timestamp tini,tend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageadapter_layout);
        bigControl = new Controller(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new TabPagerAdapter(getFragmentManager(), PagerHolder.this));       Para fragments normales
        pagAdapt = new TabPagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);     //Solo para MATERIAL
        viewPager.addOnPageChangeListener(this);
        tabPosition = viewPager.getCurrentItem();
        historicType = 0;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(Color.DKGRAY, Color.BLACK);      //Per canviar el color de la pestanya a on estas
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.androidadd512));
        Log.i("PARKACT", "He creat el floatingActionButton: ");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tabPosition==0) {
                    if (bigControl.getNumFreePeaches() > 0) {
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        DialogFragment dialogFragment = DialogNewCar.newInstance();
                        dialogFragment.show(frag, "AskRegistration");
                    } else {
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.busyParking), getString(R.string.ok));
                        dialogFragment.show(frag, "ShowMessage");
                    }
                }
                else {
                    FragmentTransaction frag = getFragmentManager().beginTransaction();
                    dialogHistoryFragment = DialogHistory.newInstance(historicType,tini,tend);
                    dialogHistoryFragment.show(frag,"DialogHistory");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.action_settings:
               break;
           case R.id.menu_drain:
               bigControl.drainParking();
               bigControl.showFastToast("Parking buidat", this);
               break;
           case R.id.menu_historicDrain:
               bigControl.drainHistoric();
               pagAdapt = new TabPagerAdapter(getSupportFragmentManager(), this, bigControl);
               viewPager.setAdapter(pagAdapt);     //Solo para MATERIAL
               bigControl.showFastToast("Historial buidat",this);
               break;
           case R.id.menu_desfer:
               bigControl.showFastToast("Encara no està implementat",this);
               break;
           default:
               break;

       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onComplete(String res) {
        Log.i("PARKINGACTIVITY", "Entro al onComplete dspres d'haver ocupat una nova plasa");
        pagAdapt.forceOnComplete(res);
        pagAdapt = new TabPagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);
    }

    @Override
    public void onFragmentInteraction(Boolean uri) {
        pagAdapt.forceOnFragmentInteraction(uri);
        pagAdapt = new TabPagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);

    }

    @Override
    public void onClick(View view) {
        Log.i("PARKACT", "ON CLICK!!!!!!!!!!!!!! de ParkingActivity");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("PARKACT", "onPageSelected!!!!!!!!!!!!!!");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(tabPosition!=viewPager.getCurrentItem()) {
            tabPosition = viewPager.getCurrentItem();
            Log.i("PARKACT", "Current item: " + tabPosition);
            if(tabPosition==0)fab.setImageDrawable(getResources().getDrawable(R.drawable.androidadd512));
            else fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_month));
        }
    }

    @Override
    public void onFragmentInteraction(int dayOfMonth, int month, int year) {
        Log.i("DATE","onFragmentInteraction Del PARKING ACTIVITY");
        if(dialogHistoryFragment!=null) {
            dialogHistoryFragment.selectedDate(dayOfMonth,month,year);
        }
    }

    @Override
    public void onFragmentInteraction(Integer currentHour, Integer currentMinute) {
        if(dialogHistoryFragment!=null) dialogHistoryFragment.selectedHour(currentHour, currentMinute);
    }

    @Override
    public void onFragmentInteraction(int i, Timestamp iniTime, Timestamp endTime) {
        historicType = i;
        if(i==0) {
            pagAdapt.showAllHistoric();
        }
        else if (i==1) {
            pagAdapt.showTodayHistoric();
        }
        else {
            pagAdapt.showHistoBetween(iniTime,endTime);
            tini=iniTime;
            tend = endTime;
        }

    }
}
