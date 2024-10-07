package com.lfereira.iot.banco;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lfereira.iot.banco.daos.ServidorDao;
import com.lfereira.iot.banco.modelo.Servidor;

@Database(entities = {
        Servidor.class,
}, version = 1, exportSchema = false)

public abstract class DataBase extends RoomDatabase {

    public static final String DATABASE = "database.db";

    public static DataBase getInstance(Context ctx) {
        return Room.databaseBuilder(ctx.getApplicationContext(), DataBase.class, DATABASE)
                .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                .allowMainThreadQueries()
                .addMigrations()
                .build();
    }

    public abstract ServidorDao servidorDao();

}
