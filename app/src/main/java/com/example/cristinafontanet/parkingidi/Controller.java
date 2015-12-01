package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Cristina on 25/11/2015.
 */
public class Controller {
    static private int maxPlaces = 15;
    int busyPlots;
    ArrayList<Parking> plots;
    BD dades;
    SQLiteDatabase db;

    public Controller(Activity vista) {
        Parking aux = null;
        plots = new ArrayList<Parking>(Collections.nCopies(maxPlaces, aux));
        dades = new BD(vista);
        busyPlots = 0;
    }

    public void BDPakingStaus() {
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual", s);
        }
        if(curs.moveToFirst()) {
            do {
                Parking nou = new Parking(curs.getString(1),Date.valueOf(curs.getString(2)), Time.valueOf(curs.getString(3)) );
                plots.set(curs.getInt(0), nou);
                ++busyPlots;
            } while(curs.moveToNext());
        }
        db.close();
    }

    public int freePeaches() {
        Log.i("Park", "Hi ha " +(maxPlaces- busyPlots) + " plases ocupades");
        return maxPlaces- busyPlots; //plots.size();
    }

    public void newBusyPlot(String matr) {
        Calendar cal = Calendar.getInstance();
        Date aux = new Date(cal.getTime().getTime());
        Time aux2 = new Time(cal.getTime().getTime());
        Parking nou = new Parking(matr,new Date(cal.getTime().getTime()), new Time(cal.getTime().getTime()));
        //plots.add(nou);
        Random r = new Random();
        int where = r.nextInt(maxPlaces);
        boolean found = false;
        Log.i("Random","where: "+where);
        for(int i =0; i < plots.size() && !found; ++i) {
            if(plots.get((where+i)%maxPlaces)==null) {
                Log.i("Random","a la i: " + i +", on la suma es: "+(where+i)+", on el modul passa a donar: "+ (where+i)%maxPlaces);
                plots.set((where+i)%maxPlaces, nou);
                ++busyPlots;
                MainActivity.buttonPlots.get((where+i)%maxPlaces).setBackground(MainActivity.busyImage);
                Log.i("MATR", "Ocupem plasa el dia " + aux + " a les " + aux2 + "a la plasa " + (where+i)%maxPlaces);
                found = true;
            }
            else Log.i("Random","a la i "+i+", la plasa "+(where+i)%maxPlaces +" esta ocupada");
        }
        if(!found) Log.i("Matr", "Ha passat algo i no s'ha ocupat cap plasa");
    }

    public void newFreePlot(int num) {
        --busyPlots;
        plots.set(num, null);
    }

    public boolean isFree(int num) {
        Log.i("MATR", "num: " + num + " i size: " + plots.size());
        if(num+1> plots.size()) {
            Log.i("MATR", "la plasa num: " + num + " està buida, el parking encara no s'ha omplert fins aqui");
            return true;
        }
        else {
            if( plots.get(num)==null) {
                Log.i("MATR", "la plasa num: " + num + " està buida");
                return true;
            }
            else {
                Log.i("MATR", "la plasa num: " + num + " està ocupada pel cotxe "+ plots.get(num).getMatricula());
                return false;
            }
        }
    }

    public void saveActualState() {
        db = dades.getWritableDatabase();
        dades.resetActualState(db);
        for(Integer i = 0; i < plots.size(); ++i) {
            if(plots.get(i)!=null) {
                String[] s = {};
                db.execSQL("INSERT INTO TActual (plot,matricula,day,entryHour) VALUES ('" + i.toString() + "','" + plots.get(i).getMatricula() + "','" + plots.get(i).getDay().toString() + "','" + plots.get(i).getEntryHour() + ")", s);
            }
        }
        db.close();
    }

    public String getCarReg(int i) {
        return plots.get(i).getMatricula();
    }

    public Date getCarDayEntry(int i) {
        return plots.get(i).getDay();
    }

    public Time getCarHourEntry(int i) {
        return plots.get(i).getEntryHour();
    }

    public void drainParking() {
        plots.clear();
        Parking aux = null;
        plots = new ArrayList<>(Collections.nCopies(maxPlaces, aux));
        busyPlots = 0;
    }
}
