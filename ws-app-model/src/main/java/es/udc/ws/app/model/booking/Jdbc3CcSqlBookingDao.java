package es.udc.ws.app.model.booking;

import java.sql.*;

public class Jdbc3CcSqlBookingDao extends AbstractSqlBookingDao {
    @Override
    public Booking create(Connection connection, Booking booking) {

        String queryString = "INSERT INTO Booking"
                + " (tripId, bookingDate, email, bookingAmount,"
                + " creditCard, price, bookingCancelDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setLong(i++, booking.getTripId());
            Timestamp date = booking.getBookingDate() != null ? Timestamp
                    .valueOf(booking.getBookingDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setString(i++, booking.getEmail());
            preparedStatement.setInt(i++, booking.getBookingAmount());
            preparedStatement.setString(i++, booking.getCreditCard());
            preparedStatement.setFloat(i++, booking.getPrice());
            date = booking.getBookingCancelDate() != null ? Timestamp
                    .valueOf(booking.getBookingCancelDate()) : null;
            preparedStatement.setTimestamp(i++, date);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key");
            }
            Long bookingId = resultSet.getLong(1);

            return new Booking(bookingId, booking.getTripId(),
                    booking.getBookingDate(), booking.getEmail(),
                    booking.getBookingAmount(), booking.getCreditCard(),
                    booking.getPrice(), booking.getBookingCancelDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

