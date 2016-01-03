package com.example.cristinafontanet.parkingidi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public class BD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "BDParking";

    private static final String Historial_TABLE_NAME = "THistorial";
    private static final String Actual_TABLE_NAME = "TActual";

    private static final String STATISTICS_TABLE_CREATE_Historial = "CREATE TABLE " + Historial_TABLE_NAME + " (matricula TEXT, entryDay TIMESTAMP , exitDay TIMESTAMP , pricePayed DOUBLE, PRIMARY KEY(matricula,entryDay) )";
    private static final String STATISTICS_TABLE_CREATE_Actual = "CREATE TABLE " + Actual_TABLE_NAME + " (plot INTEGER PRIMARY KEY, matricula TEXT, entryDay TIMESTAMP )";

    BD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STATISTICS_TABLE_CREATE_Historial);
        db.execSQL(STATISTICS_TABLE_CREATE_Actual);
    }

    public void resetActualState(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Actual_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Actual) ;
    }

    public void resetHistoricState(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Historial_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Historial) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Historial_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Historial);

        db.execSQL("DROP TABLE IF EXISTS " + Actual_TABLE_NAME);
        db.execSQL(STATISTICS_TABLE_CREATE_Actual);
    }
}