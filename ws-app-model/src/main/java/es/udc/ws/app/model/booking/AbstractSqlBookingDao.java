package es.udc.ws.app.model.booking;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlBookingDao implements SqlBookingDao {

    protected AbstractSqlBookingDao() {
    }

    @Override
    public Booking find(Connection connection, Long bookingId)
            throws InstanceNotFoundException {


        String queryString = "SELECT tripId, bookingDate, email, " +
                "bookingAmount, creditCard, price, bookingCancelDate" +
                " FROM Booking WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection
                .prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, bookingId.longValue());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(bookingId,
                        Booking.class.getName());
            }

            i = 1;
            Long tripId = resultSet.getLong(i++);
            Timestamp bookingDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime bookingDate = bookingDateAsTimestamp != null
                    ? bookingDateAsTimestamp.toLocalDateTime()
                    : null;
            String email = resultSet.getString(i++);
            int bookingAmount = resultSet.getInt(i++);
            String creditCard = resultSet.getString(i++);
            float price = resultSet.getFloat(i++);
            Timestamp bookingCancelDateAsTimestamp = resultSet
                    .getTimestamp(i++);
            LocalDateTime bookingCancelDate
                    = bookingCancelDateAsTimestamp != null
                    ? bookingCancelDateAsTimestamp.toLocalDateTime()
                    : null;

            return new Booking(bookingId, tripId, bookingDate, email,
                    bookingAmount, creditCard, price, bookingCancelDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findBookings(Connection connection, String email) {


        String queryString = "SELECT bookingId, tripId, bookingDate, " +
                "bookingAmount, creditCard, price," +
                " bookingCancelDate FROM Booking";

        if (email.length() > 0 && email != null) {
            queryString += " WHERE email = ?";
        }
        queryString += " ORDER BY bookingDate";

        try (PreparedStatement preparedStatement = connection
                .prepareStatement(queryString)) {

            int i = 1;
            if (email.length() > 0 && email != null) {
                preparedStatement.setString(i++, email);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<Booking>();

            while (resultSet.next()) {
                i = 1;
                Long bookingId = resultSet.getLong(i++);
                Long tripId = resultSet.getLong(i++);
                Timestamp bookingDateAsTimestamp = resultSet
                        .getTimestamp(i++);
                LocalDateTime bookingDate = bookingDateAsTimestamp != null
                        ? bookingDateAsTimestamp.toLocalDateTime()
                        : null;
                int bookingAmount = resultSet.getInt(i++);
                String creditCard = resultSet.getString(i++);
                float price = resultSet.getFloat(i++);
                Timestamp bookingCancelDateAsTimestamp
                        = resultSet.getTimestamp(i++);
                LocalDateTime bookingCancelDate
                        = bookingCancelDateAsTimestamp != null
                        ? bookingCancelDateAsTimestamp.toLocalDateTime()
                        : null;

                bookings.add(new Booking(bookingId, tripId,
                        bookingDate, email, bookingAmount,
                        creditCard, price, bookingCancelDate));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//FIN del FINDs


    @Override
    public void remove(Connection connection, Long bookingId)
            throws InstanceNotFoundException {

        String queryString = "DELETE FROM Booking WHERE bookingId = ?";

        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, bookingId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(bookingId,
                        Booking.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//FIN del REMOVE

    @Override
    public void update(Connection connection, Booking booking)
            throws InstanceNotFoundException {


        String queryString = "UPDATE Booking SET tripId = ?, " +
                "bookingDate = ?, email = ?, bookingAmount = ?, " +
                "creditCard = ?, price = ?," +
                " bookingCancelDate = ? WHERE bookingId = ?";

        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(queryString)) {

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
            preparedStatement.setLong(i++, booking.getBookingId());

            int updateRows = preparedStatement.executeUpdate();

            if (updateRows == 0) {
                throw new InstanceNotFoundException(booking.getTripId(),
                        Booking.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
