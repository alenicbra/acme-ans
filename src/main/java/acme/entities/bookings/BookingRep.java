
package acme.entities.bookings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passengers.Passenger;

@Repository
public interface BookingRep extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("select br.passenger from BookingPassenger br where br.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(Integer bookingId);

}
