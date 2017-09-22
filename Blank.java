package com.example.sugiharayuya.Action;


import android.graphics.Canvas;

public class Blank extends Ground {

    public Blank(int left,int top,int right,int bottom){
        super(left,top,right,bottom);
    }

    public void draw(Canvas canvas){

    }

    public boolean isSolid(){
        return false;
    }
}
