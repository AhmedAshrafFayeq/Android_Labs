package com.example.josn_with_image;

import android.graphics.Bitmap;

public class CountryModel {
    int rank;
    private String country,pop,countryImage;
    //private Bitmap countryImage;


    public String getCountryImage() {
        return countryImage;
    }

    public void setCountryImage(String countryImage) {
        this.countryImage = countryImage;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public int getRank() {
        return rank;
    }

    public String getCountry() {
        return country;
    }

    public String getPop() {
        return pop;
    }
}
