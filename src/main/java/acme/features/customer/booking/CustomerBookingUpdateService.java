
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository customerBookingRepository;


	@Override
	public void authorise() {
		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Customer.class)) {
			int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int bookingId = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;
			Booking currentBooking = this.customerBookingRepository.findBookingById(bookingId);

			if (currentBooking != null && !currentBooking.getIsPublished() && currentBooking.getCustomer().getId() == userId) {
				isAuthorised = true;

				if ("POST".equalsIgnoreCase(super.getRequest().getMethod())) {
					boolean validFlight = true;
					boolean validTravelClass = true;

					if (super.getRequest().hasData("flight")) {
						int flightId = super.getRequest().getData("flight", int.class);
						if (flightId != 0) {
							Flight selectedFlight = this.customerBookingRepository.findFlightById(flightId);
							validFlight = selectedFlight != null && !selectedFlight.getDraftMode();
						}
					}

					if (super.getRequest().hasData("travelClass")) {
						TravelClass selectedClass = super.getRequest().getData("travelClass", TravelClass.class);
						Collection<TravelClass> availableClasses = this.customerBookingRepository.findAllDistinctTravelClass();
						validTravelClass = selectedClass == null || availableClasses.contains(selectedClass);
					}

					isAuthorised = isAuthorised && validFlight && validTravelClass;
				}
			}
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking bookingWithSameLocatorCode = this.customerBookingRepository.findBookingByLocatorCode(booking.getLocatorCode());
		boolean status = bookingWithSameLocatorCode == null || bookingWithSameLocatorCode.getId() == booking.getId();
		super.state(status, "locatorCode", "acme.validation.identifier.repeated.message");
	}

	@Override
	public void perform(final Booking booking) {
		Booking newBooking = this.customerBookingRepository.findBookingById(booking.getId());

		newBooking.setFlight(booking.getFlight());
		newBooking.setLocatorCode(booking.getLocatorCode());
		newBooking.setTravelClass(booking.getTravelClass());
		newBooking.setLastNibble(booking.getLastNibble());
		this.customerBookingRepository.save(newBooking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());
		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "IsPublished", "id");
		dataset.put("travelClass", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
