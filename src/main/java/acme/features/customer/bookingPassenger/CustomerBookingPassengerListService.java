
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerListService extends AbstractGuiService<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingPassengerRepository customerBookingPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<BookingPassenger> BookingPassengers = this.customerBookingPassengerRepository.getBookingPassengersByCustomerId(customerId);
		super.getBuffer().addData(BookingPassengers);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {

		Dataset dataset = super.unbindObject(BookingPassenger, "booking", "passenger");
		dataset.put("bookingLocator", BookingPassenger.getBooking().getLocatorCode());
		dataset.put("passengerFullName", BookingPassenger.getPassenger().getFullName());
		super.getResponse().addData(dataset);
	}

}
