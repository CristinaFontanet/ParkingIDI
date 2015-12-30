package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Created by Cristina on 16/12/2015.
 */
public class historicFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private static RecyclerView mRecyclerViewx;
    public static historicAdapter hisAdapter;
    private static Controller bigControl;
    static private TabPagerAdapter pare;
    private static Activity father;
    private TextView recaptText, recaptMoney;
    private SimpleDateFormat diaF = new SimpleDateFormat(" dd/MM/yyy HH:mm:ss");
    private SimpleDateFormat diaOnlyF = new SimpleDateFormat(" dd/MM/yyy");

    public static historicFragment newInstance(Controller controller, Activity am) {
        historicFragment fragment = new historicFragment();
        bigControl = controller;
        return fragment;
    }

    public historicFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.historic_table,container,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father,LinearLayoutManager.VERTICAL,false);

        mRecyclerViewx = (RecyclerView) v.findViewById(R.id.mRecyclerView);
        mRecyclerViewx.setLayoutManager(linearLayoutManager);
        hisAdapter = new historicAdapter(bigControl,this);
        mRecyclerViewx.setAdapter(hisAdapter);

        recaptText = (TextView) v.findViewById(R.id.textRecapt);
        recaptText.setText(getString(R.string.historicRecaud));

        recaptMoney = (TextView) v.findViewById(R.id.textMoney);
        showAllHistoric();
        return v;
    }

    @Override
    public void onClick(View view) {
        Log.i("HISTO", "He clicat alguna cosaaaa a historicFragment");
    }

    public void sendFather(TabPagerAdapter tabPagerAdapter, Controller control,Activity fatherAct) {
        bigControl = control;
        father = fatherAct;
        pare = tabPagerAdapter;

    }

    public void showAllHistoric() {
        recaptText.setText(getString(R.string.historicRecaud));
        Double price = Math.rint(hisAdapter.showAllHistoric()*100)/100;
        recaptMoney.setText(price.toString()+getString(R.string.edmMoneda));
    }

    public void showTodayHistoric() {
        Calendar cal =Calendar.getInstance();
        recaptText.setText(getString(R.string.historicRecaudDay) + diaOnlyF.format(cal.getTime())+": ");
        Double price = Math.rint(hisAdapter.showTodayHistoric()*100)/100;
        recaptMoney.setText(price.toString()+getString(R.string.edmMoneda));
    }

    public void showHistoBetween(Timestamp iniTime, Timestamp endTime) {
        recaptText.setText(getString(R.string.historicRecaudBet) + diaF.format(iniTime.getTime())+"\n i "+diaF.format(endTime.getTime())+": ");
        Double price = Math.rint(hisAdapter.showHistoricBetween(iniTime, endTime)*100)/100;
        recaptMoney.setText(price.toString()+getString(R.string.edmMoneda));
    }
}
