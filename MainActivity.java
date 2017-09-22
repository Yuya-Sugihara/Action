package com.example.sugiharayuya.Action;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GameView.GameOverCallback{

    private GameView gameView;

    public void onGameOver(){
        Toast.makeText(this,"GAME OVER",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView =new GameView(this);
        gameView.setCallback(this);
        setContentView(gameView);
    }
}
