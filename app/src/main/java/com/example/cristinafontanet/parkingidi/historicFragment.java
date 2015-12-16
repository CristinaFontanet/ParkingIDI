package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/*
 * Created by Cristina on 16/12/2015.
 */
public class historicFragment extends android.app.DialogFragment {
    private static RecyclerView mRecyclerViewx;
    public static historicAdapter hisAdapter;
    private static Controller bigControl;
    private static Activity pare;

    public static historicFragment newInstance(Controller controller, Activity am) {
        historicFragment fragment = new historicFragment();
        bigControl = controller;
        pare = am;
        return fragment;
    }

    public historicFragment(){}

    public void setData(){
        Log.i("Ranking", "ESTUPIDA, ESTIC AQUI");
       // hisAdapter.update();
        hisAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.historic_table,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mRecyclerViewx = (RecyclerView) v.findViewById(R.id.mRecyclerView);
        mRecyclerViewx.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        hisAdapter = new historicAdapter(bigControl);
        mRecyclerViewx.setAdapter(hisAdapter);
        return v;
    }

}
