
package acme.entities.legs;

import java.beans.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.leg.ValidLeg;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
	@Index(columnList = "flight_id")
})
@Getter
@Setter
@ValidLeg
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidString(pattern = "^[0-9]{4}$")
	@Automapped
	@Mandatory
	private String				flightNumberNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	@ValidMoment
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	@Valid
	private LegStatus			status;

	Boolean						draftMode;
	// Relationships -----------------------------------------------------------

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Airport				departureAirport;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Airport				arrivalAirport;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	// Derived Attributes -----------------------------------------------------


	@Transient
	public int duration() {
		Instant dep = this.scheduledDeparture.toInstant();
		Instant arr = this.scheduledArrival.toInstant();

		Duration duration = Duration.between(dep, arr);

		return duration.toHoursPart();
	}

	@Transient
	public String flightNumber() {
		String iata = this.aircraft.getAirline().getIataCode();

		return iata + this.flightNumberNumber;
	}

}
