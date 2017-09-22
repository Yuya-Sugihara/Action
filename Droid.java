package com.example.sugiharayuya.Action;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Droid{

    public interface Callback{
        int getDistanceFromGround(Droid droid);

    }
    private float velocity=0;
    private Bitmap droidBitmap;
    private final Callback callback;
    Rect rect;
    private Paint paint=new Paint();
    private static final float GRAVITY=0.8f;
    private static final float WEIGHT=GRAVITY*50;




    public Droid(Bitmap bitmap,int width,int height,Callback callback){
        droidBitmap=bitmap;
        int left=180;
        int top=(height-bitmap.getHeight())/2;
        int right=left+bitmap.getWidth();
        int bottom=top+bitmap.getHeight();
        rect=new Rect(left,top,right,bottom);  //画像と四角形は同じ場所にある
        this.callback=callback;


    }

    public void jump(float power){

        velocity=power/25;
    }

    public void move(Canvas canvas){
        int distanceFromGround=callback.getDistanceFromGround(this);
       // Log.d("数値",Integer.toString(distanceFromGround));

        if(velocity<0&&velocity<-distanceFromGround){
            velocity=-distanceFromGround;
        }
        rect.offset(0,Math.round(-1*velocity));
        if(distanceFromGround==0){
            return;
        }else if(distanceFromGround<0) {
            rect.offset(0, distanceFromGround);
        }else if(distanceFromGround>canvas.getHeight()-60){
            
        }

        velocity-=GRAVITY;

    }


    public void draw(Canvas canvas){
        canvas.drawBitmap(droidBitmap,rect.left,rect.top,paint);
        canvas.drawRect(rect,paint);
    }

    public void stop(){         //ドロイドを停止させる
        velocity=0;
    }
}
