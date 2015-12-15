package com.example.cristinafontanet.parkingidi;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Cristina on 25/11/2015.
 */
public class Parking {
    String matricula;
    Timestamp endryDay;
    Timestamp exitDay;

    Parking(String mat, Timestamp entrada) {
            matricula = mat;
        endryDay = entrada;
    }

    public String getMatricula() { return matricula; }
    public Timestamp getEntryDay() { return endryDay; }
   // public Time getEntryHour(){return entryHour;}
    public Timestamp getExitHour() {return exitDay;}
    public void setExitHour(Timestamp one) {exitDay = one;}

    public boolean isNull() {
        return this.equals(null);
    }

}
