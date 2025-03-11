
package acme.entities.flightAssignments;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
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
	@Automapped
	private Duty				duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				lastUpdateMoment;

	@Mandatory
	@Automapped
	private CurrentStatus		currrentStatus;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				remarks;

	//---------------- Relationships --------------------

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private FlightCrewMember	flightCrewMember;

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Leg					leg;

}
