package com.lfereira.iot.model;

import java.util.List;

public class ProximityResponse {

    private List<ProximityData> proximidade;

    public ProximityResponse() {
    }

    public ProximityResponse(List<ProximityData> proximidade) {
        this.proximidade = proximidade;
    }

    public List<ProximityData> getProximidade() {
        return proximidade;
    }

    public void setProximidade(List<ProximityData> proximidade) {
        this.proximidade = proximidade;
    }
}
