package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class ExitCar extends android.app.DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

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
        args.putString(ARG_PARAM3,hour.toString());
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
            mHour = Time.valueOf(getArguments().getString(ARG_PARAM3));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exit_car, container, false);

        canc = (Button)v.findViewById(R.id.bcancela);
        canc.setOnClickListener(this);

        pay = (Button)v.findViewById(R.id.bPaga);
        pay.setOnClickListener(this);

        matr = (TextView) v.findViewById(R.id.matricula);
        matr.setText(getString(R.string.edmMatr) + mMatr);

        day = (TextView) v.findViewById(R.id.dia);
        day.setText(getString(R.string.edmDia)+mDay);

        hourEntry = (TextView) v.findViewById(R.id.hora);
        hourEntry.setText(getString(R.string.edmHoraEntrada) + mHour);

        Calendar cal = Calendar.getInstance();
        Time actual = new Time(cal.getTime().getTime());
        hourExit = (TextView) v.findViewById(R.id.horaSort);
        hourExit.setText(getString(R.string.edmHoraEntrada) + actual);

        minutes = (TextView) v.findViewById(R.id.calculPreu);
        long dif =actual.getTime()-mHour.getTime();
        double  minuts = Math.floor(dif/(1000*60));
        minutes.setText(getString(R.string.edmCalculPreu1)+minuts);

        price = (TextView)v.findViewById(R.id.preu);
        price.setText(getString(R.string.edmPreu)+Math.floor(minuts*pricePerMinute)+"€");
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
