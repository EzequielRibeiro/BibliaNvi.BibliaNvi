package com.projeto.biblianvi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapterFavoritoNota {

    static final String KEY_ID = "id";
    static final String KEY_FAVORITE_VERSICULO = "versiculo";
    static final String KEY_FAVORITE_CAPITULO = "capitulo";
    static final String KEY_FAVORITE_TEXTO = "texto";
    static final String KEY_FAVORITE_VERSAO = "versao";
    static final String KEY_FAVORITE_BOOKNAME = "bookname";
    static final String KEY_NOTA_TITULO = "titulo";
    static final String KEY_NOTA_DATA = "data";
    static final String KEY_NOTA_TEXTO = "texto";

    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "bibliaAdonai";
    static final String DATABASE_TABLE_FAVORITE = "favorito";
    static final String DATABASE_TABLE_NOTA = "nota";

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE_FAVORITE = "CREATE TABLE IF NOT EXISTS favorito(" +
            "id integer primary key autoincrement," +
            "capitulo text ," +
            "versiculo text ," +
            "texto text not null unique ON CONFLICT ABORT," +
            "versao text," +
            "bookname text);";
    static final String DATABASE_CREATE_NOTA = "CREATE TABLE IF NOT EXISTS nota(" +
            "id integer primary key autoincrement," +
            "titulo text," +
            "data text," +
            "texto text);";


    final Context context;
    DataBaseHelper DBHelper;
    SQLiteDatabase db;


    public DBAdapterFavoritoNota(Context cont) {

        context = cont;
        DBHelper = new DataBaseHelper(context);

    }

    //open database
    public DBAdapterFavoritoNota open() throws SQLException {

        db = DBHelper.getWritableDatabase();
        return this;

    }

    //close database
    public void close() {

        DBHelper.close();

    }

    //insert
    public long insertFavorite(String cap, String vers, String texto, String versao, String book) throws SQLException {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FAVORITE_CAPITULO, cap);
        initialValues.put(KEY_FAVORITE_VERSICULO, vers);
        initialValues.put(KEY_FAVORITE_TEXTO, texto);
        initialValues.put(KEY_FAVORITE_VERSAO, versao);
        initialValues.put(KEY_FAVORITE_BOOKNAME, book);
        return db.insert(DATABASE_TABLE_FAVORITE, null, initialValues);
    }

    //insert
    public long insertNota(String titulo, String texto, String data) throws SQLException {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTA_TITULO, titulo);
        initialValues.put(KEY_NOTA_TEXTO, texto);
        initialValues.put(KEY_NOTA_DATA, data);

        return db.insert(DATABASE_TABLE_NOTA, null, initialValues);

    }

    //delete
    public boolean deleteFavorite(long id) throws SQLException {

        return db.delete(DATABASE_TABLE_FAVORITE, "id=" + id, null) > 0;

    }

    public boolean deleteNota(long id) throws SQLException {

        return db.delete(DATABASE_TABLE_NOTA, "id=" + id, null) > 0;

    }

    //retriever all values from database
    public Cursor getAllValuesFavorite() throws SQLException {

        return db.query(DATABASE_TABLE_FAVORITE,
                new String[]{"id", KEY_FAVORITE_CAPITULO, KEY_FAVORITE_VERSICULO, KEY_FAVORITE_TEXTO, KEY_FAVORITE_VERSAO, KEY_FAVORITE_BOOKNAME}, null, null, null, null, "id DESC");

    }

    //retriever all values from database
    public Cursor getAllValuesNota() throws SQLException {

        return db.query(DATABASE_TABLE_NOTA,
                new String[]{"id", KEY_NOTA_TITULO, KEY_NOTA_TEXTO, KEY_NOTA_DATA}, null, null, null, null, "id DESC");

    }

    public boolean updateNota(int id, String titulo, String texto, String data) throws SQLException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTA_TITULO, titulo);
        contentValues.put(KEY_NOTA_TEXTO, texto);
        contentValues.put(KEY_NOTA_DATA, data);

        return db.update(DATABASE_TABLE_NOTA, contentValues, "id=" + id, null) > 0;

    }

    private static class DataBaseHelper extends SQLiteOpenHelper {


        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try {
                sqLiteDatabase.execSQL(DATABASE_CREATE_FAVORITE);
                sqLiteDatabase.execSQL(DATABASE_CREATE_NOTA);


            } catch (SQLException e) {

                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FAVORITE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NOTA);

            onCreate(sqLiteDatabase);

        }

    }


}
