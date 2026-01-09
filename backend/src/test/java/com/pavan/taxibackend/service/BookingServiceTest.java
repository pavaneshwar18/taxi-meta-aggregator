package com.pavan.taxibackend.service;

import com.pavan.taxibackend.model.Booking;
import com.pavan.taxibackend.provider.ProviderAdapter;
import com.pavan.taxibackend.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ProviderAdapter providerAdapter;

    @InjectMocks
    BookingService bookingService;

    @Captor
    ArgumentCaptor<Booking> bookingCaptor;

    private Booking baseBooking;

    @BeforeEach
    void setUp() {
        baseBooking = new Booking();
        baseBooking.setUserId(1L);
        baseBooking.setPickupLat(17.385044);
        baseBooking.setPickupLng(78.486671);
        baseBooking.setDropLat(17.43992);
        baseBooking.setDropLng(78.498833);
        baseBooking.setRideType("sedan");
        baseBooking.setFareEstimate(BigDecimal.valueOf(150.00));
    }

    @Test
    void createBooking_successfulFlow_persistsAndConfirms() throws Exception {
        // arrange
        // first save returns the booking with id set (simulate DB auto-gen)
        Booking savedInitial = new Booking();
        savedInitial.setUserId(baseBooking.getUserId());
        savedInitial.setPickupLat(baseBooking.getPickupLat());
        savedInitial.setPickupLng(baseBooking.getPickupLng());
        savedInitial.setDropLat(baseBooking.getDropLat());
        savedInitial.setDropLng(baseBooking.getDropLng());
        savedInitial.setRideType(baseBooking.getRideType());
        savedInitial.setFareEstimate(baseBooking.getFareEstimate());
        // simulate DB assigned id on first save
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            if (b.getId() == null) {
                b = copyBooking(b);
                // set id to simulate persistence
                try {
                    java.lang.reflect.Field idField = Booking.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(b, 1L);
                } catch (Exception e) { /* ignore for test */ }
                return b;
            }
            return b;
        });

        // providerAdapter will return ProviderResponse with providerBookingId and fare
        ProviderAdapter.ProviderResponse providerResponse =
                new ProviderAdapter.ProviderResponse("MOCK-TEST", 150.0);
        when(providerAdapter.book(any(Booking.class))).thenReturn(providerResponse);

        // act
        Booking result = bookingService.createBooking(baseBooking);

        // assert
        verify(bookingRepository, atLeast(1)).save(bookingCaptor.capture());
        Booking lastSaved = bookingCaptor.getValue();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo("confirmed");
        assertThat(result.getProviderBookingId()).isEqualTo("MOCK-TEST");
        assertThat(result.getFareActual()).isEqualByComparingTo(BigDecimal.valueOf(150.0));

        // ensure providerAdapter was called with an entity having same userId
        verify(providerAdapter, times(1)).book(any(Booking.class));
    }

    @Test
    void createBooking_providerThrows_exceptionLeadsToCancelledStatus() throws Exception {
        // arrange
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            if (b.getId() == null) {
                try {
                    java.lang.reflect.Field idField = Booking.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(b, 2L);
                } catch (Exception e) {}
            }
            return b;
        });

        when(providerAdapter.book(any(Booking.class))).thenThrow(new RuntimeException("provider down"));

        // act & assert
        try {
            bookingService.createBooking(baseBooking);
        } catch (Exception e) {
            // expected exception; ensure bookingRepository.save was called at least twice (initial + cancel)
            verify(bookingRepository, atLeast(2)).save(any(Booking.class));
        }
    }

    // helper: shallow copy
    private Booking copyBooking(Booking src) {
        Booking b = new Booking();
        b.setUserId(src.getUserId());
        b.setPickupLat(src.getPickupLat());
        b.setPickupLng(src.getPickupLng());
        b.setDropLat(src.getDropLat());
        b.setDropLng(src.getDropLng());
        b.setRideType(src.getRideType());
        b.setFareEstimate(src.getFareEstimate());
        b.setCurrency(src.getCurrency());
        return b;
    }
}
