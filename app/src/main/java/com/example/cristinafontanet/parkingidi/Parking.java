package com.example.cristinafontanet.parkingidi;

import java.sql.Timestamp;

/*
 * Created by CristinaFontanet on 25/11/2015.
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

    public Timestamp getExitDay() {return exitDay;}

    public void setExitDay(Timestamp one) {exitDay = one;}

}
