package com.pavan.taxibackend.mongo;

import com.pavan.taxibackend.dto.ProviderPrice;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "price_snapshots")
public class PriceSnapshot {

    @Id
    private String id;

    private Double pickupLat;
    private Double pickupLng;
    private Double dropLat;
    private Double dropLng;
    private String rideType;

    private List<ProviderPrice> results;

    @Indexed(name = "createdAt_idx")
    private Instant createdAt;

    public PriceSnapshot() {}

    public PriceSnapshot(Double pickupLat, Double pickupLng, Double dropLat, Double dropLng,
                         String rideType, List<ProviderPrice> results) {
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropLat = dropLat;
        this.dropLng = dropLng;
        this.rideType = rideType;
        this.results = results;
        this.createdAt = Instant.now();
    }

    // getters & setters
    public String getId() { return id; }
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
    public List<ProviderPrice> getResults() { return results; }
    public void setResults(List<ProviderPrice> results) { this.results = results; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
