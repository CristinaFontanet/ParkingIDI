package com.example.cristinafontanet.parkingidi;

import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * Created by CristinaFontanet on 16/12/2015.
 */
public class historicAdapter extends RecyclerView.Adapter<historicAdapter.AdapterViewHolder>{
    private Controller bigControl;
    ArrayList<Parking> contactos;// = new ArrayList<>();
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy\nHH:mm:ss");
    private AdapterViewCompat.OnClickListener mListener;


    historicAdapter(Controller control, AdapterViewCompat.OnClickListener listener){
        bigControl = control;
        mListener = listener;
        contactos = new ArrayList<>();
    }

    @Override
    public historicAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_layout, viewGroup, false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final historicAdapter.AdapterViewHolder adapterViewholder, final int position) {
        adapterViewholder.entryD.setText(logAux.format(contactos.get(position).getEntryDay()));
        adapterViewholder.exitD.setText(logAux.format(contactos.get(position).getExitDay()));
        adapterViewholder.matr.setText(contactos.get(position).getMatricula());
        adapterViewholder.price.setText(String.valueOf(contactos.get(position).getPricePayed()));
        adapterViewholder.v.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                mListener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public Double showAllHistoric() {
        contactos = new ArrayList<>();
        Double money = bigControl.bdHistoricStatus(contactos);
        notifyDataSetChanged();
        return money;
    }

    public Double showTodayHistoric() {
        contactos = new ArrayList<>();
        Double money = bigControl.bdHistoricToday(contactos);
        notifyDataSetChanged();
        return money;
    }

    public Double showHistoricBetween(Timestamp iniTime, Timestamp endTime) {
        contactos = new ArrayList<>();
        Double money = bigControl.bdHistoricBetween(contactos, iniTime, endTime);
        notifyDataSetChanged();
        return money;
    }

    public double showMonthHistoric() {
        contactos = new ArrayList<>();
        Double money = bigControl.bdHistoricMonth(contactos);
        notifyDataSetChanged();
        return money;
    }

    public void removeCar() {
        contactos = new ArrayList<>();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView entryD;
        public TextView exitD;
        public TextView matr;
        public TextView price;
        public View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.entryD = (TextView) itemView.findViewById(R.id.entryDay);
            this.exitD = (TextView) itemView.findViewById(R.id.exitDay);
            this.matr = (TextView) itemView.findViewById(R.id.matricula);
            this.price = (TextView) itemView.findViewById(R.id.payed);
            this.v = itemView;
        }
    }

    public void removeAll(){
        contactos = new ArrayList<>();
        notifyDataSetChanged();
    }
}
