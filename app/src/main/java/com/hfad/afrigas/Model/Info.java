package com.hfad.afrigas.Model;

public class Info {
    private String Price, ShortInfo, LongInfo, Ad;
    public Info() {

    }
    public Info(String Price, String ShortInfo, String LongInfo, String Ad) {
            this.Price = Price;
            this.ShortInfo = ShortInfo;
            this.LongInfo = LongInfo;
            this.Ad = Ad;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getShortInfo() {
        return ShortInfo;
    }

    public void setShortInfo(String ShortInfo) {
        ShortInfo = ShortInfo;
    }

    public String getLongInfo() {
        return LongInfo;
    }

    public void setLongInfo(String LongInfo) {
        LongInfo = LongInfo;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }
}
