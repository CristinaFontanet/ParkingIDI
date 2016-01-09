package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ParkingStatus extends android.support.v4.app.Fragment implements View.OnClickListener, DialogNewCar.OnCompleteListener, DialogExitCar.OnFragmentInteractionListener {

    private static RecyclerView mRecyclerViewx;
    public static ParkingAdapter parkAdapter;
    static private Controller bigControl;
    int actualCar;
    static private TabPagerAdapter pare;
    View parentView;
    private static ParkingActivity father;

    public ParkingStatus(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_main, container,false);
       StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father,LinearLayoutManager.VERTICAL,false);
        mRecyclerViewx = (RecyclerView) parentView.findViewById(R.id.rv);
        mRecyclerViewx.setLayoutManager(manager);

        parkAdapter = new ParkingAdapter(bigControl,this,father);
        mRecyclerViewx.setAdapter(parkAdapter);
        mRecyclerViewx.setHasFixedSize(true);
        return parentView;
    }

    public void sendFather(TabPagerAdapter pareAd, Controller am, ParkingActivity fatherAct){
        bigControl = am;
        father =fatherAct;
        pare = pareAd;
        bigControl.BDPakingStaus();
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                default:
                    int clicked = mRecyclerViewx.getChildPosition(view);
                    carClicked(clicked);
                    break;
            }
    }

        private void carClicked(int i) {
        if(!bigControl.isFree(i)) {
            actualCar = i;
            FragmentTransaction frag = getActivity().getFragmentManager().beginTransaction();
            DialogFragment dialogFragment = DialogExitCar.newInstance(i, bigControl);
            dialogFragment.show(frag,"ExitRegistre");
        }
        else {
            FragmentTransaction frag = getActivity().getFragmentManager().beginTransaction();
            DialogFragment dialogFragment = DialogNewCar.newInstance(father,i,bigControl);
            dialogFragment.show(frag, "AskRegistration");
        }
    }

    @Override
    public int onComplete(String res, int pos) {
        int where = -1;
        if(!res.isEmpty()) {
            where = bigControl.newBusyPlot(res,pos);
        }
        return where;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        bigControl.saveActualState();
    }

    @Override
    public void onPause(){
        super.onPause();
        bigControl.saveActualState();
    }

    @Override
    public void onFragmentInteraction() { parkAdapter.notifyItemChanged(actualCar); }

    public void forceNotifyDataSetChanged() { parkAdapter.notifyItemRangeChanged(0, mRecyclerViewx.getChildCount());}

    public void forceNotifyRemoved() {parkAdapter.notifyItemRangeChanged(0,parkAdapter.getItemCount()); }

    public void forceNotifyNumPlotsChanged(int removed) {
        Log.i("PLOTS","num removed: "+removed);
        if(removed>0)parkAdapter.notifyItemRangeInserted(mRecyclerViewx.getChildCount(),removed);
        else parkAdapter.notifyItemRangeRemoved(mRecyclerViewx.getChildCount()-1,removed);
    }
}
