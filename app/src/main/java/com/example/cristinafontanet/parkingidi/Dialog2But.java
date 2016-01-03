package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Dialog2But extends android.app.DialogFragment implements View.OnClickListener{

    private static String text;
    private OnCompleteListener mListener;

    static Dialog2But newInstance(String textm) {
        Dialog2But f = new Dialog2But();
        text = textm;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog2_but, container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView tex = (TextView) v.findViewById(R.id.textView2);
        tex.setText(text);

        Button b1 = (Button) v.findViewById(R.id.byes);
        b1.setOnClickListener(this);
        b1 = (Button) v.findViewById(R.id.bno);
        b1.setOnClickListener(this);
        return  v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    public void onClick(View view) {
        if(view.getId()==R.id.byes) mListener.onComplete();
        dismiss();
    }

    public interface OnCompleteListener { void onComplete(); }
}