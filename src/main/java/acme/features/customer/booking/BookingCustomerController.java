
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.Booking;
import acme.realms.Customer;

@GuiController
public class BookingCustomerController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private BookingCustomerListService		listService;

	@Autowired
	private BookingCustomerShowService		showService;

	@Autowired
	private BookingCustomerCreateService	createService;

	@Autowired
	private BookingCustomerUpdateService	updateService;

	@Autowired
	private BookingCustomerPublishService	publishService;


	@PostConstruct
	protected void setup() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
