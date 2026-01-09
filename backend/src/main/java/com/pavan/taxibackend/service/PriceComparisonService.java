package com.pavan.taxibackend.service;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;
import com.pavan.taxibackend.mongo.PriceSnapshot;
import com.pavan.taxibackend.mongo.PriceSnapshotRepository;
import com.pavan.taxibackend.provider.price.ProviderPriceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceComparisonService {

    @Autowired
    private List<ProviderPriceAdapter> adapters;

    @Autowired
    private PriceSnapshotRepository snapshotRepository;

    public List<ProviderPrice> compare(PriceRequest req) {
        List<ProviderPrice> results = adapters.stream()
                .map(adapter -> adapter.getPrice(req))
                .sorted(Comparator.comparingDouble(ProviderPrice::getPrice))
                .collect(Collectors.toList());

        // save snapshot (async could be used later; keep synchronous for now)
        try {
            PriceSnapshot snap = new PriceSnapshot(
                    req.getPickupLat(),
                    req.getPickupLng(),
                    req.getDropLat(),
                    req.getDropLng(),
                    req.getRideType(),
                    results
            );
            snapshotRepository.save(snap);
        } catch (Exception ex) {
            // Log and continue — do not fail the price response if snapshot fails
            System.err.println("Failed to save price snapshot: " + ex.getMessage());
        }

        return results;
    }
}
