package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;


public class ParkingStatus extends android.support.v4.app.Fragment implements View.OnClickListener, NewCar.OnCompleteListener, ExitCar.OnFragmentInteractionListener {

    static ArrayList<Button> buttonPlots;
    private static RecyclerView mRecyclerViewx;
    public static ParkingAdapter parkAdapter;
    Button bnew, bhisto, bday;
    static private Controller bigControl;
    int actualCar;
    static Drawable freeImage, busyImage;
    static private PagerAdapter pare;
    View parentView;
    private static Activity father;

    public ParkingStatus(){

    }

    private void fillButton(Button p1, int i) {
        p1.setOnClickListener(this);
        if (bigControl.isFree(i)) p1.setBackground(freeImage);
        else{
            p1.setBackground(busyImage);
            p1.setText(bigControl.getCarReg(i));
        }
        buttonPlots.add(p1);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_main, container,false);
       StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father,LinearLayoutManager.VERTICAL,false);
        mRecyclerViewx = (RecyclerView) parentView.findViewById(R.id.rv);
        mRecyclerViewx.setLayoutManager(manager);
        freeImage = getResources().getDrawable(R.drawable.simplegreencartopviewsquare);
        busyImage = getResources().getDrawable(R.drawable.simpleredquare);

        parkAdapter = new ParkingAdapter(bigControl,this);
        mRecyclerViewx.setAdapter(parkAdapter);
        mRecyclerViewx.setHasFixedSize(true);
        return parentView;
    }

    public void sendFather(PagerAdapter pareAd, Controller am, Activity fatherAct){
        bigControl = am;
        father =fatherAct;
        pare = pareAd;
        bigControl.BDPakingStaus();
      //  iniButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.menu_drain) {
            bigControl.drainParking();
            for(Button b: buttonPlots) {
                b.setBackground(freeImage);
                b.setText("");
            }
        }
        else if (id==R.id.menu_historicDrain) {
            bigControl.drainHistoric();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                default:
                    Log.i("CLICK", "Rebo click a ParkingStatus");
                    int clicked = mRecyclerViewx.getChildPosition(view);
                    Log.i("CLICK", "Vaig a mirar el cotxe: "+clicked);
                    carClicked(clicked);
                    break;
            }
    }

        private void carClicked(int i) {
        if(!bigControl.isFree(i)) {
            actualCar = i;
            FragmentTransaction frag = getActivity().getFragmentManager().beginTransaction();
            DialogFragment dialogFragment = ExitCar.newInstance(i,bigControl);
            dialogFragment.show(frag,"ExitRegistre");
        }
        else {
            showFastToast("Plaça lliure");
        }
    }

    @Override
    public void onComplete(String res) {
        Log.i("MATR",res);
        if(!res.isEmpty()) {
            Log.i("matr", "el cotxe "+res+ " ocupa una plasa");
            bigControl.newBusyPlot(res);
        }
        else {
            Toast toast = Toast.makeText(father, getString(R.string.introdueix_matricula), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void showFastToast(String message) {
        final Toast toast = Toast.makeText(father,message , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("SAVE", "entro a onDestroy");
        bigControl.saveActualState();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i("SAVE","entro a onPause");
        bigControl.saveActualState();
    }

    @Override
    public void onFragmentInteraction(Boolean uri) {
        Log.i("MATRRRR", "ParkingStatus rep onFragmentInteraction de ExitCar, FALTA IMPLEMENTAR!!");
        if(uri){
           // int itemPosition = mRecyclerViewx.getChildAt(actualCar);
          //  mList.get(actualCar);
            buttonPlots.get(actualCar - 1).setBackground(freeImage);
            buttonPlots.get(actualCar-1).setText("");
            bigControl.registerCarExit(actualCar - 1);
            bigControl.newFreePlot(actualCar - 1);
        }
    }
}
