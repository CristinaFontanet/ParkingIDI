package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

public class DialogDateChooser extends android.app.DialogFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private DatePicker datePicker;
    private static int[] date=null;

    public DialogDateChooser() {
        // Required empty public constructor
    }

    public static DialogDateChooser newInstance() {
        DialogDateChooser fragment = new DialogDateChooser();
        return fragment;
    }

    public static DialogDateChooser newInstance(int[] text) {
        DialogDateChooser fragment = new DialogDateChooser();
        date = text;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_dialog_date_chooser, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        if(date!=null){
            datePicker.updateDate(date[2],date[1]-1,date[0]);
        }
        Button but = (Button) v.findViewById(R.id.bAccepta);
        but.setOnClickListener(this);
        but = (Button) v.findViewById(R.id.bCancela2);
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
        date = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAccepta:
                if (mListener != null) mListener.onFragmentInteraction(datePicker.getDayOfMonth(),datePicker.getMonth()+1,datePicker.getYear());
                dismiss();
                break;
            case R.id.bCancela2:
                dismiss();
                break;
            default:
                break;
        }
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int dayOfMonth, int month, int year);
    }
}
