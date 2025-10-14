
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class CustomerBookingPassengerController extends AbstractGuiController<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingPassengerCreateService	CustomerBookingPassengerCreateService;

	@Autowired
	private CustomerBookingPassengerListService		CustomerBookingPassengerListService;

	@Autowired
	private CustomerBookingPassengerShowService		CustomerBookingPassengerShowService;

	@Autowired
	private CustomerBookingPassengerDeleteService	CustomerBookingPassengerDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.CustomerBookingPassengerListService);
		super.addBasicCommand("create", this.CustomerBookingPassengerCreateService);
		super.addBasicCommand("show", this.CustomerBookingPassengerShowService);
		super.addBasicCommand("delete", this.CustomerBookingPassengerDeleteService);

	}
}
