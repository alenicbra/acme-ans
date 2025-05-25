
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.passengers.Passenger;
import acme.features.customer.passenger.PassengerCustomerRepository;
import acme.realms.Customer;

@GuiService
public class BookingCustomerPublishService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingCustomerRepository	bookingCustomerRepository;

	@Autowired
	private PassengerCustomerRepository	passengerCustomerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.bookingCustomerRepository.findBookingById(bookingId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && booking.getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.bookingCustomerRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		boolean status1 = !booking.getLastNibble().isBlank();
		super.state(status1, "lastNibble", "customer.booking.form.error.lastNibble");
		List<Passenger> passengers = this.passengerCustomerRepository.findPassengerByBookingId(booking.getId());
		boolean status2 = !passengers.isEmpty();
		super.state(status2, "*", "customer.booking.form.error.passengers");
		List<Passenger> passenger2 = this.passengerCustomerRepository.findPassengerByBookingId(booking.getId());
		Boolean passengersNotPublished = passenger2.stream().anyMatch(p -> !p.getPublished());
		boolean status3 = !passengersNotPublished;
		super.state(status3, "*", "customer.booking.form.error.passengers2");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setPublished(true);
		this.bookingCustomerRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.bookingCustomerRepository.findAllPublishedFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());

		Boolean hasPassengers;
		hasPassengers = !this.passengerCustomerRepository.findPassengerByBookingId(booking.getId()).isEmpty();
		super.getResponse().addGlobal("hasPassengers", hasPassengers);

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "published", "id");
		dataset.put("travelClass", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
