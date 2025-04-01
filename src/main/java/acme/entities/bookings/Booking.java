
package acme.entities.bookings;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.Flight;
import acme.features.customer.passenger.PassengerCustomerRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	private TravelClass			travelClass;

	@ValidMoney
	private Money				price;

	@Optional
	private String				lastNibble;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				isPublished;

	// Relationships -------------------------------------------------------------

	@Valid
	@Mandatory
	@ManyToOne
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne
	private Flight				flight;


	@Transient
	public Money getPrice() {
		Money price = new Money();
		PassengerCustomerRepository PassengerCustomerRepository = SpringHelper.getBean(PassengerCustomerRepository.class);

		if (this.getFlight() == null) {
			price.setAmount(0.0);
			price.setCurrency("EUR");
		} else {
			Flight flight = this.getFlight();
			Integer numberOfPassenger = PassengerCustomerRepository.findPassengerByBookingId(this.getId()).size();
			price.setAmount(flight.getCost().getAmount() * numberOfPassenger);
			price.setCurrency(flight.getCost().getCurrency());
		}

		return price;
	}

}
