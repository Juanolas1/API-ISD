package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.tripservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class TripsExceptionToJsonConversor {

    public static ObjectNode toInputValidationException(InputValidationException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "InputValidation");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "InstanceNotFound");

        exceptionObject.put("instanceID", (ex.getInstanceId() != null)
                ? ex.getInstanceId().toString()
                : null);

        exceptionObject.put("instanceType",
                ex.getInstanceType().substring(
                        ex.getInstanceType().lastIndexOf('.') + 1));
        return exceptionObject;
    }

    public static ObjectNode toDateException(DateException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "DateException");
        exceptionObject.put("tripId", (ex.getTripId() != null)
                ? ex.getTripId()
                : null);

        if (ex.getTripDate() != null) {
            exceptionObject.put("tripDate", ex.getTripDate().toString());
        } else {
            exceptionObject.set("tripDate", null);
        }
        return exceptionObject;
    }
    public static ObjectNode toNoExistUser(NoExistUser ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "NoExistUser");
        exceptionObject.put("user", (ex.getUser() != null)
                ? ex.getUser()
                : null);
        exceptionObject.put("id", (ex.getId() != null)
                ? ex.getId()
                : null);

        return exceptionObject;
    }
    public static ObjectNode toAmountException(AmountException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "AmountException");
        exceptionObject.put("tripId", (ex.getTripId() != null)
                ? ex.getTripId()
                : null);

        if (ex.getAmount() > 0) {
            exceptionObject.put("amount", ex.getAmount());
        } else {
            exceptionObject.set("amount", null);
        }
        return exceptionObject;
    }


    public static ObjectNode toBookingCancelledException(BookingCancelledException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "BookingCancelledException");
        exceptionObject.put("id", (ex.getId() != null)
                ? ex.getId()
                : null);

        if (ex.getId() != null) {
            exceptionObject.put("id", ex.getId());
        } else {
            exceptionObject.set("id", null);
        }
        return exceptionObject;
    }


}
