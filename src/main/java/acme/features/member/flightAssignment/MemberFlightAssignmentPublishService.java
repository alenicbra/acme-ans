
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentPublishService extends AbstractGuiService<Member, FlightAssignment> {

	@Autowired
	public MemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean legStatus;
		int assignmentId;
		FlightAssignment assignment;
		int legId;
		Leg leg;
		Collection<Leg> validLegs;
		int memberId;
		int airlineId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByMemberId(memberId);

		if (assignment == null)
			status = false;
		else if (!assignment.isDraftMode() || assignment.getMember().getId() != memberId)
			status = false;
		else if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			validLegs = this.repository.findLegsByAirlineId(airlineId);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			legStatus = legId == 0 || leg != null && validLegs.contains(leg);

			status = legStatus;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment fa) {
		int legId;
		int memberId;

		legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		super.bindObject(fa, "duty", "currentStatus", "remarks");
		fa.setLeg(leg);
		fa.setMember(this.repository.findMemberById(memberId));
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		if (assignment.getLeg() != null) {
			boolean notSimultaneousAssignment;
			Collection<FlightAssignment> assignments = this.repository.findAssignmentsByMemberId(assignment.getMember().getId());

			notSimultaneousAssignment = true;

			for (FlightAssignment a : assignments)
				if (!(MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), a.getLeg().getScheduledDeparture()) || MomentHelper.isBefore(a.getLeg().getScheduledArrival(), assignment.getLeg().getScheduledDeparture())))
					notSimultaneousAssignment = false;

			super.state(notSimultaneousAssignment, "leg", "acme.validation.flight-assignment.simultaneous-leg.message");

			if (assignment.getDuty() != null) {
				boolean onlyOnePilot;
				FlightAssignment pilotAssignment;
				pilotAssignment = this.repository.findPilotAssignmentsByLegId(assignment.getLeg().getId());
				onlyOnePilot = pilotAssignment == null || !assignment.getDuty().equals(Duty.PILOT);
				super.state(onlyOnePilot, "duty", "acme.validation.flight-assignment.pilot-already-assigned.message");

				boolean onlyOneCopilot;
				FlightAssignment copilotAssignment;

				copilotAssignment = this.repository.findCopilotAssignmentsByLegId(assignment.getLeg().getId());
				onlyOneCopilot = copilotAssignment == null || !assignment.getDuty().equals(Duty.COPILOT);

				super.state(onlyOneCopilot, "duty", "acme.validation.flight-assignment.copilot-already-assigned.message");
			}

		}

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		assignment.setLastUpdatedMoment(MomentHelper.getCurrentMoment());
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment fa) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		int memberId;
		int airlineId;

		dutyChoice = SelectChoices.from(Duty.class, fa.getDuty());
		currentStatusChoice = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByMemberId(memberId);
		legs = this.repository.findLegsByAirlineId(airlineId);
		legChoice = SelectChoices.from(legs, "flightNumberNumber", fa.getLeg());

		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();
		boolean isCompleted;
		isCompleted = this.repository.areLegsCompletedByFlightAssignament(fa.getId(), currentMoment);

		dataset = super.unbindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "leg", "member");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("member", fa.getMember().getEmployeeCode());
		dataset.put("onlyOneCopilot", false);
		dataset.put("onlyOnePilot", false);
		dataset.put("notSimultaneousAssignment", false);
		dataset.put("availableMember", false);
		dataset.put("notCompletedLeg", false);
		dataset.put("legId", legChoice.getSelected().getKey());
		dataset.put("memberId", fa.getMember().getId());
		dataset.put("isCompleted", isCompleted);

		super.getResponse().addData(dataset);
	}

}
