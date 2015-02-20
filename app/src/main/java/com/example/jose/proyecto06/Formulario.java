package com.example.jose.proyecto06;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Formulario extends Activity {

    private static String TAG = "formulario";

    private TextView labelId;
    private EditText nombre;
    private EditText descripcion;
    private CheckBox megusta;
    private Uri uriFoto;
    private Button btnFotoNueva;
    private Button btnFotoVer;
    private ImageView foto;

    //Identificador de la base de datos
    private DataBaseHelper mDbHelper;
    private SQLiteDatabase db;
    private long id;

    //
    // Modo del formulario
    //
    private int modo;

    //Botones
    private Button boton_guardar;
    private Button boton_cancelar;

    //Tipos definidos para el multimedia
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Log.i(TAG, "Entra en onCreate");
        //Capturamos los datos enviados
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        // Consultamos la base de datos
        mDbHelper = new DataBaseHelper(this);
        db = mDbHelper.getWritableDatabase();

        //
        // Obtenemos los elementos de la vista
        //
        labelId = (TextView) findViewById(R.id.label_id);
        nombre = (EditText) findViewById(R.id.nombre);
        descripcion = (EditText) findViewById(R.id.descripcion);
        megusta = (CheckBox) findViewById(R.id.checkBox_megusta);
        foto = (ImageView) findViewById(R.id.imageViewFoto);
        btnFotoNueva = (Button) findViewById(R.id.boton_nueva);
        btnFotoVer = (Button) findViewById(R.id.boton_ver);

        //
        // Obtenemos el identificador del registro si viene indicado
        //
        if (extra.containsKey(DataBaseHelper.ID)) {
            id = extra.getLong(DataBaseHelper.ID);
            consultar(id);
        }
        //Botones de guardado y cancelar
        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        //
        // Establecemos el modo del formulario
        //
        establecerModo(extra.getInt(mDbHelper.C_MODO));

        //
        // Definimos las acciones para los dos botones
        //
        boton_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        btnFotoNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                uriFoto = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                camara.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto); // set the image file name
                // start the image capture Intent
                startActivityForResult(camara, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                //Colocar la foto
                foto.setImageURI(uriFoto);
            }
        });

        btnFotoVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(new File(uriFoto.getPath())), "image/jpg");
                startActivity(i);
            }
        });
    }

    private void consultar(long id) {
        Log.i(TAG, "Entra en consultar");
        //
        // Consultamos el centro por el identificador
        //
        Cursor cursor = mDbHelper.getRegistro(id);
        Log.i(TAG, "Crea el cursor");
        labelId.setText("Id: " + cursor.getString(cursor.getColumnIndex(DataBaseHelper.ID)));
        Log.i(TAG, "Genera el label ID");
        nombre.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TITULO)));
        Log.i(TAG, "Genera el label titulo");

        uriFoto = Uri.parse(cursor.getString(cursor.getColumnIndex(DataBaseHelper.FOTO)));

        Log.i(TAG, "Generada la Uri a partir del registro de la BBDD");
        Log.i(TAG, uriFoto.getPath());

        /**
         BitmapFactory bmf = new BitmapFactory();
         Bitmap bm = bmf.decodeFile(uriFoto.getPath().toString());
         Log.i(TAG, "creado el bitmap a partir utilizando la uri");
         foto.setImageBitmap(bm);
         */

        ((BitmapDrawable)foto.getDrawable()).getBitmap().recycle();
        foto.setImageURI(uriFoto); //ESTA LINEA PETA LA SEGUNDA VEZ QUE SE EJECUTA
        Log.i(TAG, "foto asignada al ImageView");
        descripcion.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DESCRIPCION)));

        int mola = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.AMOR_ODIO)));

        if (mola == 1) {
            megusta.setChecked(true);
        } else {
            megusta.setChecked(false);
        }

        /**
         try {
         }catch(OutOfMemoryError e){
         Toast.makeText(Formulario.this, "Error de memoria",
         Toast.LENGTH_LONG).show();
         db.close();
         mDbHelper.close();
         cursor.close();
         }
         */


    }

    private void establecerModo(int m) {
        this.modo = m;

        if (modo == mDbHelper.C_VISUALIZAR) {
            //deshabilitar los elementos que no serán utilizables
            this.nombre.setEnabled(false);
            this.descripcion.setEnabled(false);
            this.boton_guardar.setEnabled(false);
            this.btnFotoNueva.setEnabled(false);
            this.megusta.setEnabled(false);
        } else if ((modo == mDbHelper.C_CREAR) || (modo == mDbHelper.C_EDITAR)) {
            //habilitar los elementos
            this.setTitle(R.string.crear_titulo);
            this.nombre.setEnabled(true);
            this.boton_guardar.setEnabled(true);
            this.descripcion.setEnabled(true);
            this.btnFotoNueva.setEnabled(true);
            this.megusta.setEnabled(true);
        }
    }

    private void guardar() {
        //
        // Obtenemos los datos del formulario
        //
        ContentValues reg = new ContentValues();
        if (modo == mDbHelper.C_EDITAR) {
            reg.put(mDbHelper.ID, id);
            reg.put(mDbHelper.FOTO, uriFoto.getPath());
            reg.put(mDbHelper.TITULO, nombre.getText().toString());
            reg.put(mDbHelper.DESCRIPCION, descripcion.getText().toString());
            if (megusta.isChecked()) {
                reg.put(mDbHelper.AMOR_ODIO, "1");
            } else {
                reg.put(mDbHelper.AMOR_ODIO, "0");
            }
        }

        if (modo == mDbHelper.C_CREAR) {
            ContentValues values = new ContentValues();

            values.put(DataBaseHelper.TITULO, nombre.getText().toString());
            values.put(DataBaseHelper.FOTO, uriFoto.getPath());
            values.put(DataBaseHelper.DESCRIPCION, descripcion.getText().toString());
            if (megusta.isChecked()) {
                values.put(DataBaseHelper.AMOR_ODIO, "1");
            } else {
                values.put(DataBaseHelper.AMOR_ODIO, "0");
            }


            db.insert(DataBaseHelper.TABLE_NAME, null, values);
            values.clear();

            mDbHelper.insert(reg);

            Toast.makeText(Formulario.this, "Se ha añadido un nuevo registro con una foto alojada en " + uriFoto.getPath(),
                    Toast.LENGTH_SHORT).show();
        } else if (modo == mDbHelper.C_EDITAR) {
            Toast.makeText(Formulario.this, "Modificación realizada con éxito. Foto alojada en " + uriFoto.getPath(),
                    Toast.LENGTH_SHORT).show();
            mDbHelper.update(reg);
        }

        //
        // Devolvemos el control
        //
        setResult(RESULT_OK);
        finish();
    }

    private void cancelar() {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
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

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    protected void onPause() {
        //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
        setResult(RESULT_OK);


    }

    /**
     @Override protected void onRestart() {
     super.onResume();
     Toast.makeText(this, "He vuelto", Toast.LENGTH_SHORT).show();
     foto.setImageURI(uriFoto);
     mDbHelper.close();
     db.close();
     }
     */


}
