-- ----------------------------------------------------------------------------
-- Model
-- -----------------------------------------------------------------------------
DROP TABLE Booking;
DROP TABLE Trip;

-- --------------------------------- Trip ------------------------------------

CREATE TABLE Trip ( tripId BIGINT NOT NULL AUTO_INCREMENT,
                    city VARCHAR(40) COLLATE latin1_bin NOT NULL,
                    description VARCHAR(255) COLLATE latin1_bin NOT NULL,
                    creationDate DATETIME NOT NULL,
                    price FLOAT NOT NULL,
                    amount INT NOT NULL,
                    tripDate  DATETIME NOT NULL,
                    maxAmount INT NOT NULL,
                    CONSTRAINT TripPK PRIMARY KEY(tripId),
                    CONSTRAINT price CHECK (price > 0),
                    CONSTRAINT maxAmount CHECK (maxAmount > 0)
) ENGINE = InnoDB;
-- --------------------------------- Booking ------------------------------------
CREATE TABLE Booking ( bookingId BIGINT NOT NULL AUTO_INCREMENT,
                       tripId BIGINT NOT NULL,
                       bookingDate DATETIME,
                       email VARCHAR(255) COLLATE latin1_bin NOT NULL,
                       bookingAmount INT NOT NULL,
                       creditCard VARCHAR(255) COLLATE latin1_bin NOT NULL,
                       price FLOAT NOT NULL,
                       bookingCancelDate DATETIME,
                       CONSTRAINT BookingPK PRIMARY KEY(bookingId),
                       CONSTRAINT BookingTripIdFK FOREIGN KEY (tripId)
                           REFERENCES Trip(tripId) ON DELETE CASCADE
) ENGINE = InnoDB;
