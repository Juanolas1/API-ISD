package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.trip.Trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TripToRestTripDtoConversor {

    public static List<RestTripDto> toRestTripDtos(List<Trip> trips) {

        List<RestTripDto> tripDtos = new ArrayList<>(trips.size());

        for (Trip trip : trips) {
            tripDtos.add(toRestTripDto(trip));
        }

        return tripDtos;
    }

    public static RestTripDto toRestTripDto(Trip trip) {
        return new RestTripDto(trip.getTripId(), trip.getCity(), trip.getDescription(),
                trip.getPrice(), trip.getAmount(), trip.getTripDate().toString(), trip.getMaxAmount());
    }

    public static Trip toTrip(RestTripDto trip) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\"yyyy-MM-dd'T'HH:mm\"");
        LocalDateTime dateTime = LocalDateTime.parse(trip.getTripDate(), formatter);
        return new Trip(trip.getTripId(), trip.getCity(), trip.getDescription(),
                trip.getPrice(), trip.getAmount(), dateTime, trip.getMaxAmount());
    }

}
