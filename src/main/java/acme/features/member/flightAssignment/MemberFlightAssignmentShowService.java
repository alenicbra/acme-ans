
package acme.features.member.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentShowService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean isFlightAssignmentOwner = false;
		boolean existingFA = false;
		boolean authorization;

		Member fcmLogged;
		FlightAssignment faSelected;

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (!super.getRequest().getData().isEmpty()) {
			Integer faId = super.getRequest().getData("id", Integer.class);
			if (faId != null) {
				fcmLogged = this.repository.findFlighCrewMemberById(fcmIdLogged);

				faSelected = this.repository.findFlightAssignmentById(faId);
				existingFA = faSelected != null;
				if (faSelected != null)
					isFlightAssignmentOwner = faSelected.getMember() == fcmLogged;
			}
		}

		authorization = isFlightAssignmentOwner && existingFA;

		super.getResponse().setAuthorised(authorization);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		SelectChoices statuses;

		SelectChoices flightcrewsDuties;

		SelectChoices legChoices;
		List<Leg> legs;

		statuses = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());

		flightcrewsDuties = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		legs = this.repository.findAllLegs();
		legChoices = SelectChoices.from(legs, "label", flightAssignment.getLeg());

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		Member flightCrewMember = this.repository.findFlighCrewMemberById(fcmIdLogged);

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("duties", flightcrewsDuties);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", flightCrewMember);
		dataset.put("FCMname", flightCrewMember.getIdentity().getName() + " " + flightCrewMember.getIdentity().getSurname());
		dataset.put("faId", flightAssignment.getId());

		super.getResponse().addData(dataset);
	}

}
