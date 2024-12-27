package es.udc.ws.app.model.tripservice.exceptions;

import java.time.LocalDateTime;

public class DateException extends Exception {
    private Long tripId;
    private LocalDateTime tripDate;


    public DateException(Long tripId, LocalDateTime tripDate) {
        super("Trip with id=\"" + tripId + "\"\n cannot be " + tripDate+" Date");
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
