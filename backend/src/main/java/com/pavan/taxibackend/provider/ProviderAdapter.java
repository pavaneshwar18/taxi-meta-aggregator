package com.pavan.taxibackend.provider;

import com.pavan.taxibackend.model.Booking;

public interface ProviderAdapter {
    /**
     * Book the ride with the provider. This is a mock contract for now.
     * Return booking id from provider or throw exception on failure.
     */
    ProviderResponse book(Booking booking) throws Exception;

    class ProviderResponse {
        public final String providerBookingId;
        public final double fare;

        public ProviderResponse(String providerBookingId, double fare) {
            this.providerBookingId = providerBookingId;
            this.fare = fare;
        }
    }
}
