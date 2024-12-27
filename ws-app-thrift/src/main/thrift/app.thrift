namespace java es.udc.ws.app.thrift


struct ThriftTripDto {
    1: i64 tripId;
    2: string city;
    3: string description;
    4: double price;
    5: i16 amount;
    6: string tripDate;
    7: i16 maxAmount;
}



exception ThriftInputValidationException {
    1: string message;
}


exception ThriftInstanceNotFoundException {
    1: string instanceId;
    2: string instanceType;
}


exception ThriftAmountException {
    1: i64 tripId;
    2: i16 amount;
}


exception ThriftDateException {
    1: i64 tripId;
    2: string tripDate;
}





service ThriftTripService {

      ThriftTripDto addTrip(1: ThriftTripDto tripDto) throws (1: ThriftInputValidationException e1,
      2: ThriftAmountException e2, 3: ThriftDateException e3)

      void updateTrip(1: ThriftTripDto trip) throws (1: ThriftInputValidationException e1,
       2: ThriftInstanceNotFoundException e2, 3: ThriftAmountException e3,
       4: ThriftDateException e4)

}

