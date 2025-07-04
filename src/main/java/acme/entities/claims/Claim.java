
package acme.entities.claims;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Claim", indexes = {
    @Index(columnList = "assistance_agent_id"),
    @Index(columnList = "leg_id"),
    @Index(columnList = "email"),
    @Index(columnList = "type"),
    @Index(columnList = "indicator"),
    @Index(columnList = "registrationMoment")
})
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
	@Automapped
	private String				description;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private IndicatorType		indicator;

	@Automapped
	private boolean				draftMode;

	// Relationships -------------------------------------------------------------

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Leg					leg;
}
