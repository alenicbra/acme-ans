
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingListService		customerBookingListService;

	@Autowired
	private CustomerBookingShowService		customerBookingShowService;

	@Autowired
	private CustomerBookingCreateService	customerBookingCreateService;

	@Autowired
	private CustomerBookingUpdateService	customerBookingUpdateService;

	@Autowired
	private CustomerBookingPublishService	customerBookingPublishService;

	@Autowired
	private CustomerBookingDeleteService	customerBookingDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.customerBookingListService);
		super.addBasicCommand("show", this.customerBookingShowService);
		super.addBasicCommand("create", this.customerBookingCreateService);
		super.addBasicCommand("update", this.customerBookingUpdateService);
		super.addCustomCommand("publish", "update", this.customerBookingPublishService);
		super.addBasicCommand("delete", this.customerBookingDeleteService);
	}

}
