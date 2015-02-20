package com.example.jose.proyecto06;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Odio extends ListActivity {


    private static String TAG ="odio";

    private DataBaseHelper mDbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter mAdapter;
    private Cursor c;

    public static final String C_MODO  = "modo" ;
    public static final int C_VISUALIZAR = 551 ;
    public static final int C_CREAR = 552 ;
    public static final int C_EDITAR = 553 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odio);



        // Consultamos la base de datos
        mDbHelper = new DataBaseHelper(this);
        db = mDbHelper.getWritableDatabase();


        Log.i(TAG,"consulta bbdd realizada");

        //Leemos la base de datos y mostramos la informacion

        //Cursor c = mDbHelper.readRegistro(db);
        //Cursor c = db.rawQuery("SELECT * FROM amor_odio WHERE amor_odio = '0'", new String[]{});
        Cursor c = db.query( true, mDbHelper.TABLE_NAME, mDbHelper.columns, mDbHelper.AMOR_ODIO + "=0" ,null , null, null, null, null);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, c,
                DataBaseHelper.columns, new int[] { R.id._id, R.id.titulo },
                0);
        Log.i(TAG,"creado el adaptador");
        setListAdapter(mAdapter);
        Log.i(TAG,"lanzado el adaptador");

        Button boton = (Button)findViewById(R.id.button_odio);

        //lanzar el formulario en modo crear
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Odio.this, Formulario.class);

                i.putExtra(C_MODO, C_CREAR);
                startActivityForResult(i, C_CREAR);
            }
        });
    }



    public void editHandler(View v) {
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        TextView id =(TextView) vwParentRow.findViewById(R.id._id);
        Intent i = new Intent(Odio.this, Formulario.class);

        i.putExtra(C_MODO, C_EDITAR);
        i.putExtra(mDbHelper.ID, Long.valueOf((String)id.getText()));


        this.startActivityForResult(i, C_EDITAR);
    }

    public void viewHandler(View v) {
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        TextView id =(TextView) vwParentRow.findViewById(R.id._id);
        Intent i = new Intent(Odio.this, Formulario.class);

        i.putExtra(C_MODO, C_VISUALIZAR);
        i.putExtra(mDbHelper.ID, Long.valueOf((String)id.getText()));


        this.startActivityForResult(i, C_VISUALIZAR);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_amor, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK) {
            // Consultamos la base de datos
            mDbHelper = new DataBaseHelper(this);
            db = mDbHelper.getWritableDatabase();
            Cursor c = db.query( true, mDbHelper.TABLE_NAME, mDbHelper.columns, mDbHelper.AMOR_ODIO + "=0" ,null , null, null, null, null);
            //Cursor c = db.rawQuery ("SELECT * FROM amor_odio WHERE amor_odio = '0'", new String[]{});

            mAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, c,
                    DataBaseHelper.columns, new int[] { R.id._id, R.id.titulo },
                    0);
            setListAdapter(mAdapter);
            mAdapter.changeCursor(c);
            mAdapter.notifyDataSetChanged();
        }

    }
}
