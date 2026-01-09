package com.pavan.taxibackend.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class BookingResponse {
    private Long id;
    private Long userId;
    private Integer providerId;
    private String providerBookingId;
    private Double pickupLat;
    private Double pickupLng;
    private Double dropLat;
    private Double dropLng;
    private String rideType;
    private BigDecimal fareEstimate;
    private BigDecimal fareActual;
    private String currency;
    private String status;
    private String providerPayload;
    private Instant createdAt;

    public BookingResponse() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getProviderId() { return providerId; }
    public void setProviderId(Integer providerId) { this.providerId = providerId; }
    public String getProviderBookingId() { return providerBookingId; }
    public void setProviderBookingId(String providerBookingId) { this.providerBookingId = providerBookingId; }
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
    public BigDecimal getFareEstimate() { return fareEstimate; }
    public void setFareEstimate(BigDecimal fareEstimate) { this.fareEstimate = fareEstimate; }
    public BigDecimal getFareActual() { return fareActual; }
    public void setFareActual(BigDecimal fareActual) { this.fareActual = fareActual; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProviderPayload() { return providerPayload; }
    public void setProviderPayload(String providerPayload) { this.providerPayload = providerPayload; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
