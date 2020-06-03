package com.example.android.xo;

public class Rank {
    private int r;
    private String name;

    public Rank(String name,int r){
        this.r=r;
        this.name=name;
    }

    public int getR() {
        return r;
    }

    public void incR(){
        this.r++;
    }

    public void setR(int r) {
        this.r = r;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
