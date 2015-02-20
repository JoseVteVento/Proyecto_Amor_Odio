package com.example.jose.proyecto06;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Datos de la tabla
    final private static String NAME = "amor_odio_db";
    final static String TABLE_NAME = "amor_odio";

    final static String ID = "_id";
    final static String TITULO = "titulo";
    final static String FOTO = "foto";
    final static String DESCRIPCION = "descripcion";
    final static String AMOR_ODIO = "amor_odio"; // 1 para amor y 0 para odio

    //Comandos
    final static String[] columns = { ID, TITULO, FOTO, DESCRIPCION, AMOR_ODIO };

    //Crear la sentencia SQL apra crear la tabla
    //no permite campos vacios
    final private static String CREATE_CMD =
            "CREATE TABLE "+ TABLE_NAME +" (" + ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TITULO + " TEXT NOT NULL, "
                    + FOTO + " TEXT, "
                    + DESCRIPCION + " TEXT, "
                    + AMOR_ODIO + " TEXT NOT NULL)";


    final private static Integer VERSION = 1;
    final private Context mContext;


    //Modos edicion
    public static final String C_MODO  = "modo" ;
    public static final int C_VISUALIZAR = 551 ;
    public static final int C_CREAR = 552 ;
    public static final int C_EDITAR = 553 ;

    //Constructor
    public DataBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    //Creación de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos la base de datos
        Log.i(this.getClass().toString(), "Tabla AMOR_ODIO creada");
        db.execSQL(CREATE_CMD);

        /**
        //La rellenamos
        ContentValues values = new ContentValues();


        values.put(DataBaseHelper.TITULO, "Pino ");
        values.put(DataBaseHelper.FOTO, "tal");
        values.put(DataBaseHelper.DESCRIPCION, "Pino centenario dentro del colegio del Saler");
        values.put(DataBaseHelper.AMOR_ODIO, "1");
        db.insert(DataBaseHelper.TABLE_NAME, null, values);
        values.clear();

        values.put(DataBaseHelper.TITULO, "Abeto");
        values.put(DataBaseHelper.FOTO, "talytal");
        values.put(DataBaseHelper.DESCRIPCION, "Abeto de los Alpes");
        values.put(DataBaseHelper.AMOR_ODIO, "1");
        db.insert(DataBaseHelper.TABLE_NAME, null, values);
        values.clear();

        values.put(DataBaseHelper.TITULO, "Ortiga");
        values.put(DataBaseHelper.FOTO, "talytalytal");
        values.put(DataBaseHelper.DESCRIPCION, "Ortiga super tóxica");
        values.put(DataBaseHelper.AMOR_ODIO, "0");
        db.insert(DataBaseHelper.TABLE_NAME, null, values);
        values.clear();
        */

    }

    //Actualización de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }

    //Borrando de la base de datos
    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }

    //Lectura de la base de datos
    public  Cursor readRegistro(SQLiteDatabase db) {
        return db.query(TABLE_NAME,
                columns, null, new String[] {}, null, null,
                null);
    }

    /**
     * Devuelve cursor con todos las columnas del registro
     */
    public Cursor getRegistro(long id) throws SQLException
    {
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor c = db.query( true, TABLE_NAME, columns, ID + "=" + id, null, null, null, null, null);

        //Nos movemos al primer registro de la consulta
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /**
     * Inserta los valores en un registro de la tabla
     */
    public long insert(ContentValues reg)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, reg);
    }
    /**
     * Inserta los valores en un registro de la tabla
     */
    public long update(ContentValues reg)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        if (reg.containsKey(ID))
        {
            //
            // Obtenemos el id y lo borramos de los valores
            //
            long id = reg.getAsLong(ID);

            reg.remove(ID);

            //
            // Actualizamos el registro con el identificador que hemos extraido
            //
            return db.update(TABLE_NAME, reg, "_id=" + id, null);
        }
        //return db.insert(TABLE_NAME, null, reg);
        return db.update(TABLE_NAME, reg, null, null);

    }
}
