package com.example.cristinafontanet.parkingidi;

import java.sql.Timestamp;

/*
 * Created by CristinaFontanet on 25/11/2015.
 */
public class Parking {
    String matricula;
    Timestamp endryDay;
    Timestamp exitDay;
    int plot;
    double pricePayed;

    Parking(String mat, Timestamp entrada, int plotUsed) {
        matricula = mat;
        endryDay = entrada;
        pricePayed = 0.0;
        plot = plotUsed;
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

    public int getPlot(){return plot;}

    public void setExitDay(Timestamp one) {exitDay = one;}

    public void setPricePayed(Double one) {pricePayed = one;}

    public double getPricePayed() {
        return pricePayed;
    }

    public void setPlot(int plot) {
        this.plot = plot;
    }
}
