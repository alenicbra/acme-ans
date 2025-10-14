
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
	Booking getBookingById(int bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId")
	Collection<Passenger> getAllPassengersByCustomer(int customerId);

	@Query("SELECT br.passenger FROM BookingPassenger br WHERE br.booking.id=:bookingId")
	Collection<Passenger> getPassengersInBooking(int bookingId);

	@Query("SELECT b FROM Booking b WHERE b.customer.id=:customerId and b.published=false")
	Collection<Booking> getBookingsByCustomerId(int customerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.booking.customer.id=:customerId")
	Collection<BookingPassenger> getBookingPassengersByCustomerId(int customerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.id=:BookingPassengerId")
	BookingPassenger getBookingPassengerByBookingPassengerId(int BookingPassengerId);

	@Query("SELECT br.booking FROM BookingPassenger br WHERE br.booking.id=:bookingId")
	Booking getBookingFromBookingPassenger(int bookingId);

	@Query("SELECT br.passenger FROM BookingPassenger br WHERE br.passenger.id=:passengerId")
	Passenger getPassengerFromBookingPassenger(int passengerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.passenger.id = :passengerId and br.booking.id = :bookingId")
	BookingPassenger getBookingPassengerByPassengerIdAndBookingId(int passengerId, int bookingId);

}
