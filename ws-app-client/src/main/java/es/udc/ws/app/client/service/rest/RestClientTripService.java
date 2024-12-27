package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.ClientBookingCancelledException;
import es.udc.ws.app.client.service.exceptions.ClientDateException;
import es.udc.ws.app.client.service.exceptions.ClientAmountException;
import es.udc.ws.app.client.service.exceptions.ClientNoExistUser;
import es.udc.ws.app.client.service.rest.json.JsonToClientBookingDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientTripDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientTripService implements ClientTripService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTripService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addTrip(ClientTripDto trip)
            throws InputValidationException, ClientDateException, ClientAmountException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "trip")
                    .bodyStream(toInputStream(trip), ContentType.create("application/json")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientTripDtoConversor.toClientTripDto(response.getEntity().getContent()).getTripId();

        } catch (InputValidationException | ClientDateException | ClientAmountException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTrip(ClientTripDto trip)
            throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException {
        try {
            HttpResponse response = Request.Put(getEndpointAddress() + "trip/" + trip.getTripId()
                            + "?tripId=" + URLEncoder.encode(String.valueOf(trip.getTripId()), StandardCharsets.UTF_8)
                            + "&city=" + URLEncoder.encode(String.valueOf(trip.getCity()), StandardCharsets.UTF_8)
                            + "&description=" + URLEncoder.encode(String.valueOf(trip.getDescription()), StandardCharsets.UTF_8)
                            + "&price=" + URLEncoder.encode(String.valueOf(trip.getPrice()), StandardCharsets.UTF_8)
                            + "&amount=" + URLEncoder.encode(String.valueOf(trip.getAmount()), StandardCharsets.UTF_8)
                            + "&tripDate=" + URLEncoder.encode(String.valueOf(trip.getTripDate()), StandardCharsets.UTF_8)
                            + "&maxAmount=" + URLEncoder.encode(String.valueOf(trip.getMaxAmount()), StandardCharsets.UTF_8))
                    .bodyStream(toInputStream(trip), ContentType.create("application/json"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException | InputValidationException
                 | ClientDateException | ClientAmountException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ClientTripDto> findTrips(String city, LocalDateTime tripDateStart, LocalDateTime tripDateEnd) {
        try {
            HttpResponse response = Request.Get(getEndpointAddress() + "trip"
                            + "?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                            + "&tripStart=" + URLEncoder.encode(String.valueOf(tripDateStart), StandardCharsets.UTF_8)
                            + "&tripEnd=" + URLEncoder.encode(String.valueOf(tripDateEnd), StandardCharsets.UTF_8))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long addBooking(String email, int amount, String creditCard, Long tripId)
            throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "booking")
                    .bodyForm(Form.form()
                            .add("email", email)
                            .add("bookingAmount", Integer.toString(amount))
                            .add("creditCard", creditCard)
                            .add("tripId", Long.toString(tripId)).build()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientBookingDtoConversor.toClientBookingDto(response.getEntity().getContent()).getBookingId();
        } catch (InputValidationException | InstanceNotFoundException
                 | ClientAmountException | ClientDateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingId, String email)
            throws InputValidationException, InstanceNotFoundException, ClientBookingCancelledException, ClientNoExistUser, ClientDateException  {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "booking/" + bookingId + "/")
                    .bodyForm(Form.form().add("email", email).build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        } catch (InputValidationException | InstanceNotFoundException | ClientBookingCancelledException |
                 ClientNoExistUser | ClientDateException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ClientBookingDto> findBookings(String email) {
        try {

            HttpResponse response = Request.Get(getEndpointAddress()
                            + "booking?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientBookingDtoConversor.toClientBookingDtos(response.getEntity().getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }


    private InputStream toInputStream(ClientTripDto trip) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(
                    new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientTripDtoConversor.toObjectNode(trip));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void validateStatusCode(int successCode, HttpResponse response)
            throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.
                            fromNotFoundErrorCode(
                                    response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.
                            fromBadRequestErrorCode(
                                    response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.
                            fromForbiddenErrorCode(
                                    response.getEntity().getContent());


                case HttpStatus.SC_PRECONDITION_FAILED:
                    throw JsonToClientExceptionConversor.
                            fromPreconditionFailedErrorCode(
                                    response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
