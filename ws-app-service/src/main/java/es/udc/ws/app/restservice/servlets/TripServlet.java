package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.DateException;
import es.udc.ws.app.model.tripservice.exceptions.AmountException;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.app.restservice.dto.TripToRestTripDtoConversor;
import es.udc.ws.app.restservice.json.JsonToRestTripDtoConversor;
import es.udc.ws.app.restservice.json.TripsExceptionToJsonConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripServlet extends RestHttpServletTemplate {

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {


        ServletUtils.checkEmptyPath(req);

        RestTripDto tripDto = JsonToRestTripDtoConversor.toServiceTripDto(req.getInputStream());
        Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);


        try {
            trip = TripServiceFactory.getService().addTrip(trip);
        } catch (DateException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED,
                    TripsExceptionToJsonConversor.toDateException(ex),
                    null);
            return;
        } catch (AmountException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED,
                    TripsExceptionToJsonConversor.toAmountException(ex),
                    null);
            return;
        }

        tripDto = TripToRestTripDtoConversor.toRestTripDto(trip);
        String tripURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + trip.getTripId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", tripURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestTripDtoConversor.toObjectNode(tripDto), headers);
    }


    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        Long tripId = ServletUtils.getIdFromPath(req, "trip");

        RestTripDto tripDto = JsonToRestTripDtoConversor.toServiceTripDto(req.getInputStream());

        if (!tripId.equals(tripDto.getTripId())) {
            throw new InputValidationException("Invalid Request: invalid tripId");
        }
        Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);

        try {
            TripServiceFactory.getService().updateTrip(trip);
        } catch (DateException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED,
                    TripsExceptionToJsonConversor.toDateException(ex),
                    null);
            return;
        } catch (AmountException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_PRECONDITION_FAILED,
                    TripsExceptionToJsonConversor.toAmountException(ex),
                    null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    TripsExceptionToJsonConversor.toInstanceNotFoundException(ex),
                    null);
            return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String city = req.getParameter("city");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateStart = null;
        LocalDateTime dateEnd = null;

        String tripStart = req.getParameter("tripStart");
        if (!tripStart.isEmpty()) dateStart = LocalDateTime.parse(tripStart, formatter);

        String tripEnd = req.getParameter("tripEnd");
        if (!tripEnd.isEmpty()) dateEnd = LocalDateTime.parse(tripEnd, formatter);

        List<Trip> trips = TripServiceFactory.getService().findTrips(city, dateStart, dateEnd);

        List<RestTripDto> tripDtos = TripToRestTripDtoConversor.toRestTripDtos(trips);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestTripDtoConversor.toArrayNode(tripDtos), null);
    }

}
