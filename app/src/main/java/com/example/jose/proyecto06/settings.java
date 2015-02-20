package com.example.jose.proyecto06;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class settings extends Activity {

    //se hace public para que se pueda acceder desde cualquier parte de la app
    public static final String SETTINGS = "on_off";

    private SharedPreferences pref;
    private boolean audio_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //cohesionar el togglebutton
        final ToggleButton t_boton = (ToggleButton) findViewById(R.id.toggleButton);

        //capturar el registro de preferencias
        pref = getSharedPreferences(SETTINGS, 0);

        //asigna el valor de la preferencia con llave audio
        audio_actual = pref.getBoolean("audio",true );

        //pone el estado del togglebutton segun la preferencia guardada
        t_boton.setChecked(audio_actual);




        t_boton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor pe  = pref.edit();

                //guarda los valores según el estado del togglebutton
                if (t_boton.isChecked()){
                    audio_actual = true;
                    pe.putBoolean("audio",audio_actual);

                }else {
                    audio_actual = false;
                    pe.putBoolean("audio",audio_actual);

                }

                pe.commit();

                //Aqui debe parar o poner en marcha la musica
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
}
