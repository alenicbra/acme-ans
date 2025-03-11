
package acme.entities.flightStatuses;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightStatus extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				departureTime;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				arrivalTime;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private StatusOfFlight		status;

	@ValidNumber(min = 0, max = 1440, fraction = 0)
	@Optional
	@Automapped
	private Integer				delayMinutes;

	@Automapped
	private boolean				indicator;

	// Relationships ----------------------------------------------------------

	@Valid
	@Mandatory
	@OneToOne
	private Flight				flight;

}
