package com.lfereira.iot.model;

public class ProximityData {

    private int id;
    private String valor;
    private String data_hora;


    public ProximityData() {
    }

    public ProximityData(int id, String valor, String data_hora) {
        this.id = id;
        this.valor = valor;
        this.data_hora = data_hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }
}

