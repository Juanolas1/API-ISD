package es.udc.ws.app.model.trip;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlTripDao {
    Trip create(Connection connection, Trip trip);

    Trip find(Connection connection, Long tripId)
            throws InstanceNotFoundException;

    List<Trip> findTrips(Connection connection, String city, LocalDateTime ini, LocalDateTime fin)
            throws InstanceNotFoundException;

    void remove(Connection connection, Long tripId)
            throws InstanceNotFoundException;

    void update(Connection connection, Trip trip)
            throws InstanceNotFoundException;

}
