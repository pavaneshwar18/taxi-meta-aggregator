package com.pavan.taxibackend.dto;

import jakarta.validation.constraints.NotNull;

public class PriceRequest {

    @NotNull
    private Double pickupLat;

    @NotNull
    private Double pickupLng;

    @NotNull
    private Double dropLat;

    @NotNull
    private Double dropLng;

    @NotNull
    private String rideType;

    public Double getPickupLat() { return pickupLat; }
    public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

    public Double getPickupLng() { return pickupLng; }
    public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }

    public Double getDropLat() { return dropLat; }
    public void setDropLat(Double dropLat) { this.dropLat = dropLat; }

    public Double getDropLng() { return dropLng; }
    public void setDropLng(Double dropLng) { this.dropLng = dropLng; }

    public String getRideType() { return rideType; }
    public void setRideType(String rideType) { this.rideType = rideType; }
}
