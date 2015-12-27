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

/*
 * Created by CristinaFontanet on 20/12/2015.
 */
public class ParkingActivity extends AppCompatActivity implements View.OnClickListener, NewCar.OnCompleteListener, ExitCar.OnFragmentInteractionListener{
    ParkingStatus bu;
    Controller bigControl;
    PagerAdapter pagAdapt;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageadapter_layout);
        bigControl = new Controller(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new PagerAdapter(getFragmentManager(), PagerHolder.this));       Para fragments normales
        pagAdapt = new PagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);     //Solo para MATERIAL

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(Color.DKGRAY, Color.BLACK);      //Per canviar el color de la pestanya a on estas
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Log.i("PARKACT", "He creat el floatingActionButton: ");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bigControl.getNumFreePeaches()>0) {
                    FragmentTransaction frag = getFragmentManager().beginTransaction();
                    DialogFragment dialogFragment = NewCar.newInstance();
                    dialogFragment.show(frag, "AskRegistration");
                }
                else {
                    FragmentTransaction frag = getFragmentManager().beginTransaction();
                    DialogFragment dialogFragment = BasicDialog.newInstance(getString(R.string.busyParking), getString(R.string.ok));
                    dialogFragment.show(frag,"ShowMessage");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.menu_drain) {
            bigControl.drainParking();

        }
        else if (id==R.id.menu_historicDrain) {
            bigControl.drainHistoric();
            pagAdapt = new PagerAdapter(getSupportFragmentManager(), this, bigControl);
            viewPager.setAdapter(pagAdapt);     //Solo para MATERIAL

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
        bu = PagerAdapter.ajudaParking;
        bu.onComplete(res);
        pagAdapt = new PagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);
    }

    @Override
    public void onFragmentInteraction(Boolean uri) {
        bu = PagerAdapter.ajudaParking;
        bu.onFragmentInteraction(uri);
        pagAdapt = new PagerAdapter(getSupportFragmentManager(), this, bigControl);
        viewPager.setAdapter(pagAdapt);

    }

    @Override
    public void onClick(View view) {
        Log.i("PARKACT", "ON CLICK!!!!!!!!!!!!!!");
    }

    //   @Override
 ////   public void onComplete(String res) {
  //      bu = PagerAdapter.ajudaParking;
    //    bu.nouJoc();
 //   }
}
