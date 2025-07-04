
package acme.entities.trackingLogs;

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
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TrackingLog", indexes = {
	@Index(columnList = "claim_id"), 
	@Index(columnList = "lastUpdateMoment"), 
	@Index(columnList = "indicator"), 
	@Index(columnList = "resolutionPercentage")
})
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Debe actualizarse cada vez que se actualice
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				lastUpdateMoment;

	@ValidString(max = 50)
	@Mandatory
	@Automapped
	private String				step;

	// Tiene que ser incremental, es decir, cuantos más TrackingLogs haya con un mismo claim, más aumenta ese porcentaje. Cuando llega a 100% puede acetarse esa Claim
	@ValidNumber(min = 0, max = 100)
	@Mandatory
	@Automapped
	private Double				resolutionPercentage;

	// When a claim is accepted or rejected, the system must store its resolution indicating the reason why was rejected or the compensation to offer 
	// Si reason no es null, denied
	@ValidString(max = 255)
	@Optional
	@Automapped
	private String				resolutionReason;

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
	private Claim				claim;

}
