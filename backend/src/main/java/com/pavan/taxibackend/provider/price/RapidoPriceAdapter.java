package com.pavan.taxibackend.provider.price;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;
import org.springframework.stereotype.Component;

@Component
public class RapidoPriceAdapter implements ProviderPriceAdapter {

    @Override
    public ProviderPrice getPrice(PriceRequest req) {
        double base = 20;
        double distance = distance(req);
        double price = base + distance * 8; // Rapido mock rate
        return new ProviderPrice("Rapido", price);
    }

    private double distance(PriceRequest req) {
        return Math.sqrt(
                Math.pow(req.getPickupLat() - req.getDropLat(), 2) +
                        Math.pow(req.getPickupLng() - req.getDropLng(), 2)
        ) * 111;
    }
}
