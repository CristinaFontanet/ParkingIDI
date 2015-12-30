package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogHistory extends android.app.DialogFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private TextView tiniDate,tIniHour,fiDate,tFiHour,tIni,tEnd;
    private int dateChoose,hourChoose;
    private RadioButton rbAll,rbDay,rbDates;
    private static Timestamp iniTime, endTime;
    SimpleDateFormat hourF = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dayF = new SimpleDateFormat("dd/MM/yyy");
    SimpleDateFormat diaF = new SimpleDateFormat("dd/MM/yyy HH:mm");
    private static int histType;

    public DialogHistory() {
        // Required empty public constructor
    }


    public static DialogHistory newInstance(int historicType, Timestamp tini, Timestamp tend) {
        DialogHistory fragment = new DialogHistory();
        histType = historicType;
        iniTime = tini;
        endTime = tend;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_history, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rbAll = (RadioButton) v.findViewById(R.id.rbAll);
        rbAll.setOnClickListener(this);
        rbDay = (RadioButton) v.findViewById(R.id.rbDay);
        rbDay.setOnClickListener(this);
        rbDates = (RadioButton) v.findViewById(R.id.rbDates);
        rbDates.setOnClickListener(this);

        Button show = (Button) v.findViewById(R.id.bShow);
        show.setOnClickListener(this);

        tIni = (TextView) v.findViewById(R.id.textView14);
        tEnd = (TextView) v.findViewById(R.id.textView17);

        tiniDate = (TextView) v.findViewById(R.id.tiniDate);
        tiniDate.setText(getResources().getString(R.string.hdTriaData));
        tiniDate.setOnClickListener(this);

        fiDate = (TextView) v.findViewById(R.id.fiDate);
        fiDate.setText(getResources().getString(R.string.hdTriaData));
        fiDate.setOnClickListener(this);

        tIniHour = (TextView) v.findViewById(R.id.tiniHour);
        tIniHour.setText(getResources().getString(R.string.hdTriaHora));
        tIniHour.setOnClickListener(this);

        tFiHour= (TextView) v.findViewById(R.id.fiHour);
        tFiHour.setText(getResources().getString(R.string.hdTriaHora));
        tFiHour.setOnClickListener(this);

        if(histType==0)rbAll.setChecked(true);
        else if(histType==1)rbDay.setChecked(true);
        else {
            rbDates.setChecked(true);
            tiniDate.setText(dayF.format(iniTime.getTime()));
            tIniHour.setText(hourF.format(iniTime.getTime()));
            fiDate.setText(dayF.format(endTime.getTime()));
            tFiHour.setText(hourF.format(endTime.getTime()));
        }

        if(rbDates.isChecked()) showExtras();
        else hideExtras();

        return v;
    }

    private void hideExtras(){
        tFiHour.setVisibility(View.INVISIBLE);
        tiniDate.setVisibility(View.INVISIBLE);
        tIniHour.setVisibility(View.INVISIBLE);
        fiDate.setVisibility(View.INVISIBLE);
        tEnd.setVisibility(View.INVISIBLE);
        tIni.setVisibility(View.INVISIBLE);
    }

    private void showExtras() {
        tFiHour.setVisibility(View.VISIBLE);
        tiniDate.setVisibility(View.VISIBLE);
        tIniHour.setVisibility(View.VISIBLE);
        fiDate.setVisibility(View.VISIBLE);
        tEnd.setVisibility(View.VISIBLE);
        tIni.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bShow:
                showClicked();
                break;
            case R.id.tiniDate:
                dateChoose = 0;
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment;
                if(tiniDate.getText().equals(getString(R.string.hdTriaData))) dialogFragment = DialogDateChooser.newInstance();
                else dialogFragment = DialogDateChooser.newInstance(tiniDate.getText());
                dialogFragment.show(frag,"DialogDateIniChoose");
                break;
            case R.id.tiniHour:
                hourChoose =0;
                FragmentTransaction frag2 = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment2;
                if (tIniHour.getText().equals(getString(R.string.hdTriaHora)))dialogFragment2 = DialogHourChoose.newInstance();
                else dialogFragment2 = DialogHourChoose.newInstance(tIniHour.getText());
                dialogFragment2.show(frag2,"DialogHourIniChoose");
                break;
            case R.id.fiDate:
                dateChoose =1;
                FragmentTransaction frag3 = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment3;
                if(fiDate.getText().equals(getString(R.string.hdTriaData))) dialogFragment3 = DialogDateChooser.newInstance();
                else dialogFragment3 = DialogDateChooser.newInstance(fiDate.getText());
                dialogFragment3.show(frag3,"DialogDateEndChoose");
                break;
            case R.id.fiHour:
                hourChoose =1;
                FragmentTransaction frag4 = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment4;
                if (tFiHour.getText().equals(getString(R.string.hdTriaHora)))dialogFragment4 = DialogHourChoose.newInstance();
                else dialogFragment4 = DialogHourChoose.newInstance(tFiHour.getText());
                dialogFragment4.show(frag4,"DialogHourEndChoose");
                break;
            case R.id.rbAll:
                hideExtras();
                break;
            case R.id.rbDay:
                hideExtras();
                break;
            case R.id.rbDates:
                showExtras();
                break;
            default:
                break;
        }
    }

    private void showErrorMessage(String message) {
        FragmentTransaction frag = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = DialogBasic.newInstance(message, getString(R.string.ok));
        dialogFragment.show(frag, "ShowErrorMessage");
    }
    private void showClicked() {
        if(mListener!=null) {
            if(rbAll.isChecked()) {
                mListener.onFragmentInteraction(0,null,null);
                dismiss();
            }
            else if(rbDay.isChecked()) {
                mListener.onFragmentInteraction(1, null, null);
                dismiss();
            }
            else {
                boolean inc = false;
                String message = null;
                if(tiniDate.getText().equals(getResources().getString(R.string.hdTriaData))) {
                    inc = true;
                    message = getResources().getString(R.string.hderrorNoDIni);
                }
                else if(fiDate.getText().equals(getResources().getString(R.string.hdTriaData))) {
                    inc = true;
                    message =getResources().getString(R.string.hderrorNoDFi);
                }
                else if (tIniHour.getText().equals(getResources().getString(R.string.hdTriaHora))) {
                    inc = true;
                    message =getResources().getString(R.string.hderrorNoHIni);
                }
                else if (tFiHour.getText().equals(getResources().getString(R.string.hdTriaHora))) {
                    inc = true;
                    message =getResources().getString(R.string.hderrorNoHFi);
                }

                if(inc) showErrorMessage(message);
                else {
                    try {
                        String newdate = tiniDate.getText() + " " + tIniHour.getText();
                        Date aux = diaF.parse(newdate);
                        iniTime = new Timestamp(aux.getTime());
                        newdate = fiDate.getText() + " " + tFiHour.getText();
                        aux = diaF.parse(newdate);
                        endTime = new Timestamp(aux.getTime());

                    } catch (ParseException e) {
                        showErrorMessage(getString(R.string.hderrorDesconegut));
                        e.printStackTrace();
                        dismiss();
                    }

                    if(iniTime.getTime()<endTime.getTime()) {
                        Log.i("DATE", "Ini: " + iniTime + ", endTime: " + endTime);
                        mListener.onFragmentInteraction(2, iniTime, endTime);
                        dismiss();
                    }
                    else showErrorMessage(getString(R.string.hderrorIniciPosteriorFi));
                }
            }
        }
    }

    public void selectedDate(int dayOfMonth, int month,int year) {
        Log.i("DATE","Data seleccionada: "+dayOfMonth+"/"+month+"/"+year);
        if(dateChoose==0) {
            tiniDate.setText(dayOfMonth + "/" + month + "/" + year);
        }
        else {
            fiDate.setText(dayOfMonth + "/" + month + "/" + year);
        }
    }

    public void selectedHour(int hour, int min) {
        if(hourChoose==0) {
            tIniHour.setText(hour + ":" + min);
        }
        else {
            tFiHour.setText(hour + ":" + min);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int i, Timestamp iniTime, Timestamp endTime);
    }
}