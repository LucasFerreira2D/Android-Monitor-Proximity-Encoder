package com.lfereira.iot.banco.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "SERVIDOR", primaryKeys = {"url","porta"})
public class Servidor implements Serializable {

    @NonNull
    @ColumnInfo(name = "url")
    public String url;

    @NonNull
    @ColumnInfo(name = "porta")
    public Long porta;


    public Servidor() {
    }

    public Servidor(@NonNull String url, @NonNull Long porta) {
        this.url = url;
        this.porta = porta;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public Long getPorta() {
        return porta;
    }

    public void setPorta(@NonNull Long porta) {
        this.porta = porta;
    }
}
