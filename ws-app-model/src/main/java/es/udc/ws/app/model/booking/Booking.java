package es.udc.ws.app.model.booking;

import java.time.LocalDateTime;
import java.util.Objects;

public class Booking {
    private Long bookingId;
    private Long tripId;
    private LocalDateTime bookingDate;
    private String email;
    private int bookingAmount;
    private String creditCard;
    private float price;
    private LocalDateTime bookingCancelDate;

    public Booking(Long bookingId, Long tripId, LocalDateTime bookingDate,
                   String email, int bookingAmount, String creditCard,
                   float price, LocalDateTime bookingCancelDate) {
        this.bookingId = bookingId;
        this.tripId = tripId;
        this.bookingDate = bookingDate;
        this.email = email;
        this.bookingAmount = bookingAmount;
        this.creditCard = creditCard;
        this.price = price;
        this.bookingCancelDate = bookingCancelDate;
    }

    public Booking(Long tripId, LocalDateTime bookingDate,
                   String email, int bookingAmount, String creditCard,
                   float price, LocalDateTime bookingCancelDate) {
        this.tripId = tripId;
        this.bookingDate = (bookingDate != null) ? bookingDate.withNano(0) : null;
        this.email = email;
        this.bookingAmount = bookingAmount;
        this.creditCard = creditCard;
        this.price = price;
        this.bookingCancelDate = (bookingCancelDate != null) ? bookingCancelDate.withNano(0) : null;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingId == booking.bookingId && tripId == booking.tripId &&
                bookingAmount == booking.bookingAmount && Float.compare(booking.price, price) == 0 &&
                Objects.equals(bookingDate, booking.bookingDate) && Objects.equals(email, booking.email) &&
                Objects.equals(creditCard, booking.creditCard) &&
                Objects.equals(bookingCancelDate, booking.bookingCancelDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, tripId, bookingDate, email, bookingAmount, creditCard, price, bookingCancelDate);
    }
}
