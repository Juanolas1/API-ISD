package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.booking.Booking;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.app.restservice.dto.BookingToRestBookingDtoConversor;
import es.udc.ws.app.restservice.dto.RestBookingDto;
import es.udc.ws.app.restservice.json.JsonToRestBookingDtoConversor;
import es.udc.ws.app.restservice.json.TripsExceptionToJsonConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingServlet extends RestHttpServletTemplate {


    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        //ServletUtils.checkEmptyPath(req);
        if (req.getPathInfo() == null) {

            int bookingAmount = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "bookingAmount"));
            String email = ServletUtils.getMandatoryParameter(req, "email");
            String creditCard = ServletUtils.getMandatoryParameter(req, "creditCard");
            Long tripId = Long.parseLong(ServletUtils.getMandatoryParameter(req, "tripId"));
            Booking booking = null;
            try {

                booking = TripServiceFactory.getService().addBooking(email, bookingAmount, creditCard, tripId);

                assert booking != null;
                RestBookingDto bookingDto = BookingToRestBookingDtoConversor.toRestBookingDto(booking);
                String bookingURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + booking.getBookingId().toString();
                Map<String, String> headers = new HashMap<>(1);
                headers.put("Location", bookingURL);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                        JsonToRestBookingDtoConversor.toObjectNode(bookingDto), headers);

            } catch (AmountException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED, TripsExceptionToJsonConversor.toAmountException(ex), null);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, TripsExceptionToJsonConversor.toInstanceNotFoundException(ex), null);
            } catch (DateException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED,
                        TripsExceptionToJsonConversor.toDateException(ex),
                        null);
                return;
            }
        } else {
            Long bookingId = ServletUtils.getIdFromPath(req, "booking");
            String email = ServletUtils.getMandatoryParameter(req, "email");
            try {
                TripServiceFactory.getService().cancelBooking(bookingId, email);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        TripsExceptionToJsonConversor.toInputValidationException(ex),
                        null);
                return;
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        TripsExceptionToJsonConversor.toInstanceNotFoundException(ex),
                        null);
                return;
            } catch (BookingCancelledException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        TripsExceptionToJsonConversor.toBookingCancelledException(ex),
                        null);
            } catch (DateException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        TripsExceptionToJsonConversor.toDateException(ex),
                        null);
            }catch (NoExistUser ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        TripsExceptionToJsonConversor.toNoExistUser(ex),
                        null);
            }

        }
    }


    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String email = req.getParameter("email");

        List<Booking> bookings;
        try {
            bookings = TripServiceFactory.getService().findBookings(email);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                    TripsExceptionToJsonConversor.toInputValidationException(ex),
                    null);
            return;
        }

        List<RestBookingDto> bookingDtos = BookingToRestBookingDtoConversor.toRestBookingDtos(bookings);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestBookingDtoConversor.toArrayNode(bookingDtos), null);
    }


}
