
package acme.entities.flightAssignments;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.flightCrewMembers.FlightCrewMember;
import acme.entities.legs.Leg;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private Duty				duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private CurrentStatus		currrentStatus;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				remarks;

	@Automapped
	private boolean				draftMode;

	//---------------- Relationships --------------------

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Leg					leg;

}
