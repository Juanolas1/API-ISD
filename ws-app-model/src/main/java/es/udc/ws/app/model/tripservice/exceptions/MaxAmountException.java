package es.udc.ws.app.model.tripservice.exceptions;

public class MaxAmountException extends Exception{
    private Long tripId;
    private int maxAmount;


    public MaxAmountException(Long tripId, int maxAmount) {
        super("Trip with id=\"" + tripId + "\n cannot be "+maxAmount);
        this.tripId = tripId;
        this.maxAmount=maxAmount;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

}
