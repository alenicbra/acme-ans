
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
import acme.realms.Customer;

@GuiService
public class BookingCustomerUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingCustomerRepository BookingCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		Integer bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.BookingCustomerRepository.findBookingById(bookingId);
		status = status && booking != null;
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = status && booking.getCustomer().getId() == customerId && !booking.getIsPublished();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.BookingCustomerRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking bookingWithSameLocatorCode = this.BookingCustomerRepository.findBookingByLocatorCode(booking.getLocatorCode());
		boolean status = bookingWithSameLocatorCode == null || bookingWithSameLocatorCode.getId() == booking.getId();
		super.state(status, "locatorCode", "acme.validation.identifier.repeated.message");
	}

	@Override
	public void perform(final Booking booking) {
		Booking newBooking = this.BookingCustomerRepository.findBookingById(booking.getId());

		newBooking.setFlight(booking.getFlight());
		newBooking.setLocatorCode(booking.getLocatorCode());
		newBooking.setTravelClass(booking.getTravelClass());
		newBooking.setLastNibble(booking.getLastNibble());
		this.BookingCustomerRepository.save(newBooking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.BookingCustomerRepository.findAllFlight();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());
		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
