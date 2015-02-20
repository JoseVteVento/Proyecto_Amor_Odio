package com.example.jose.proyecto06;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;


public class Intro extends Activity {

    private static final long RETARDO = 5000; //constante que usaré para el tiempo de espera
    private boolean control = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //establece la posicion de la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Para no mostrar el titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_intro);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                //crear el intent
                Intent i = new Intent().setClass(Intro.this, MainActivity.class);
                //lanzar el intent
                startActivity(i);
            }
        };

        //crear el obj Timer que lanzará el TimerTask
        Timer t = new Timer();
        //parametriza el temporizador con el timertask y el tiempo de espera
        t.schedule(tt, RETARDO);


        final ProgressBar sp = (ProgressBar)findViewById(R.id.spinner);
        sp.setVisibility(View.VISIBLE);//pone al spinner en marcha

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (control){//inicialmente estará en false
            finish();
        }
        control = true; //cuando vuelva a esta clase desde el mainactivity, en el
        //if finalizará
    }
}
