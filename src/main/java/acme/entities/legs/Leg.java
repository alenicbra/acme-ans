
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	@Column(unique = true)
	@Mandatory
	private String				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	private legStatus			status;

	@ValidString
	@Automapped
	@Mandatory
	private String				departureAirpot;

	@ValidString
	@Automapped
	@Mandatory
	private String				arrivalAirport;

	@ValidString
	@Automapped
	@Mandatory
	private String				aircraft;

	// Relationships -----------------------------------------------------------

	@Mandatory
	@ManyToOne
	private Flight				flight;


	private enum legStatus {

		ON_TIME, DELAYED, CANCELLED, LANDED
	}

}
