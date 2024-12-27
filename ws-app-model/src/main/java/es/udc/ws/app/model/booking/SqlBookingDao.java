package es.udc.ws.app.model.booking;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlBookingDao {
    Booking create(Connection connection, Booking booking);

    Booking find(Connection connection, Long bookingId)
            throws InstanceNotFoundException;

    void remove(Connection connection, Long bookingId)
            throws InstanceNotFoundException;

    void update(Connection connection, Booking booking)
            throws InstanceNotFoundException;


    List<Booking> findBookings(Connection connection, String email)
            throws InstanceNotFoundException;


}
