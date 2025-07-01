
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberActivityLogCreateService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment assignment;
		int memberId;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = assignment != null && assignment.getMember().getId() == memberId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog al;
		int masterId;
		FlightAssignment fa;

		masterId = super.getRequest().getData("masterId", int.class);
		fa = this.repository.findFlightAssignmentById(masterId);

		al = new ActivityLog();
		al.setFlightAssignment(fa);
		al.setDraftMode(true);

		super.getBuffer().addData(al);
	}

	@Override
	public void bind(final ActivityLog al) {
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		super.bindObject(al, "typeOfIncident", "description", "severityLevel");
		al.setDraftMode(true);
		al.setFlightAssignment(assignment);
		al.setRegistrationMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final ActivityLog al) {
		;
	}

	@Override
	public void perform(final ActivityLog al) {
		this.repository.save(al);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
		dataset.put("draftMode", true);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
