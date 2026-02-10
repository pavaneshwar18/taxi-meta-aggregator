package com.pavan.taxibackend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {
    private Long userId;
    private Double pickupLat;
    private Double pickupLng;
    private Double dropLat;
    private Double dropLng;
    private String rideType;
    private BigDecimal fareEstimate;

    public BookingRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public Double getPickupLat() {
        return pickupLat;
    }

    public Double getPickupLng() {
        return pickupLng;
    }

    public Double getDropLat() {
        return dropLat;
    }

    public Double getDropLng() {
        return dropLng;
    }

    public String getRideType() {
        return rideType;
    }

    public BigDecimal getFareEstimate() {
        return fareEstimate;
    }

    public void setPickupLat(Double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public void setPickupLng(Double pickupLng) {
        this.pickupLng = pickupLng;
    }

    public void setDropLat(Double dropLat) {
        this.dropLat = dropLat;
    }

    public void setDropLng(Double dropLng) {
        this.dropLng = dropLng;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public void setFareEstimate(BigDecimal fareEstimate) {
        this.fareEstimate = fareEstimate;
    }
}
