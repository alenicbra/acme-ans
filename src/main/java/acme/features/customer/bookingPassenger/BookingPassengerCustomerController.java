
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class BookingPassengerCustomerController extends AbstractGuiController<Customer, BookingPassenger> {

	@Autowired
	private BookingPassengerCustomerCreateService BookingPassengerCustomerCreateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.BookingPassengerCustomerCreateService);

	}
}
