package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonToClientTripDtoConversor {

    public static ObjectNode toObjectNode(ClientTripDto trip) throws
            IOException {

        ObjectNode tripObject = JsonNodeFactory.instance.objectNode();
        if (!Objects.isNull(trip.getTripId())) {
            tripObject.put("tripId", trip.getTripId());
        }

        tripObject.put("city", trip.getCity())
                .put("description", trip.getDescription())
                .put("price", trip.getPrice()).put("amount", trip.getAmount())
                .put("tripDate", trip.getTripDate().toString())
                .put("maxAmount", trip.getMaxAmount());
        return tripObject;
    }

    public static ClientTripDto toClientTripDto(InputStream jsonTrip)
            throws ParsingException {

        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrip);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientTripDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    public static List<ClientTripDto> toClientTripDtos(InputStream jsonTrips) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrips);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode tripsArray = (ArrayNode) rootNode;
                List<ClientTripDto> tripDtos = new ArrayList<>(tripsArray.size());
                for (JsonNode tripNode : tripsArray) {
                    tripDtos.add(toClientTripDto(tripNode));
                }
                return tripDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    public static ClientTripDto toClientTripDto(JsonNode tripNode) throws ParsingException {
        if (tripNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode tripObject = (ObjectNode) tripNode;

            JsonNode tripIdNode = tripObject.get("tripId");
            Long tripId = (tripIdNode != null)
                    ? tripIdNode.longValue()
                    : null;

            String city = tripObject.get("city").textValue().trim();
            String description = tripObject.get("description").textValue().trim();
            float price = tripObject.get("price").floatValue();
            int amount = tripObject.get("amount").intValue();
            JsonNode tripDateNode = tripObject.get("tripDate");
            LocalDateTime tripDate = LocalDateTime.parse(tripDateNode.textValue().trim());
            int maxAmount = tripObject.get("maxAmount").intValue();

            return new ClientTripDto(tripId, city, description, price, amount, tripDate, maxAmount);
        }
    }
}
