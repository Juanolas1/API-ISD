package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.ClientAmountException;
import es.udc.ws.app.client.service.exceptions.ClientBookingCancelledException;
import es.udc.ws.app.client.service.exceptions.ClientDateException;
import es.udc.ws.app.client.service.exceptions.ClientNoExistUser;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftClientTripService implements ClientTripService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientTripService.endpointAddress";

    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    private ThriftTripService.Client getClient(){
        try {
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);
            return new ThriftTripService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Long addTrip (ClientTripDto tripDto) throws InputValidationException, ClientAmountException, ClientDateException{

        ThriftTripService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try {
            transport.open();
            return client.addTrip(ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(tripDto)).getTripId();
        } catch (ThriftAmountException e) {
            throw new ClientAmountException(e.getTripId(), e.getAmount());
        } catch (ThriftDateException e) {
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime tripDate=LocalDateTime.parse(e.getTripDate(), formatter);
            throw new ClientDateException(e.getTripId(),tripDate);
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            transport.close();
        }
    }

    @Override
    public void updateTrip(ClientTripDto tripDto) throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException {
        ThriftTripService.Client client=getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            client.updateTrip(ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(tripDto));
        } catch (ThriftAmountException e) {
            throw new ClientAmountException(e.getTripId(), e.getAmount());
        } catch (ThriftDateException e) {
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime tripDate=LocalDateTime.parse(e.getTripDate(), formatter);
            throw new ClientDateException(e.getTripId(),tripDate);
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    //LAS CABEZERAS DEL RESTO DE FUNCIONALIDADES
    @Override
    public List<ClientTripDto> findTrips(String city, LocalDateTime tripDateStart, LocalDateTime tripDateEnd) {
        return null;
    }

    @Override
    public Long addBooking(String email, int amount, String creditCard, Long tripId) throws InputValidationException, InstanceNotFoundException, ClientAmountException, ClientDateException {
        return null;
    }

    @Override
    public void cancelBooking(Long bookingId, String email) throws InputValidationException, InstanceNotFoundException, ClientBookingCancelledException, ClientNoExistUser, ClientDateException {

    }

    @Override
    public List<ClientBookingDto> findBookings(String email) throws InputValidationException {
        return null;
    }






}
