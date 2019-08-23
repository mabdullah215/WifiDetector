package com.wifi.detector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    String initialText="Searching";
    int numDots=10;
    int counter=0;
    boolean enabled=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if(numDots>10)
                {
                    enabled=true;
                    numDots=1;
                    initialText="Searching";
                    counter++;

                    if(counter>1)
                    {
                        timer.cancel();
                        finish();
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }
                }
               numDots++;
               initialText=initialText+".";
               setText(initialText);



            }
        }, 0, 500);//put here time 1000 milliseconds=1 second




    }


    private void setText(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.tv_loading)).setText(text);
            }
        });
    }


}
