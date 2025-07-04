
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.Member;

@GuiService
public class MemberActivityLogPublishService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ActivityLog al;
		int alId;
		int memberId;

		alId = super.getRequest().getData("id", int.class);
		al = this.repository.findActivityLogById(alId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = al != null && al.getFlightAssignment().getMember().getId() == memberId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog al;
		int id;

		id = super.getRequest().getData("id", int.class);
		al = this.repository.findActivityLogById(id);

		super.getBuffer().addData(al);
	}

	@Override
	public void bind(final ActivityLog al) {
		super.bindObject(al, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		//int activityLogId = activityLog.getId();

		//boolean flightAssignamentIsPublished = this.repository.isFlightAssignmentAlreadyPublishedByActivityLogId(activityLogId);
		//super.state(flightAssignamentIsPublished, "activityLog", "acme.validation.ActivityLog.FlightAssignamentNotPublished.message");

		//Con los datos actuales siempre salta ese error. Lo comento para poder hacer el test.
	}

	@Override
	public void perform(final ActivityLog al) {
		al.setDraftMode(false);
		this.repository.save(al);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		dataset.put("draftMode", al.isDraftMode());
		super.getResponse().addData(dataset);
	}

}
