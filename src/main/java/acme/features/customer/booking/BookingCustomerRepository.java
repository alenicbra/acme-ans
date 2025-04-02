
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@Repository
public interface BookingCustomerRepository extends AbstractRepository {

	@Query("SELECT c FROM Customer c WHERE c.id = :customerId")
	Customer findCustomerById(Integer customerId);

	@Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("SELECT b FROM Booking b")
	List<Booking> findAllBooking();

	@Query("SELECT c FROM Customer c WHERE c.userAccount.id = :userAccountId")
	Customer findCustomerByUserAccountId(Integer userAccountId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomer(Integer customerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findFlightById(Integer flightId);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findAllFlight();

}
