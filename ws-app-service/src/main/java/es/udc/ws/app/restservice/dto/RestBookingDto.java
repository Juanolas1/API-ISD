package es.udc.ws.app.restservice.dto;


public class RestBookingDto {

    private Long bookingId;
    private Long tripId;
    private String bookingDate;
    private String email;
    private int bookingAmount;
    private String creditCard;
    private float price;

    private String bookingCancelDate;

    RestBookingDto() {
    }

    public RestBookingDto(long bookingId, long tripId, String bookingDate, String email, int bookingAmount, String creditCard, float price, String bookingCancelDate) {
        StringBuilder card = new StringBuilder(creditCard);

        for (int i = creditCard.length() - 5; i >= 0; i--) {
            card.setCharAt(i, '*');
        }

        this.bookingId = bookingId;
        this.tripId = tripId;
        this.bookingDate = bookingDate;
        this.email = email;
        this.bookingAmount = bookingAmount;
        this.creditCard = card.toString();
        this.price = price;
        this.bookingCancelDate = bookingCancelDate;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
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
        return this.creditCard;
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


    public String getBookingCancelDate() {
        return bookingCancelDate;
    }

    public void setBookingCancelDate(String bookingCancelDate) {
        this.bookingCancelDate = bookingCancelDate;
    }
}
