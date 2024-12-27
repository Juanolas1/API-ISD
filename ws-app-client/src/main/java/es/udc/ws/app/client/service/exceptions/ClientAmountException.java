package es.udc.ws.app.client.service.exceptions;

public class ClientAmountException extends Exception {
    private Long tripId;
    private int amount;


    public ClientAmountException(Long tripId, int amount) {
        super("Id=\"" + tripId + "\"\n cannot be " + amount);
        this.tripId = tripId;
        this.amount = amount;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
