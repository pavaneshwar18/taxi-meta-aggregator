package com.pavan.taxibackend.provider;

import com.pavan.taxibackend.model.Booking;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockProviderAdapter implements ProviderAdapter {

    @Override
    public ProviderResponse book(Booking booking) {
        // Simulate provider booking success with random id and fare = estimate
        String providerBookingId = "MOCK-" + UUID.randomUUID().toString().substring(0,8);
        double fare = booking.getFareEstimate() != null ? booking.getFareEstimate().doubleValue() : 0.0;
        return new ProviderResponse(providerBookingId, fare);
    }
}
