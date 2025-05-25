
package acme.features.customer.bookingPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiService
public class BookingPassengerCustomerDeleteService extends AbstractGuiService<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingPassengerCustomerRepository bookingPassengerCustomerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer BookingPassengerId = super.getRequest().getData("id", int.class);
		BookingPassenger BookingPassenger = this.bookingPassengerCustomerRepository.getBookingPassengerByBookingPassengerId(BookingPassengerId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && BookingPassenger.getBooking().getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int BookingPassengerId = super.getRequest().getData("BookingPassengerId", int.class);
		BookingPassenger BookingPassenger = this.bookingPassengerCustomerRepository.getBookingPassengerByBookingPassengerId(BookingPassengerId);

		super.getBuffer().addData(BookingPassenger);
	}

	@Override
	public void bind(final BookingPassenger BookingPassenger) {
		super.bindObject(BookingPassenger, "passenger", "booking");
	}

	@Override
	public void validate(final BookingPassenger BookingPassenger) {

	}

	@Override
	public void perform(final BookingPassenger BookingPassenger) {
		this.bookingPassengerCustomerRepository.delete(BookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {
		Dataset dataset = super.unbindObject(BookingPassenger, "booking", "passenger");

		super.getResponse().addData(dataset);

	}

}
