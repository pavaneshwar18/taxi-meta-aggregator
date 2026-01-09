package com.pavan.taxibackend.provider.price;

import com.pavan.taxibackend.dto.PriceRequest;
import com.pavan.taxibackend.dto.ProviderPrice;

public interface ProviderPriceAdapter {
    ProviderPrice getPrice(PriceRequest req);
}
