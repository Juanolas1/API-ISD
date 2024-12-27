package es.udc.ws.app.model.tripservice.exceptions;

public class BookingCancelledException extends Exception{

    private Long id;

    public BookingCancelledException(Long id) {
        super("Booking "+id+" is cancelled");
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
