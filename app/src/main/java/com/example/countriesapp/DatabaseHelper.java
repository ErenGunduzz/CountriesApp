package com.example.countriesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "CountryApp.db";

    public DatabaseHelper(@Nullable Context context) { super(context, DATABASE_NAME, null, 1); }

    //Database oluşturulur
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE favorites(id INTEGER PRIMARY KEY AUTOINCREMENT, countryName TEXT )");

        }catch (SQLException e){
            Log.e("DatabaseHelper","Error creating table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS favorites");
        onCreate(db);
    }

    //Yeni favoriyi database'e ekleme
    public boolean addFavorite(String inp_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("countryName", inp_name);
        long result = db.insert("favorites", null, contentValues);
        db.close();

        if (result == -1 ){
            return false;
        }else{
            return true;
        }

    }

    //Favorilenmiş bir ülkeyi database'den çıkarma
    public void removeFavorite(String countryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favorites", "countryName = ?", new String[]{countryName});
        db.close();
    }



    public List<String> getAllFavorites(){
        List<String> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT countryName FROM favorites", null );

        if (cursor.moveToFirst()) {
            do {
                favorites.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favorites;
    }

    public boolean isFavorite(String countryName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM favorites WHERE countryName =?",new String[]{countryName});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return exists;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

}

