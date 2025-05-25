
package acme.features.customer.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT c FROM Customer c WHERE c.id = :customerId")
	Customer findCustomerById(Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.id=:passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking findBookingById(int bookingId);

	@Query("SELECT bp.passenger FROM BookingPassenger bp WHERE bp.booking.id = :bookingId")
	List<Passenger> findAllPassengerByBookingId(Integer bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId")
	Collection<Passenger> findPassengersByCustomer(Integer customerId);

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.passenger.id =:passengerId")
	List<BookingPassenger> findAllBookingPassengersByPassengerId(Integer passengerId);

}
