package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.tripservice.TripServiceFactory;
import es.udc.ws.app.model.tripservice.exceptions.AmountException;
import es.udc.ws.app.model.tripservice.exceptions.DateException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.thrift.ThriftTripService;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public class ThriftTripServiceImpl implements ThriftTripService.Iface {

    @Override
    public ThriftTripDto addTrip(ThriftTripDto tripDto) throws ThriftInputValidationException,
            ThriftAmountException, ThriftDateException {

        Trip trip = TripToThriftTripDtoConversor.toTrip(tripDto);

        try{
            Trip newTrip = TripServiceFactory.getService().addTrip(trip);
            return TripToThriftTripDtoConversor.toThriftTripDto(newTrip);
        } catch (AmountException e) {
            throw new ThriftAmountException(e.getTripId(),(short) e.getAmount());
        } catch (DateException e) {
            throw new ThriftDateException(e.getTripId(),e.getTripDate().toString());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void updateTrip(ThriftTripDto tripDto) throws ThriftInputValidationException, ThriftInstanceNotFoundException,
    ThriftAmountException, ThriftDateException{


        Trip trip = TripToThriftTripDtoConversor.toTrip(tripDto);
        try {
            TripServiceFactory.getService().updateTrip(trip);
        } catch (AmountException e) {
            throw new ThriftAmountException(e.getTripId(),(short) e.getAmount());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (DateException e) {
            throw new ThriftDateException(e.getTripId(),e.getTripDate().toString());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

}
