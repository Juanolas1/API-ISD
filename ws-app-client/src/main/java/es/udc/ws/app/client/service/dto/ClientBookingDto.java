package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientBookingDto {

    private long bookingId;
    private long tripId;
    private LocalDateTime bookingDate;
    private String email;
    private int bookingAmount;
    private String creditCard;
    private float price;
    private LocalDateTime bookingCancelDate;

    public  ClientBookingDto(){
    }

    public ClientBookingDto(long bookingId, long tripId, LocalDateTime bookingDate, String email, int bookingAmount, String creditCard, float price, LocalDateTime bookingCancelDate) {
        this.bookingId = bookingId;
        this.tripId = tripId;
        this.bookingDate = bookingDate;
        this.email = email;
        this.bookingAmount = bookingAmount;
        this.creditCard = creditCard;
        this.price = price;
        this.bookingCancelDate = bookingCancelDate;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(int bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getBookingCancelDate() {
        return bookingCancelDate;
    }

    public void setBookingCancelDate(LocalDateTime bookingCancelDate) {
        this.bookingCancelDate = bookingCancelDate;
    }
}
