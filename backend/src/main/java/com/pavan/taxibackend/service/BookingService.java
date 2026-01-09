package com.pavan.taxibackend.service;

import com.pavan.taxibackend.model.Booking;
import com.pavan.taxibackend.provider.ProviderAdapter;
import com.pavan.taxibackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ProviderAdapter providerAdapter;

    @Transactional
    public Booking createBooking(Booking booking) throws Exception {
        // persist initial booking as initiated
        booking.setStatus("initiated");
        Booking saved = bookingRepository.save(booking);

        // call provider (mock)
        try {
            ProviderAdapter.ProviderResponse resp = providerAdapter.book(saved);
            saved.setProviderBookingId(resp.providerBookingId);
            saved.setFareActual(java.math.BigDecimal.valueOf(resp.fare));
            saved.setStatus("confirmed");
            saved.setProviderPayload("{\"mock\":\"provider_response\"}");
            saved = bookingRepository.save(saved);
        } catch (Exception ex) {
            saved.setStatus("cancelled");
            bookingRepository.save(saved);
            throw ex;
        }
        return saved;
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
}
