package com.example.cristinafontanet.parkingidi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogPlot extends android.app.DialogFragment implements View.OnClickListener{

    Button b1;
    private static int num;

    static DialogPlot newInstance(int numero) {
        DialogPlot f = new DialogPlot();
        num = numero;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_plot, container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        b1 = (Button) v.findViewById(R.id.but2);
        b1.setOnClickListener(this);

        TextView number = (TextView) v.findViewById(R.id.textNumber);
        CharSequence aux = number.getText();
        number.setText(" "+num+"  ");

        return  v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}