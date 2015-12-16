package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public final class Controller {
    static private int maxPlaces = 15;
    private static final double pricePerMinute = 0.02;
    private static final int segDay = 24*60*60;
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private int busyPlots;
    private ArrayList<Parking> plots;
    private BDController bdContr;

    private Timestamp actualT;
    private double price;

    public Controller(Activity vista) {
        plots = new ArrayList<>(Collections.nCopies(maxPlaces, (Parking)null));
        busyPlots = 0;
        bdContr = new BDController(vista);
    }
    public void BDPakingStaus() { busyPlots = bdContr.bDParkingStatus(plots);}

    public void bdHistoricStatus(ArrayList<Parking> contactos) {bdContr.bdHistoricStatus(contactos);}

    public void saveActualState() { bdContr.saveActualState(plots);}

    public int getNumFreePeaches() {
        Log.i("Park", "Hi ha " + (maxPlaces - busyPlots) + " plases lliures");
        return maxPlaces- busyPlots; //plots.size();
    }

    public void newBusyPlot(String matr) {
        Calendar cal = Calendar.getInstance();
        Date aux = new Date(cal.getTime().getTime());
        Parking nou = new Parking(matr,new Timestamp(cal.getTime().getTime()));
        Random r = new Random();
        int where = r.nextInt(maxPlaces);
        boolean found = false;
        Log.i("Random","where: "+where);
        for(int i =0; i < plots.size() && !found; ++i) {
            if(plots.get((where+i)%maxPlaces)==null) {
                Log.i("Random","a la i: " + i +", on la suma es: "+(where+i)+", on el modul passa a donar: "+ (where+i)%maxPlaces);
                plots.set((where + i) % maxPlaces, nou);
                ++busyPlots;
                MainActivity.buttonPlots.get((where+i)%maxPlaces).setBackground(MainActivity.busyImage);
                MainActivity.buttonPlots.get((where+i)%maxPlaces).setText(matr);
                Log.i("ENTRY", "Ocupem plasa el dia " + aux + "a la plasa " + (where + i) % maxPlaces);
                found = true;
            }
            else Log.i("ENTRY","a la i "+i+", la plasa "+(where+i)%maxPlaces +" esta ocupada");
        }
        if(!found) Log.i("ENTRY", "Ha passat algo i no s'ha ocupat cap plasa");
    }

    public void newFreePlot(int num) {
        --busyPlots;
        plots.set(num, null);
    }

    public boolean isFree(int num) {
        Log.i("LOAD", "num: " + num + " i size: " + plots.size());
        if(num+1> plots.size()) {
            Log.i("LOAD", "la plasa num: " + num + " està buida, el parking encara no s'ha omplert fins aqui");
            return true;
        }
        else {
            if( plots.get(num)==null) {
                Log.i("LOAD", "la plasa num: " + num + " està buida");
                return true;
            }
            else {
                Log.i("LOAD", "la plasa num: " + num + " està ocupada pel cotxe "+ plots.get(num).getMatricula());
                return false;
            }
        }
    }

    public String getCarReg(int i) {
        return plots.get(i).getMatricula();
    }

    public Timestamp getCarDayEntry(int i) {
        return plots.get(i).getEntryDay();
    }

    public void drainParking() {
        plots.clear();
        plots = new ArrayList<>(Collections.nCopies(maxPlaces, (Parking)null));
        busyPlots = 0;
    }

    public int difDays(Timestamp mDay) { //Els dies que ha passat el parking, es 0 si entra i surt el mateix dia.
        Calendar cal = Calendar.getInstance();
        Date actualD = new Date(cal.getTime().getTime());
        return (int)TimeUnit.MILLISECONDS.toDays(actualD.getTime()-mDay.getTime());
    }

    public double minsCalcul(int i){
        Timestamp mDay = plots.get(i).getEntryDay();
        Calendar cal = Calendar.getInstance();
        actualT = new Timestamp(cal.getTime().getTime());
        Log.i("HoraEntrada", "mDay: " + logAux.format(mDay) + ", el q implica: " + TimeUnit.MILLISECONDS.toSeconds(mDay.getTime()) / 60 + " minuts");
        Log.i("HoraSortida", "Actual: " + actualT + ", el q implica: " + TimeUnit.MILLISECONDS.toSeconds(actualT.getTime()) / 60 + " minuts");

        int days = difDays(mDay);

        double  min;
        if(days>0) {
            cal.add(Calendar.DATE,1);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            long midnight = cal.getTimeInMillis();  //Mitjanit del dia seguent, per poder saber quants minuts hi ha estat aquell dia

            min = (double)(TimeUnit.MILLISECONDS.toSeconds(midnight - mDay.getTime()))/60; //mins des de l'hora d'entrada fins a les 12 d'aquell mateix dia
            Log.i("Hores","De l'entrada a "+mDay+" fins les 12 de la nit d'aquell dia, serien "+ min+" minuts, q son "+min/60+" hores i "+min%60 +" minuts");
            min +=(segDay*days)/60;
            Log.i("Hores","I tenint en compte que hi ha estat "+days+" dies, sumem"+(segDay*days)/60+" minuts-> "+(segDay*days)/(60*60)+" hores i " +(segDay*days)%(60*60)+" min en total");
            cal.add(Calendar.DATE, -1);
            double  min2 = (double)(TimeUnit.MILLISECONDS.toSeconds(actualT.getTime()-cal.getTimeInMillis()))/60;
            Log.i("Hores","I que surt a les "+actualT+" aixo son "+min2+" minuts, q son->"+min2/60+" hores i "+ min2%60+" minuts");
        }
        else {
            long diffT = actualT.getTime() - mDay.getTime();
            min = (double)TimeUnit.MILLISECONDS.toSeconds(diffT)/60;
        }
        Log.i("Calc", "s'ha estat al parking "+ min+ " min");
        return min;
    }

    public void registerCarExit(int num) {
        plots.get(num).setExitDay(actualT);
        plots.get(num).setPricePayed(price);
        bdContr.registerCarExit(plots.get(num));
    }

    public Double priceCalcul(double min) {
        price = Math.rint(min*pricePerMinute*100)/100;
        return price;
    }

    public void drainHistoric() {
        bdContr.drainHistoric();
    }
}
