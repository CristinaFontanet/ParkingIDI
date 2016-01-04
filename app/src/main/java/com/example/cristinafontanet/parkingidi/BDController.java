package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Created by CristinaFontanet on 15/12/2015.
 */
public class BDController {
    BD dades;
    SQLiteDatabase db;
    SimpleDateFormat diaOnlyF = new SimpleDateFormat("yyy-MM-dd 00:00:00.0");
    SimpleDateFormat monthOnlyF = new SimpleDateFormat("yyy-MM-01 00:00:00.0");
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    String orderASC ="exitDay ASC, entryDay ASC";
    String orderDESC ="exitDay DESC, entryDay DESC";
    String order;

    BDController(Activity vista) {
        dades = new BD(vista);
        order = orderDESC;
    }
/** INSERTS*/
public void saveActualState(ArrayList<Parking> plots) {
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
    Log.i("SAVE", "Ja he acabat de guardar les places");
}

    public void saveNewEntry(int where, Parking car) {
        db = dades.getWritableDatabase();
        if(car!=null) {
            String[] s = {};
            Log.i("SAVE", "Guardo a la pos " + where + " el cotxe amb matricula " + car.getMatricula() + " el dia " + car.getEntryDay());
            db.execSQL("INSERT INTO TActual (plot,matricula,entryDay) VALUES ('" + where + "','" + car.getMatricula() + "','" + car.getEntryDay().getTime() + "')", s);
        }
        db.close();
    }


/** DELETE */
    public void drainHistoric() {
        db = dades.getWritableDatabase();
        dades.resetHistoricState(db);
        db.close();
    }

    public void drainActualStatus() {
        db = dades.getWritableDatabase();
        dades.resetActualState(db);
        db.close();
    }

    public void removeFromStatus(int plot) {
        db = dades.getWritableDatabase();
        db.execSQL("DELETE FROM TActual WHERE plot= " + plot + " ;");
        db.close();
    }

    public Parking removeFromHistory(String matricula) {
        Parking res = null;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE matricula='"+matricula+ "' ORDER BY exitDay DESC ;", s);
        }
        if(curs.moveToFirst()) {
            res = new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3));
        }
        else Log.i("UNDOBD", "NOOOO he trobat el cotxe a l'historial");
        if(db != null && res!=null) {
            String[] s = {};
            db.execSQL("DELETE FROM THistorial WHERE matricula='"+matricula+ "' and exitDay="+res.getExitDay().getTime()+" ;", s);
        }
        db.close();
        return res;
    }

