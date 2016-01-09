package com.example.cristinafontanet.parkingidi;

/*
 * Created by Cristina on 09/01/2016.
 */
public class SummaryDay {
    private String date;
    private int numExits;
    private double price;

    SummaryDay(String date, double price) {
        this.date = date;
        numExits = 1;
        this.price = price;
    }

    public void addExit(double pri) {
        ++numExits;
        price+=pri;
    }

    public double getPrice() {return price;}

    public String getDate() {return date;}

    public int getNumExits() {return numExits; }

    public double getAverage(){return Math.rint((price/numExits)*100)/100;}

}
