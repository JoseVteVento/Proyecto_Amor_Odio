package com.example.jose.proyecto06;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {

    private static String TAG = "main_activity";

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private SharedPreferences pref = null;
    private boolean audio_actual;

    private DataBaseHelper mDbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter mAdapter;
    private Cursor c;

    public static final String C_MODO = "modo";
    public static final int C_VISUALIZAR = 551;
    public static final int C_CREAR = 552;
    public static final int C_EDITAR = 553;

    //se hace public para que se pueda acceder desde cualquier parte de la app
    public static final String SETTINGS = "on_off";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creamos una nueva DataBase
        mDbHelper = new DataBaseHelper(this);
        db = mDbHelper.getWritableDatabase();

        //cohesionar elementos interfaz/logica
        ImageButton bt_corazon = (ImageButton) findViewById(R.id.imageButton);
        ImageButton bt_rayo = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton bt_settings = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton bt_on_off = (ImageButton) findViewById(R.id.imageButton4);

        //capturar el registro de preferencias
        pref = getSharedPreferences(SETTINGS, 0);
        //asigna el valor de la preferencia con llave audio
        audio_actual = pref.getBoolean("audio", true);

        //capturar el servivio para gestionar el sonido
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.cancion);//carga la cancion
        if (audio_actual) {
            mediaPlayer.start();//la inicia
        } else {
            if (mediaPlayer.isPlaying()) {//por si esta sonando y no debiera
                mediaPlayer.stop();
            }
        }


        //implementar el boton settings
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //crear el intent que lanzará el activity settings
                Intent i_settings = new Intent(MainActivity.this, settings.class);
                startActivity(i_settings);

            }
        });

        //implementar el boton amor
        bt_corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crear el intent y lanzarlo
                Intent i_amor = new Intent(MainActivity.this, Amor.class);
                startActivity(i_amor);
                Log.i(TAG, "lanzado amor");


            }
        });

        //implmentar el boton de salir
        bt_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();

                db.close();
                mDbHelper.close();

                finish();
            }
        });

        //implementar el boton rayo
        bt_rayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent odio = new Intent(MainActivity.this, Odio.class);
                startActivity(odio);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        //capturar el registro de preferencias
        pref = getSharedPreferences(SETTINGS, 0);
        //asigna el valor de la preferencia con llave audio
        audio_actual = pref.getBoolean("audio", true);

        if (audio_actual) {

            if (!mediaPlayer.isPlaying()) {//si el boton esta en on y no esta sonando...
                mediaPlayer = MediaPlayer.create(this, R.raw.cancion);//carga la cancion de nuevo
                mediaPlayer.start();//la inicia
            }
        } else {
            if (mediaPlayer.isPlaying()) {//si el boton esta en off y esta sonando...
                mediaPlayer.stop();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
       //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        /** if (mediaPlayer.isPlaying()){//si el boton esta en off y esta sonando...
         mediaPlayer.stop();
         }
         */
    }



    // Close database
    @Override
    protected void onDestroy() {

        //mDbHelper.deleteDatabase();
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {//si el boton esta en off y esta sonando...
            mediaPlayer.stop();
        }
    }
}
