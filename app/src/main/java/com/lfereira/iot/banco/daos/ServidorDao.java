package com.lfereira.iot.banco.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.lfereira.iot.banco.modelo.Servidor;

@Dao
public interface ServidorDao extends ModeloDao<Servidor>{

    @Query("SELECT * FROM SERVIDOR LIMIT 1")
    ListenableFuture<Servidor> buscar();
}
