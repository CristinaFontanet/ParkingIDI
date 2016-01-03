package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public class DialogExitCar extends android.app.DialogFragment implements View.OnClickListener {

    private static Controller bigControl;
    private static String mMatr;
    private static Timestamp mDay;
    private static int car;

    Button canc,pay;
    TextView matr,day,day2, hourEntry,minutes,price, hourExit;

    private OnFragmentInteractionListener mListener;

    public static DialogExitCar newInstance(Integer numCar, Controller controller) {
        DialogExitCar fragment = new DialogExitCar();
        car = numCar;
        bigControl = controller;
        mMatr = bigControl.getCarReg(car);
        mDay = bigControl.getCarDayEntry(car);
        return fragment;
    }

    public DialogExitCar() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exit_car, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        canc = (Button)v.findViewById(R.id.bcancela);
        canc.setOnClickListener(this);

        pay = (Button)v.findViewById(R.id.bPaga);
        pay.setOnClickListener(this);

        matr = (TextView) v.findViewById(R.id.matricula);
        matr.setText(mMatr);

        TextView pric = (TextView) v.findViewById(R.id.textView9);
        pric.setText(bigControl.getPrice().toString()+getString(R.string.edmCalculPreu1));

        SimpleDateFormat diaF = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat horaF = new SimpleDateFormat("HH:mm:ss");
        Date actualT = new Date(Calendar.getInstance().getTime().getTime());
        int days = bigControl.difDays(mDay);

        day = (TextView) v.findViewById(R.id.dia);
        day2 = (TextView) v.findViewById(R.id.dia2);
        Log.i("Calc","Hi ha estat "+ days+" dies");

            day.setText(diaF.format(mDay));
            day2.setText(diaF.format(actualT));

        hourEntry = (TextView) v.findViewById(R.id.horaE);
        hourEntry.setText(horaF.format(mDay));

        hourExit = (TextView) v.findViewById(R.id.horaS);
        hourExit.setText(horaF.format(actualT));

        double min = bigControl.minsCalcul(car); //Math.rint(min*100)/100+" minuts"
        String minuts = (int)(min/60)+":"+(int)(min%60)+":"+(int)((min*60)%60);

        minutes = (TextView) v.findViewById(R.id.minutsPassats);
        if(days==0)minutes.setText(minuts);
        else {
            min-=days*24*60;
            minuts = (int)(min/60)+":"+(int)(min%60)+":"+(int)((min*60)%60);
            minutes.setText(days+ getString(R.string.edmDies)+"\n" + minuts);
        }
        price = (TextView)v.findViewById(R.id.calculPreu);

        price.setText(bigControl.priceCalcul(min)+getString(R.string.edmMoneda));
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bPaga:
                if (mListener != null) {
                    bigControl.registerCarExit(car);
                    bigControl.newFreePlot(car);
                    mListener.onFragmentInteraction();
                }
                dismiss();
                break;
            case R.id.bcancela:
                dismiss();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
         void onFragmentInteraction();
    }
}
