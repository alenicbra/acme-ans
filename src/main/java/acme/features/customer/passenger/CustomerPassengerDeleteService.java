
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
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getMethod().equals("POST");

		try {
			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Integer passengerId = super.getRequest().getData("id", Integer.class);
			Passenger passenger = this.repository.findPassengerById(passengerId);
			status = status && customerId == passenger.getCustomer().getId() && !passenger.getIsPublished();
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger = new Passenger();
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		List<BookingPassenger> bookingPassengersOfPassenger = this.repository.findAllBookingPassengersByPassengerId(passenger.getId());
		super.state(bookingPassengersOfPassenger.isEmpty(), "*", "customer.passenger.form.error.associatedBookings");

	}

	@Override
	public void perform(final Passenger passenger) {

		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "isPublished");
		super.getResponse().addData(dataset);

	}

}
