package es.udc.ws.app.model.tripservice.exceptions;

public class TripNotRemovableException extends Exception {
    private Long tripId;


    public TripNotRemovableException(Long tripId) {
        super("Id=\"" + tripId + "\n cannot be deleted");
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }


}
