package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DialogHourChoose.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DialogHourChoose#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogHourChoose extends  android.app.DialogFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private TimePicker timePicker;
    private static CharSequence hour=null;

    public DialogHourChoose() {
        // Required empty public constructor
    }

    public static DialogHourChoose newInstance() {
        DialogHourChoose fragment = new DialogHourChoose();
        return fragment;
    }

    public static DialogFragment newInstance(CharSequence text) {
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
            Log.i("DATE","DE la hora "+hour);
            timePicker.setCurrentHour(Integer.valueOf(hour.subSequence(0,2).toString()));
            timePicker.setCurrentMinute(Integer.valueOf(hour.subSequence(3, 5).toString()));
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
                Log.i("DATE", "Entro en el default del DialogHourChoose OnClick");
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Integer currentHour, Integer currentMinute);
    }
}
