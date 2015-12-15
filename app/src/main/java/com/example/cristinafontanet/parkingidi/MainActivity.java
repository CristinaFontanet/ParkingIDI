package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, NewCar.OnCompleteListener, ExitCar.OnFragmentInteractionListener {

    static ArrayList<Button> buttonPlots;
    Button bnew, bhisto, bday;
    static private Controller bigControl;
    int actualCar;
    static Drawable freeImage, busyImage;

    private void iniButtons() {
        buttonPlots = new ArrayList<>(15);
        freeImage = getResources().getDrawable(R.drawable.simplegreencartopviewsquare);
        busyImage = getResources().getDrawable(R.drawable.simpleredquare);

        bday = (Button)findViewById(R.id.bDiari);
        bday.setOnClickListener(this);

        bnew = (Button)findViewById(R.id.bnou);
        bnew.setOnClickListener(this);

        bhisto = (Button)findViewById(R.id.bhistorial);
        bhisto.setOnClickListener(this);

        Button p1;
        p1 = (Button)findViewById(R.id.p1);
        p1.setOnClickListener(this);
        if(bigControl.isFree(1)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p2);
        p1.setOnClickListener(this);
        if(bigControl.isFree(2)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p3);
        p1.setOnClickListener(this);
        if(bigControl.isFree(3)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p4);
        p1.setOnClickListener(this);
        if(bigControl.isFree(4)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p5);
        p1.setOnClickListener(this);
        if(bigControl.isFree(5)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p6);
        p1.setOnClickListener(this);
        if(bigControl.isFree(6)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p7);
        p1.setOnClickListener(this);
        if(bigControl.isFree(7)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p8);
        p1.setOnClickListener(this);
        if(bigControl.isFree(8)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p9);
        p1.setOnClickListener(this);
        if(bigControl.isFree(9)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p10);
        p1.setOnClickListener(this);
        if(bigControl.isFree(10)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p11);
        p1.setOnClickListener(this);
        if(bigControl.isFree(11)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p12);
        p1.setOnClickListener(this);
        if(bigControl.isFree(12)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p13);
        p1.setOnClickListener(this);
        if(bigControl.isFree(13)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p14);
        p1.setOnClickListener(this);
        if(bigControl.isFree(14)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

        p1 = (Button)findViewById(R.id.p15);
        p1.setOnClickListener(this);
        if(bigControl.isFree(15)) p1.setBackground(freeImage);
        else p1.setBackground(busyImage);
        buttonPlots.add(p1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bigControl = new Controller(this);
        bigControl.BDPakingStaus();
        iniButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.bnou:
                    if(bigControl.getNumFreePeaches()>0) {
                        FragmentTransaction frag = this.getFragmentManager().beginTransaction();
                        DialogFragment dialogFragment = NewCar.newInstance();
                        dialogFragment.show(frag, "AskRegistration");
                    }
                    else {
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        DialogFragment dialogFragment = BasicDialog.newInstance(getString(R.string.busyParking), getString(R.string.ok));
                        dialogFragment.show(frag,"ShowMessage");
                    }
                    break;
                case R.id.bhistorial:
                    bigControl.saveActualState();
                    break;
                case R.id.p1:
                    carClicked(1);
                    break;
                case R.id.p2:
                    carClicked(2);
                    break;
                case R.id.p3:
                    carClicked(3);
                    break;
                case R.id.p4:
                    carClicked(4);
                    break;
                case R.id.p5:
                    carClicked(5);
                    break;
                case R.id.p6:
                    carClicked(6);
                    break;
                case R.id.p7:
                    carClicked(7);
                    break;
                case R.id.p8:
                    carClicked(8);
                    break;
                case R.id.p9:
                    carClicked(9);
                    break;
                case R.id.p10:
                    carClicked(10);
                    break;
                case R.id.p11:
                    carClicked(11);
                    break;
                case R.id.p12:
                    carClicked(12);
                    break;
                case R.id.p13:
                    carClicked(13);
                    break;
                case R.id.p14:
                    carClicked(14);
                    break;
                case R.id.p15:
                    carClicked(15);
                    break;
                default:
                    break;
            }
    }

    private void carClicked(int i) {
        if(!bigControl.isFree(i)) {
            actualCar = i;
            FragmentTransaction frag = this.getFragmentManager().beginTransaction();
            DialogFragment dialogFragment = ExitCar.newInstance(i-1,bigControl);
            dialogFragment.show(frag, "ExitRegistre");
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
            Toast toast = Toast.makeText(this, getString(R.string.introdueix_matricula), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void showFastToast(String message) {
        final Toast toast = Toast.makeText(this,message , Toast.LENGTH_SHORT);
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
    public void onFragmentInteraction(Boolean uri) {
        Log.i("MATRRRR", "MainActivity rep onFragmentInteraction de ExitCar");
        if(uri){
            buttonPlots.get(actualCar - 1).setBackground(freeImage);
            bigControl.newFreePlot(actualCar - 1);
            showFastToast("Falta guardar al registre la sortida del cotxe i guardar-ho");
            Log.i("Matr", "Falta guardar al registre la sortida del cotxe");
        }
    }
}
