package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.sql.Timestamp;
import java.util.ArrayList;

/*
 * Created by CristinaFontanet on 15/12/2015.
 */
public class BDController {
    BD dades;
    SQLiteDatabase db;

    BDController(Activity vista) {
        dades = new BD(vista);
    }

    public int bDParkingStatus(ArrayList<Parking> plots) {
        int busyPlots = 0;
        db = dades.getReadableDatabase();
        Cursor curs;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual", s);
            if(curs.moveToFirst()) {
                do {
                    Timestamp auxi = new Timestamp(curs.getLong(2));
                    Parking nou = new Parking(curs.getString(1),auxi);
                    plots.set(curs.getInt(0), nou);
                    ++busyPlots;
                    Log.i("LOADBD", "Carrego a la pos " + curs.getInt(0) + " el cotxe amb matricula " + plots.get(curs.getInt(0)).getMatricula() + " el dia " + auxi );
                } while(curs.moveToNext());
            }
            curs.close();
            db.close();
        }
        Log.i("LOAD", "Despres de carregar el parking, tenim "+busyPlots+" places ocupades");
        return busyPlots;
    }

    public void saveActualState(ArrayList<Parking> plots) {
       // Log.i("SAVE","Vaig a guardar l'estat del parking. Hi ha ocupades: "+ busyPlots+" places");
        db = dades.getWritableDatabase();
        dades.resetActualState(db);
        for(Integer i = 0; i < plots.size(); ++i) {
            if(plots.get(i)!=null) {
                String[] s = {};
                Log.i("SAVE", "Guardo a la pos " + i + " el cotxe amb matricula " + plots.get(i).getMatricula() + " el dia " + plots.get(i).getEntryDay() );
                db.execSQL("INSERT INTO TActual (plot,matricula,entryDay) VALUES ('" + i.toString() + "','" + plots.get(i).getMatricula() + "','" + plots.get(i).getEntryDay().getTime()+ "')", s);
            }
            else Log.i("SAVE", "NOO guardo la plasa "+i+" ja que està lliure");
        }
        db.close();
        Log.i("SAVE","Ja he acabat de guardar les places");
    }

    public void registerCarExit(Parking car) {
        db = dades.getWritableDatabase();
        //dades.resetActualState(db);
        if(car!=null) {
            String[] s = {};
            Log.i("SAVE","Arxivo el cotxe "+car.getMatricula()+" amb sortida del dia "+ car.getExitDay()+ " i preu pagat de "+car.getPricePayed());
            db.execSQL("INSERT INTO THistorial (matricula, entryDay, exitDay, pricePayed) VALUES ('" + car.getMatricula() + "','" + car.getEntryDay().getTime()+ "','" + car.getExitDay().getTime()+  "','" + car.getPricePayed()+"')", s);
        }
        else Log.e("SAVE", "Error al guardar la sortida del cotxe, el la plasa està lliure");
        db.close();
    }

    public void bdHistoricStatus(ArrayList<Parking> contactos) {
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial ORDER BY entryDay ASC, exitDay ASC", s);
        }
        if(curs.moveToFirst()) {
            do {
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        db.close();
        Log.i("HISTORY", "He carregat " + contactos.size() + " cotxes q ja han sortit");
    }

    public void drainHistoric() {
        db = dades.getWritableDatabase();
        dades.resetHistoricState(db);
        db.close();
    }
}
