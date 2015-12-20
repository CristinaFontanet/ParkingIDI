package com.example.cristinafontanet.parkingidi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/*
 * Created by CristinaFontanet on 20/12/2015.
 */
public class ParkingActivity extends FragmentActivity implements NewCar.OnCompleteListener, ExitCar.OnFragmentInteractionListener{
    ParkingStatus bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageadapter_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new PagerAdapter(getFragmentManager(), PagerHolder.this));       Para fragments normales
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),this));     //Solo para MATERIAL

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(Color.DKGRAY, Color.BLACK);      //Per canviar el color de la pestanya a on estas
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_joc__ranking, menu);
        return true;
    }

    @Override
    public void onComplete(String res) {
        bu = PagerAdapter.ajudaParking;
        bu.onComplete(res);
    }

    @Override
    public void onFragmentInteraction(Boolean uri) {
        bu = PagerAdapter.ajudaParking;
        bu.onFragmentInteraction(uri);

    }

    //   @Override
 ////   public void onComplete(String res) {
  //      bu = PagerAdapter.ajudaParking;
    //    bu.nouJoc();
 //   }
}
