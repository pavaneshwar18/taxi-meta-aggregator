package com.pavan.taxibackend.provider.price;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;
import org.springframework.stereotype.Component;

@Component
public class UberPriceAdapter implements ProviderPriceAdapter {

    @Override
    public ProviderPrice getPrice(PriceRequest req) {
        // Fake pricing algorithm
        double base = 50;
        double distance = distance(req);
        double price = base + distance * 12; // Uber mock rate
        return new ProviderPrice("Uber", price);
    }

    private double distance(PriceRequest req) {
        return Math.sqrt(
                Math.pow(req.getPickupLat() - req.getDropLat(), 2) +
                        Math.pow(req.getPickupLng() - req.getDropLng(), 2)
        ) * 111;
    }
}
