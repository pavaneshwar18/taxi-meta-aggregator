package com.pavan.taxibackend.controller;

import com.pavan.taxibackend.mongo.PriceSnapshot;
import com.pavan.taxibackend.mongo.PriceSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceSnapshotController {

    @Autowired
    private PriceSnapshotRepository snapshotRepository;

    // GET /api/prices/snapshots?rideType=sedan
    @GetMapping("/snapshots")
    public List<PriceSnapshot> getSnapshots(@RequestParam String rideType) {
        return snapshotRepository.findTop20ByRideTypeOrderByCreatedAtDesc(rideType);
    }
}
