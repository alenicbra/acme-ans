
package acme.entities.flights;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.utils.ValidShortString;
import acme.entities.airports.Airport;
import acme.entities.legs.Leg;
import acme.features.airline_manager.legs.AirlineManagerLegRepository;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
    @Index(columnList = "manager_id"),
    @Index(columnList = "draft_mode"),
    @Index(columnList = "tag")
})
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidShortString
	@Mandatory
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				indication;

	@ValidMoney
	@Mandatory
	@Automapped
	private Money				cost;

	@Optional
	@Automapped
	@ValidString
	private String				description;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager		manager;

	Boolean						draftMode;
	// Derived Attributes -----------------------------------------------------


	@Transient
	public Date scheduledDeparture() {
		AirlineManagerLegRepository repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		Leg first = repository.findFirstLegByFlightIdOrderByScheduledDeparture(this.getId());
		return first != null ? first.getScheduledDeparture() : null;
	}

	@Transient
	public Date scheduledArrival() {
		AirlineManagerLegRepository repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		Leg last = repository.findFirstLegByFlightIdOrderByScheduledDepartureDesc(this.getId());
		return last != null ? last.getScheduledArrival() : null;
	}

	@Transient
	public Airport origin() {
		AirlineManagerLegRepository repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		Leg first = repository.findFirstLegByFlightIdOrderByScheduledDeparture(this.getId());
		return first != null ? first.getDepartureAirport() : null;
	}

	@Transient
	public Airport destination() {
		AirlineManagerLegRepository repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		Leg last = repository.findFirstLegByFlightIdOrderByScheduledDepartureDesc(this.getId());
		return last != null ? last.getArrivalAirport() : null;
	}

	@Transient
	public int totalLayovers() {
		AirlineManagerLegRepository repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		return (int) repository.getLayoversByFlight(this);
	}
}
