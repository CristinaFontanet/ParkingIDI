package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.Timestamp;

/*
 * Created by CristinaFontanet on 20/12/2015.
 */
public class ParkingActivity extends AppCompatActivity implements View.OnClickListener, DialogNewCar.OnCompleteListener, DialogExitCar.OnFragmentInteractionListener, ViewPager.OnPageChangeListener, DialogHistory.OnFragmentInteractionListener, DialogDateChooser.OnFragmentInteractionListener,DialogHourChoose.OnFragmentInteractionListener, Dialog2But.OnCompleteListener, DialogPriceChanger.OnFragmentInteractionListener {
    private Controller bigControl;
    private TabPagerAdapter pagAdapt;
    private DialogHistory dialogHistoryFragment;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private int tabPosition;
    private int historicType;
    private Timestamp tini,tend;

    public void hideFloatingActionButton() {
        fab.hide();
    }

    public void showFloatingActionButton() {
        fab.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageadapter_layout);
        SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Double price = Double.valueOf(settings.getString("Price", "0.02"));

        bigControl = new Controller(this,price);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFloatingActionButton();
            }
        });

    }

    private void changePrice(Double newPrice) {
        SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Price", newPrice.toString());
        editor.apply();
        bigControl.changePrice(newPrice);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.menu_help:
               FragmentTransaction frag = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment = DialogHelp.newInstance();
               dialogFragment.show(frag, "ShowErrorMessage");
               break;
           case R.id.menu_about:
               FragmentTransaction frag2 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment2 = DialogAbout.newInstance();
               dialogFragment2.show(frag2, "ShowErrorMessage");
               break;
           case R.id.menu_drain:
               bigControl.drainParking();
               pagAdapt.forceParkingRemoved();
               showSnackBarNoti(getString(R.string.resetParkingStatus), getString(R.string.ok));
               break;
           case R.id.menu_historicDrain:
               bigControl.drainHistoric();
               showSnackBarNoti(getString(R.string.resetHistory), getString(R.string.ok));
               pagAdapt.forceHistoricNotifyRemoved();
               break;
           case R.id.menu_price:
               FragmentTransaction frag4 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment4 = DialogPriceChanger.newInstance(bigControl.getPrice());
               dialogFragment4.show(frag4, "ShowPriceChangeDialog");
               break;
           case R.id.menu_desfer:
               FragmentTransaction frag3 = getFragmentManager().beginTransaction();
               int moveType = bigControl.getLastMoveType();
               Log.i("UNDO","lastMoveType: "+ moveType);
               DialogFragment dialogFragment3;
               if(moveType==-1) dialogFragment3 = DialogBasic.newInstance(getString(R.string.noPreviousAction),getString(R.string.ok));
               else {
                   String matr = bigControl.getLastMoveMatr();
                   if(moveType==0) dialogFragment3 = Dialog2But.newInstance(getString(R.string.undoEntry)+" "+matr+"?");
                   else dialogFragment3 = Dialog2But.newInstance(getString(R.string.undoExit)+" "+matr+"?");
               }
               dialogFragment3.show(frag3, "ShowErrorMessage");
               break;
           default:
               break;

       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override   //en ocupar una nova plasa
    public int onComplete(String res) {
        int where = pagAdapt.forceOnComplete(res);
        pagAdapt.forceParkingNotifyDataSetChanged();
        FragmentTransaction frag = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = DialogPlot.newInstance(where+1);
        dialogFragment.show(frag, "ShowOKMessage");

        return where;
    }

    @Override   //en clicar a pagar o cancelar de DialogExitCar
    public void onFragmentInteraction() {
        showHistoric();
        pagAdapt.forceOnFragmentInteraction();
    }

    @Override
    public void onClick(View view) {
        Log.i("PARKACT", "ON CLICK!!!!!!!!!!!!!! de ParkingActivity");
    }

    private void onClickFloatingActionButton() {
        if(tabPosition==0) {
            if (bigControl.getNumFreePeaches() > 0) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogNewCar.newInstance(this);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //conjuntament amb onPageScrollStateChanged, que si que la necessito
    }
    @Override
    public void onPageSelected(int position) {
       //conjuntament amb onPageScrollStateChanged, que si que la necessito
    }

    @Override   //en canviar de page, canviem la imatge del botó
    public void onPageScrollStateChanged(int state) {
        if(tabPosition!=viewPager.getCurrentItem()) {
            tabPosition = viewPager.getCurrentItem();
            if(tabPosition==0)fab.setImageDrawable(getResources().getDrawable(R.drawable.androidadd512));
            else fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_month));
        }
    }

    @Override   //en triar una data de DialogDateChooser
    public void onFragmentInteraction(int dayOfMonth, int month, int year) {
        Log.i("DATE","onFragmentInteraction Del PARKING ACTIVITY");
        if(dialogHistoryFragment!=null) {
            dialogHistoryFragment.selectedDate(dayOfMonth, month, year);
        }
    }

    @Override   //en triar una hora de DialogHourChoose
    public void onFragmentInteraction(Integer currentHour, Integer currentMinute) {
        if(dialogHistoryFragment!=null) dialogHistoryFragment.selectedHour(currentHour, currentMinute);
    }

    @Override   //en clicar al boto mostrar de DialogHistory
    public void onFragmentInteraction(int i, Timestamp iniTime, Timestamp endTime) {
        historicType = i;
        if(i==2) {
            tini=iniTime;
            tend = endTime;
        }
        showHistoric();
    }

    private void showSnackBarNoti(String message, String buttonMessage) {
        View.OnClickListener snackClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        };
        //Length: short:2s , long:3,5s & indefinite: until dismissed or another snackbar
        Snackbar snackbar = Snackbar.make(findViewById(R.id.pageadapter_layout), message, Snackbar.LENGTH_LONG)
                .setAction(buttonMessage, snackClickListener);
        snackbar.setActionTextColor(Color.GREEN);
        View sbView = snackbar.getView();
        TextView tView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void changeViewOrder() {
        bigControl.bdChangeViewOrder();
        showHistoric();
        bigControl.showNormalToast(getString(R.string.toastOrderChanges),this);
    }

    private void showHistoric(){
        if(historicType==0) pagAdapt.showAllHistoric();
        else if(historicType==1)pagAdapt.showTodayHistoric();
        else if(historicType==2)pagAdapt.showHistoBetween(tini,tend);
        else if(historicType==3)pagAdapt.showMonthHistoric();
    }

    @Override   //en clicar yes del Dialog2But
    public void onComplete() {
        bigControl.undoLastMove();
        pagAdapt.forceParkingNotifyDataSetChanged();
        showHistoric();
    }

    @Override
    public void onFragmentInteraction(Double price) {
        Log.i("PRICE","Price changed to "+price);
        bigControl.changePrice(price);
    }
}
