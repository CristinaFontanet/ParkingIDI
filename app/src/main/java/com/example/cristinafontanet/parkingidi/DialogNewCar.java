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
    Button enter;
    private static ParkingActivity father;
    private static int position;
    private OnCompleteListener mListener;


    public static DialogNewCar newInstance(ParkingActivity parent, int pos) {
        DialogNewCar aux = new DialogNewCar();
        father = parent;
        position = pos;
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
        enter = (Button) v.findViewById(R.id.button);
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
        if (view.getId()== R.id.button &&mListener != null) {
            if(!matr.getText().toString().isEmpty()) {
                mListener.onComplete(matr.getText().toString(),position);
                dismiss();
            }
            else {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = DialogBasic.newInstance(getString(R.string.introdueix_matricula), getString(R.string.ok));
                dialogFragment.show(frag, "ShowErrorMessage");
            }
        }
    }

    public interface OnCompleteListener { int onComplete(String res, int pos); }


}
