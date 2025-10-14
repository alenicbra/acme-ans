
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class CustomerBookingPassengerController extends AbstractGuiController<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerListService		listService;

	@Autowired
	private CustomerBookingPassengerCreateService	createService;

	@Autowired
	private CustomerBookingPassengerShowService		showService;

	@Autowired
	private CustomerBookingPassengerDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
