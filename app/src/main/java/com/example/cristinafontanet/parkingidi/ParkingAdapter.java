package com.example.cristinafontanet.parkingidi;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;

/*
 * Created by CristinaFontanet on 16/12/2015.
 */
public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.BusyPlotViewHolder>{
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private AdapterViewCompat.OnClickListener mListener;
    private Controller bigControl;


    ParkingAdapter(Controller control, AdapterViewCompat.OnClickListener listener){
        mListener = listener;
        bigControl = control;
    }

    @Override
    public BusyPlotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_card_layout, viewGroup, false);
        //view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT));
        return new BusyPlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BusyPlotViewHolder adapterViewholder, final int position) {
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  params.height=80;
        //   adapterViewholder.itemView.setLayoutParams(params);

        adapterViewholder.id=position;
        adapterViewholder.idCar.setText(position+1 + ".");
        Log.i("NEW","VAig a fer Bind del num: "+position+ " a la pos: "+position);
        if(bigControl.isFree(position)) {
            adapterViewholder.matr.setText("LLiure");
            adapterViewholder.entryD.setText(" ");
            adapterViewholder.layout.setBackgroundColor(Color.parseColor("#b6fcd5"));
        }
        else {
            adapterViewholder.entryD.setText(logAux.format(bigControl.getCarDayEntry(position)));
            adapterViewholder.matr.setText(bigControl.getCarReg(position));
            adapterViewholder.layout.setBackgroundColor(Color.parseColor("#ff9898"));
        }
        adapterViewholder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class BusyPlotViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public int id;
        public TextView entryD;
        public TextView matr;
        public TextView idCar;
        public RelativeLayout layout;
        // public ImageView photoId;
        public View v;
        public BusyPlotViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cV1);
            this.entryD = (TextView) itemView.findViewById(R.id.eD1);
            this.matr = (TextView) itemView.findViewById(R.id.ma1);
            this.idCar = (TextView) itemView.findViewById(R.id.idCar);
            this.layout = (RelativeLayout) itemView.findViewById(R.id.carLayout);
            this.v = itemView;
        }

        public void drain() {

        }
    }

}

