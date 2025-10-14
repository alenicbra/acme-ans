
package acme.features.customer.bookingPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerDeleteService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int BookingPassengerId;
		BookingPassenger BookingPassenger;
		Booking booking;
		Passenger passenger;
		Customer customer;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		if (status && super.getRequest().hasData("id")) {
			BookingPassengerId = super.getRequest().getData("id", int.class);
			BookingPassenger = this.repository.findBookingPassengerById(BookingPassengerId);
			booking = BookingPassenger == null ? null : BookingPassenger.getBooking();
			passenger = BookingPassenger == null ? null : BookingPassenger.getPassenger();
			customer = booking == null ? null : booking.getCustomer();

			status = BookingPassenger != null && super.getRequest().getPrincipal().hasRealm(customer) && booking.getDraftMode() && passenger != null && passenger.getCustomer().equals(customer);

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingPassenger BookingPassenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		BookingPassenger = this.repository.findBookingPassengerById(id);

		super.getBuffer().addData(BookingPassenger);
	}

	@Override
	public void bind(final BookingPassenger BookingPassenger) {
		;
	}

	@Override
	public void validate(final BookingPassenger BookingPassenger) {
		;
	}

	@Override
	public void perform(final BookingPassenger BookingPassenger) {
		this.repository.delete(BookingPassenger);
	}

}
