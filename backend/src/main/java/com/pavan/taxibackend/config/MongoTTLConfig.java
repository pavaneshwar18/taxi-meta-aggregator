package com.pavan.taxibackend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.domain.Sort;

@Configuration
public class MongoTTLConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initTTL() {
        // 30 days expiry
        long expireAfterSeconds = 60L * 60 * 24 * 30;

        IndexOperations indexOps = mongoTemplate.indexOps("price_snapshots");
        indexOps.createIndex(
                new Index()
                        .on("createdAt", Sort.Direction.ASC)
                        .expire(expireAfterSeconds)
        );

    }
}
