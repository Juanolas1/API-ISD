package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestTripDtoConversor {

    public static ObjectNode toObjectNode(RestTripDto trip) {

        ObjectNode tripObject = JsonNodeFactory.instance.objectNode();

        if (trip.getTripId() != null) {
            tripObject.put("tripId", trip.getTripId());
        }
        tripObject.put("city", trip.getCity())
                .put("description", trip.getDescription())
                .put("price", trip.getPrice())
                .put("amount", trip.getAmount())
                .put("tripDate", trip.getTripDate())
                .put("maxAmount", trip.getMaxAmount());

        return tripObject;
    }

    public static ArrayNode toArrayNode(List<RestTripDto> trips) {

        ArrayNode tripsNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < trips.size(); i++) {
            RestTripDto tripDto = trips.get(i);
            ObjectNode tripObject = toObjectNode(tripDto);
            tripsNode.add(tripObject);
        }
        return tripsNode;
    }

    public static RestTripDto toServiceTripDto(InputStream jsonTrip)
            throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrip);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode tripObject = (ObjectNode) rootNode;

                JsonNode tripIdNode = tripObject.get("tripId");
                Long tripId = (tripIdNode != null)
                        ? tripIdNode.longValue()
                        : null;

                String city = tripObject.get("city").textValue().trim();
                String description = tripObject.get("description").textValue().trim();
                float price = tripObject.get("price").floatValue();
                int amount = tripObject.get("amount").intValue();
                String tripDate = tripObject.get("tripDate").toString();
                int maxAmount = tripObject.get("maxAmount").intValue();

                return new RestTripDto(tripId, city, description, price, amount, tripDate, maxAmount);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
