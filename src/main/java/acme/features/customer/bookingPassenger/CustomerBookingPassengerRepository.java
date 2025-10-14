
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking findBookingById(Integer bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.id=:passengerId")
	Passenger findPassengerById(Integer passengerId);

	@Query("SELECT bp FROM BookingPassenger bp where bp.id = :id")
	BookingPassenger findBookingPassengerById(int id);

	@Query("SELECT bp FROM BookingPassenger bp where bp.booking.id = :bookingId")
	Collection<BookingPassenger> findBookingPassengerByBookingId(int bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId")
	Collection<Passenger> findAllPassengersByCustomerId(Integer customerId);

	@Query("SELECT bp.passenger FROM BookingPassenger bp WHERE bp.booking.id=:bookingId")
	Collection<Passenger> findAllPassengersByBookingId(Integer bookingId);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.isPublished = true and p not in (select br.passenger from BookingPassenger br where br.booking.id = :bookingId)")
	Collection<Passenger> findNOTAssignedPassengersByCustomerIdAndBookingId(int customerId, int bookingId);

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.booking.id =:bookingId AND bp.passenger.id =:passengerId")
	BookingPassenger findBookingPassengerByBothIds(Integer bookingId, Integer passengerId);

}
