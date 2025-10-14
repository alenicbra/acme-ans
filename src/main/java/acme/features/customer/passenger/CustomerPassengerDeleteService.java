
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingPassenger;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository customerPassengerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		try {

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int passengerId = super.getRequest().getData("id", int.class);
			Passenger passenger = this.customerPassengerRepository.findPassengerById(passengerId);
			status = status && !(passenger == null) && customerId == passenger.getCustomer().getId() && !passenger.getPublished();

		} catch (Exception E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("passengerId", int.class);
		Passenger passenger = this.customerPassengerRepository.findPassengerById(passengerId);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		List<BookingPassenger> bookingPassengers = (List<BookingPassenger>) this.customerPassengerRepository.findAllBookingPassengersByPassengerId(passenger.getId());
		super.state(bookingPassengers.isEmpty(), "*", "customer.booking.form.error.existingRecord");

	}

	@Override
	public void perform(final Passenger passenger) {
		this.customerPassengerRepository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
		super.getResponse().addData(dataset);

	}

}
