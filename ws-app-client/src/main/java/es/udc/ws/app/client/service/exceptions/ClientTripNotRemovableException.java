package es.udc.ws.app.client.service.exceptions;

public class ClientTripNotRemovableException extends Exception {
    private Long tripId;


    public ClientTripNotRemovableException(Long tripId) {
        super("Trip with id=\"" + tripId + "\n cannot be deleted");
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }


}
