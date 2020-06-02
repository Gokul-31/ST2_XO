package com.example.android.xo;

public class Symbol {
    private boolean marked;
    private int mark;  //0 for x and 1 for O

    public Symbol(){
        this.marked=false;
        this.mark=-1;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
