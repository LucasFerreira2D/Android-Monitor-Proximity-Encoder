package com.lfereira.iot.banco.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;


@Dao
public interface ModeloDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> inserir(T valor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> inserirRetornar(T valor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> inserir(List<T> valor);

    @Update
    ListenableFuture<Void> atualizar(T valor);

    @Delete
    ListenableFuture<Void> excluir(T valor);

    @Delete
    ListenableFuture<Void> excluirTodos(List<T> valor);

}
