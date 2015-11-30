package com.example.cristinafontanet.parkingidi;

import java.sql.Time;
import java.sql.Date;

/**
 * Created by Cristina on 25/11/2015.
 */
public class Parking {
    String matricula;
    Date dia;
    Time entryHour;
    Time exitHour;

    Parking(String mat, Date entrada, Time entHour) {
            matricula = mat;
            dia = entrada;
            entryHour = entHour;
    }

    public String getMatricula() { return matricula; }
    public Date getDay() { return dia; }
    public Time getEntryHour(){return entryHour;}
    public Time getExitHour() {return exitHour;}
    public void setExitHour(Time one) {exitHour = one;}

    public boolean isNull() {
        return this.equals(null);
    }

}
