package es.udc.ws.app.model.tripservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.booking.SqlBookingDao;
import es.udc.ws.app.model.booking.SqlBookingDaoFactory;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

public class TripServiceImpl implements TripService {

    private final DataSource dataSource;
    private final SqlTripDao tripDao;
    private final SqlBookingDao bookingDao;

    public TripServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        tripDao = SqlTripDaoFactory.getDao();
        bookingDao = SqlBookingDaoFactory.getDao();
    }



    private void validateTrip(Trip trip) throws InputValidationException, DateException, AmountException {

        PropertyValidator.validateMandatoryString("city", trip.getCity());
        PropertyValidator.validateMandatoryString("description",
                trip.getDescription());
        PropertyValidator.validateDouble("price", trip.getPrice(), 0, 100000);


        if (!(trip.getTripDate().minusHours(72).
                isAfter(LocalDateTime.now()))) {
            throw new DateException(trip.getTripId(),
                    trip.getTripDate());
        }

        if (trip.getAmount() < 0 || trip.getAmount() > trip.getMaxAmount()) {
            throw new AmountException(trip.getTripId(),
                    trip.getMaxAmount());

        }
    }

    private void validateEmail(String email) throws InputValidationException {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InputValidationException("Invalid email"
                    + " (it must have the format name@email.xx): "
                    + email);
        }
    }

    @Override
    public Trip addTrip(Trip trip) throws InputValidationException, AmountException, DateException {

        validateTrip(trip);
        trip.setCreationDate(LocalDateTime.now().withNano(0));
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.
                        TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                if (trip.getTripDate().minusHours(72).
                        isAfter(LocalDateTime.now())) {
                    if ((trip.getMaxAmount() > 0)) {
                        Trip createdTrip = tripDao.create(connection, trip);
                        connection.commit();
                        return createdTrip;
                    } else {
                        connection.commit();
                        throw new AmountException(trip.getTripId(), trip.getMaxAmount());
                    }
                } else {
                    connection.commit();
                    throw new DateException(trip.getTripId(),
                            trip.getTripDate());
                }


            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTrip(Trip trip) throws InputValidationException,
            InstanceNotFoundException, AmountException, DateException {
        validateTrip(trip);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.
                        TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Trip original = tripDao.find(connection, trip.getTripId());
                trip.setCreationDate(original.getCreationDate());

                if (trip.getMaxAmount() < original.getAmount() || trip.getMaxAmount() == 0) {
                    connection.commit();
                    throw new AmountException(trip.getTripId(),
                            trip.getMaxAmount());
                }
                if ((original.getTripDate().isAfter(trip.getTripDate()))
                        || (original.getTripDate().isBefore(LocalDateTime.now().withNano(0)))
                        || trip.getTripDate().compareTo(LocalDateTime.now().withNano(0).plusHours(72)) <= 0) {
                    connection.commit();
                    throw new DateException(trip.getTripId(), trip.getTripDate());
                }
                trip.setAmount(original.getAmount());
                tripDao.update(connection, trip);
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error | DateException |
                     AmountException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Trip> findTrips(String city, LocalDateTime ini,
                                LocalDateTime fin) {

        try (Connection connection = dataSource.getConnection()) {
            return tripDao.findTrips(connection, city, ini, fin);
        } catch (SQLException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking addBooking(String email, int bookingAmount, String creditCard, Long tripId) throws InputValidationException,
            InstanceNotFoundException, AmountException, DateException {

        PropertyValidator.validateCreditCard(creditCard);
        PropertyValidator.validateMandatoryString("email", email);
        validateEmail(email);

        try (Connection connection = dataSource.getConnection()) {
            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Trip trip = tripDao.find(connection, tripId);
                Booking booking = new Booking(tripId, LocalDateTime.now(), email,
                        bookingAmount, creditCard, trip.getPrice(), null);
                if (!trip.getTripDate().isBefore(LocalDateTime.now().plusDays(1))) {
                    if ((bookingAmount < 6 && bookingAmount > 0)
                            && (bookingAmount < (trip.getMaxAmount() - trip.getAmount()))) {
                        booking = bookingDao.create(connection, booking);
                        trip.setAmount(trip.getAmount() + bookingAmount);
                        tripDao.update(connection, trip);
                        connection.commit();
                        return booking;
                    } else {
                        connection.commit();
                        throw new AmountException(trip.getTripId(), trip.getMaxAmount() - trip.getAmount());
                    }
                } else {
                    connection.commit();
                    throw new DateException(tripId, booking.getBookingDate().withSecond(0));
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error  e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingId, String email) throws
            InputValidationException, InstanceNotFoundException, DateException, NoExistUser, BookingCancelledException {

        PropertyValidator.validateMandatoryString("email", email);
        validateEmail(email);

        try (Connection connection = dataSource.getConnection()) {
            connection.setTransactionIsolation(Connection.
                    TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            Booking original = bookingDao.find(connection, bookingId);
            Trip trip = tripDao.find(connection, original.getTripId());
            if (original.getBookingCancelDate() != null) {
                connection.commit();
                throw new BookingCancelledException(original.getBookingId());
            }

            if (!Objects.equals(original.getEmail(), email)) {
                connection.commit();
                throw new NoExistUser(email, original.getBookingId());
            }

            if (original.getBookingDate() != null && trip.getTripDate().minusHours(48).
                    isAfter(LocalDateTime.now())) {
                original.setBookingDate(null);
                original.setBookingCancelDate(LocalDateTime.now().withNano(0));
                trip.setAmount(trip.getAmount() + original.getBookingAmount());
                tripDao.update(connection, trip);
                bookingDao.update(connection, original);
                connection.commit();
            } else {
                connection.commit();
                throw new DateException(trip.getTripId(), trip.getTripDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findBookings(String email)
            throws InputValidationException {

        PropertyValidator.validateMandatoryString("email", email);
        validateEmail(email);

        try (Connection connection = dataSource.getConnection()) {
            return bookingDao.findBookings(connection, email);
        } catch (SQLException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
