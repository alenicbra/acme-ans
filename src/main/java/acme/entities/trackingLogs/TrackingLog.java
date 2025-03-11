
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				lastUpdateMoment;

	@ValidString(max = 50)
	@Mandatory
	@Automapped
	private String				step;

	@ValidNumber(min = 0, max = 100)
	@Mandatory
	@Automapped
	private Double				resolutionPercentage;

	@ValidString(max = 255)
	@Mandatory
	@Automapped
	private String				resolutionReason;

	@Automapped
	private boolean				indicator;

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
	private Claim				claim;

}
