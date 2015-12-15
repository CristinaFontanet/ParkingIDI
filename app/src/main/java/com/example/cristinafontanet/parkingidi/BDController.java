package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Cristina on 15/12/2015.
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
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual", s);
        }
        if(curs.moveToFirst()) {
            do {
                Timestamp auxi = new Timestamp(curs.getLong(2));
                Date aux = new Date(auxi.getTime());

                Parking nou = new Parking(curs.getString(1),auxi);
                plots.set(curs.getInt(0), nou);
                ++busyPlots;
                Log.i("LOADBD", "Carrego a la pos " + curs.getInt(0) + " el cotxe amb matricula " + plots.get(curs.getInt(0)).getMatricula() + " el dia " + auxi );
            } while(curs.moveToNext());
        }
        db.close();
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

}
