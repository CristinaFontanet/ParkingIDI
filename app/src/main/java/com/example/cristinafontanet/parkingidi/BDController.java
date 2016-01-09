package com.example.cristinafontanet.parkingidi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/*
 * Created by CristinaFontanet on 15/12/2015.
 */
public class BDController {
    BD dades;
    SQLiteDatabase db;
    SimpleDateFormat diaOnlyF = new SimpleDateFormat("yyy-MM-dd 00:00:00.0");
    SimpleDateFormat monthOnlyF = new SimpleDateFormat("yyy-MM-01 00:00:00.0");
    private SimpleDateFormat logAux = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private SimpleDateFormat export = new SimpleDateFormat("yyy/MM/dd");
    String orderASCH ="exitDay ASC, entryDay ASC";
    String orderDESCH ="exitDay DESC, entryDay DESC";
    String orderASCP ="entryDay ASC";
    String orderDESCP ="entryDay DESC";
    String order,orderP;

    protected static final int exportExcError = -2;
    protected static final int exportOk = 0;

    BDController(Activity vista) {
        dades = new BD(vista);
        order = orderDESCH;
    }
/** INSERTS*/
    public void saveActualState(ArrayList<Parking> plots) {
    db = dades.getWritableDatabase();
    dades.resetActualState(db);
        try {
    for(Integer i = 0; i < plots.size(); ++i) {
        if(plots.get(i)!=null) {
            String[] s = {i.toString(),plots.get(i).getMatricula()};
            db.execSQL("INSERT INTO TActual (plot,matricula,entryDay) VALUES (?,?,'" + plots.get(i).getEntryDay().getTime()+ "')", s);
        }
    }
    db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
        }
}

    public void saveNewEntry(int where, Parking car) {
        try {
            db = dades.getWritableDatabase();
            if (car != null) {
                String[] s = {car.getMatricula()};
                db.execSQL("INSERT INTO TActual (plot,matricula,entryDay) VALUES ('" + where + "',?,'" + car.getEntryDay().getTime() + "')", s);
            }
            db.close();
        }
        catch (Exception exc) {

        }
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
        try {
        db.execSQL("DELETE FROM TActual WHERE plot= " + plot + " ;");
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
        }
        db.close();
    }

    public Parking removeFromHistory(String matricula) {
        Parking res = null;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        try {
        if(db != null) {
            String[] s = {matricula};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE matricula=? ORDER BY exitDay DESC ;", s);
        }
        if(curs.moveToFirst()) {
            res = new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3));
        }
        if(db != null && res!=null) {
            String[] s = {matricula};
            db.execSQL("DELETE FROM THistorial WHERE matricula=? and exitDay="+res.getExitDay().getTime()+" ;", s);
        }
        db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
        }
        return res;
    }

/** SELECTS*/
    public int bDParkingStatus(ArrayList<Parking> plots) {
    int busyPlots = 0;
    db = dades.getReadableDatabase();
    Cursor curs;
        try {
    if(db != null) {
        String[] s = {};
        curs = db.rawQuery("SELECT * FROM TActual", s);
        if(curs.moveToFirst()) {
            do {
                Timestamp auxi = new Timestamp(curs.getLong(2));
                Parking nou = new Parking(curs.getString(1),auxi,curs.getInt(0));
                plots.set(curs.getInt(0), nou);
                ++busyPlots;
            } while(curs.moveToNext());
        }
        curs.close();
        db.close();
    }
        }
            catch(Exception exc) {
                //   exc.printStackTrace();
            }
    return busyPlots;
}

    public Double bdHistoricStatus(ArrayList<Parking> contactos) {
        Double payed = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        try {
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
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
            return payed;
        }
        return payed;
    }

    public Double bdHistoricToday(ArrayList<Parking> contactos) {
        Calendar cal =Calendar.getInstance();
        Date auxday= cal.getTime();
        Timestamp day = Timestamp.valueOf(diaOnlyF.format(auxday));
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        try {
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
        curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual WHERE entryDay>="+day.getTime()+" ORDER BY "+orderP, s);
        }
        if(curs.moveToFirst()) {
            do {
                contactos.add(new Parking(curs.getString(1), new Timestamp(curs.getLong(2)),curs.getInt(0)));
            } while(curs.moveToNext());
        }
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
            return money;
        }
        db.close();
        return money;
    }

    public Double bdHistoricMonth(ArrayList<Parking> contactos) {
        Calendar cal =Calendar.getInstance();
        Date auxday= cal.getTime();
        Timestamp day = Timestamp.valueOf(monthOnlyF.format(auxday));
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        try {
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
        curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual WHERE entryDay>="+day.getTime()+" ORDER BY "+orderP, s);
        }
        if(curs.moveToFirst()) {
            do {
                contactos.add(new Parking(curs.getString(1), new Timestamp(curs.getLong(2)),curs.getInt(0)));
            } while(curs.moveToNext());
        }
        db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
            return money;
        }
        return money;
    }

    public Double bdHistoricBetween(ArrayList<Parking> contactos, Timestamp iniTime, Timestamp endTime) {
        Double money = 0.0;
        db = dades.getReadableDatabase();
        Cursor curs = null;
        try {
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM THistorial WHERE (exitDay>="+iniTime.getTime()+" and exitDay<="+endTime.getTime()+") or (entryDay>="+iniTime.getTime()+" and entryDay<="+endTime.getTime()+") ORDER BY "+order, s);
        }
        if(curs.moveToFirst()) {
            do {
                money+=curs.getDouble(3);
                contactos.add(new Parking(curs.getString(0), new Timestamp(curs.getLong(1)), new Timestamp(curs.getLong(2)),curs.getDouble(3)));
            } while(curs.moveToNext());
        }
        curs = null;
        if(db != null) {
            String[] s = {};
            curs = db.rawQuery("SELECT * FROM TActual WHERE entryDay>="+iniTime.getTime()+" and entryDay<="+endTime.getTime()+" ORDER BY "+orderP, s);
        }
        if(curs.moveToFirst()) {
            do {
                contactos.add(new Parking(curs.getString(1), new Timestamp(curs.getLong(2)),curs.getInt(0)));
            } while(curs.moveToNext());
        }
        db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
            return money;
        }
        return money;
    }

