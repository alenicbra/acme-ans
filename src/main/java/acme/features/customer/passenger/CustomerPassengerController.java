
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerListService	customerPassengerListService;

	@Autowired
	private CustomerPassengerShowService	customerPassengerShowService;

	@Autowired
	private CustomerPassengerCreateService	customerPassengerCreateService;

	@Autowired
	private CustomerPassengerUpdateService	customerPassengerUpdateService;

	@Autowired
	private CustomerPassengerPublishService	customerPassengerpublishService;

	@Autowired
	private CustomerPassengerDeleteService	customerPassengerDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.customerPassengerListService);
		super.addBasicCommand("show", this.customerPassengerShowService);
		super.addBasicCommand("create", this.customerPassengerCreateService);
		super.addBasicCommand("update", this.customerPassengerUpdateService);
		super.addCustomCommand("publish", "update", this.customerPassengerpublishService);
		super.addBasicCommand("delete", this.customerPassengerDeleteService);
	}

}
