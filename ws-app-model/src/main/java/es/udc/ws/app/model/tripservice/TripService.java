package es.udc.ws.app.model.tripservice;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {


    //ADDTRIP [FUNC-1]
    Trip addTrip(Trip trip)
            throws InputValidationException, AmountException, DateException;

    //UPDATE TRIP [FUNC-2]
    void updateTrip(Trip trip)
            throws InputValidationException, InstanceNotFoundException, AmountException, DateException;

    //FIND TRIPS POR CIUDAD O CIUDAD Y FECHAS [FUNC-3]
    List<Trip> findTrips(String city, LocalDateTime ini,
                         LocalDateTime fin);

    //ADDBOOKING [FUNC-4]
    Booking addBooking(String email, int bookingAmount, String creditCard, Long tripId)
            throws InputValidationException, InstanceNotFoundException, AmountException, DateException;

    //CANCELBOOKING [FUNC-5]
    void cancelBooking(Long bookingId, String email)
            throws InputValidationException, InstanceNotFoundException, DateException,
            NoExistUser, BookingCancelledException;

    //FINDBOOKINGS [FUNC-6]
    List<Booking> findBookings(String email)
            throws InputValidationException;

}
