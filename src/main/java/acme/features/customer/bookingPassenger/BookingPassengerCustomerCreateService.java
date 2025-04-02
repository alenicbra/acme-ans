
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class BookingPassengerCustomerCreateService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private BookingPassengerCustomerRepository bookingPassengerCustomerRepository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingPassengerCustomerRepository.getBookingById(bookingId);
		status = status && booking != null && !booking.getIsPublished();
		Integer passengerId = super.getRequest().getData("passengerId", int.class);
		Passenger passenger = this.bookingPassengerCustomerRepository.getPassengerById(passengerId);
		status = status && passenger != null && passenger.getIsPublished();
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = customerId == booking.getCustomer().getId() && customerId == passenger.getCustomer().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingPassengerCustomerRepository.getBookingById(bookingId);
		BookingPassenger bookingPassenger = new BookingPassenger();
		bookingPassenger.setBooking(booking);
		super.getBuffer().addData(bookingPassenger);
	}

	@Override
	public void bind(final BookingPassenger bookingPassenger) {
		super.bindObject(bookingPassenger, "passenger", "booking");
	}

	@Override
	public void validate(final BookingPassenger bookingPassenger) {

	}

	@Override
	public void perform(final BookingPassenger bookingPassenger) {
		this.bookingPassengerCustomerRepository.save(bookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger bookingPassenger) {
		assert bookingPassenger != null;

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<Passenger> alreadyAddedPassengers = this.bookingPassengerCustomerRepository.getPassengersInBooking(bookingId);
		Collection<Passenger> noAddedPassengers = this.bookingPassengerCustomerRepository.getAllPassengersByCustomerId(customerId).stream().filter(p -> !alreadyAddedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(noAddedPassengers, "fullName", bookingPassenger.getPassenger());
		Dataset dataset = super.unbindObject(bookingPassenger, "passenger", "booking");
		dataset.put("passengers", passengerChoices);
		super.getResponse().addData(dataset);

	}

}
