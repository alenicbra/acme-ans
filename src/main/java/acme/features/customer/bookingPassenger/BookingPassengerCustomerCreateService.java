
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassengers;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class BookingPassengerCustomerCreateService extends AbstractGuiService<Customer, BookingPassengers> {

	@Autowired
	private BookingPassengerCustomerRepository BookingPassengerCustomerRepository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.BookingPassengerCustomerRepository.getBookingById(bookingId);
		status = status && booking != null && !booking.getIsPublished();

		Integer passengerId = super.getRequest().getData("passengerId", int.class);
		Passenger passenger = this.BookingPassengerCustomerRepository.getPassengerById(passengerId);
		status = status && passenger != null && passenger.getIsPublished();

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = customerId == booking.getCustomer().getId() && customerId == passenger.getCustomer().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.BookingPassengerCustomerRepository.getBookingById(bookingId);
		BookingPassengers bookingPassenger = new BookingPassengers();
		bookingPassenger.setBooking(booking);
		super.getBuffer().addData(bookingPassenger);
	}

	@Override
	public void bind(final BookingPassengers bookingPassenger) {
		super.bindObject(bookingPassenger, "passenger", "booking");
	}

	@Override
	public void validate(final BookingPassengers bookingPassenger) {

	}

	@Override
	public void perform(final BookingPassengers bookingPassenger) {
		this.BookingPassengerCustomerRepository.save(bookingPassenger);
	}

	@Override
	public void unbind(final BookingPassengers bookingPassenger) {
		assert bookingPassenger != null;

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("bookingId", int.class);

		Collection<Passenger> alreadyAddedPassengers = this.BookingPassengerCustomerRepository.getPassengersInBooking(bookingId);
		Collection<Passenger> noAddedPassengers = this.BookingPassengerCustomerRepository.getAllPassengersByCustomerId(customerId).stream().filter(p -> !alreadyAddedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(noAddedPassengers, "fullName", bookingPassenger.getPassenger());

		Dataset dataset = super.unbindObject(bookingPassenger, "passenger", "booking");
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);

	}

}
