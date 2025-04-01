
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
public class BookingCustomerShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingCustomerRepository	BookingCustomerRepository;

	@Autowired
	private PassengerCustomerRepository	PassengerCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		Integer bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.BookingCustomerRepository.findBookingById(bookingId);
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = status && booking.getCustomer().getId() == customerId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.BookingCustomerRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.BookingCustomerRepository.findAllFlight();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());
		List<Passenger> passengers = this.PassengerCustomerRepository.findPassengerByBookingId(booking.getId());
		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished");
		dataset.put("travelClass", travelClasses);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("passengers", passengers);

		super.getResponse().addData(dataset);
	}

}
