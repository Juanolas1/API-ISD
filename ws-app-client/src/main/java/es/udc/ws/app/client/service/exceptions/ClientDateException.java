package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientDateException extends Exception {
    private Long tripId;
    private LocalDateTime tripDate;


    public ClientDateException(Long tripId, LocalDateTime tripDate) {
        super("Id=\"" + tripId + "\"\n cannot be " + tripDate + " Date");
        this.tripId = tripId;
        this.tripDate = tripDate;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDateTime tripDate) {
        this.tripDate = tripDate;
    }


}

