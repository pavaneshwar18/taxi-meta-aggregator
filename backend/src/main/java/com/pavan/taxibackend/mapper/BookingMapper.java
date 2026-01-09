package com.pavan.taxibackend.mapper;

import com.pavan.taxibackend.dto.BookingRequest;
import com.pavan.taxibackend.dto.BookingResponse;
import com.pavan.taxibackend.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequest req) {
        if (req == null) return null;
        Booking b = new Booking();
        b.setUserId(req.getUserId());
        b.setPickupLat(req.getPickupLat());
        b.setPickupLng(req.getPickupLng());
        b.setDropLat(req.getDropLat());
        b.setDropLng(req.getDropLng());
        b.setRideType(req.getRideType());
        b.setFareEstimate(req.getFareEstimate());
        // status, provider fields will be set by service
        return b;
    }

    public BookingResponse toResponse(Booking b) {
        if (b == null) return null;
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setUserId(b.getUserId());
        r.setProviderId(b.getProviderId());
        r.setProviderBookingId(b.getProviderBookingId());
        r.setPickupLat(b.getPickupLat());
        r.setPickupLng(b.getPickupLng());
        r.setDropLat(b.getDropLat());
        r.setDropLng(b.getDropLng());
        r.setRideType(b.getRideType());
        r.setFareEstimate(b.getFareEstimate());
        r.setFareActual(b.getFareActual());
        r.setCurrency(b.getCurrency());
        r.setStatus(b.getStatus());
        r.setProviderPayload(b.getProviderPayload());
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }
}
