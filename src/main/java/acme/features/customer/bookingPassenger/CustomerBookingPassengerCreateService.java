
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
public class CustomerBookingPassengerCreateService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int customerId;
		int bookingId;
		Booking booking;
		Customer currentCustomer;

		currentCustomer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		customerId = currentCustomer.getId();

		if (super.getRequest().hasData("bookingId")) {
			bookingId = super.getRequest().getData("bookingId", int.class);
			booking = this.repository.findBookingById(bookingId);

			if (booking != null) {
				status = booking.getCustomer().getId() == customerId && booking.getDraftMode();

				if (status && super.getRequest().getMethod().equals("POST") && super.getRequest().hasData("passenger")) {
					int passengerId = super.getRequest().getData("passenger", int.class);

					if (passengerId != 0) {
						Passenger passenger = this.repository.findPassengerById(passengerId);

						if (passenger == null || passenger.getCustomer().getId() != customerId || passenger.getDraftMode() == true)
							status = false;

						if (status) {
							Collection<Passenger> assignedPassengers = this.repository.findAssignedPassengersByBookingId(bookingId);
							if (assignedPassengers.contains(passenger))
								status = false;
						}
					}
				}
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingPassenger BookingPassenger;
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		BookingPassenger = new BookingPassenger();
		BookingPassenger.setBooking(booking);

		super.getBuffer().addData(BookingPassenger);
	}

	@Override
	public void bind(final BookingPassenger BookingPassenger) {
		super.bindObject(BookingPassenger, "passenger");
	}

	@Override
	public void validate(final BookingPassenger BookingPassenger) {
		;
	}

	@Override
	public void perform(final BookingPassenger BookingPassenger) {
		this.repository.save(BookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger BookingPassenger) {
		Dataset dataset;
		Collection<Passenger> notAssignedPassengers;
		SelectChoices choicesPassengers;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);

		notAssignedPassengers = this.repository.findNotAssignedPassengersByCustomerAndBookingId(customerId, bookingId);
		choicesPassengers = SelectChoices.from(notAssignedPassengers, "fullName", BookingPassenger.getPassenger());

		dataset = super.unbindObject(BookingPassenger, "passenger", "booking");
		dataset.put("passengers", choicesPassengers);

		super.getResponse().addData(dataset);
	}
}
