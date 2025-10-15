
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerShowService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository repository;


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(isCustomer);

		if (isCustomer && !super.getRequest().getData().isEmpty()) {
			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int BookingPassengerId = super.getRequest().getData("id", int.class);
			BookingPassenger BookingPassenger = this.repository.findBookingPassengerById(BookingPassengerId);

			if (BookingPassenger != null) {
				Booking booking = BookingPassenger.getBooking();
				Passenger passenger = BookingPassenger.getPassenger();

				boolean isAuthorised = booking.getCustomer().getId() == customerId && passenger != null && passenger.getCustomer().getId() == customerId;

				super.getResponse().setAuthorised(isAuthorised);
			} else
				super.getResponse().setAuthorised(false);
		}
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
	public void unbind(final BookingPassenger BookingPassenger) {
		Dataset dataset;
		Collection<Passenger> passengers;
		SelectChoices choicesPassengers;
		Booking booking;

		passengers = this.repository.findAllPassengers();
		choicesPassengers = SelectChoices.from(passengers, "fullName", BookingPassenger.getPassenger());
		booking = BookingPassenger.getBooking();

		dataset = super.unbindObject(BookingPassenger, "booking", "passenger");

		dataset.put("bookingId", BookingPassenger.getBooking().getId());
		dataset.put("passengerId", choicesPassengers.getSelected().getKey());
		dataset.put("passengerName", BookingPassenger.getPassenger().getFullName());

		super.getResponse().addGlobal("booking", booking);
		super.getResponse().addGlobal("passengers", choicesPassengers);
		super.getResponse().addData(dataset);
	}
}
