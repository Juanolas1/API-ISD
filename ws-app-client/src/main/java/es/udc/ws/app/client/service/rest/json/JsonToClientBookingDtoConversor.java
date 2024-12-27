package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientBookingDtoConversor {

    public static ClientBookingDto toClientBookingDto(InputStream jsonBooking) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonBooking);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode bookingObject = (ObjectNode) rootNode;

                JsonNode bookingIdNode = bookingObject.get("bookingId");
                Long bookingId = (bookingIdNode != null)
                        ? bookingIdNode.longValue()
                        : null;

                JsonNode tripIdNode = bookingObject.get("tripId");
                Long tripId = (tripIdNode != null)
                        ? tripIdNode.longValue()
                        : null;

                JsonNode bookingDateNode = bookingObject.get("bookingDate");
                LocalDateTime bookingDate = LocalDateTime.parse(bookingDateNode.textValue().trim());
                String email = bookingObject.get("email").textValue().trim();
                int bookingAmount = bookingObject.get("bookingAmount").intValue();
                String creditCard = bookingObject.get("creditCard").textValue().trim();
                float price = bookingObject.get("price").floatValue();
                JsonNode bookingCancelDateNode = bookingObject.get("bookingCancelDate");
//                LocalDateTime bookingCancelDate = LocalDateTime.parse(bookingCancelDateNode.textValue().trim());

                LocalDateTime canceled = null;
                if (!bookingObject.get("bookingCancelDate").isNull())
                    canceled = LocalDateTime.parse(bookingCancelDateNode.textValue().trim());


                return new ClientBookingDto(bookingId, tripId, bookingDate, email, bookingAmount, creditCard, price, canceled);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientBookingDto> toClientBookingDtos(InputStream jsonBookings) throws ParsingException {
        try {


            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonBookings);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode bookingArray = (ArrayNode) rootNode;
                List<ClientBookingDto> bookingDtos = new ArrayList<>(bookingArray.size());
                for (JsonNode bookingNode : bookingArray) {
                    bookingDtos.add(toClientBookingDto(bookingNode));
                }
                return bookingDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    private static ClientBookingDto toClientBookingDto(JsonNode bookingNode) throws ParsingException {
        if (bookingNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode bookingObject = (ObjectNode) bookingNode;
            JsonNode bookingIdNode = bookingObject.get("bookingId");
            Long bokingId = (bookingIdNode != null)
                    ? bookingIdNode.longValue()
                    : null;

            Long tripId = bookingObject.get("tripId").longValue();

            LocalDateTime bookingDate = null;
            if (!bookingObject.get("bookingDate").isNull())
                bookingDate = LocalDateTime.parse(bookingObject.get("bookingDate").textValue().trim());

            //LocalDateTime bookingDate = LocalDateTime.parse(bookingObject.get("bookingDate").textValue().trim());
            String email = bookingObject.get("email").textValue().trim();
            int bookingAmount = bookingObject.get("bookingAmount").intValue();
            String creditCard = bookingObject.get("creditCard").textValue().trim();
            float price = bookingObject.get("price").floatValue();

            LocalDateTime bookingCancelDate = null;
            if (!bookingObject.get("bookingCancelDate").isNull())
                bookingCancelDate = LocalDateTime.parse(bookingObject.get("bookingCancelDate").textValue().trim());

            //LocalDateTime bookingCancelDate = LocalDateTime.parse(bookingObject.get("bookingCancelDate").textValue().trim());

            return new ClientBookingDto(bokingId, tripId, bookingDate, email, bookingAmount, creditCard, price, bookingCancelDate);
        }
    }


}
