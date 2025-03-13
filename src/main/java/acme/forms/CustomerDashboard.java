
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.bookings.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				lastFiveDestinations;
	private Money						moneyInBookings;
	private Map<TravelClass, Integer>	bookingsByTravelClass;
	private Money						countBookingCost;
	private Money						averageBookingCost;
	private Money						minimumBookingCost;
	private Money						maximumBookingCost;
	private Money						standardDeviationBookingCost;
	private Integer						countNumberPassengers;
	private Double						averageNumberPassengers;
	private Integer						minimumNumberPassengers;
	private Integer						maximumNumberPassengers;
	private Double						standardDeviationNumberPassengers;

}
