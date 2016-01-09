package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class DialogPlotsChanger extends android.app.DialogFragment implements View.OnClickListener {

    private static int oldNum;
    private EditText editText;
    private OnFragmentInteractionListener mListener;

    public DialogPlotsChanger() {
        // Required empty public constructor
    }

    public static DialogPlotsChanger newInstance(int actualNum) {
        DialogPlotsChanger fragment = new DialogPlotsChanger();
        oldNum = actualNum;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_dialog_plots_changer, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Button accept = (Button) v.findViewById(R.id.button8);
        accept.setOnClickListener(this);
        accept = (Button) v.findViewById(R.id.button9);
        accept.setOnClickListener(this);
        editText = (EditText) v.findViewById(R.id.plotNumberText);
        editText.setText(Integer.toString(oldNum));
        editText.setSelection(Integer.toString(oldNum).length());
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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button8) dismiss();
        else if(v.getId()==R.id.button9){
            int num = Integer.valueOf(editText.getText().toString());
            if(num!=0) {
                mListener.onFragmentInteraction(num);
                dismiss();
            }
            else {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.plotsMin), getString(R.string.ok),getString(R.string.title_error));
                dialogFragment.show(frag, "ShowErrorMessage");
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int num);
    }
}
