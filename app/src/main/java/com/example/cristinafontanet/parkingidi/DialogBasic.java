package com.example.cristinafontanet.parkingidi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogBasic extends android.app.DialogFragment implements View.OnClickListener{

    Button b1;
    TextView tex;
    static Bundle args;

    static DialogBasic newInstance(String text, String boto) {
        DialogBasic f = new DialogBasic();
        args = new Bundle();
        args.putString("text",text);
        args.putString("boto", boto);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_basic_dialog, container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tex = (TextView) v.findViewById(R.id.textView2);
        tex.setText(args.getString("text","ErrorBundle"));
        //if(bigLetter)tex.setTextAppearance(getActivity(),setStyle(R.style.BigLetters,getTheme()));
        b1 = (Button) v.findViewById(R.id.but1);
        b1.setOnClickListener(this);
        b1.setText(args.getString("boto", "OK"));
        return  v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        //No volem que faci res en clicar al boto del dialog
        dismiss();
    }
}