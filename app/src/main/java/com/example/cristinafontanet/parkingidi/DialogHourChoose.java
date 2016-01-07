package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;


public class DialogHourChoose extends  android.app.DialogFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private TimePicker timePicker;
    private static int[] hour=null;

    public DialogHourChoose() {
        // Required empty public constructor
    }

    public static DialogHourChoose newInstance() {
        DialogHourChoose fragment = new DialogHourChoose();
        return fragment;
    }

    public static DialogHourChoose newInstance(int[] text) {
        DialogHourChoose fragment = new DialogHourChoose();
        hour = text;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_dialog_hour_choose, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if(hour!=null){
            timePicker.setCurrentHour(hour[0]);
            timePicker.setCurrentMinute(hour[1]);
        }
        Button but = (Button) v.findViewById(R.id.bAccepta2);
        but.setOnClickListener(this);
        but = (Button) v.findViewById(R.id.bCancela3);
        but.setOnClickListener(this);
        return v;
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
        hour = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAccepta2:
                if (mListener != null) mListener.onFragmentInteraction(timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                dismiss();
                break;
            case R.id.bCancela3:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Integer currentHour, Integer currentMinute);
    }
}
