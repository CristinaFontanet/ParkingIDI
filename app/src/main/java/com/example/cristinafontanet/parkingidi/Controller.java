package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public final class Controller {

    static private int maxPlaces = 15;
    private static double pricePerMinute = 0.02;
    private static final int segDay = 24*60*60;
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private int busyPlots;
    private ArrayList<Parking> plots;
    private BDController bdContr;
    private Parking lastMoved;
    private ParkingActivity father;

    private Timestamp actualT;
    private double price;
    private static File exportFile;

    public Controller(ParkingActivity vista, Double price) {
        plots = new ArrayList<>(Collections.nCopies(maxPlaces, (Parking)null));
        busyPlots = 0;
        pricePerMinute = price;
        bdContr = new BDController(vista);
        father = vista;
    }

/** BD  */
    public void BDPakingStaus() { busyPlots = bdContr.bDParkingStatus(plots);}

    public Double bdHistoricStatus(ArrayList<Parking> contactos) { return bdContr.bdHistoricStatus(contactos); }

    public Double bdHistoricToday(ArrayList<Parking> contactos) { return bdContr.bdHistoricToday(contactos); }

    public Double bdHistoricBetween(ArrayList<Parking> contactos, Timestamp iniTime, Timestamp endTime) { return bdContr.bdHistoricBetween(contactos, iniTime, endTime); }

    public Double bdHistoricMonth(ArrayList<Parking> contactos) { return bdContr.bdHistoricMonth(contactos); }

    public void saveActualState() { bdContr.saveActualState(plots);}

    public void saveNewEntry(int where, Parking car) { bdContr.saveNewEntry(where, car); }

    public void bdChangeViewOrder() {bdContr.changeOrder(); }

/** Consultes */
    public int getNumFreePeaches() {
        Log.i("Park", "Hi ha " + (maxPlaces - busyPlots) + " plases lliures");
        return maxPlaces- busyPlots;
    }

    public boolean isFree(int num) {
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

    public String getCarReg(int i) { return plots.get(i).getMatricula(); }

    public Timestamp getCarDayEntry(int i) { return plots.get(i).getEntryDay();  }

    public int getLastMoveType() {
        if (lastMoved == null) return -1;
        else if(lastMoved.getExitDay()==null) return 1;
        else return 0;
    }

    public String getLastMoveMatr() { return lastMoved.getMatricula(); }

    public Double getPrice() {  return pricePerMinute; }

/** Notifications */
    public void showNormalToast(String message, ParkingActivity father) {
        Toast toast = Toast.makeText(father, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showFastToast(String message, Activity father) {
        final Toast toast = Toast.makeText(father, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

/** New Car */
    public int newBusyPlot(String matr, int pos) {
        Calendar cal = Calendar.getInstance();
        Random r = new Random();
        Parking nou;
        int definitive = -1;
        if(pos>=0) {
            nou = new Parking(matr,new Timestamp(cal.getTime().getTime()),pos);
            definitive = pos;
            plots.set(definitive, nou);
            ++busyPlots;
            saveNewEntry(definitive, nou);
            lastMoved = nou;
        }
        else {
            int where = r.nextInt(maxPlaces);
            boolean found = false;
            Log.i("Random", "where: " + where);
            for (int i = 0; i < plots.size() && !found; ++i) {
                if (plots.get((where + i) % maxPlaces) == null) {
                    definitive = (where + i) % maxPlaces;
                    Log.i("Random", "a la i: " + i + ", on la suma es: " + (where + i) + ", on el modul passa a donar: " + definitive);
                    nou = new Parking(matr, new Timestamp(cal.getTime().getTime()), definitive);
                    plots.set(definitive, nou);
                    ++busyPlots;
                    found = true;
                    saveNewEntry(definitive, nou);
                    lastMoved = nou;
                }
            }
        }
        return definitive;
    }

/** Exit car */
    public void newFreePlot(int num) {
        lastMoved = plots.get(num);
        --busyPlots;
        plots.set(num, null);
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
        Log.i("Calc", "s'ha estat al parking " + min + " min");
        return min;
    }

    public void registerCarExit(int num) {
        plots.get(num).setExitDay(actualT);
        plots.get(num).setPricePayed(price);
        bdContr.registerCarExit(num, plots.get(num));
    }

    public Double priceCalcul(double min) {
        price = Math.rint(min*pricePerMinute*100)/100;
        return price;
    }


/** Drains */
    public void drainParking() {
        plots.clear();
        plots = new ArrayList<>(Collections.nCopies(maxPlaces, (Parking)null));
        busyPlots = 0;
        bdContr.drainActualStatus();
    }

    public void drainHistoric() {
        bdContr.drainHistoric();
    }

/** Undo */
    public void undoLastMove() {
        int plot = lastMoved.getPlot();
        Parking last = null;
        Log.i("UNDO","Desfaig el moviment de la plasa "+plot+", on hi havia el cotxe "+lastMoved.getMatricula());
        if(lastMoved.getExitDay()==null) {  //undoEntry
            newFreePlot(plot);
            bdContr.removeFromStatus(plot);
        }
        else {      //undoExit
            last = bdContr.removeFromHistory(lastMoved.getMatricula());
            last.setPlot(plot);
            bdContr.saveNewEntry(plot,last);
            plots.set(plot,last);
            ++busyPlots;
        }
        lastMoved = null;
    }

    public void changePrice(Double newPrice) {
        pricePerMinute = newPrice;
        SharedPreferences settings = father.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Price", newPrice.toString());
        editor.apply();
    }


    public boolean exportHistorialState() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state))  return false;
        else {
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists())exportDir.mkdirs();
            Calendar cal =Calendar.getInstance();
            exportFile = new File(exportDir, "FontanetCristinaParking.csv");
            return bdContr.exportToCSV(exportFile);
        }
    }

    public File getFilePath() {
        return exportFile;
    }
}