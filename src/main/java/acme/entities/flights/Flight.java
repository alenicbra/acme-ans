
package acme.entities.flights;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airports.Airport;
import acme.entities.legs.LegRepository;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidString(max = 50)
	@Mandatory
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				indication;

	@ValidMoney
	@Mandatory
	@Automapped
	private Money				cost;

	@Optional
	@Automapped
	@ValidString(max = 254)
	private String				description;

	// Derived Attributes -----------------------------------------------------


	@Transient
	public Date scheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstByFlight(this).getScheduledDeparture();
	}

	@Transient
	public Date scheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastByFlight(this).getScheduledArrival();
	}

	@Transient
	public Airport origin() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstByFlight(this).getDepartureAirpot();
	}

	@Transient
	public Airport destination() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastByFlight(this).getArrivalAirport();
	}

	@Transient
	public int totalLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return (int) repository.getLayoversByFlight(this);
	}
}
