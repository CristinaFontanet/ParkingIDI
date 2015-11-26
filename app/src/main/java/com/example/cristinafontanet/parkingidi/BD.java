package com.example.cristinafontanet.parkingidi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CristinaFontanet on 25/11/2015.
 */
public class BD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "BDParking";

    private static final String Historial_TABLE_NAME = "THistorial";
    private static final String Actual_TABLE_NAME = "TActual";

    private static final String STATISTICS_TABLE_CREATE_Historial = "CREATE TABLE " + Historial_TABLE_NAME + " (matricula TEXT, dia TEXT, horaEntrada HOUR, dataSortida HOUR, PRIMARY KEY(matricula,dia,horaEntrada) )";
    private static final String STATISTICS_TABLE_CREATE_Actual = "CREATE TABLE " + Actual_TABLE_NAME + " (plasa INTEGER PRIMARY KEY, matricula TEXT, dia DATE, horaEntrada HOUR)";

    BD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STATISTICS_TABLE_CREATE_Historial);
        db.execSQL(STATISTICS_TABLE_CREATE_Actual);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Historial_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Historial);

        db.execSQL("DROP TABLE IF EXISTS " + Actual_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Actual);
    }
}