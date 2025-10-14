
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerListService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository repository;


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(isCustomer);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		super.getResponse().setAuthorised(customerId == booking.getCustomer().getId());

	}

	@Override
	public void load() {
		Collection<BookingPassenger> BookingPassengers;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		BookingPassengers = this.repository.findBookingPassengersByBookingId(bookingId);

		super.getBuffer().addData(BookingPassengers);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {
		Dataset dataset;

		dataset = super.unbindObject(BookingPassenger, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.birthDate", "passenger.specialNeeds", "passenger.draftMode");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<BookingPassenger> BookingPassengers) {
		int bookingId;
		Booking booking;
		final boolean showCreate;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		showCreate = booking.getDraftMode() == true && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
