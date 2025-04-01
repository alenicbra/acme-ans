
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class PassengerCustomerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerCustomerRepository passengerCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		if (!super.getRequest().getData().isEmpty()) {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.passengerCustomerRepository.getBookingById(bookingId);
			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = status && booking.getCustomer().getId() == customerId;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (!super.getRequest().getData().containsKey("bookingId"))
			passengers = this.passengerCustomerRepository.getPassengersByCustomer(customerId);
		else {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.passengerCustomerRepository.findPassengerByBookingId(bookingId);
		}
		super.getBuffer().addData(passengers);
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "isPublished");

		super.getResponse().addData(dataset);
		super.addPayload(dataset, passenger, "specialNeeds");
	}

}
