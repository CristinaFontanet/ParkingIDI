package com.example.cristinafontanet.parkingidi;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ParkingStatus extends android.support.v4.app.Fragment implements View.OnClickListener, DialogNewCar.OnCompleteListener, DialogExitCar.OnFragmentInteractionListener {

    private static RecyclerView mRecyclerViewx;
    private static ParkingAdapter parkAdapter;
    private static Controller bigControl;
    static int actualCar;
    private static ParkingActivity father;

    public ParkingStatus(){ }

    private void newPage(View parentView) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father,LinearLayoutManager.VERTICAL,false);
        mRecyclerViewx = (RecyclerView) parentView.findViewById(R.id.rv);
        mRecyclerViewx.setLayoutManager(manager);
        mRecyclerViewx.setHasFixedSize(true);

        parkAdapter = new ParkingAdapter(bigControl,this,father);
        mRecyclerViewx.setAdapter(parkAdapter);
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.activity_main, container,false);
       newPage(parentView);
        return parentView;
    }

    public void sendFather(Controller am, ParkingActivity fatherAct){
        bigControl = am;
        father =fatherAct;
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

    public void forceNotifyNumPlotsChanged() {
        newPage(getView());
    }
}
