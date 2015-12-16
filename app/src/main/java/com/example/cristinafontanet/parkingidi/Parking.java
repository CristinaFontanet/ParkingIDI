package com.example.cristinafontanet.parkingidi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public class Parking {
    String matricula;
    Timestamp endryDay;
    Timestamp exitDay;
    double pricePayed;

    Parking(String mat, Timestamp entrada) {
        matricula = mat;
        endryDay = entrada;
        pricePayed = 0.0;
    }

    Parking(String mat, Timestamp entrada,Timestamp sortida, double preu) {
        matricula = mat;
        endryDay = entrada;
        exitDay = sortida;
        pricePayed = preu;
    }

    public String getMatricula() { return matricula; }

    public Timestamp getEntryDay() { return endryDay; }

    public Timestamp getExitDay() {return exitDay;}

    public void setExitDay(Timestamp one) {exitDay = one;}

    public void setPricePayed(Double one) {pricePayed = one;}

    public double getPricePayed() {
        return pricePayed;
    }
}
