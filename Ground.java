package com.example.sugiharayuya.Action;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ground {

    private int COLOR= Color.rgb(180,180,180);

    final Rect rect;
    private Paint paint=new Paint();

    public Ground(int left,int top,int right,int bottom){

        rect=new Rect(left,top,right,bottom);
        paint.setColor(COLOR);
    }

    void draw(Canvas canvas){

        canvas.drawRect(rect,paint);
    }

    void move(int moveToLeft){
        rect.offset(-moveToLeft,0);
    }

    public boolean isShown(int width,int height){
        return rect.intersects(0,0,width,height);       //地面が重なっているか判定
    }

    public boolean isAvailable(){       //地面を表示させるか判定

        return rect.right>0;
    }

    public boolean isSolid(){
        return true;
    }

}
