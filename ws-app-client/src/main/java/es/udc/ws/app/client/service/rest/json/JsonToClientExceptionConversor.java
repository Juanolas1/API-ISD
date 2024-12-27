package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized  error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(
            JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }


    public static Exception fromNotFoundErrorCode(InputStream ex)
            throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON "
                        + "(object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                }if (errorType.equals("NoValidAmount")) {
                    return toClientNoValidAmount(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: "
                            + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }



    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceID").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromPreconditionFailedErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType) {
                    case "DateException":
                        return toClientDateException(rootNode);
                    case "AmountException":
                        return toClientAmountException(rootNode);
                    case "TripNotRemovableException":
                        return toClientTripNotRemovableException(rootNode);
                    default:
                        throw new ParsingException("Unrecognized error type: "
                                + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType) {
                    case "DateException":
                        return toClientDateException(rootNode);
                    case "BookingCancelledException":
                        return toClientBookingCancelledException(rootNode);
                    case "NoExistUser":
                        return toClientNoExistUser(rootNode);

                    default:
                        throw new ParsingException("Unrecognized error type: "
                                + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }




    private static ClientDateException toClientDateException(JsonNode rootNode) {
        Long tripId = (rootNode.get("tripId").textValue() != null) ? Long.parseLong(rootNode.get("tripId").textValue()) : null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime tripDate = LocalDateTime.parse(rootNode.get("tripDate").textValue(), df);
        return new ClientDateException(tripId, tripDate);
    }


    private static ClientAmountException toClientAmountException(JsonNode rootNode) {
        Long tripId = (rootNode.get("tripId").textValue()!=null) ? Long.parseLong(rootNode.get("tripId").textValue()) : null;
        int amount = rootNode.get("amount").intValue();
        return new ClientAmountException(tripId, amount);
    }

    private static ClientNoValidAmount toClientNoValidAmount(JsonNode rootNode) {
        int amount = rootNode.get("amount").intValue();
        return new ClientNoValidAmount(amount);
    }

    private static ClientNoExistUser toClientNoExistUser(JsonNode rootNode) {
        Long id = (rootNode.get("id").textValue() != null) ? Long.parseLong(rootNode.get("id").textValue()) : null;
        String user = (rootNode.get("user").textValue() != null) ? (rootNode.get("user").textValue()) : null;
        return new ClientNoExistUser(user, id);
    }

    private static ClientBookingCancelledException toClientBookingCancelledException(JsonNode rootNode) {
        Long id = (rootNode.get("id").textValue() != null) ? Long.parseLong(rootNode.get("id").textValue()) : null;
        return new ClientBookingCancelledException(id);
    }


    private static ClientTripNotRemovableException toClientTripNotRemovableException(JsonNode rootNode) {
        Long tripId = Long.valueOf(rootNode.get("tripId").textValue());
        return new ClientTripNotRemovableException(tripId);
    }

}
