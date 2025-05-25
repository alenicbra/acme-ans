
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiController
public class PassengerCustomerController extends AbstractGuiController<Customer, Passenger> {

	@Autowired
	private PassengerCustomerListService	passengerCustomerListService;

	@Autowired
	private PassengerCustomerShowService	passengerCustomerShowService;

	@Autowired
	private PassengerCustomerCreateService	passengerCustomerCreateService;

	@Autowired
	private PassengerCustomerUpdateService	passengerCustomerUpdateService;

	@Autowired
	private PassengerCustomerPublishService	passengerCustomerpublishService;

	@Autowired
	private PassengerCustomerDeleteService	passengerCustomerDeleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.passengerCustomerListService);
		super.addBasicCommand("show", this.passengerCustomerShowService);
		super.addBasicCommand("create", this.passengerCustomerCreateService);
		super.addBasicCommand("update", this.passengerCustomerUpdateService);
		super.addCustomCommand("publish", "update", this.passengerCustomerpublishService);
		super.addBasicCommand("delete", this.passengerCustomerDeleteService);
	}

}
