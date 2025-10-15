
package acme.entities.flightAssignments;

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
import acme.client.components.validation.ValidString;
import acme.entities.legs.Leg;
import acme.realms.Member;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "member_id, leg_id"), @Index(columnList = "member_id"), @Index(columnList = "duty, leg_id, draftMode")
})
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory(message = "Must not be null")
	@Automapped
	@Enumerated(EnumType.STRING)
	private Duty				duty;

	@Mandatory(message = "Must not be null")
	@ValidMoment(past = true, min = "2000/01/01 00:00", max = "2100/01/01 00:00", message = "Must be past")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdatedMoment;

	@Mandatory(message = "Must not be null")
	@Automapped
	@Enumerated(EnumType.STRING)
	private CurrentStatus		currentStatus;

	@Optional
	@ValidString(min = 0, max = 255, message = "Must not be too long")
	@Automapped
	private String				remarks;

	@Mandatory(message = "Must not be null")
	@Automapped
	private boolean				draftMode;

	//---------------- Relationships --------------------

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Member				member;

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Leg					leg;

}
