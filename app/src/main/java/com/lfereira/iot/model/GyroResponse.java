package com.lfereira.iot.model;

import java.util.List;

public class GyroResponse {

    private List<GyroData> giro;


    public GyroResponse() {
    }

    public GyroResponse(List<GyroData> giro) {
        this.giro = giro;
    }

    public List<GyroData> getGiro() {
        return giro;
    }

    public void setGiro(List<GyroData> giro) {
        this.giro = giro;
    }
}
