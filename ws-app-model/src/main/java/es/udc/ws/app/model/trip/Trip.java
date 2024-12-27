package es.udc.ws.app.model.trip;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trip {
    private Long tripId;
    private String city;
    private String description;
    private LocalDateTime creationDate;
    private float price;
    private int amount;
    private LocalDateTime tripDate;
    private int maxAmount;

    public Trip(Long tripId, String city, String description,
                LocalDateTime creationDate, float price, int amount,
                LocalDateTime tripDate, int maxAmount) {
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.creationDate = creationDate;
        this.price = price;
        this.amount = amount;
        this.tripDate = tripDate;
        this.maxAmount = maxAmount;
    }

    public Trip(String city, String description,
                LocalDateTime creationDate, float price, int amount,
                LocalDateTime tripDate, int maxAmount) {
        this.city = city;
        this.description = description;
        this.creationDate = creationDate;
        this.price = price;
        this.amount = amount;
        this.tripDate = tripDate;
        this.maxAmount = maxAmount;
    }

    public Trip(String city, String description,
                LocalDateTime creationDate, float price,
                LocalDateTime tripDate, int maxAmount) {
        this.city = city;
        this.description = description;
        this.creationDate = creationDate;
        this.price = price;
        this.amount = maxAmount;
        this.tripDate = tripDate;
        this.maxAmount = maxAmount;
    }

    public Trip(Long tripId, String city, String description,
                float price, int amount, LocalDateTime tripDate,
                int maxAmount) {
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.tripDate = tripDate;
        this.maxAmount = maxAmount;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return tripId == trip.tripId && Float.compare(trip.price, price) == 0
                && amount == trip.amount && maxAmount == trip.maxAmount
                && Objects.equals(city, trip.city)
                && Objects.equals(description, trip.description)
                && Objects.equals(creationDate, trip.creationDate)
                && Objects.equals(tripDate, trip.tripDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, city, description, creationDate, price,
                amount, tripDate, maxAmount);
    }
}
