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

public class DialogNewCar extends android.app.DialogFragment implements View.OnClickListener {

    EditText matr;
    private static ParkingActivity father;
    private static int position;
    private OnCompleteListener mListener;
    private static Controller bigControl;


    public static DialogNewCar newInstance(ParkingActivity parent, int pos, Controller contr) {
        DialogNewCar aux = new DialogNewCar();
        father = parent;
        position = pos;
        bigControl = contr;
        return aux;}

    public DialogNewCar() {
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
        getDialog().setTitle(getString(R.string.edcTitle));
        matr = (EditText) v.findViewById(R.id.editText);
        Button enter = (Button) v.findViewById(R.id.button);
        enter.setOnClickListener(this);
        enter = (Button) v.findViewById(R.id.button7);
        enter.setOnClickListener(this);
        return v;
    }


    // Xq l'Activity faci de listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        father.hideFloatingActionButton();
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
        father.showFloatingActionButton();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button7) dismiss();
        else if (view.getId()== R.id.button &&mListener != null) {
            if(matr.getText().toString().isEmpty() || matr.getText().toString().trim().isEmpty()){
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.introdueix_matricula), getString(R.string.ok),getString(R.string.title_error));
                dialogFragment.show(frag, "ShowErrorMessage");
            }
            else if(bigControl.existsCar(matr.getText().toString().trim())) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.matriculaJaDins), getString(R.string.ok), getString(R.string.title_error));
                dialogFragment.show(frag, "ShowErrorMessage");
            }
            else {
                mListener.onComplete(matr.getText().toString(),position);
                dismiss();
            }
        }
    }

    public interface OnCompleteListener { int onComplete(String res, int pos); }

}
