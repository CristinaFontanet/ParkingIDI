package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

public class DialogDateChooser extends android.app.DialogFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private DatePicker datePicker;
    private static CharSequence date=null;

    public DialogDateChooser() {
        // Required empty public constructor
    }

    public static DialogDateChooser newInstance() {
        DialogDateChooser fragment = new DialogDateChooser();
        return fragment;
    }

    public static DialogFragment newInstance(CharSequence text) {
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
//        Log.i("DATE","Posem:"+date.subSequence(0,1)+"/"+date.subSequence(2,3)+"/"+date.subSequence(4,date.length()-1));
        if(date!=null){
            Log.i("DATE","DE la data "+date);
            Log.i("DATE", "Posem:" + date.subSequence(0, 2) + "/" + date.subSequence(3, 5) + "/" + date.subSequence(6, date.length()));
            datePicker.updateDate(Integer.valueOf(date.subSequence(6,date.length()).toString()),Integer.valueOf(date.subSequence(3, 5).toString()), Integer.valueOf(date.subSequence(0,2).toString()));
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
                Log.i("DATE","Entro en el default");
                break;
        }
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int dayOfMonth, int month, int year);
    }
}
