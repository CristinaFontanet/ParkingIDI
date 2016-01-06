package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

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

    private static final int idUndo = 0;
    private static final int idFile = 1;
    private static final int idDrainParking = 2;
    private static final int idDrainStatus = 3;

    private static final int undoExit = 1;
    private static final int undoError = -1;
    private static final int undoEntry = 0;

    public void hideFloatingActionButton() {
        fab.hide();
    }

    public void showFloatingActionButton() {
        fab.show();
    }

    private void showHistoric(){
        if(historicType==0) pagAdapt.showAllHistoric();
        else if(historicType==1)pagAdapt.showTodayHistoric();
        else if(historicType==2)pagAdapt.showHistoBetween(tini,tend);
        else if(historicType==3)pagAdapt.showMonthHistoric();
    }

    public void changeViewOrder() {
        bigControl.bdChangeViewOrder();
        showHistoric();
        bigControl.showNormalToast(getString(R.string.toastOrderChanges), this);
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
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.circleButton)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.menu_help:
               FragmentTransaction frag = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment = DialogHelp.newInstance();
               dialogFragment.show(frag, "ShowHelpDialog");
               break;
           case R.id.menu_about:
               FragmentTransaction frag2 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment2 = DialogAbout.newInstance();
               dialogFragment2.show(frag2, "ShowAboutDialog");
               break;
           case R.id.menu_drain:
               FragmentTransaction frag3 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment3 = Dialog2But.newInstance(getString(R.string.alertResetStatus),idDrainParking);
               dialogFragment3.show(frag3, "ShowDrainParkingDialog");
               break;
           case R.id.menu_historicDrain:
               FragmentTransaction frag4 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment4 = Dialog2But.newInstance(getString(R.string.alertResetHistorial),idDrainStatus);
               dialogFragment4.show(frag4, "ShowDrainHistoricDialog");
               break;
           case R.id.menu_price:
               FragmentTransaction frag5 = getFragmentManager().beginTransaction();
               DialogFragment dialogFragment5 = DialogPriceChanger.newInstance(bigControl.getPrice());
               dialogFragment5.show(frag5, "ShowPriceChangeDialog");
               break;
           case R.id.menu_desfer:
               FragmentTransaction frag6 = getFragmentManager().beginTransaction();
               int moveType = bigControl.getLastMoveType();
               DialogFragment dialogFragment6;
               if(moveType==undoError) dialogFragment6 = DialogBasic.newInstance(getString(R.string.noPreviousAction),getString(R.string.ok),getString(R.string.title_error));
               else {
                   String matr = bigControl.getLastMoveMatr();
                   if(moveType==undoEntry) dialogFragment6 = Dialog2But.newInstance(getString(R.string.undoEntry)+" "+matr+"?",idUndo);
                   else dialogFragment6 = Dialog2But.newInstance(getString(R.string.undoExit)+" "+matr+"?",idUndo);
               }
               dialogFragment6.show(frag6, "ShowUndoMessage");
               break;
           case R.id.menu_export:
               int result = bigControl.exportHistorialState();
               if(result==0) {
                   FragmentTransaction frag7 = getFragmentManager().beginTransaction();
                   DialogFragment dialogFragment7 = Dialog2But.newInstance(getString(R.string.exportOk),idFile);
                   dialogFragment7.show(frag7, "ShowExportDialog");
               }
               else if(result==-1){
                   FragmentTransaction frag7 = getFragmentManager().beginTransaction();
                   DialogFragment dialogFragment7 = DialogBasic.newInstance(getString(R.string.exportNoWrite),getString(R.string.ok),getString(R.string.title_error));
                   dialogFragment7.show(frag7, "ShowError1ExportDialog");
               }
               else if (result==-2) {
                   FragmentTransaction frag7 = getFragmentManager().beginTransaction();
                   DialogFragment dialogFragment7 = DialogBasic.newInstance(getString(R.string.exportNo),getString(R.string.ok),getString(R.string.title_error));
                   dialogFragment7.show(frag7, "ShowError2ExportDialog");
               }
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

    @Override
    public void onClick(View view) {  }

    private void onClickFloatingActionButton() {
        if(tabPosition==0) {
            if (bigControl.getNumFreePeaches() > 0) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogNewCar.newInstance(this,-1);
                dialogFragment.show(frag, "AskRegistration");
            } else {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.busyParking), getString(R.string.ok),getString(R.string.title_error));
                dialogFragment.show(frag, "ShowBusyMessage");
            }
        }
    }

    public void historicButtonClicked(){
        FragmentTransaction frag = getFragmentManager().beginTransaction();
        dialogHistoryFragment = DialogHistory.newInstance(historicType,tini,tend);
        dialogHistoryFragment.show(frag,"DialogHistory");
    }

    @Override   //en ocupar una nova plasa
    public int onComplete(String res, int pos) {
        int where = pagAdapt.forceOnComplete(res,pos);
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
            if(tabPosition==0)fab.show();
            else fab.hide();
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

    @Override   //en clicar yes del Dialog2But
    public void onComplete(int who) {
        if(who==idUndo) {
            bigControl.undoLastMove();
            pagAdapt.forceParkingNotifyDataSetChanged();
            showHistoric();
        }
        else if (who==idFile) {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(Uri.fromFile(bigControl.getFilePath()),"text/csv");
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.exportNoWrite),getString(R.string.ok),getString(R.string.title_error));
                dialogFragment.show(frag, "ShowError1ExportDialog");
            }
        }
        else if(who==idDrainParking) {
            bigControl.drainParking();
            pagAdapt.forceParkingRemoved();
            showSnackBarNoti(getString(R.string.resetParkingStatus), getString(R.string.ok));
        }
        else if(who==idDrainStatus){
            bigControl.drainHistoric();
            showSnackBarNoti(getString(R.string.resetHistory), getString(R.string.ok));
            pagAdapt.forceHistoricNotifyRemoved();
        }
    }

    @Override       //en canviar el preu
    public void onFragmentInteraction(Double price) {
        Log.i("PRICE", "Price changed to " + price);
        bigControl.changePrice(price);
    }

}
