
package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$\\")
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	private Date				pruchaseMoment;

	@Mandatory
	private TravelClass			travelClass;

	@Mandatory
	@ValidMoney
	private Money				price;

	@Optional
	private String				lastNibble;

}
