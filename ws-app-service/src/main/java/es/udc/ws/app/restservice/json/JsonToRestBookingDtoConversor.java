package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestBookingDto;

import java.util.List;

public class JsonToRestBookingDtoConversor {

    public static ObjectNode toObjectNode(RestBookingDto booking) {
        ObjectNode bookingNode = JsonNodeFactory.instance.objectNode();

        if (booking.getBookingId() != null) {
            bookingNode.put("bookingId", booking.getBookingId());
        }

        bookingNode.put("bookingId", booking.getBookingId())
                .put("tripId", booking.getTripId())
                .put("bookingDate", booking.getBookingDate())
                .put("email", booking.getEmail())
                .put("bookingAmount", booking.getBookingAmount())
                .put("creditCard", booking.getCreditCard())
                .put("price", booking.getPrice())
                .put("bookingCancelDate", booking.getBookingCancelDate());
        return bookingNode;
    }

    public static ArrayNode toArrayNode(List<RestBookingDto> bookings) {

        ArrayNode bookingsNode = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < bookings.size(); i++) {
            RestBookingDto bookingDto = bookings.get(i);
            ObjectNode bookingObject = toObjectNode(bookingDto);
            bookingsNode.add(bookingObject);
        }
        return bookingsNode;
    }
}
