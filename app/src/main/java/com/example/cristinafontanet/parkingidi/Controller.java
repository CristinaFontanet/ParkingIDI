package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Cristina on 25/11/2015.
 */
public class Controller {
    static private int maxPlaces = 15;
    ArrayList<Parking> plots;
    BD dades;
    SQLiteDatabase db;

    public Controller(Activity vista) {
        plots = new ArrayList<>(15);
        dades = new BD(vista);
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
                plots.add(curs.getInt(0), nou);
            } while(curs.moveToNext());
        }
        db.close();
    }

    public int freePeaches() {
        Log.i("Park", "Hi ha " + plots.size() + " plases ocupades");
        return maxPlaces- plots.size();
    }

    public void newBusyPlot(String matr) {
       // java.sql.Date aux = new java.sql.Date();
        Calendar cal = Calendar.getInstance();
        Date aux = new Date(cal.getTime().getTime());
        Time aux2 = new Time(cal.getTime().getTime());
        Log.i("MATR", "Ocupem plasa el dia "+aux+" a les "+aux2+"a la plasa "+ (plots.size()));
        Parking nou = new Parking(matr,new Date(cal.getTime().getTime()), new Time(cal.getTime().getTime()));
        //plots.add(nou);
        plots.add(plots.size(),nou);
        MainActivity.buttonPlots.get(plots.size()-1).setBackground(MainActivity.busyImage);

    }

    public void newFreePlot(int num) {
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
            String[] s = {};
            db.execSQL("INSERT INTO TActual (plot,matricula,day,entryHour) VALUES ('" + i.toString() + "','" + plots.get(i).getMatricula() + "','"+  plots.get(i).getDay().toString()+ "','"+ plots.get(i).getEntryHour()+")", s);
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
    }
}
