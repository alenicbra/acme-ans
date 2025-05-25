
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.features.customer.passenger.PassengerCustomerRepository;
import acme.realms.Customer;

@GuiService
public class BookingCustomerShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingCustomerRepository	bookingCustomerRepository;

	@Autowired
	private PassengerCustomerRepository	passengerCustomerRepository;


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
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.bookingCustomerRepository.findAllPublishedFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());

		Boolean hasPassengers;
		hasPassengers = !this.passengerCustomerRepository.findPassengerByBookingId(booking.getId()).isEmpty();
		super.getResponse().addGlobal("hasPassengers", hasPassengers);

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "published");
		dataset.put("travelClass", travelClasses);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
