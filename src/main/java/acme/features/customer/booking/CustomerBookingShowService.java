
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
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	customerBookingRepository;

	@Autowired
	private CustomerPassengerRepository	customerPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		try {

			Integer bookingId = super.getRequest().getData("id", Integer.class);
			Booking booking = this.customerBookingRepository.findBookingById(bookingId);

			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			status = booking.getCustomer().getId() == customerId;

		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();
		List<Passenger> passengers = this.customerPassengerRepository.findAllPassengerByBookingId(booking.getId());

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished");
		dataset.put("travelClass", travelClasses);

		if (!flights.isEmpty()) {
			SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
			dataset.put("flights", flightChoices);
		}

		dataset.put("passengers", passengers);

		dataset.put("city", booking.getFlight().destination());
		dataset.put("country", this.customerBookingRepository.findDestinationAirportByFlightId(booking.getFlight().getId()).getCountry());

		super.getResponse().addData(dataset);
	}

}
