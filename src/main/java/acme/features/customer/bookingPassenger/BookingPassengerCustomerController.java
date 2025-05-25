
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class BookingPassengerCustomerController extends AbstractGuiController<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingPassengerCustomerCreateService	bookingPassengerCustomerCreateService;

	@Autowired
	private BookingPassengerCustomerListService		bookingPassengerCustomerListService;

	@Autowired
	private BookingPassengerCustomerShowService		bookingPassengerCustomerShowService;

	@Autowired
	private BookingPassengerCustomerDeleteService	bookingPassengerCustomerDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.bookingPassengerCustomerCreateService);
		super.addBasicCommand("list", this.bookingPassengerCustomerListService);
		super.addBasicCommand("show", this.bookingPassengerCustomerShowService);
		super.addBasicCommand("delete", this.bookingPassengerCustomerDeleteService);

	}
}
