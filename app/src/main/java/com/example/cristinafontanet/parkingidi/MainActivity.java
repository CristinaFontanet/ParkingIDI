package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, NewCar.OnCompleteListener, NewCar.OnFragmentInteractionListener {

    ImageView p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15;
    Button nou;
    Controller bigControl;

    public static final int SHORT = 3;

   // protected DateFormat();

    private void iniButtons() {

        nou = (Button)findViewById(R.id.nou);
        nou.setOnClickListener(this);

        p1 = (ImageView)findViewById(R.id.p1);
        p1.setOnClickListener(this);

        p2 = (ImageView)findViewById(R.id.p2);
        p2.setOnClickListener(this);

        p3 = (ImageView)findViewById(R.id.p3);
        p3.setOnClickListener(this);

        p4 = (ImageView)findViewById(R.id.p4);
        p4.setOnClickListener(this);

        p5 = (ImageView)findViewById(R.id.p5);
        p5.setOnClickListener(this);

        p6 = (ImageView)findViewById(R.id.p6);
        p6.setOnClickListener(this);

        p7 = (ImageView)findViewById(R.id.p7);
        p7.setOnClickListener(this);

        p8 = (ImageView)findViewById(R.id.p8);
        p8.setOnClickListener(this);

        p9 = (ImageView)findViewById(R.id.p9);
        p9.setOnClickListener(this);

        p10 = (ImageView)findViewById(R.id.p10);
        p10.setOnClickListener(this);

        p11 = (ImageView)findViewById(R.id.p11);
        p11.setOnClickListener(this);

        p12 = (ImageView)findViewById(R.id.p12);
        p12.setOnClickListener(this);

        p13 = (ImageView)findViewById(R.id.p13);
        p13.setOnClickListener(this);

        p14 = (ImageView)findViewById(R.id.p14);
        p14.setOnClickListener(this);

        p15 = (ImageView)findViewById(R.id.p15);
        p15.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bigControl = new Controller(this);
        bigControl.estatPakingBD();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.nou:
                    FragmentTransaction frag = this.getFragmentManager().beginTransaction();
                    DialogFragment dialogFragment = NewCar.newInstance();
                    dialogFragment.show(frag, "bu");

            }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
