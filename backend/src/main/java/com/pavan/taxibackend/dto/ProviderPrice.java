package com.pavan.taxibackend.dto;

public class ProviderPrice {

    private String provider;
    private double price;

    public ProviderPrice() {}

    public ProviderPrice(String provider, double price) {
        this.provider = provider;
        this.price = price;
    }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
