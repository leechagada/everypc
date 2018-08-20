package com.example.dana.everypc;

public class Getter {

    private int min_price;
    private int avr_price;
    private String prod_name;
    private String maker;
    private String danawaUrl;  //int? long으로 해주면 되나???

    //사용자가 입력할일은 없으니 set은 필요가 없고 get만 필요한듯 하다.

    public String getProd_name() {
        return prod_name;
    }

    public String getMaker() {
        return maker;
    }

    public int getMin_price() {
        return min_price;
    }

    public int getAvr_price() {
        return avr_price;
    }

    public String getDanawaUrl() {
        return danawaUrl;
    }

}