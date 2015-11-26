package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Cristina on 25/11/2015.
 */
public class Controller {

    ArrayList<Parking> places;
    BD dades;
    SQLiteDatabase db;

    public Controller(Activity vista) {
        places = new ArrayList<>(15);
        dades = new BD(vista);
    }

    public void estatPakingBD() {
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual", s);
        }
        if(curs.moveToFirst()) {
            do {
                Parking nou = new Parking(curs.getString(0), Date.valueOf(curs.getString(1)));
                places.add(nou);
            } while(curs.moveToNext());
        }
        db.close();
    }
}
