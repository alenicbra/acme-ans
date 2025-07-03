
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
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository customerBookingRepository;


	@Override
	public void authorise() {
		boolean status;
		Booking b;
		int bId;
		int customerId;

		bId = super.getRequest().getData("id", int.class);
		b = this.customerBookingRepository.findBookingById(bId);
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = b != null && b.getCustomer().getId() == customerId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		List<BookingPassenger> bookingPassengers = (List<BookingPassenger>) this.customerBookingRepository.findAllBookingPassengersByBookingId(booking.getId());
		super.state(bookingPassengers.isEmpty(), "*", "customer.booking.form.error.existingPassengers");

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void perform(final Booking booking) {
		this.customerBookingRepository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);

		Flight flight = this.customerBookingRepository.findBookingById(booking.getId()).getFlight();

		if (!flights.isEmpty()) {
			SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", flight);
			dataset.put("flights", flightChoices);
		}

		super.getResponse().addData(dataset);

	}

}
