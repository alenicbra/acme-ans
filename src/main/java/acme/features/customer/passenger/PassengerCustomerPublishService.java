
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class PassengerCustomerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerCustomerRepository passengerCustomerRepository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.passengerCustomerRepository.getPassengerById(passengerId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && passenger.getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Passenger passenger = this.passengerCustomerRepository.getPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {

	}

	@Override
	public void validate(final Passenger passenger) {

	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setIsPublished(true);
		this.passengerCustomerRepository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "isPublished");

		super.getResponse().addData(dataset);
	}

}
