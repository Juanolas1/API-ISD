package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.ClientTripServiceFactory;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }
        ClientTripService clientTripService = ClientTripServiceFactory.getService();
        if ("-addExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[]{4, 5});

            //-addExc <city> <description> <date> <price> <maxPlaces>
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime date = LocalDateTime.parse(args[3], formatter);

                Long tripId = clientTripService.addTrip(new
                        ClientTripDto(0, args[1], args[2], Float.parseFloat(args[4]), 0, date, Integer.parseInt(args[5])));

                System.out.println("trip " + tripId
                        + " created successfully");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-reserve".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{2, 4});
            //-reserve <userEmail> <excursionId> <creditCardNumber> <places>
            try {
                Long bookingId = clientTripService.addBooking(args[1], Integer.parseInt(args[4]), args[3], Long.valueOf(args[2]));
                System.out.println("booking " + bookingId + " created successfully");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-updateExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[]{1, 5, 6});
            //-updateExc <excId> <city> <description> <date> <price> <maxPlaces>
            try {//AQUI VA UN CERO?
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime date = LocalDateTime.parse(args[4], formatter);
                clientTripService.updateTrip(new ClientTripDto(Long.parseLong(args[1]),
                        args[2], args[3], Float.parseFloat(args[5]), 0, date, Integer.parseInt(args[6])));
                System.out.println("Your trip " + args[1] +
                        " has been successfully. ");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[]{1});
            //-cancel <reservationId> <userEmail>
            try {
                clientTripService.cancelBooking(Long.valueOf(args[1]), args[2]);
                System.out.println("Your booking " + args[1] +
                        " has been cancelled. ");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-findExcursions".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[]{});
            //-findExcursion <city> <fromDate> <toDate>
            try {
                LocalDateTime inicio = null;
                LocalDateTime fin = null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                if (!args[2].isEmpty()) {
                    LocalDate aux = LocalDate.parse(args[2], formatter);
                    inicio = aux.atStartOfDay();
                }
                if (!args[3].isEmpty()) {
                    LocalDate aux = LocalDate.parse(args[3], formatter);
                    fin = aux.atStartOfDay();

                }

                List<ClientTripDto> trips = clientTripService.findTrips(args[1], inicio, fin);

                System.out.println("Found " + trips.size() +
                        " trip(s) the date '" + args[2] + "' and '"
                        + args[3] + "'" + " in the city '" + args[1] + "'");
                for (ClientTripDto tripDto : trips) {
                    System.out.println("**\ntripId: " + tripDto.getTripId() +
                            "\n city: " + tripDto.getCity() +
                            "\n description: " + tripDto.getDescription() +
                            "\n price: " + tripDto.getPrice() +
                            "\n amount: " + tripDto.getAmount() +
                            "\n tripDate: " + tripDto.getTripDate() +
                            "\n maxAmount: " + tripDto.getMaxAmount());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-findReservations".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});
            //-findReservations <userEmail>
            try {

                List<ClientBookingDto> bookings = clientTripService.findBookings(args[1]);
                System.out.println("Found " + bookings.size() +
                        " bookings(s)");
                for (ClientBookingDto bookingDto : bookings) {
                    System.out.println("**\nbookingId: " + bookingDto.getBookingId() +
                            "\n tripId: " + bookingDto.getTripId() +
                            "\n bookingDate: " + bookingDto.getBookingDate() +
                            "\n email: " + bookingDto.getEmail() +
                            "\n bookingAmount: " + bookingDto.getBookingAmount() +
                            "\n creditCard: " + bookingDto.getCreditCard() +
                            "\n price: " + bookingDto.getPrice() +
                            "\n bookingCancelDate: " + bookingDto.getBookingCancelDate());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            printUsage();
        }
    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if (expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addExc] TripServiceClient -addExc  <city> "
                + "<description> <date> <price> "
                + "<maxAmount>\n" +
                "    [reserve] TripServiceClient -reserve <email> <bookingId> <creditCard> <amount>\n" +
                "    [updateExc] TripServiceClient -findRaces <tripId>"
                + "<city> <description> <date> <price> <maxAmount>\n" +
                "    [cancel] TripServiceClient -inscribe <bookingId>"
                + " <email>\n" +
                "    [findExcursion] TripServiceClient -findInscriptions"
                + " <city> <tripDateStart> <tripDateEnd>\n" +
                "    [findReservations] TripServiceClient -update"
                + " <email>\n");
    }

}
