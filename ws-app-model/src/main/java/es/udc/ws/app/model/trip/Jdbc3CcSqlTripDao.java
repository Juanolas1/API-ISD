package es.udc.ws.app.model.trip;

import java.sql.*;

public class Jdbc3CcSqlTripDao extends AbstractSqlTripDao {
    @Override
    public Trip create(Connection connection, Trip trip) {

        String queryString = "INSERT INTO Trip"
                + " (city, description, creationDate,"
                + " price, amount, tripDate, maxAmount) VALUES (?, ?, ?, ?," +
                " ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setString(i++, trip.getCity());
            preparedStatement.setString(i++, trip.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip
                    .getCreationDate().withNano(0)));
            preparedStatement.setFloat(i++, trip.getPrice());
            preparedStatement.setInt(i++, trip.getAmount());
            preparedStatement.setTimestamp(i++, Timestamp
                    .valueOf(trip.getTripDate().withNano(0)));
            preparedStatement.setInt(i++, trip.getMaxAmount());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long tripId = resultSet.getLong(1);

            return new Trip(tripId, trip.getCity(), trip.getDescription(),
                    trip.getCreationDate(), trip.getPrice(), trip.getAmount(),
                    trip.getTripDate(), trip.getMaxAmount());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
