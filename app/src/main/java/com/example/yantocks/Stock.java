package com.example.yantocks;

public class Stock {
    private int id;
    private String shortName;
    private String fullName;
    private String country;
    private String sector;
    private String currency;
    private double close;
    private double high;
    private double low;
    private String changeDayFlat;
    private String changeDayPercent;
    private String timeUpdate;
    private int favourite;

    public Stock(int id, String shortName, String fullName, String country, String sector,
                 String currency, double close, double high, double low, String changeDayFlat,
                 String changeDayPercent, String timeUpdate, int favourite) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.country = country;
        this.sector = sector;
        this.currency = currency;
        this.close = close;
        this.high = high;
        this.low = low;
        this.changeDayFlat = changeDayFlat;
        this.changeDayPercent = changeDayPercent;
        this.timeUpdate = timeUpdate;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCountry() {
        return country;
    }

    public String getSector() {
        return sector;
    }

    public String getCurrency() {
        return currency;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public String getChangeDayFlat() {
        return changeDayFlat;
    }

    public String getChangeDayPercent() {
        return changeDayPercent;
    }

    public String getTimeUpdate() {
        return timeUpdate;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}

    /*
  "id": 85,
          "name": "Advanced Micro Devices Inc",
          "short_name": "AMD",
          "country": "United-states",
          "sector": "Technology",  // Stock Sector
          "exch": "Nasdaq",  // Exchange name
          "ccy": "USD"  // Stock currency symbol
          "id": "15",   // Stock ID
          "s": "AAPL",   // Symbol (Apple)
          "c": "188.89", // Close / Current Price
          "h": "190.38", // High
          "l": "183.66", // Low
          "ch": "+5.61", // Change in 1 Day
          "cp: "+3.06%", // Change in percentage in 1 Day
          "cty: "united-states", // Country name
            "exch: "Nasdaq", // Stock exchange name
            "ccy: "USD", // Currency symbol
            "t: "1599249599", // time in unix
            "tm": "2019-10-15 12:30:00" // When price changed last time

*/