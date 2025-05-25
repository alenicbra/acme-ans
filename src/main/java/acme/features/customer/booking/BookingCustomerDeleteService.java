
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.features.customer.passenger.PassengerCustomerRepository;
import acme.realms.Customer;

@GuiService
public class BookingCustomerDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingCustomerRepository	bookingCustomerRepository;

	@Autowired
	private PassengerCustomerRepository	passengerCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		try {

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.bookingCustomerRepository.findBookingById(bookingId);
			status = status && !(booking == null) && customerId == booking.getCustomer().getId() && !booking.getPublished();

		} catch (Exception E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingCustomerRepository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		List<BookingPassenger> bookingPassengers = (List<BookingPassenger>) this.bookingCustomerRepository.findAllBookingPassengerByBookingId(booking.getId());
		super.state(bookingPassengers.isEmpty(), "*", "customer.booking.form.error.existingRecord");

	}

	@Override
	public void perform(final Booking booking) {
		this.bookingCustomerRepository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.bookingCustomerRepository.findAllPublishedFlights();

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "published", "id");
		dataset.put("travelClass", travelClasses);

		Boolean hasPassengers;
		hasPassengers = !this.passengerCustomerRepository.findPassengerByBookingId(booking.getId()).isEmpty();
		super.getResponse().addGlobal("hasPassengers", hasPassengers);

		if (!flights.isEmpty()) {
			SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
			dataset.put("flights", flightChoices);
		}

		super.getResponse().addData(dataset);

	}

}
