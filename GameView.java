package com.example.sugiharayuya.Action;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Handler;


public class GameView extends View {      //地面に着地させること

    private Droid droid;
    private Ground lastGround;
    private int timecount=0;

    private final int GROUND_HEIGHT=50;
    private int MOVE_TO_LEFT=3;       //ステージの移動速度
    private static final int ADD_GROUND_COUNT=5;
    private static final int GROUND_WIDTH=340;
    private static final int GROUND_BLOCK_HEIGHT=100;
    private final List<Ground> groundList=new ArrayList<>();
    private final Random rand=new Random(System.currentTimeMillis());


    private final Droid.Callback droidCallback=new Droid.Callback(){
        public int getDistanceFromGround(Droid droid){

            int width=getWidth();
            int height=getHeight();
            for(Ground ground:groundList){
                if(!ground.isShown(width,height)){  //地面が重なっていないか判定
                    continue;
                }
                boolean horizontal=!(droid.rect.left>=ground.rect.right||droid.rect.right<=ground.rect.left);
                if(horizontal){
                    if(!lastGround.isSolid()){
                        return Integer.MAX_VALUE;
                    }
                    int distanceFromGround=ground.rect.top-droid.rect.bottom;
                    if(distanceFromGround<0){
                        gameOver();
                        return Integer.MAX_VALUE;
                    }
                    return distanceFromGround;
                }
            }

            return Integer.MAX_VALUE;       //地面の上にいない場合、最大値を返す

        }

    };

    private final Handler handler=new Handler();

    public interface GameOverCallback{
        void onGameOver();
    }

    public GameOverCallback gameOverCallback;

    public void setCallback(GameOverCallback callback){
        gameOverCallback=callback;
    }

    private final AtomicBoolean isGameOver=new AtomicBoolean();

    private void gameOver(){  //GameOverの時の処理
        if(isGameOver.get()){       //isGameOverがtrueの時は何もしない
            return;
        }
        isGameOver.set(true);       //isGameOverをtrueにする
        droid.stop();

        handler.post(new Runnable(){
            public void run(){

                gameOverCallback.onGameOver();
            }
        });
    }




    public GameView(Context context){

        super(context);

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int width=canvas.getWidth();
        int height=canvas.getHeight();


        if(droid==null){    //ドロイドが存在しない場合
            Bitmap droidBitmap= BitmapFactory.decodeResource(getResources(),com.example.sugiharayuya.Action.drawable.droid2);
            droid=new Droid(droidBitmap,width,height,droidCallback);
        }

        if(lastGround==null){
            int top=height-GROUND_HEIGHT;
            lastGround=new Ground(0,top,width,height);
            groundList.add(lastGround);
        }

        if(lastGround.isShown(width,height)){
            for(int i=0;i<ADD_GROUND_COUNT;i++){
                int left=lastGround.rect.right;
                int groundHeight=rand.nextInt(height/GROUND_BLOCK_HEIGHT)*GROUND_BLOCK_HEIGHT/2+GROUND_HEIGHT;
                //nextIntは次のint型の乱数を生成する
                int top=height-groundHeight;
                int right=left+GROUND_WIDTH;

                if(i%2==0){
                    lastGround=new Ground(left,top,right,height);       //新しい地面を生成
                }else{
                    lastGround=new Blank(left,height,right,height);
                }

                groundList.add(lastGround);
            }
        }

        for(int i=0;i<groundList.size();i++){
            Ground ground=groundList.get(i);

            if(ground.isAvailable()) {
                ground.move(MOVE_TO_LEFT);
                if (ground.isShown(width,height)) {
                    ground.draw(canvas);
                }
            }else{
                groundList.remove(ground);
                i--;
            }
        }

        droid.move(canvas);   //ドロイドが移動する
        droid.draw(canvas);


        timecount++;        //時間がたつにつれ地面の移動速度が増加
        if(timecount%50==0){
            MOVE_TO_LEFT++;
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                jumpDroid(event.getY());

        }

        return super.onTouchEvent(event);
    }

    private void jumpDroid(float height){
        if(droidCallback.getDistanceFromGround(droid)>0){
            return;
        }
        droid.jump(height);

    }

}
