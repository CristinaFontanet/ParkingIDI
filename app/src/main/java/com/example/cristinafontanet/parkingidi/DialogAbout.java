package com.example.cristinafontanet.parkingidi;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class DialogAbout extends android.app.DialogFragment implements View.OnClickListener {

    public DialogAbout() {
        // Required empty public constructor
    }

    public static DialogAbout newInstance() {
        DialogAbout fragment = new DialogAbout();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dialog_about, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView text = (TextView) v.findViewById(R.id.aboutText);
        text.setText(getString(R.string.text_about));
        Button close = (Button) v.findViewById(R.id.closeAbout);
        close.setOnClickListener(this);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.closeAbout) {
            dismiss();
        }
    }
}
