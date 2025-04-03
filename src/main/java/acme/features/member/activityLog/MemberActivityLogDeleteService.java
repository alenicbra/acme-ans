
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.Member;

@GuiService
public class MemberActivityLogDeleteService extends AbstractGuiService<Member, ActivityLog> {

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
		super.bindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void perform(final ActivityLog al) {
		this.repository.delete(al);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
