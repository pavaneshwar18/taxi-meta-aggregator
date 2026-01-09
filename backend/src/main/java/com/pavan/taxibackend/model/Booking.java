package com.pavan.taxibackend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "provider_id")
    private Integer providerId;

    @Column(name = "provider_booking_id")
    private String providerBookingId;

    @Column(name = "pickup_lat")
    private Double pickupLat;

    @Column(name = "pickup_lng")
    private Double pickupLng;

    @Column(name = "drop_lat")
    private Double dropLat;

    @Column(name = "drop_lng")
    private Double dropLng;

    @Column(name = "ride_type")
    private String rideType;

    @Column(name = "fare_estimate")
    private BigDecimal fareEstimate;

    @Column(name = "fare_actual")
    private BigDecimal fareActual;

    @Column(name = "currency")
    private String currency = "INR";

    @Column(name = "status")
    private String status;

    @Lob
    @Column(name = "provider_payload")
    private String providerPayload;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public Booking() {}

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public String getProviderBookingId() {
        return providerBookingId;
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

    public BigDecimal getFareActual() {
        return fareActual;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getProviderPayload() {
        return providerPayload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public void setProviderBookingId(String providerBookingId) {
        this.providerBookingId = providerBookingId;
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

    public void setFareActual(BigDecimal fareActual) {
        this.fareActual = fareActual;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProviderPayload(String providerPayload) {
        this.providerPayload = providerPayload;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
