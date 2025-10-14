
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("select b from Booking  b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select p from Passenger p where p.customer.id = :id")
	Collection<Passenger> findPassengersByCustomerId(int id);

	@Query("select br from BookingPassenger br where br.booking.id = :bookingId")
	Collection<BookingPassenger> findBookingPassengersByBookingId(int bookingId);

	@Query("select br.passenger from BookingPassenger br where br.booking.id = :bookingId")
	Collection<Passenger> findAssignedPassengersByBookingId(int bookingId);

	@Query("select br.passenger from BookingPassenger br where br.booking.id = :bookingId")
	Passenger findAssignedPassengerByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("select br.booking from BookingPassenger br where br.passenger.id = :id")
	Booking findBookingByPassengerId(int id);

	@Query("select c from Customer c")
	Collection<Customer> findAllCustomers();

	@Query("select br from BookingPassenger br where br.id = :id")
	BookingPassenger findBookingPassengerById(int id);

	@Query("select p from Passenger p")
	Collection<Passenger> findAllPassengers();

	@Query("select br.booking from BookingPassenger br where br.id = :id")
	Booking findBookingByBookingPassengerId(int id);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = false and p not in (select br.passenger from BookingPassenger br where br.booking.id = :bookingId)")
	Collection<Passenger> findNotAssignedPassengersByCustomerAndBookingId(int customerId, int bookingId);

}