/** SELECTS*/
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
                Parking nou = new Parking(curs.getString(1),auxi,curs.getInt(0));
                plots.set(curs.getInt(0), nou);
                ++busyPlots;
                Log.i("LOADBD", "Carrego a la pos " + curs.getInt(0) + " el cotxe amb matricula " + plots.get(curs.getInt(0)).getMatricula() + " el dia " + auxi );
            } while(curs.moveToNext());
        }
        curs.close();
        db.close();
    }
    Log.i("LOAD", "Despres de carregar el parking, tenim " + busyPlots + " places ocupades");
    return busyPlots;
}

    public Double bdHistoricStatus(ArrayList<Parking> contactos) {
        Double payed = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial ORDER BY "+order, s);
        }
        if(curs.moveToFirst()) {
            do {
                payed +=curs.getDouble(3);
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        db.close();
        Log.i("HISTORY", "He carregat " + contactos.size() + " cotxes q ja han sortit");
        return payed;
    }

    public Double bdHistoricToday(ArrayList<Parking> contactos) {
        Calendar cal =Calendar.getInstance();
        Date auxday= cal.getTime();
        Timestamp day = Timestamp.valueOf(diaOnlyF.format(auxday));
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE exitDay>="+day.getTime()+" ORDER BY "+order, s);
        }
        if(curs.moveToFirst()) {
            do {
                money+=curs.getDouble(3);
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        db.close();
        Log.i("HISTORY", "He carregat " + contactos.size() + " cotxes q ja han sortit el dia " + day);
        return money;
    }

    public Double bdHistoricMonth(ArrayList<Parking> contactos) {
        Calendar cal =Calendar.getInstance();
        Date auxday= cal.getTime();
        Timestamp day = Timestamp.valueOf(monthOnlyF.format(auxday));
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE exitDay>="+day.getTime()+" ORDER BY "+order, s);
        }
        if(curs.moveToFirst()) {
            do {
                money+=curs.getDouble(3);
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        db.close();
        Log.i("HISTORY", "He carregat " + contactos.size() + " cotxes q ja han sortit el mes " + day);
        return money;
    }

    public Double bdHistoricBetween(ArrayList<Parking> contactos, Timestamp iniTime, Timestamp endTime) {
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE exitDay>="+iniTime.getTime()+" and exitDay<="+endTime.getTime()+" ORDER BY "+order, s);
        }
        if(curs.moveToFirst()) {
            do {
                money+=curs.getDouble(3);
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        db.close();
        Log.i("HISTORY", "He carregat " + contactos.size() + " cotxes q ja han sortit entre el dia " + iniTime + " i el dia " + endTime);
        return money;
    }

/** Others*/
    public void changeOrder(){
        if(order.equals(orderDESC)) order = orderASC;
        else order= orderDESC;
    }

    public void registerCarExit(int num, Parking car) {
        db = dades.getWritableDatabase();
      //  dades.resetActualState(db);
        if(car!=null) {
            String[] s = {};
            Log.i("SAVE","Arxivo el cotxe de la plasa "+num+", amb matr: "+car.getMatricula()+" amb sortida del dia "+ car.getExitDay()+ " i preu pagat de "+car.getPricePayed());
            db.execSQL("INSERT INTO THistorial (matricula, entryDay, exitDay, pricePayed) VALUES ('" + car.getMatricula() + "','" + car.getEntryDay().getTime() + "','" + car.getExitDay().getTime() + "','" + car.getPricePayed() + "')", s);
            db.execSQL("DELETE FROM TActual WHERE plot= "+num+" ;");
        }
        else Log.e("SAVE", "Error al guardar la sortida del cotxe, el la plasa està lliure");
        db.close();
    }

    public boolean exportToCSV(File file) {
        PrintWriter printWriter = null;
        try {
            file.createNewFile();
            printWriter = new PrintWriter(new FileWriter(file));

            db = dades.getReadableDatabase();
            printWriter.println("FIRST TABLE OF THE DATABASE");
            printWriter.println("Matricula,diaEntrada,diaSortida,Preu");

            Cursor curCSV = null;
            if(db != null) {
                String[] s = {};
                curCSV = db.rawQuery("SELECT * FROM THistorial; ", s);
            }
            if(curCSV.moveToFirst()) {
                do {
                    //matricula TEXT, entryDay TIMESTAMP , exitDay TIMESTAMP , pricePayed DOUBLE, PRIMARY KEY(matricula,entryDay)
 //contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
                    String matr = curCSV.getString(0);
                    Long entryDate = curCSV.getLong(1);
                    Long exitDate = curCSV.getLong(2);
                    Double price = curCSV.getDouble(3);
                    Log.i("EXPORT","Exportem el cotxe "+matr);

                    String record = matr + ", " + logAux.format(entryDate) + ", " + logAux.format(exitDate) + ", " + price;
                    printWriter.println(record); //write the record in the .csv file
                } while(curCSV.moveToNext());
            }
            curCSV.close();
            db.close();
        }
        catch(Exception exc) {
            //if there are any exceptions
            Log.i("EXPORT","EXCEPCIOOOOOOOOOOOOOOOOOOOOOOO");

            exc.printStackTrace();
            return false;
        }
        finally {
            if(printWriter != null) printWriter.close();
        }
        return true;
    }
}
