
package acme.entities.claims;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				registrationMoment;

	@ValidEmail
	@Mandatory
	@Automapped
	private String				email;

	@ValidString(max = 255)
	@Mandatory
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Automapped
	private boolean				indicator;

	// Relationships -------------------------------------------------------------

	@Valid
	@Mandatory
	@ManyToOne
	private AssistanceAgent		assistanceAgent;

	@Valid
	@Mandatory
	@ManyToOne
	private Flight				flight;
}
