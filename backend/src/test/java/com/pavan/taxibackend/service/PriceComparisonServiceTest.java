package com.pavan.taxibackend.service;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;
import com.pavan.taxibackend.provider.price.ProviderPriceAdapter;
import com.pavan.taxibackend.mongo.PriceSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceComparisonServiceTest {

    @Mock
    ProviderPriceAdapter adapterA;

    @Mock
    ProviderPriceAdapter adapterB;

    @Mock
    PriceSnapshotRepository snapshotRepository; // to avoid NPE if service tries to save

    private PriceComparisonService priceComparisonService;

    @BeforeEach
    void setUp() {
        // create real service instance then inject the mocks using ReflectionTestUtils
        priceComparisonService = new PriceComparisonService();
        ReflectionTestUtils.setField(priceComparisonService, "adapters", List.of(adapterA, adapterB));
        ReflectionTestUtils.setField(priceComparisonService, "snapshotRepository", snapshotRepository);
    }

    @Test
    void compare_returnsSortedResults_cheapestFirst() {
        // arrange
        PriceRequest req = new PriceRequest();
        req.setPickupLat(17.0);
        req.setPickupLng(78.0);
        req.setDropLat(17.1);
        req.setDropLng(78.1);
        req.setRideType("sedan");

        when(adapterA.getPrice(req)).thenReturn(new ProviderPrice("A", 200.0));
        when(adapterB.getPrice(req)).thenReturn(new ProviderPrice("B", 100.0));

        // act
        var results = priceComparisonService.compare(req);

        // assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getProvider()).isEqualTo("B");
        assertThat(results.get(0).getPrice()).isEqualTo(100.0);
        assertThat(results.get(1).getProvider()).isEqualTo("A");
    }
}
