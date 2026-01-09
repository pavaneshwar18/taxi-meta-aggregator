package com.pavan.taxibackend.controller;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;
import com.pavan.taxibackend.service.PriceComparisonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceComparisonController {

    @Autowired
    private PriceComparisonService priceService;

    @PostMapping("/compare")
    public List<ProviderPrice> comparePrices(@RequestBody @Valid PriceRequest req) {
        return priceService.compare(req);
    }
}
