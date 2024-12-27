package es.udc.ws.app.model.trip;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlTripDao implements SqlTripDao {

    protected AbstractSqlTripDao() {
    }

    @Override
    public Trip find(Connection connection, Long tripId)
            throws InstanceNotFoundException {


        String queryString = "SELECT city, description, creationDate, price," +
                " amount, tripDate, maxAmount FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.
                prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, tripId.longValue());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(tripId,
                        Trip.class.getName());
            }

            i = 1;
            String city = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp.
                    toLocalDateTime();
            float price = resultSet.getFloat(i++);
            int amount = resultSet.getInt(i++);
            creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime tripDate = creationDateAsTimestamp.
                    toLocalDateTime();
            int maxAmount = resultSet.getInt(i++);


            return new Trip(tripId, city, description, creationDate, price,
                    amount, tripDate, maxAmount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//FIN del FIND

    @Override
    public List<Trip> findTrips(Connection connection, String city, LocalDateTime ini, LocalDateTime fin) {


        String queryString = "SELECT tripId, description, creationDate, price,"
                + " amount, tripDate, maxAmount FROM Trip";

        if (city != null) {
            queryString += " WHERE city = ?";
            if (ini != null && fin != null) {
                queryString += " AND tripDate < ? AND tripdate > ?";
            }
        }

        queryString += " ORDER BY city";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString)) {

            int i = 1;
            if (city != null) {
                preparedStatement.setString(i++, city);
                if (ini != null && fin != null) {
                    preparedStatement.setTimestamp(i++,
                            Timestamp.valueOf(fin));
                    preparedStatement.setTimestamp(i++,
                            Timestamp.valueOf(ini.minusHours(24)));
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Trip> trips = new ArrayList<Trip>();
            while (resultSet.next()) {

                i = 1;
                Long tripId = resultSet.getLong(i++);
                String description = resultSet.getString(i++);
                Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateAsTimestamp.
                        toLocalDateTime();
                float price = resultSet.getFloat(i++);
                int amount = resultSet.getInt(i++);
                creationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime tripDate = creationDateAsTimestamp.
                        toLocalDateTime();
                int maxAmount = resultSet.getInt(i++);
                trips.add(new Trip(tripId, city, description, creationDate,
                        price, amount, tripDate, maxAmount));
            }
            return trips;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override

    public void remove(Connection connection, Long tripId)
            throws InstanceNotFoundException {

        String queryString = "DELETE FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.
                prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, tripId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(tripId,
                        Trip.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//FIN del REMOVE

    @Override
    public void update(Connection connection, Trip trip)
            throws InstanceNotFoundException {


        String queryString = "UPDATE Trip SET city = ?, description = ?," +
                " price = ?, amount = ?, tripDate = ?," +
                " maxAmount = ? WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.
                prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, trip.getCity());
            preparedStatement.setString(i++, trip.getDescription());
            preparedStatement.setFloat(i++, trip.getPrice());
            preparedStatement.setInt(i++, trip.getAmount());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.
                    getTripDate()));
            preparedStatement.setInt(i++, trip.getMaxAmount());
            preparedStatement.setLong(i++, trip.getTripId());

            int updateRows = preparedStatement.executeUpdate();

            if (updateRows == 0) {
                throw new InstanceNotFoundException(trip.getTripId(),
                        Trip.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
