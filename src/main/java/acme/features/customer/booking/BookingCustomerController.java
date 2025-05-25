
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
	private BookingCustomerListService		bookingCustomerListService;

	@Autowired
	private BookingCustomerShowService		bookingCustomerShowService;

	@Autowired
	private BookingCustomerCreateService	bookingCustomerCreateService;

	@Autowired
	private BookingCustomerUpdateService	bookingCustomerUpdateService;

	@Autowired
	private BookingCustomerPublishService	bookingCustomerPublishService;
	@Autowired
	private BookingCustomerDeleteService	bookingCustomerDeleteService;


	@PostConstruct
	protected void setup() {
		super.addBasicCommand("list", this.bookingCustomerListService);
		super.addBasicCommand("show", this.bookingCustomerShowService);
		super.addBasicCommand("create", this.bookingCustomerCreateService);
		super.addBasicCommand("update", this.bookingCustomerUpdateService);
		super.addCustomCommand("publish", "update", this.bookingCustomerPublishService);
		super.addBasicCommand("delete", this.bookingCustomerDeleteService);
	}
}
