package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.booking.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingToRestBookingDtoConversor {

    public static RestBookingDto toRestBookingDto(Booking booking) {
        return new RestBookingDto(booking.getBookingId(), booking.getTripId(),
                booking.getBookingDate() != null
                        ? booking.getBookingDate().toString()
                        : null, booking.getEmail(),
                booking.getBookingAmount(), booking.getCreditCard(),
                booking.getPrice(),
                booking.getBookingCancelDate() != null
                        ? booking.getBookingCancelDate().toString()
                        : null);
    }

    public static List<RestBookingDto> toRestBookingDtos(
            List<Booking> bookings) {
        List<RestBookingDto> bookingDtos = new ArrayList<>(bookings.size());
        for (Booking booking : bookings) {
            bookingDtos.add(toRestBookingDto(booking));
        }
        return bookingDtos;
    }

}
