
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
public class CustomerBookingPassengerCreateService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository customerBookingPassengerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingPassenger BookingPassenger = new BookingPassenger();

		super.getBuffer().addData(BookingPassenger);

	}

	@Override
	public void bind(final BookingPassenger BookingPassenger) {
		super.bindObject(BookingPassenger, "passenger", "booking");
	}

	@Override
	public void validate(final BookingPassenger BookingPassenger) {
		Passenger passenger = BookingPassenger.getPassenger();
		Booking booking = BookingPassenger.getBooking();

		BookingPassenger BookingPassengerCompare = null;
		if (passenger != null && booking != null)
			BookingPassengerCompare = this.customerBookingPassengerRepository.getBookingPassengerByPassengerIdAndBookingId(passenger.getId(), booking.getId());
		boolean status1 = BookingPassengerCompare == null || BookingPassengerCompare.getId() == BookingPassenger.getId();
		super.state(status1, "*", "customer.BookingPassenger.form.error.alreadyCreated");
	}

	@Override
	public void perform(final BookingPassenger BookingPassenger) {
		this.customerBookingPassengerRepository.save(BookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Collection<Passenger> passengers = this.customerBookingPassengerRepository.getAllPassengersByCustomer(customerId);
		Collection<Booking> bookings = this.customerBookingPassengerRepository.getBookingsByCustomerId(customerId);
		SelectChoices passengerChoices = SelectChoices.from(passengers, "fullName", BookingPassenger.getPassenger());
		SelectChoices bookingChoices = SelectChoices.from(bookings, "locatorCode", BookingPassenger.getBooking());
		Dataset dataset = super.unbindObject(BookingPassenger, "passenger", "booking");
		dataset.put("passengers", passengerChoices);
		dataset.put("bookings", bookingChoices);

		super.getResponse().addData(dataset);

	}

}
