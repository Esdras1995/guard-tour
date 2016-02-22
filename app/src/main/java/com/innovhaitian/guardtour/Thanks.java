package com.innovhaitian.guardtour;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CountDownLatch;

public class Thanks extends AppCompatActivity {

    TextView seconde;
    LocalTimeStore localTimeStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        seconde = (TextView)findViewById(R.id.second);
        localTimeStore = new LocalTimeStore(this);
        Log.i("essais", "Current : "+ System.currentTimeMillis()+"\nStored : "+localTimeStore.getStoreTime()+"\nDifference : "+(System.currentTimeMillis() - localTimeStore.getStoreTime()));

        new CountDownTimer(localTimeStore.getRemainingTime() - (System.currentTimeMillis() - localTimeStore.getStoreTime()), 1000){
            public void onTick(long millisUntillFinish){
                seconde.setText("00:00:" + millisUntillFinish/1000);
            }

            public void onFinish(){
                seconde.setText("Fait");
                startActivity(new Intent(Thanks.this, MainActivity.class));
                localTimeStore.clearTimeStored();
                localTimeStore.setTimeStored(false);
                finish();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
