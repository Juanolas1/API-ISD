package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.booking.SqlBookingDao;
import es.udc.ws.app.model.booking.SqlBookingDaoFactory;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripService;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.DateException;
import es.udc.ws.app.model.tripservice.exceptions.AmountException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import es.udc.ws.app.model.tripservice.exceptions.TripNotRemovableException;
import es.udc.ws.app.model.tripservice.exceptions.DateException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
public class AppServiceTest {

    private final String Email1 = "pepe@gmail.com";
    private final String Email2 = "alvy@gmail.com";

    private final String CREDIT_CARD1 = "1234567890123456";
    private final String CREDIT_CARD2 = "6543210987654321";


    private static TripService tripService = null;
    private static SqlBookingDao bookingDao = null;
    private static SqlTripDao tripDao = null;

    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        tripService = TripServiceFactory.getService();
        bookingDao = SqlBookingDaoFactory.getDao();
        tripDao = SqlTripDaoFactory.getDao();
    }

    private Trip getValidTrip(String description) {
        return new Trip( "Lugo", description,
                LocalDateTime.now().minusHours(120).withNano(0),
                10, LocalDateTime.of(2023, 1,
                1, 1, 1, 1), 10);
    }

    private Trip getValidTrip() {
        return getValidTrip("Buen viaje");
    }

    private Booking getValidBooking(String email, Long tripId) {
        Long aux = (long)2.0;
        return new Booking(tripId,
                LocalDateTime.now().minusHours(80).withNano(0), email,3,
                "6654579876543216",2,null);
    }

    private Booking getValidBooking(long tripId) {
        return getValidBooking("fulano@gmail.com", tripId);
    }

    private Trip createMovie(Trip trip) {

        Trip addedtrip = null;
        try {
            addedtrip = tripService.addTrip(trip);
        } catch (InputValidationException | AmountException | DateException e) {
            throw new RuntimeException(e);
        }
        return addedtrip;

    }

    public void removeTrip(Long tripId) throws InstanceNotFoundException,
            TripNotRemovableException {
        DataSource dataSource =
                DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection
                        .TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                tripDao.remove(connection, tripId);

                connection.commit();
            } catch (InstanceNotFoundException  e) {
                connection.commit();
                throw e;
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

    public void removeBooking(Long bookingId) throws InstanceNotFoundException{
        DataSource dataSource =
                DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection
                        .TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);


                bookingDao.remove(connection, bookingId);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
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

    public Trip findTrip(Long tripId) throws InputValidationException,
            InstanceNotFoundException{
        DataSource dataSource =
                DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            return tripDao.find(connection, tripId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Booking findBooking(Long bookingId) throws InputValidationException
            , InstanceNotFoundException{
        DataSource dataSource =
                DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            return bookingDao.find(connection, bookingId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private Trip createTrip(Trip trip) {

        Trip addedTrip = null;
        try {
            addedTrip = tripService.addTrip(trip);
        } catch (InputValidationException | AmountException | DateException e) {
            throw new RuntimeException(e);
        }
        return addedTrip;
    }

    private Booking createBooking(Booking booking) {
        Booking addedBooking;
        try {
            addedBooking = tripService.addBooking(booking.getEmail(),
                    booking.getBookingAmount(), booking.getCreditCard(),
                    booking.getTripId());

        } catch (InputValidationException | InstanceNotFoundException | AmountException | DateException e) {
            throw new RuntimeException(e);
        }
        return addedBooking;
    }

    @Test
    public void testAddTripAndFindTrip() throws InputValidationException
            , InstanceNotFoundException, TripNotRemovableException {

        Trip trip = getValidTrip();
        Trip addedTrip = null;

        try {
            //CREACION
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedTrip = tripService.addTrip(trip);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);
            //BUSQUEDA

            Trip foundTrip = findTrip(addedTrip.getTripId());

            assertEquals(addedTrip, foundTrip);
            assertEquals(foundTrip.getAmount(), trip.getAmount());
            assertEquals(foundTrip.getCity(), trip.getCity());
            assertEquals(foundTrip.getDescription(), trip.getDescription());
            assertEquals(foundTrip.getMaxAmount(), trip.getMaxAmount());
            assertEquals(foundTrip.getPrice(), foundTrip.getPrice());
            assertTrue((foundTrip.getCreationDate()
                    .compareTo(beforeCreationDate) >= 0)
                    && (foundTrip.getCreationDate().
                    compareTo(afterCreationDate) <= 0));
        } catch (AmountException e) {
            e.printStackTrace();
        } catch (DateException e) {
            e.printStackTrace();
        } finally {
            if (addedTrip != null)
                removeTrip(addedTrip.getTripId());
        }
    }


    @Test
    public void testAddInvalidTrip() {
        assertThrows(DateException.class, () -> {
            Trip trip = getValidTrip();
            trip.setTripDate(trip.getCreationDate());
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(AmountException.class, () -> {
            Trip trip = getValidTrip();
            trip.setAmount(trip.getAmount() + 1);
            Trip addedTrip = tripService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
    }

    @Test
    public void testUpdateTrip() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException, AmountException, DateException {
        Trip trip = createTrip(getValidTrip());
        try {
            Trip tripToUpdate = trip;

            tripToUpdate.setCity("Nueva ciudad");

            tripService.updateTrip(tripToUpdate);

            Trip updatedTrip = findTrip(tripToUpdate.getTripId());

            tripToUpdate.setCreationDate(updatedTrip.getCreationDate());

            assertEquals(tripToUpdate, updatedTrip);
            assertEquals(updatedTrip.getTripId(), updatedTrip.getTripId());
            assertEquals(updatedTrip.getCity(), updatedTrip.getCity());
            assertEquals(updatedTrip.getDescription(),
                    updatedTrip.getDescription());
            assertEquals(updatedTrip.getCreationDate(),
                    updatedTrip.getCreationDate());
            assertEquals(updatedTrip.getPrice(), updatedTrip.getPrice());
            assertEquals(updatedTrip.getAmount(), updatedTrip.getAmount());
            assertEquals(updatedTrip.getTripDate(), updatedTrip.getTripDate());
            assertEquals(updatedTrip.getMaxAmount(),
                    updatedTrip.getMaxAmount());

        } finally {
            removeTrip(trip.getTripId());
        }
    }//testUpdateTrip

    @Test
    public void testUpdateInvalidTripDate() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException {
        Long tripId = createTrip(getValidTrip()).getTripId();
        try {
            Trip trip = findTrip(tripId);
            trip.setTripDate(LocalDateTime.now().withNano(0));
            assertThrows(DateException.class, () -> tripService
                    .updateTrip(trip));
        } finally {
            removeTrip(tripId);
        }
    }

    @Test
    public void testUpdateInvalidTripMaxAmount() throws
            InputValidationException, InstanceNotFoundException,
            TripNotRemovableException {
        Long tripId = createTrip(getValidTrip()).getTripId();
        try {
            Trip trip = findTrip(tripId);
            trip.setMaxAmount(0);
            assertThrows(AmountException.class, () -> tripService
                    .updateTrip(trip));
        } finally {
            removeTrip(tripId);
        }
    }

    @Test
    public void testFindTripsByCity() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException, AmountException, DateException {

        Trip trip1 = getValidTrip();
        Trip trip2 = getValidTrip();
        Trip trip3 = getValidTrip();

        Trip addedTrip1 = null;
        Trip addedTrip2 = null;
        Trip addedTrip3 = null;

        try {
            trip3.setCity("Ourense");
            addedTrip1 = tripService.addTrip(trip1);
            addedTrip2 = tripService.addTrip(trip2);
            addedTrip3 = tripService.addTrip(trip3);


            List<Trip> foundTripsLugo
                    = tripService.findTrips("Lugo",null,null);
            List<Trip> foundTripsOurense
                    = tripService.findTrips("Ourense",null,null);

            assertEquals(foundTripsLugo.size(), 2);
            assertEquals(foundTripsLugo.get(0),addedTrip1);
            assertEquals(foundTripsLugo.get(1),addedTrip2);
            assertEquals(foundTripsOurense.size(), 1);
            assertEquals(foundTripsOurense.get(0),addedTrip3);


        } finally {
            if (addedTrip1 != null) {
                removeTrip(addedTrip1.getTripId());
            }
            if (addedTrip2 != null) {
                removeTrip(addedTrip2.getTripId());
            }
            if (addedTrip3 != null) {
                removeTrip(addedTrip3.getTripId());
            }
        }
    }


    @Test
    public void testFindTripsByCityAndDate() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException {

        Trip trip1 = getValidTrip();
        Trip trip2 = getValidTrip();
        Trip trip3 = getValidTrip();

        Trip addedTrip1 = null;
        Trip addedTrip2 = null;
        Trip addedTrip3 = null;

        try {
            trip3.setCity("Ourense");

            LocalDateTime ini =  LocalDate.of(2022, 10, 1).atStartOfDay();
            LocalDateTime valid = LocalDate.of(2024, 1, 10).atStartOfDay();
            LocalDateTime fin =  LocalDate.of(2022, 10, 20).atStartOfDay();
            LocalDateTime noValid = LocalDate.of(2022, 2, 1).atStartOfDay();

            trip1.setTripDate(valid);
            trip2.setTripDate(noValid);
            trip3.setTripDate(valid);


            addedTrip1 = tripService.addTrip(trip1);
            addedTrip2 = tripService.addTrip(trip2);
            addedTrip3 = tripService.addTrip(trip3);


            List<Trip> foundTripsLugo = tripService.findTrips("Lugo",ini,fin);
            List<Trip> foundTripsOurense = tripService.findTrips("Ourense",ini,fin);

            assertEquals(foundTripsLugo.size(), 1);
            assertEquals(foundTripsLugo.get(0),addedTrip1);

            assertEquals(foundTripsOurense.size(), 1);
            assertEquals(foundTripsOurense.get(0),addedTrip3);


        } catch (AmountException e) {
            e.printStackTrace();
        } catch (DateException e) {
            e.printStackTrace();
        } finally {
            if (addedTrip1 != null) {
                removeTrip(addedTrip1.getTripId());
            }
            if (addedTrip2 != null) {
                removeTrip(addedTrip2.getTripId());
            }
            if (addedTrip3 != null) {
                removeTrip(addedTrip3.getTripId());
            }
        }
    }
/*
    @Test
    public void testCancelBooking() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException {
        Trip trip = getValidTrip();
        Trip addedTrip = null;
        Booking addedBooking=null;

        try {
            addedTrip = tripService.addTrip(trip);
            Booking originalBooking
                    = createBooking(getValidBooking(addedTrip.getTripId()));

            addedBooking= tripService.addBooking(originalBooking.getEmail(),
                    originalBooking.getBookingAmount(), originalBooking.getCreditCard(),
                    originalBooking.getTripId());

            tripService.cancelBooking(addedBooking.getBookingId(),
                    addedBooking.getEmail());

            Booking updatedBooking = findBooking(addedBooking.getBookingId());

            assertNotNull(addedBooking.getBookingDate());
            assertNull(updatedBooking.getBookingDate());
            assertEquals(addedBooking.getBookingId(),
                    updatedBooking.getBookingId());
            assertEquals(addedBooking.getTripId(),updatedBooking.getTripId());
//           assertEquals(addedBooking.getBookingDate(),
//                   updatedBooking.getBookingDate());
            assertEquals(addedBooking.getEmail(),updatedBooking.getEmail());
            assertEquals(addedBooking.getCreditCard(),
                    updatedBooking.getCreditCard());
            assertEquals(addedBooking.getBookingAmount(),
                    updatedBooking.getBookingAmount());

        } catch (MaxAmountException e) {
            e.printStackTrace();
        } catch (DateException e) {
            e.printStackTrace();
        } finally {
            if(addedBooking!=null) {
                removeBooking(addedBooking.getBookingId());
            }
            if(addedTrip!=null) {
                removeTrip(addedTrip.getTripId());
            }

        }
    }
*/


    @Test
    public void testAddBookingAndFindBooking() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException {
        Trip trip = getValidTrip();
        Trip addedTrip = null;
        Booking addedBooking=null;

        try {
            addedTrip = tripService.addTrip(trip);

            addedBooking = tripService.addBooking(Email1, 2,CREDIT_CARD1, addedTrip.getTripId());

            Booking foundBooking = findBooking(addedBooking.getBookingId());

            assertEquals(foundBooking,addedBooking);
            assertEquals(foundBooking.getBookingId(),
                    addedBooking.getBookingId());
            assertEquals(foundBooking.getTripId(),addedBooking.getTripId());
            assertEquals(foundBooking.getBookingDate(),
                    addedBooking.getBookingDate());
            assertEquals(foundBooking.getEmail(),Email1);
            assertEquals(foundBooking.getBookingAmount(),
                    2);
            assertEquals(foundBooking.getCreditCard(),
                    CREDIT_CARD1);
            assertEquals(foundBooking.getBookingDate(),addedBooking.getBookingDate());
            assertEquals(foundBooking.getBookingId(),
                    addedBooking.getBookingId());

        } catch (AmountException e) {
            e.printStackTrace();
        } catch (DateException e) {
            e.printStackTrace();
        } finally {
            if(addedBooking!=null)
                removeBooking(addedBooking.getBookingId());

            if (addedTrip != null)
                removeTrip(addedTrip.getTripId());
        }
    }

    @Test
    public void testAddInvalidBooking() {
        assertThrows(InstanceNotFoundException.class, () -> {
            Booking booking= getValidBooking(-1);
            booking.setBookingAmount(-2);
            booking = tripService.addBooking(booking.getEmail(), booking.getBookingAmount(),
                    booking.getCreditCard(), booking.getTripId());
            removeBooking(booking.getBookingId());
        });
    }



/*
    @Test
    public void testFindBookings() throws InputValidationException,
            InstanceNotFoundException, TripNotRemovableException, MaxAmountException, DateException {

        Trip trip = getValidTrip();
        trip = tripService.addTrip(trip);

        Booking b1 = null;
        Booking b2 = null;
        Booking b3 = null;

        try{
            List<Booking> bookings = new ArrayList<>();
            b1 = tripService.addBooking(Email1, 1,CREDIT_CARD1,trip.getTripId());
            b2 = tripService.addBooking(Email1, 1,CREDIT_CARD1,trip.getTripId());
            b3 = tripService.addBooking(Email2, 2,CREDIT_CARD2,trip.getTripId());

            bookings = tripService.findBookings("pepe@gmail.com");

            assertEquals(bookings.size(),2);


        } finally {
            if(b1!=null) {
                removeBooking(b1.getBookingId());
            }
            if(b2!=null) {
                removeBooking(b2.getBookingId());
            }
            if(b3!=null) {
                removeBooking(b3.getBookingId());
            }
            removeTrip(trip.getTripId());
        }
    }
*/




}







