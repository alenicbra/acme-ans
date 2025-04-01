
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class BookingCustomerCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingCustomerRepository bookingCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int customerId = principal.getId();
		Customer customer = this.bookingCustomerRepository.findCustomerById(customerId);
		Date date = MomentHelper.getCurrentMoment();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		int length = 6 + random.nextInt(3);
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = chars.charAt(random.nextInt(chars.length()));
			sb.append(c);
		}

		booking.setCustomer(customer);
		booking.setPurchaseMoment(date);
		booking.setLocatorCode(sb.toString());
		booking.setIsPublished(false);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		boolean status = this.bookingCustomerRepository.findBookingByLocatorCode(booking.getLocatorCode()) == null;
		super.state(status, "locatorCode", "acme.validation.identifier.repeated.message");
	}

	@Override
	public void perform(final Booking booking) {
		this.bookingCustomerRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.bookingCustomerRepository.findAllFlight();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());
		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
