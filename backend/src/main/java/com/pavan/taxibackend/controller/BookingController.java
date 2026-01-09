package com.pavan.taxibackend.controller;

import com.pavan.taxibackend.dto.BookingRequest;
import com.pavan.taxibackend.dto.BookingResponse;
import com.pavan.taxibackend.mapper.BookingMapper;
import com.pavan.taxibackend.model.Booking;
import com.pavan.taxibackend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @PostMapping
    public BookingResponse createBooking(@RequestBody @Valid BookingRequest req) throws Exception {
        Booking entity = bookingMapper.toEntity(req);
        Booking saved = bookingService.createBooking(entity);
        return bookingMapper.toResponse(saved);
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {
        Booking b = bookingService.getById(id);
        return bookingMapper.toResponse(b);
    }
}
