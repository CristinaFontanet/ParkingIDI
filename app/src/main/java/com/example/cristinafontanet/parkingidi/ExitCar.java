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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ExitCar extends android.app.DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int segDay = 24*60*60;

    private static final double pricePerMinute = 0.02;

    // TODO: Rename and change types of parameters
    private String mMatr;
    private Date mDay;
    private Time mHour;

    Button canc,pay;
    TextView matr,day, hourEntry,minutes,price, hourExit;

    private OnFragmentInteractionListener mListener;

    public static ExitCar newInstance(String matr, Date day, Time hour) {
        ExitCar fragment = new ExitCar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, matr);
        args.putString(ARG_PARAM2, day.toString());
        args.putLong(ARG_PARAM3,hour.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    public ExitCar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMatr = getArguments().getString(ARG_PARAM1);
            mDay = Date.valueOf(getArguments().getString(ARG_PARAM2));
            mHour =new Time(getArguments().getLong(ARG_PARAM3));
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
        matr.setText(getString(R.string.edmMatr) + mMatr);

        SimpleDateFormat diaF = new SimpleDateFormat("dd/MM/yyy");
        Calendar cal = Calendar.getInstance();
        Time actualT = new Time(cal.getTime().getTime());
        Date actualD = new Date(cal.getTime().getTime());
        Log.i("HoraEntrada", "mHour: " + mHour + ", el q implica: " + TimeUnit.MILLISECONDS.toSeconds(mHour.getTime()) / 60 + " minuts");
        Log.i("HoraSortida", "Actual: " + actualT + ", el q implica: " + TimeUnit.MILLISECONDS.toSeconds(actualT.getTime()) / 60 + " minuts");
      //Els dies que ha passat el parking, es 0 si entra i surt el mateix dia.
        int days = (int)TimeUnit.MILLISECONDS.toDays(actualD.getTime()-mDay.getTime());

        double  min=0.0;

        if(days>0) {
            cal.add(Calendar.DATE,1);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            long midnight = cal.getTimeInMillis();  //Mitjanit del dia seguent, per poder saber quants minuts hi ha estat aquell dia

            min = (double)(TimeUnit.MILLISECONDS.toSeconds(midnight - mHour.getTime()))/60; //mins des de l'hora d'entrada fins a les 12 d'aquell mateix dia
            Log.i("Hores","De l'entrada a "+mHour+" fins les 12 de la nit d'aquell dia, serien "+ min+" minuts, q son "+min/60+" hores i "+min%60 +" minuts");
            min +=(segDay*days)/60;
            Log.i("Hores","I tenint en compte que hi ha estat "+days+" dies, sumem"+(segDay*days)/60+" minuts-> "+(segDay*days)/(60*60)+" hores i " +(segDay*days)%(60*60)+" min en total");
            cal.add(Calendar.DATE, -1);
            double  min2 = (double)(TimeUnit.MILLISECONDS.toSeconds(actualT.getTime()-cal.getTimeInMillis()))/60;
            Log.i("Hores","I que surt a les "+actualT+" aixo son "+min2+" minuts, q son->"+min2/60+" hores i "+ min2%60+" minuts");
        }
        else {
            long diffT = actualT.getTime() - mHour.getTime();
            min = (double)TimeUnit.MILLISECONDS.toSeconds(diffT)/60;
        }
        Log.i("Calc", "s'ha estat al parking "+ min+ " min");


        day = (TextView) v.findViewById(R.id.dia);
        if(days==0) day.setText(getString(R.string.edmDia)+" "+diaF.format(mDay));
        else day.setText("Dia entrada: "+diaF.format(mDay)+", dia sortida: "+diaF.format(actualT));

        hourEntry = (TextView) v.findViewById(R.id.hora);
        hourEntry.setText(getString(R.string.edmHoraEntrada) + mHour);

        hourExit = (TextView) v.findViewById(R.id.horaSort);
        hourExit.setText(getString(R.string.edmHoraSortida) + actualT);

        minutes = (TextView) v.findViewById(R.id.calculPreu);
        minutes.setText(getString(R.string.edmCalculPreu1)+" "+min+" minuts");

        price = (TextView)v.findViewById(R.id.preu);
        price.setText(getString(R.string.edmPreu)+" "+min*pricePerMinute+"€");
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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

    /**
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Boolean uri);
    }
}
