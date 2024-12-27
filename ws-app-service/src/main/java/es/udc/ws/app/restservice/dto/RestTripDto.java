package es.udc.ws.app.restservice.dto;


public class RestTripDto {

    private Long tripId;
    private String city;
    private String description;
    private float price;
    private int amount;
    private String tripDate;
    private int maxAmount;

    public RestTripDto() {
    }

    public RestTripDto(Long tripId, String city, String description, float price, int amount, String tripDate, int maxAmount) {
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

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }


    @Override
    public String toString() {
        return "RestTripDto[" +
                "tripId=" + tripId +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", maxAmount=" + maxAmount +
                ']';
    }
}
