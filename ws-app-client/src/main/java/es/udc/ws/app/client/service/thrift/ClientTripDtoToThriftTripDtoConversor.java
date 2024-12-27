package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ClientTripDtoToThriftTripDtoConversor {

    public static ThriftTripDto toThriftTripDto(ClientTripDto clientTripDto){
        Long tripId= clientTripDto.getTripId();

        return new ThriftTripDto(tripId == null ? -1 : tripId.longValue(), clientTripDto.getCity(),
                clientTripDto.getDescription(), (double) clientTripDto.getPrice(),
                (short) clientTripDto.getAmount(), clientTripDto.getTripDate().toString(),
                (short) clientTripDto.getMaxAmount());
    }

    private static ClientTripDto toClientTripDto(ThriftTripDto trip){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime tripDate=LocalDateTime.parse(trip.getTripDate(), formatter);

        return new ClientTripDto(trip.getTripId(),
                trip.getCity(),
                trip.getDescription(),
                (float) trip.getPrice(),
                trip.getAmount(),
                tripDate,
                trip.getMaxAmount());
    }





}
