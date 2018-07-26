package worldskills.emparejaappn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegistroBBDD extends SQLiteOpenHelper {

    private static final String NOMBRE_BBDD="registro.db";
    private static final int VERSION=1;
    private static final String TABLA_NOMBRE="CREATE TABLE REGISTROS(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
    "NOMBRE TEXT, PUNTAJE INTEGER, TIEMPO INTEGER, DIFICULTAD TEXT, MODO TEXT)";

    public RegistroBBDD(Context context){
        super(context,NOMBRE_BBDD,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA_NOMBRE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CREATE"+TABLA_NOMBRE);
        sqLiteDatabase.execSQL(TABLA_NOMBRE);
    }
    public void guardarDatos(String nom, int pun, int time, String dificultad, String modo){

        SQLiteDatabase db=getWritableDatabase();

        ContentValues valores=new ContentValues();

        valores.put("NOMBRE",nom);
        valores.put("PUNTAJE",pun);
        valores.put("TIEMPO",time);
        valores.put("DIFICULTAD",dificultad);
        valores.put("MODO",modo);

        db.insert("REGISTROS",null,valores);

    }
    public Cursor cargarDatos(String dificultad,String modo){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;

        try{
            String[] column={"NOMBRE","PUNTAJE","TIEMPO"};
            String selection="DIFICULTAD" + " =? " + " AND " + "MODO" +  " =? ";
            String[] selecArgs={dificultad,modo};
            String orderBy="PUNTAJE DESC";
            String limit="4";

            cursor=db.query("REGISTROS",column,selection,selecArgs,null,null,orderBy,limit);

        }catch (Exception e){}

        return cursor;
    }
}
