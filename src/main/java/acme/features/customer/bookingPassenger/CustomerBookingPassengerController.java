
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
	private CustomerBookingPassengerCreateService	customerBookingPassengerCreateService;

	@Autowired
	private CustomerBookingPassengerDeleteService	customerBookingPassengerDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.customerBookingPassengerCreateService);
		super.addBasicCommand("delete", this.customerBookingPassengerDeleteService);

	}
}
