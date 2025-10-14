
package acme.features.customer.bookingPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerShowService extends AbstractGuiService<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingPassengerRepository customerBookingPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer BookingPassengerId = super.getRequest().getData("id", int.class);
		BookingPassenger BookingPassenger = this.customerBookingPassengerRepository.getBookingPassengerByBookingPassengerId(BookingPassengerId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && BookingPassenger.getBooking().getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		BookingPassenger BookingPassengers = this.customerBookingPassengerRepository.getBookingPassengerByBookingPassengerId(id);
		super.getBuffer().addData(BookingPassengers);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {
		Boolean publishedBooking = BookingPassenger.getBooking().getPublished();
		Dataset dataset = super.unbindObject(BookingPassenger, "booking", "passenger");
		dataset.put("bookingLocator", BookingPassenger.getBooking().getLocatorCode());
		dataset.put("passengerFullName", BookingPassenger.getPassenger().getFullName());
		dataset.put("publishedBooking", publishedBooking);
		super.getResponse().addData(dataset);
	}

}
