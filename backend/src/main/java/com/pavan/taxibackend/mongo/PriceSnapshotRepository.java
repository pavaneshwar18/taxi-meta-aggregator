package com.pavan.taxibackend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PriceSnapshotRepository extends MongoRepository<PriceSnapshot, String> {
    List<PriceSnapshot> findTop20ByRideTypeOrderByCreatedAtDesc(String rideType);
}
