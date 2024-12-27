package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.ClientBookingCancelledException;
import es.udc.ws.app.client.service.exceptions.ClientDateException;
import es.udc.ws.app.client.service.exceptions.ClientAmountException;
import es.udc.ws.app.client.service.exceptions.ClientNoExistUser;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientTripService {

    //ADDTRIP [FUNC-1]
    Long addTrip(ClientTripDto trip)
            throws InputValidationException, ClientDateException, ClientAmountException;

    //UPDATE TRIP [FUNC-2]
    void updateTrip(ClientTripDto trip)
            throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException;

    //FIND TRIPS POR CIUDAD O CIUDAD Y FECHAS [FUNC-3]
    List<ClientTripDto> findTrips(String city, LocalDateTime tripDateStart, LocalDateTime tripDateEnd);

    //ADDBOOKING [FUNC-4]
    Long addBooking(String email, int amount, String creditCard, Long tripId)
            throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException;

    //CANCELBOOKING [FUNC-5]
    void cancelBooking(Long bookingId, String email)
            throws InputValidationException, InstanceNotFoundException, ClientBookingCancelledException, ClientNoExistUser, ClientDateException;

    //FINDBOOKINGS [FUNC-6]
    List<ClientBookingDto> findBookings(String email)
            throws InputValidationException;

}
