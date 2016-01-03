package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DialogPriceChanger extends android.app.DialogFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private static Double actualPrice;
    private EditText editText;

    public DialogPriceChanger() {
        // Required empty public constructor
    }

    public static DialogPriceChanger newInstance(Double price) {
        DialogPriceChanger fragment = new DialogPriceChanger();
        actualPrice = price;
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
        View v =inflater.inflate(R.layout.fragment_dialog_price_changer, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        editText = (EditText) v.findViewById(R.id.editText2);
        editText.setText(Double.toString(actualPrice));
        editText.setSelection(actualPrice.toString().length());
        editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==1 && !s.toString().contains(".")) {
                    editText.setText(editText.getText().toString()+".");
                    editText.setSelection(editText.getText().toString().length());
                    Log.i("DEC"," estic al textWatcher");
                }
                if(s.toString().length()==1 && s.toString().equals(".")) editText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        Button acc = (Button) v.findViewById(R.id.button3);
        acc.setOnClickListener(this);
        acc = (Button) v.findViewById(R.id.button4);
        acc.setOnClickListener(this);

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
        if(v.getId()==R.id.button4) {
            mListener.onFragmentInteraction(Double.valueOf(editText.getText().toString()));
        }
        dismiss();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Double uri);
    }

    private class DecimalDigitsInputFilter implements InputFilter {
        Pattern mPattern;

        public DecimalDigitsInputFilter() {
            mPattern = Pattern.compile("([0-9]\\.[0-9]{0,1})?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if(!matcher.matches()) return "";
            return null;
        }
    }
}
