
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.Member;

@GuiService
public class MemberActivityLogShowService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ActivityLog al;

		al = this.repository.findActivityLogById(super.getRequest().getData("id", int.class));
		status = al != null && super.getRequest().getPrincipal().hasRealm(al.getFlightAssignment().getMember());

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
	public void unbind(final ActivityLog al) {
		Dataset dataset;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		super.getResponse().addData(dataset);
	}

}
