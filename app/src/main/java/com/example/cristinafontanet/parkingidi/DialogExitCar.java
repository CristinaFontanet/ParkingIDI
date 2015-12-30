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

    private static final String ARG_PARAM1 = "param1";
    private static Controller bigControl;
    private String mMatr;
    private Timestamp mDay;
    private int car;

    Button canc,pay;
    TextView matr,day,day2, hourEntry,minutes,price, hourExit;

    private OnFragmentInteractionListener mListener;

    public static DialogExitCar newInstance(Integer numCar, Controller controller) {
        DialogExitCar fragment = new DialogExitCar();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, numCar);
        fragment.setArguments(args);
        bigControl = controller;
        return fragment;
    }

    public DialogExitCar() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            car = getArguments().getInt(ARG_PARAM1);
            mMatr = bigControl.getCarReg(car);
            mDay = bigControl.getCarDayEntry(car);
        }
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
      //Log.i("COBRAR", "Ha estat " + Math.rint(min * 100) / 100 + " minuts, q son: " + (Math.rint(min * 100) / 100) / 60 +" hores i "+ (Math.rint(min * 100) / 100)%60+" minuts");
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
                if (mListener != null)mListener.onFragmentInteraction(true);
                dismiss();
                break;
            case R.id.bcancela:
                if (mListener != null)mListener.onFragmentInteraction(false);
                dismiss();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
         void onFragmentInteraction(Boolean uri);
    }
}
