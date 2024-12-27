package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientTripDto {

    private long tripId;
    private String city;
    private String description;
    private float price;
    private int amount;
    private LocalDateTime tripDate;
    private int maxAmount;


    public ClientTripDto() {
    }

    public ClientTripDto(long tripId, String city, String description, float price, int amount, LocalDateTime tripDate, int maxAmount) {
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.tripDate = tripDate;
        this.maxAmount = maxAmount;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDateTime tripDate) {
        this.tripDate = tripDate;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

}