/** Others*/
    public void changeOrder(){
        if(order.equals(orderDESCH)) {
            order = orderASCH;
            orderP = orderASCP;
        }
        else {
            order= orderDESCH;
            orderP=orderDESCP;
        }
    }

    public void registerCarExit(int num, Parking car) {
        db = dades.getWritableDatabase();
        try{
        if(car!=null) {
            String[] s = {car.getMatricula()};
            db.execSQL("INSERT INTO THistorial (matricula, entryDay, exitDay, pricePayed) VALUES (?,'" + car.getEntryDay().getTime() + "','" + car.getExitDay().getTime() + "','" + car.getPricePayed() + "')", s);
            db.execSQL("DELETE FROM TActual WHERE plot= "+num+" ;");
        }
         db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
        }
    }

    public int exportToCSV(File file) {
        PrintWriter printWriter = null;
        try {
            file.createNewFile();
            printWriter = new PrintWriter(new FileWriter(file));

            db = dades.getReadableDatabase();
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
                    String record = matr + ", " + logAux.format(entryDate) + ", " + logAux.format(exitDate) + ", " + price;
                    printWriter.println(record);
                } while(curCSV.moveToNext());
            }
            curCSV.close();
            db.close();
        }
        catch(Exception exc) {
         //   exc.printStackTrace();
            return exportExcError;
        }
        finally {
            if(printWriter != null) printWriter.close();
        }
        return exportOk;
    }

    public int exportResumToCSV(File file) {
        PrintWriter printWriter = null;
        HashMap<String,SummaryDay> data = new HashMap<>();
        try {
            file.createNewFile();
            printWriter = new PrintWriter(new FileWriter(file));

            db = dades.getReadableDatabase();
            printWriter.println("Any, Mes, Dia, Sortides, Recaptat, mitjaPerCotxe");

            Cursor curCSV = null;
            if(db != null) {
                String[] s = {};
                curCSV = db.rawQuery("SELECT exitDay, pricePayed FROM THistorial ORDER BY exitDay ASC; ", s);
            }
            if(curCSV.moveToFirst()) {
                do {
                    Date exitDate = new java.sql.Date(curCSV.getLong(0));
                    Double price = curCSV.getDouble(1);
                    String aux = export.format(exitDate);

                    if(data.containsKey(aux)) data.get(aux).addExit(price);
                    else {
                        SummaryDay another = new SummaryDay(aux,price);
                        data.put(aux,another);
                    }
                } while(curCSV.moveToNext());
            }
            curCSV.close();
            db.close();
        }
        catch(Exception exc) {
            //   exc.printStackTrace();
            return exportExcError;
        }
        finally {
            for(String i: data.keySet()){
                String d[] = i.split("/");
                String record = d[0] + ", " + d[1] + ", " + d[2] + ", " + data.get(i).getNumExits()+", "+data.get(i).getPrice()+", "+data.get(i).getAverage();
                printWriter.println(record);
            }
            if(printWriter != null) printWriter.close();

        }
        return exportOk;
    }
}
