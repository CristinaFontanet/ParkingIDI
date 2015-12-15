package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class NewCar extends android.app.DialogFragment implements View.OnClickListener {

    EditText matr;
    Button enter;

    public static NewCar newInstance() {
        NewCar fragment = new NewCar();
        return fragment;
    }

    public NewCar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_car, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        matr = (EditText) v.findViewById(R.id.editText);
        enter = (Button) v.findViewById(R.id.button);
        enter.setOnClickListener(this);
        return v;
    }

    private OnCompleteListener mListener;

    // Xq l'Activity faci de listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCompleteListener) activity;
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
        if (view.getId()== R.id.button &&mListener != null) {
            mListener.onComplete(matr.getText().toString());
        }
        dismiss();
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(String res);
    }


}
