package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripToThriftTripDtoConversor {

    public static Trip toTrip(ThriftTripDto trip){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime tripDate=LocalDateTime.parse(trip.getTripDate(), formatter);
        return new Trip(trip.getTripId(), trip.getCity(), trip.getDescription(),
                (float) trip.getPrice(), trip.getAmount(),tripDate,trip.getMaxAmount());
    }

    public static ThriftTripDto toThriftTripDto(Trip trip){
        return new ThriftTripDto(trip.getTripId(), trip.getCity(), trip.getDescription(),
                trip.getPrice(), (short) trip.getAmount(),trip.getTripDate().toString(),(short) trip.getMaxAmount());
    }





}
