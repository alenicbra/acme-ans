
package acme.entities.legs;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.MomentHelper;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
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

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")  // IATA code + 4 dígitos
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = false) //min = Current time, max = 2201/01/01  00:00:00
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment //min = scheduledDeparture + 1 minute, max = 2201/01/01  00:00:00
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	// Derived attributes -----------------------------------------------------

	@Mandatory
	@Automapped
	private boolean				draftMode;
	// Relationships -----------------------------------------------------------


	@Transient
	public Double getDuration() {
		if (this.getScheduledDeparture() == null || this.getScheduledArrival() == null)
			return null;

		return MomentHelper.computeDuration(this.scheduledDeparture, this.scheduledArrival).toMinutes() / 60.0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;


	@Transient
	public String getLabel() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

		String departureCity = this.departureAirport.getCity();
		String departureCountry = this.departureAirport.getCountry();
		String arrivalCity = this.arrivalAirport.getCity();
		String arrivalCountry = this.arrivalAirport.getCountry();
		String departureTime = timeFormat.format(this.getScheduledDeparture());
		String arrivalTime = timeFormat.format(this.getScheduledArrival());

		return String.format("%s: %s - %s: %s %s - %s", departureCountry, departureCity, arrivalCountry, arrivalCity, departureTime, arrivalTime);

	}

}
