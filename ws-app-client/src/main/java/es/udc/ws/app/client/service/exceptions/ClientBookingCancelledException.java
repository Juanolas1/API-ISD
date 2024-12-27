package es.udc.ws.app.client.service.exceptions;

public class ClientBookingCancelledException extends Exception{

    private Long id;

    public ClientBookingCancelledException(Long id) {
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
