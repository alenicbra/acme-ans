
package acme.features.member.activityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.LegStatus;
import acme.realms.Member;

@GuiService
public class MemberActivityLogCreateService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {

		boolean fakeUpdate = true;

		Member fcmLogged;
		FlightAssignment faSelected;
		boolean existingFA = false;
		boolean isFlightAssignmentOwner = false;
		boolean isPublished = false;

		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			if (id != 0)
				fakeUpdate = false;
		}

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (!super.getRequest().getData().isEmpty() && super.getRequest().getData() != null) {
			Integer faId = super.getRequest().getData("faId", Integer.class);
			if (faId != null) {
				fcmLogged = this.repository.findFlighCrewMemberById(fcmIdLogged);
				List<FlightAssignment> allFA = this.repository.findAllFlightAssignments();
				faSelected = this.repository.findFlightAssignmentById(faId);
				existingFA = faSelected != null || allFA.contains(faSelected) && faSelected != null;
				if (existingFA) {
					isFlightAssignmentOwner = faSelected.getMember() == fcmLogged;
					isPublished = !faSelected.isDraftMode();
				}
			}
		}
		super.getResponse().setAuthorised(fakeUpdate && isFlightAssignmentOwner && isPublished);
	}

	@Override
	public void load() {
		int faId = super.getRequest().getData("faId", int.class);
		FlightAssignment flightAssignmentRelated = this.repository.findFlightAssignmentById(faId);

		ActivityLog activityLog = new ActivityLog();
		activityLog.setDraftMode(true);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setFlightAssignment(flightAssignmentRelated);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {

		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final ActivityLog activityLog) {

		FlightAssignment fa = this.repository.findFlightAssignmentById(super.getRequest().getData("faId", Integer.class));

		boolean faCompleted = false;

		if (fa.getLeg().getStatus().equals(LegStatus.LANDED) || fa.getLeg().getStatus().equals(LegStatus.CANCELLED))
			faCompleted = true;
		super.state(faCompleted, "*", "acme.validation.activityLog-faNotCompleted.message");

		boolean faNotCancelled = true;
		if (fa.getCurrentStatus().equals(CurrentStatus.CANCELLED) || fa.getCurrentStatus().equals(CurrentStatus.PENDING))
			faNotCancelled = false;
		super.state(faNotCancelled, "*", "acme.validation.activityLog-faNotCancelled.message");

		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("faId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}
}
